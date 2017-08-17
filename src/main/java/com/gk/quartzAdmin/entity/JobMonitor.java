package com.gk.quartzAdmin.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * 任务监控
 * Date:  17/8/4 下午7:10
 */
public class JobMonitor {

    /**
     * 主键
     */
    @JSONField(name = "id")
    private Integer id;
    /**
     * 集群名
     */
    @JSONField(name = "cluster_name")
    private String clusterName;
    /**
     * 实例名
     */
    @JSONField(name = "instance_name")
    private String instanceName;
    /**
     * 任务名
     */
    @JSONField(name = "job_name")
    private String jobName;
    /**
     * 报警人id集合
     */
    @JSONField(name = "alarm_contacts_ids")
    private String alarmContactsIds;
    /**
     * 最大执行时间(单位:ms,超过会报警)
     */
    @JSONField(name = "max_execute_time")
    private Integer maxExecuteTime;
    /**
     * 创建时间
     */
    @JSONField(name = "create_time")
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getAlarmContactsIds() {
        return alarmContactsIds;
    }

    public void setAlarmContactsIds(String alarmContactsIds) {
        this.alarmContactsIds = alarmContactsIds;
    }

    public Integer getMaxExecuteTime() {
        return maxExecuteTime;
    }

    public void setMaxExecuteTime(Integer maxExecuteTime) {
        this.maxExecuteTime = maxExecuteTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}

