package com.gk.quartzAdmin.dao;

import com.gk.quartzAdmin.entity.DeletedJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 已删除的任务Dao
 * Date:  17/7/18 下午7:29
 */
@Repository
public class DeletedJobDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 已删除的任务分页集合
     * @param clusterName 集群名
     * @param beginIndex 开始索引
     * @param pageSize 每页显示数量
     * @return 已删除的任务分页集合
     */
    public List<DeletedJob> list(String clusterName, Integer beginIndex, Integer pageSize) {
        String sql = "select * from deleted_job where cluster_name = ? limit ?, ?";
        RowMapper<DeletedJob> rowMapper = new BeanPropertyRowMapper<DeletedJob>(DeletedJob.class);
        List<DeletedJob> deletedJobs = jdbcTemplate.query(sql, new Object[]{clusterName, beginIndex, pageSize}, rowMapper);
        return deletedJobs;
    }

    /**
     * 已删除的任务总数
     * @param clusterName 集群名
     * @return 已删除的任务总数
     */
    public Integer count(String clusterName) {
        String sql = "select count(*) from deleted_job where cluster_name = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{clusterName}, Integer.class);
    }

    /**
     * 插入删除的任务
     * @param job 已删除的任务对象
     */
    public void insertDeletedJob(DeletedJob job) {
        String sql = "insert into deleted_job (instance_name, cluster_name, job_name, job_desc, trigger_type, trigger_expression) values(?, ?, ?, ?, ?, ?)";
        Object[] params = {job.getInstanceName(), job.getClusterName(), job.getJobName(), job.getJobDesc(), job.getTriggerType(), job.getTriggerExpression()};
        jdbcTemplate.update(sql, params);
    }
}
