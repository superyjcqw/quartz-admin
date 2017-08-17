package com.gk.quartzAdmin.monitor;

import com.gk.quartzAdmin.base.BaseQuartzMonitor;
import com.gk.quartzAdmin.entity.DispatchLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 慢执行监控(查询最近两小时内未处理的慢执行job)
 * Date:  17/7/13 上午11:06
 */
@Service
public class SlowExecuteMonitor extends BaseQuartzMonitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlowExecuteMonitor.class);

    private final static String TITLE = "定时器慢执行监控";

    private final static String CONTENT = "集群名:%s\n实例名:%s\n任务名:%s\n预计执行时间:%sms\n实际执行时长:%sms";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void init() {
        setInitialDelay(10000L);
        setPeriod(5000L);
    }

    @Override
    public void run() {
        List<DispatchLog> dispatchLogs = (List<DispatchLog>) jdbcTemplate.query("select t.cluster_name, t.instance_name, t.job_name, t.execute_duration,j.max_execute_time  from " +
                " dispatch_log t right join job_monitor j on t.cluster_name = j.cluster_name and t.instance_name = j.instance_name and t.job_name = j.job_name " +
                " WHERE j.max_execute_time > 0 and t.execute_duration > j.max_execute_time  and t.begin_time > unix_timestamp(date_sub(now(), interval 2 hour)) * 1000 " +
                " and t.slow_execute_notice_status = 1 ", new Object[]{}, new BeanPropertyRowMapper(DispatchLog.class));
        dispatchLogs.forEach(dispatchLog -> {
            LOGGER.info("集群名({})实例名({})任务名({})预计执行时间({}ms)实际执行时长({}ms)", dispatchLog.getClusterName(), dispatchLog.getInstanceName(), dispatchLog.getJobName(), dispatchLog.getMaxExecuteTime(), dispatchLog.getExecuteDuration());
            sendEmail(getAlarmEmails(dispatchLog.getClusterName(), dispatchLog.getInstanceName(), dispatchLog.getJobName()), TITLE,
                    String.format(CONTENT, dispatchLog.getClusterName(), dispatchLog.getInstanceName(), dispatchLog.getJobName(), dispatchLog.getMaxExecuteTime(), dispatchLog.getExecuteDuration()));
            jdbcTemplate.update("update dispatch_log set slow_execute_notice_status = 2  where id = ?", dispatchLog.getId());
        });
    }
}
