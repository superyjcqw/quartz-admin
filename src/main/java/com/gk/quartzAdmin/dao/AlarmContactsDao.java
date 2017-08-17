package com.gk.quartzAdmin.dao;

import com.gk.quartzAdmin.entity.AlarmContacts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 报警人Dao
 * Date:  17/7/22 下午2:30
 */
@Repository
public class AlarmContactsDao {

    private static final String sql = "select id, name, email, status, create_time from alarm_contacts where 1 = 1 ";

    private static final RowMapper<AlarmContacts> rowMapper = new BeanPropertyRowMapper<>(AlarmContacts.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 报警人分页集合
     * @param beginIndex 查询开始索引
     * @param pageSize 每页索引数量
     * @return 报警人分页集合
     */
    public List<AlarmContacts> list(Integer beginIndex, Integer pageSize) {
        String querySql = sql + "and status = 1 order by id ASC limit ?, ?";
        List<AlarmContacts> alarmContactsList = jdbcTemplate.query(querySql, new Object[]{beginIndex, pageSize}, rowMapper);
        return alarmContactsList;
    }

    /**
     * 报警人总数
     * @return 报警人总数
     */
    public int count() {
        String sql = "select count(*) from alarm_contacts where status = 1";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    /**
     * 根据Id查询报警人
     * @param id 报警人id
     * @return 报警人对象
     */
    public AlarmContacts findById(Integer id) {
        String querySql = sql + "and id = ?";
        AlarmContacts alarmContacts = jdbcTemplate.queryForObject(querySql, rowMapper, id);
        return alarmContacts;
    }

    /**
     * 删除报警人
     * @param id 报警人id
     */
    public void delete(Integer id) {
        String sql = "update alarm_contacts set status = ? where id = ?";
        Object[] params = {AlarmContacts.ALARM_CONTACTS_DELETE, id};
        jdbcTemplate.update(sql, params);
    }

    /**
     * 修改报警人
     * @param alarmContacts 报警人对象
     */
    public void update(AlarmContacts alarmContacts) {
        String sql = "update alarm_contacts set name = ?, email = ? where id = ?";
        Object[] params = {alarmContacts.getName(), alarmContacts.getEmail(), alarmContacts.getId()};
        jdbcTemplate.update(sql, params);
    }

    /**
     * 添加报警人
     * @param alarmContacts 报警人对象
     */
    public void insert(AlarmContacts alarmContacts) {
        String sql = "insert into alarm_contacts(name, email, status, create_time) values(?, ?, ?, ?)";
        Object[] params = {alarmContacts.getName(), alarmContacts.getEmail(), alarmContacts.getStatus(), alarmContacts.getCreateTime()};
        jdbcTemplate.update(sql, params);
    }

    /**
     * 查询所有报警人邮件
     * @return 所有报警人邮件
     */
    public List<AlarmContacts> listAllEmails() {
        String querySql = "select id, name, email from alarm_contacts where status = 1 order by id  ASC";
        return jdbcTemplate.query(querySql, rowMapper);
    }

}
