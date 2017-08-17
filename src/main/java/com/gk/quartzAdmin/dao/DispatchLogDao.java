package com.gk.quartzAdmin.dao;

import com.gk.quartzAdmin.entity.DispatchLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 调度日志Dao
 * Date:  17/7/23 下午3:28
 */
@Repository
public class DispatchLogDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 调度日志分页集合
     * @param clusterName 集群名
     * @param jobName 任务名
     * @param beginIndex 开始索引
     * @param pageSize 每页显示数量
     * @return 调度日志分页集合
     */
    public List<DispatchLog> list(String clusterName, String jobName, Integer status, Integer beginIndex, Integer pageSize) {
        List<Object> paramsList = new ArrayList<>();
        String sql = "select * from dispatch_log where cluster_name = ? ";
        paramsList.add(clusterName);
        if (!StringUtils.isEmpty(jobName)) {
            sql += " and job_name = ?";
            paramsList.add(jobName);
        }
        if (status != null) {
            sql += " and status = ?";
            paramsList.add(status);
        }
        sql += " order by id desc limit ?, ? ";
        paramsList.add(beginIndex);
        paramsList.add(pageSize);
        RowMapper<DispatchLog> rowMapper = new BeanPropertyRowMapper<DispatchLog>(DispatchLog.class);
        List<DispatchLog> quartzJobs = jdbcTemplate.query(sql, paramsList.toArray(), rowMapper);
        return quartzJobs;
    }

    /**
     * 调度日志总数
     * @param clusterName 集群名
     * @param jobName 任务名
     * @param status 状态
     * @return 调度日志总数
     */
    public Integer count(String clusterName, String jobName, Integer status) {
        List<Object> paramsList = new ArrayList<>();
        String sql = "select count(*) from dispatch_log where cluster_name = ?";
        paramsList.add(clusterName);
        if (!StringUtils.isEmpty(jobName)) {
            sql += " and job_name = ?";
            paramsList.add(jobName);
        }
        if (status != null) {
            sql += " and status = ?";
            paramsList.add(status);
        }
        return jdbcTemplate.queryForObject(sql, paramsList.toArray(), Integer.class);
    }
}
