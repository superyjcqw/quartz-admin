package com.gk.quartzAdmin.dao;


import com.gk.quartzAdmin.entity.QuartzCluster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * quartz集群管理Dao
 * Date:  17/7/11 上午11:27
 */
@Repository
public class QuartzClusterDao {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    //基本sql语句
    String baseSql = "select id, name, instance_name, datasource, memo ,status, remote_node_host from quartz_cluster where 1=1";

    /**
     * 集群分页集合
     * @param beginIndex 开始索引
     * @param pageSize 每页显示数量
     * @return 集群分页集合
     */
    public List<QuartzCluster> list(Integer beginIndex, Integer pageSize) {
        String querySql = baseSql + " and status = 1 limit ?, ?";
        RowMapper<QuartzCluster> rowMapper = new BeanPropertyRowMapper<QuartzCluster>(QuartzCluster.class);
        return jdbcTemplate.query(querySql, new Object[]{beginIndex, pageSize}, rowMapper);
    }

    /**
     * 集群总数
     * @return 集群总数
     */
    public Integer count() {
        String sql = "select count(*) from quartz_cluster where status = 1";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    /**
     * 查询所有集群
     * @return 所有集群
     */
    public List<QuartzCluster> list() {
        String querySql = baseSql + " and status =1";
        RowMapper<QuartzCluster> rowMapper = new BeanPropertyRowMapper<QuartzCluster>(QuartzCluster.class);
        return jdbcTemplate.query(querySql, rowMapper);
    }

    /**
     * 新增集群
     * @param quartzCluster 集群对象
     */
    public Integer insert(final QuartzCluster quartzCluster) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update((Connection connection) -> {
                    String insertSql = "insert into quartz_cluster (name, instance_name, datasource, memo, status, remote_node_host) values(?,?,?,?,?,?)";
                    PreparedStatement ps = connection.prepareStatement(insertSql, new String[]{"name", "instance_name", "datasource", "memo", "status", "remote_node_host"});
                    ps.setString(1, quartzCluster.getName() == null ? "" : quartzCluster.getName());
                    ps.setString(2, quartzCluster.getInstanceName() == null ? "" : quartzCluster.getInstanceName());
                    ps.setString(3, quartzCluster.getDataSource() == null ? "" : quartzCluster.getDataSource());
                    ps.setString(4, quartzCluster.getMemo() == null ? "" : quartzCluster.getMemo());
                    ps.setInt(5, quartzCluster.getStatus() == null ? QuartzCluster.STATUS_USE : quartzCluster.getStatus());
                    ps.setString(6, quartzCluster.getRemoteNodeHost() == null ? "" : quartzCluster.getRemoteNodeHost());
                    return ps;
                },
                keyHolder);
        return keyHolder.getKey().intValue();
    }

    /**
     * 查询集群详情
     * @param id 集群id
     * @return 集群对象
     */
    public QuartzCluster detail(Integer id) {
        //sql语句
        String sql = baseSql + " and id = ?";
        RowMapper<QuartzCluster> rowMapper = new BeanPropertyRowMapper<QuartzCluster>(QuartzCluster.class);
        QuartzCluster quartzCluster = jdbcTemplate.queryForObject(sql, rowMapper, id);
        return quartzCluster;
    }

    /**
     * 根据集群名查询集群数量
     * @param name 集群名
     * @param ignoredId 被忽略的集群id
     * @return 集群数量
     */
    public Integer countByName(String name, Integer ignoredId) {
        String sql = "select count(*) from quartz_cluster where status = 1 and name = ?";
        List<Object> paramsList = new ArrayList<>();
        paramsList.add(name);
        if (ignoredId != null) {
            sql += " and id != ?";
            paramsList.add(ignoredId);
        }
        return jdbcTemplate.queryForObject(sql, paramsList.toArray(), Integer.class);
    }

    /**
     * 根据集群名获取集群详情
     * @param name 集群名
     * @return 集群对象
     */
    public QuartzCluster findDetailByName(String name) {
        String sql = baseSql + " and name = ? and status = 1";
        RowMapper<QuartzCluster> rowMapper = new BeanPropertyRowMapper<QuartzCluster>(QuartzCluster.class);
        return jdbcTemplate.queryForObject(sql, rowMapper, name);
    }

    /**
     * 更新集群信息
     * @param quartzCluster 集群对象
     */
    public void update(QuartzCluster quartzCluster) {
        String updateSql = "update quartz_cluster set name = ?, instance_name = ?, datasource = ?,memo = ?, remote_node_host = ? where id = ? ";
        Object[] params = {quartzCluster.getName(), quartzCluster.getInstanceName(), quartzCluster.getDataSource(), quartzCluster.getMemo(), quartzCluster.getRemoteNodeHost(), quartzCluster.getId()};
        jdbcTemplate.update(updateSql, params);
    }

    /**
     * 删除集群
     * @param id 集群id
     */
    public void delete(Integer id) {
        String deleteSql = "update quartz_cluster set status = ? where id = ? ";
        Object[] params = {QuartzCluster.STATUS_DELETE, id};
        jdbcTemplate.update(deleteSql, params);
    }

}
