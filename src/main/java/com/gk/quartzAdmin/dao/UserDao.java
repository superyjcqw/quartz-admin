package com.gk.quartzAdmin.dao;

import com.gk.quartzAdmin.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户管理Dao
 * Date:  17/7/17 下午7:03
 */
@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 基础的sql查询
     */
    private static final String sql = "select id, username, login_name, password, create_time, status from user where 1 = 1 ";

    private static final RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);

    /**
     * 用户分页集合
     * @param beginIndex 开始索引
     * @param pageSize 每页显示数量
     * @return 用户分页集合
     */
    public List<User> list(Integer beginIndex, Integer pageSize) {
        String querySql = sql + "and status != 3 order by id  ASC limit ?, ?";
        List<User> userList = jdbcTemplate.query(querySql, new Object[]{beginIndex, pageSize}, rowMapper);
        return userList;
    }

    /**
     * 用户总数量
     * @return 用户总数量
     */
    public int count() {
        String sql = "select count(1) from user where status != 3";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    /**
     * 根据登录名查询用户数
     * @param loginName 登录名
     * @param ignoredId 被忽略的用户尖
     * @return 用户数
     */
    public Integer countByLoginName(String loginName, Integer ignoredId) {
        String sql = "select count(*) from user where status != 3 and login_name = ?";
        List<Object> paramsList = new ArrayList<>();
        paramsList.add(loginName);
        if (ignoredId != null) {
            sql += " and id != ?";
            paramsList.add(ignoredId);
        }
        return jdbcTemplate.queryForObject(sql, paramsList.toArray(), Integer.class);
    }

    /**
     * 根据id查询用户
     *
     * @param id 用户id
     * @return 用户对象
     */
    public User findUserById(Integer id) {
        String querySql = sql + " and status != 3 and id = ?";
        User user = jdbcTemplate.queryForObject(querySql, rowMapper, id);
        return user;
    }

    /**
     * 根据登录名查询一个用户
     * @param loginName 登录名
     * @return 用户对象
     */
    public User findUserByLoginName(String loginName) {
        String querySql = sql + " and status != 3 and login_name = ?";
        List<User> list = jdbcTemplate.query(querySql, new Object[]{loginName}, rowMapper);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 修改用户状态
     * @param id 用户id
     */
    public void updateUserStatus(Integer id) {
        String sql = "update user set status = ? where id = ?";
        Object[] params = {User.USER_DELETE, id};
        jdbcTemplate.update(sql, params);
    }

    /**
     * 修改用户基本信息
     * @param user 用户对象
     */
    public void updateUser(User user) {
        String sql = "update user set username = ?, login_name = ?, status = ? where id = ?";
        Object[] params = {user.getUsername(), user.getLoginName(), user.getStatus(), user.getId()};
        jdbcTemplate.update(sql, params);
    }

    /**
     * 修改用户密码
     * @param id 用户id
     * @param password 密码
     */
    public void updatePassword(Integer id, String password) {
        String sql = "update user set password = ? where id = ?";
        Object[] params = {password, id};
        jdbcTemplate.update(sql, params);
    }

    /**
     * 添加用户
     * @param user 用户对象
     */
    public void insertUser(User user) {
        String sql = "insert into user(userName, login_name, password, status, create_time) values(?, ?, ?, ?, ?)";
        Object[] params = {user.getUsername(), user.getLoginName(), user.getPassword(), user.getStatus(), user.getCreateTime()};
        jdbcTemplate.update(sql, params);
    }
}
