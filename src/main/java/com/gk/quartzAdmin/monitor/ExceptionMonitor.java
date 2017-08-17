package com.gk.quartzAdmin.monitor;

import com.gk.quartzAdmin.base.BaseQuartzMonitor;
import com.gk.quartzAdmin.entity.DispatchLog;
import com.google.common.collect.ArrayListMultimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 异常监控(查询最近两小时内未处理的异常job)
 * Date:  17/7/12 下午8:05
 */
@Service
public class ExceptionMonitor extends BaseQuartzMonitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionMonitor.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void init() {
        setInitialDelay(10000L);
        setPeriod(60 * 1000L);
    }

    private final static String TITLE = "定时器异常监控";

    private final static String CONTENT = "集群名:%s\n实例名:%s\n任务名:%s\n异常次数:%s\n异常:%s";

    @Override
    public void run() {
        List<DispatchLog> list = (List<DispatchLog>) jdbcTemplate.query("select id, cluster_name, instance_name, job_name, exception  from " +
                " dispatch_log WHERE begin_time > unix_timestamp( date_sub(now(), interval 2 hour)) * 1000 and status = 2 and exception_notice_status = 1 ", new Object[]{}, new BeanPropertyRowMapper(DispatchLog.class));
        ArrayListMultimap<String, DispatchLog> multimap = ArrayListMultimap.create();
        list.forEach(dispatchLog -> {
            String key = dispatchLog.getClusterName() + "*" + dispatchLog.getInstanceName() + "*" + dispatchLog.getJobName() + "*" + dispatchLog.getException();
            multimap.put(key, dispatchLog);
        });
        for (String info : multimap.keySet()) {
            DispatchLog dispatchLog = multimap.get(info).get(0);
            LOGGER.info("集群名({})实例名({})任务名({})发生异常({})次", dispatchLog.getClusterName(), dispatchLog.getInstanceName(), dispatchLog.getJobName(), multimap.get(info).size());
            sendEmail(getAlarmEmails(dispatchLog.getClusterName(), dispatchLog.getInstanceName(), dispatchLog.getJobName()), TITLE, String.format(CONTENT, dispatchLog.getClusterName(), dispatchLog.getInstanceName(), dispatchLog.getJobName(), multimap.get(info).size(), dispatchLog.getException()));
            List<Long> ids = new ArrayList<>();
            multimap.get(info).forEach(log -> ids.add(log.getId()));
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("ids", ids);
            new NamedParameterJdbcTemplate(jdbcTemplate).update("update dispatch_log set exception_notice_status = 2  where id in (:ids)", parameters);

        }
    }
}
