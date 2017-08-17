package com.gk.quartzAdmin.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 * Date:  17/7/12 下午6:05
 */
public class User implements Serializable {

    /**
     * 用户状态 -- 启用
     */
    public static final Integer USER_ENABLE = 1;
    /**
     * 用户状态 -- 禁用
     */
    public static final Integer USER_DISABLE = 2;
    /**
     * 用户状态 -- 已删除
     */
    public static final Integer USER_DELETE = 3;

    /**
     * 编号
     */
    @JSONField(name = "id")
    private Integer id;
    /**
     * 用户昵称
     */
    @JSONField(name = "username")
    private String username;
    /**
     * 用户登录名称
     */
    @JSONField(name = "login_name")
    private String loginName;
    /**
     * 密码
     */
    @JSONField(name = "password")
    private String password;
    /**
     * 创建时间
     */
    @JSONField(name = "create_time")
    private Date createTime;
    /**
     * 用户状态
     */
    @JSONField(name = "status")
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
