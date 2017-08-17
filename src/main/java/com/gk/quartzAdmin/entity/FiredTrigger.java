package com.gk.quartzAdmin.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 已被点火的触发器
 * Date:  17/7/14 下午8:02
 */
public class FiredTrigger {

    /**
     * 描述(任务名)
     */
    @JSONField(name = "DESCRIPTION")
    private String description;
    /**
     * 映射服务名
     */
    @JSONField(name = "JOB_NAME")
    private String jobName;
    /**
     * 实例名
     */
    @JSONField(name = "SCHED_NAME")
    private String schedName;
    /**
     * 点火时间
     */
    @JSONField(name = "FIRED_TIME")
    private Long firedTime;
    /**
     * 状态
     */
    @JSONField(name = "STATE")
    private String state;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getSchedName() {
        return schedName;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }

    public Long getFiredTime() {
        return firedTime;
    }

    public void setFiredTime(Long firedTime) {
        this.firedTime = firedTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
