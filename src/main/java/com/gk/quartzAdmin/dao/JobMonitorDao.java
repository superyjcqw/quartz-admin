package com.gk.quartzAdmin.dao;

import com.gk.quartzAdmin.entity.JobMonitor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 任务监控Dao
 * Date:  17/8/4 下午7:16
 */
@Repository
public class JobMonitorDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 插入任务监控
     * @param jobMonitor
     */
    public void insertOrUpdateJobMonitor(JobMonitor jobMonitor) {
        String sql = "insert into job_monitor (cluster_name, instance_name, job_name, alarm_contacts_ids, max_execute_time, create_time) values(?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE alarm_contacts_ids = ?, max_execute_time = ?";
        Object[] params = {jobMonitor.getClusterName(), jobMonitor.getInstanceName(), jobMonitor.getJobName(), jobMonitor.getAlarmContactsIds(), jobMonitor.getMaxExecuteTime(), jobMonitor.getCreateTime(), jobMonitor.getAlarmContactsIds(), jobMonitor.getMaxExecuteTime()};
        jdbcTemplate.update(sql, params);
    }

    /**
     * 通过名称查询一条任务监控
     * @param clusterName 集群名
     * @param instanceName 实例名
     * @param jobName 任务名
     * @return 任务监控对象
     */
    public JobMonitor findOneJobMonitorByName(String clusterName, String instanceName, String jobName) {
        String sql = "select max_execute_time, alarm_contacts_ids  from job_monitor where cluster_name = ? and instance_name = ? and job_name = ?";
        List<JobMonitor> list = jdbcTemplate.query(sql, new Object[]{clusterName, instanceName, jobName}, new BeanPropertyRowMapper<>(JobMonitor.class));
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 删除任务监控
     * @param jobMonitor
     */
    public void deleteJobMonitor(JobMonitor jobMonitor) {
        String sql = "delete from job_monitor where cluster_name = ? and instance_name = ? and job_name = ? ";
        Object[] params = {jobMonitor.getClusterName(), jobMonitor.getInstanceName(), jobMonitor.getJobName()};
        jdbcTemplate.update(sql, params);
    }
}
