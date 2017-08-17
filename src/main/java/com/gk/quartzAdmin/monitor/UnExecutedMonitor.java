package com.gk.quartzAdmin.monitor;

import com.gk.quartzAdmin.base.BaseQuartzMonitor;
import com.gk.quartzAdmin.base.QuartzClusterDataSourcePool;
import com.gk.quartzAdmin.entity.QuartzCluster;
import com.gk.quartzAdmin.entity.QuartzJob;
import com.gk.quartzAdmin.service.QuartzClusterService;
import com.gk.quartzAdmin.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 未执行监控
 * Author: liuhuan
 * Date:  17/7/12 下午3:32
 */
@Service
public class UnExecutedMonitor extends BaseQuartzMonitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnExecutedMonitor.class);

    @Autowired
    private QuartzClusterDataSourcePool dataSourcePool;

    private final static String TITLE = "定时器未执行监控";

    private final static String CONTENT = "集群名:%s\n实例名:%s\n任务名:%s\n计划执行时间:%s\n当前时间:%s";

    @Override
    public void init() {
        setInitialDelay(10000L);
        setPeriod(60 * 1000L);
    }

    @Override
    public void run() {
        List<QuartzCluster> quartzClusterList = QuartzClusterService.getCacheList();
        quartzClusterList.forEach(quartzCluster ->
                {
                    JdbcTemplate jdbcTemplate = dataSourcePool.getJdbcTemplate(quartzCluster.getName());
                    List<QuartzJob> list = (List<QuartzJob>) jdbcTemplate.query("select t.JOB_NAME, t.SCHED_NAME, t.TRIGGER_STATE, t.NEXT_FIRE_TIME from " +
                            " QRTZ_TRIGGERS t WHERE t.SCHED_NAME = ? and t.TRIGGER_STATE != 'BLOCKED' and t.TRIGGER_STATE != 'PAUSED' and  t.NEXT_FIRE_TIME < unix_timestamp(now() - 2) * 1000", new Object[]{quartzCluster.getInstanceName()}, new BeanPropertyRowMapper(QuartzJob.class));
                    list.forEach(quartzJob -> {
                                LOGGER.info("集群名({})实例名({})任务名({})未执行,计划执行时间({})当前时间({})", quartzCluster.getName(), quartzJob.getSchedName(), quartzJob.getJobName(),
                                        DateUtil.format(new Date(quartzJob.getNextFireTime()), DateUtil.DATE_FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND),
                                        DateUtil.format(new Date(), DateUtil.DATE_FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND));
                                sendEmail(getAlarmEmails(quartzCluster.getName(), quartzJob.getSchedName(), quartzJob.getJobName()), TITLE,
                                        String.format(CONTENT, quartzCluster.getName(), quartzJob.getSchedName(), quartzJob.getJobName(),
                                                DateUtil.format(new Date(quartzJob.getNextFireTime()), DateUtil.DATE_FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND),
                                                DateUtil.format(new Date(), DateUtil.DATE_FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)));
                            }
                    );
                }

        );
    }
}
