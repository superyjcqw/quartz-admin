package com.gk.quartzAdmin.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * 报警人
 * Date:  17/7/13 下午8:01
 */
public class AlarmContacts {

    /**
     * 状态 -- 正常
     */
    public static Integer ALARM_CONTACTS_NORMAL = 1;

    /**
     * 状态 -- 删除
     */
    public static Integer ALARM_CONTACTS_DELETE = 2;

    /**
     * 编号
     */
    @JSONField(name = "id")
    private Integer id;
    /**
     * 名字
     */
    @JSONField(name = "name")
    private String name;
    /**
     * 邮箱
     */
    @JSONField(name = "email")
    private String email;
    /**
     * 状态
     */
    @JSONField(name = "status")
    private Integer status;
    /**
     * 创建时间
     */
    @JSONField(name = "create_time")
    private Date createTime;
    /**
     * 是否选中
     */
    private Boolean checked;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean isChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}
