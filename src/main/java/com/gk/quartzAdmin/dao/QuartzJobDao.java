package com.gk.quartzAdmin.dao;

import com.gk.quartzAdmin.base.QuartzClusterDataSourcePool;
import com.gk.quartzAdmin.entity.FiredTrigger;
import com.gk.quartzAdmin.entity.QuartzJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * job管理Dao
 * Date:  17/7/11 下午5:28
 */
@Repository
public class QuartzJobDao {

    @Autowired
    private QuartzClusterDataSourcePool dataSourcePool;

    /**
     * 查询任务分页集合
     * @param clusterName 集群名
     * @param scheduleName 实例名
     * @param triggerState 触发器状态
     * @param jobName 任务名
     * @param beginIndex 开始索引
     * @param pageSize 每页显示数量
     * @return 任务分页集合
     */
    public List<QuartzJob> list(String clusterName, String scheduleName, String triggerState, String jobName, Integer beginIndex, Integer pageSize) {
        JdbcTemplate jdbcTemplate = dataSourcePool.getJdbcTemplate(clusterName);
        List<Object> paramsList = new ArrayList<>();
        String sql = "select qt.*, qct.CRON_EXPRESSION, qst.REPEAT_INTERVAL from QRTZ_TRIGGERS qt " +
                "left join QRTZ_CRON_TRIGGERS qct on qt.SCHED_NAME = qct.SCHED_NAME and qt.TRIGGER_NAME = qct.TRIGGER_NAME and  qt.TRIGGER_GROUP = qct.TRIGGER_GROUP " +
                "left join QRTZ_SIMPLE_TRIGGERS qst on qst.SCHED_NAME = qt.SCHED_NAME and qt.TRIGGER_NAME = qst.TRIGGER_NAME and  qt.TRIGGER_GROUP = qst.TRIGGER_GROUP " +
                "where qt.SCHED_NAME = ?";
        paramsList.add(scheduleName);
        if (!StringUtils.isEmpty(triggerState)) {
            sql += " and qt.TRIGGER_STATE = ?";
            paramsList.add(triggerState);
        }
        if (!StringUtils.isEmpty(jobName)) {
            sql += " and qt.JOB_NAME like ?";
            paramsList.add("%" + jobName + "%");
        }
        sql += " order by qt.START_TIME desc limit ?, ?";
        paramsList.add(beginIndex);
        paramsList.add(pageSize);
        RowMapper<QuartzJob> rowMapper = new BeanPropertyRowMapper<QuartzJob>(QuartzJob.class);
        List<QuartzJob> quartzJobs = jdbcTemplate.query(sql, paramsList.toArray(), rowMapper);
        return quartzJobs;
    }

    /**
     * 查询任务总数
     * @param clusterName 集群名
     * @param scheduleName 实例名
     * @param triggerState 触发器状态
     * @param jobName 任务名
     * @return 任务总数
     */
    public Integer count(String clusterName, String scheduleName, String triggerState, String jobName) {
        JdbcTemplate jdbcTemplate = dataSourcePool.getJdbcTemplate(clusterName);
        List<Object> paramsList = new ArrayList<>();
        String sql = "select count(1) from QRTZ_TRIGGERS where SCHED_NAME = ?";
        paramsList.add(scheduleName);
        if (!StringUtils.isEmpty(triggerState)) {
            sql += " and TRIGGER_STATE = ?";
            paramsList.add(triggerState);
        }
        if (!StringUtils.isEmpty(jobName)) {
            sql += " and JOB_NAME like ?";
            paramsList.add("%" + jobName + "%");
        }
        return jdbcTemplate.queryForObject(sql, paramsList.toArray(), Integer.class);
    }

    /**
     * 查询已经点火的触发器集合
     * @param clusterName 集群名
     * @param scheduleName 实例名
     * @return 已经点火的触发器集合
     */
    public List<FiredTrigger> firedTriggerList(String clusterName, String scheduleName) {
        JdbcTemplate jdbcTemplate = dataSourcePool.getJdbcTemplate(clusterName);
        String sql = "SELECT QT.DESCRIPTION, QT.JOB_NAME, QFT.SCHED_NAME, QFT.FIRED_TIME, QFT.STATE FROM QRTZ_FIRED_TRIGGERS QFT LEFT JOIN " +
                "QRTZ_TRIGGERS QT ON QFT.SCHED_NAME = QT.SCHED_NAME AND QFT.TRIGGER_NAME = QT.TRIGGER_NAME " +
                "WHERE QFT.SCHED_NAME = ? ORDER BY QFT.FIRED_TIME ASC";

        RowMapper<FiredTrigger> rowMapper = new BeanPropertyRowMapper<>(FiredTrigger.class);
        return jdbcTemplate.query(sql, new Object[]{scheduleName}, rowMapper);
    }

    /**
     * 查询一个任务
     * @param clusterName 集群名
     * @param scheduleName 实例名
     * @param jobName 任务名
     * @return 一个任务
     */
    public QuartzJob findOne(String clusterName, String scheduleName, String jobName) {
        JdbcTemplate jdbcTemplate = dataSourcePool.getJdbcTemplate(clusterName);
        String sql = "select qt.*, qct.CRON_EXPRESSION, qst.REPEAT_INTERVAL from QRTZ_TRIGGERS qt " +
                "left join QRTZ_CRON_TRIGGERS qct on qt.SCHED_NAME = qct.SCHED_NAME and qt.TRIGGER_NAME = qct.TRIGGER_NAME and  qt.TRIGGER_GROUP = qct.TRIGGER_GROUP " +
                "left join QRTZ_SIMPLE_TRIGGERS qst on qst.SCHED_NAME = qt.SCHED_NAME and qt.TRIGGER_NAME = qst.TRIGGER_NAME and  qt.TRIGGER_GROUP = qst.TRIGGER_GROUP " +
                "where qt.SCHED_NAME = ? and qt.JOB_NAME = ?";
        RowMapper<QuartzJob> rowMapper = new BeanPropertyRowMapper<QuartzJob>(QuartzJob.class);
        QuartzJob job = jdbcTemplate.queryForObject(sql, new Object[]{scheduleName, jobName}, rowMapper);
        return job;
    }
}
