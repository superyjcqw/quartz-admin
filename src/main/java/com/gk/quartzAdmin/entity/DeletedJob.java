package com.gk.quartzAdmin.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * 已删除的任务
 * Date:  17/7/21 下午2:24
 */
public class DeletedJob {

    /**
     * id 主键
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
     * 任务描述
     */
    @JSONField(name = "job_desc")
    private String jobDesc;
    /**
     * 触发器类型
     */
    @JSONField(name = "trigger_type")
    private String triggerType;
    /**
     * 触发器表达式
     */
    @JSONField(name = "trigger_expression")
    private String triggerExpression;
    /**
     * 删除时间
     */
    @JSONField(name = "time")
    private Date time;

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

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public String getTriggerExpression() {
        return triggerExpression;
    }

    public void setTriggerExpression(String triggerExpression) {
        this.triggerExpression = triggerExpression;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
