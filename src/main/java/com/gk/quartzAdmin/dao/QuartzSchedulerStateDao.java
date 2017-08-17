package com.gk.quartzAdmin.dao;

import com.gk.quartzAdmin.base.QuartzClusterDataSourcePool;
import com.gk.quartzAdmin.entity.SchedulerState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 集群实例状态Dao
 * Date:  17/7/14 下午7:01
 */
@Repository
public class QuartzSchedulerStateDao {

    @Autowired
    private QuartzClusterDataSourcePool dataSourcePool;

    String baseSql = "select * from QRTZ_SCHEDULER_STATE where 1=1";

    /**
     * 查询集群实例状态集合
     * @param clusterName 集群名
     * @param instanceName 实例名
     * @return 集群实例状态集合
     */
    public List<SchedulerState> list(String clusterName, String instanceName) {
        String querySql = baseSql + " and SCHED_NAME = ?";
        RowMapper<SchedulerState> rowMapper = new BeanPropertyRowMapper<>(SchedulerState.class);
        JdbcTemplate jdbcTemplate = dataSourcePool.getJdbcTemplate(clusterName);
        return jdbcTemplate.query(querySql, new Object[]{instanceName}, rowMapper);
    }
}
