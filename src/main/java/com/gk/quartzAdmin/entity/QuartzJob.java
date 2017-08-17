package com.gk.quartzAdmin.entity;


import com.alibaba.fastjson.annotation.JSONField;

import java.util.LinkedHashMap;

/**
 * 任务
 * Date:  17/7/11 下午3:02
 */
public class QuartzJob {

    /**
     * 触发器状态 - 等待中：触发器正在等待到时间，来被出发。比如说当前时间为20:00，下次触发时间为20:05，则此时，该定时器处于该状态
     */
    public static final String TRIGGER_STATE_WAITING = "WAITING";
    /**
     * 触发器状态 - 获得中：当到达触发时间时，定时器会尝试去获得`WAITING`状态的触发器，并设置成当前状态
     */
    public static final String TRIGGER_STATE_ACQUIRED = "ACQUIRED";
    /**
     * 触发器状态 - 执行中：该状态主要用于集群时，标记某个触发器正在某个节点的定时器正在执行种。触发器本身不会设成该状态
     */
    public static final String TRIGGER_STATE_EXECUTING = "EXECUTING";
    /**
     * 触发器状态 - 完成：触发器已经完成。处于当前状态后，触发器再也不会触发。
     */
    public static final String TRIGGER_STATE_COMPLETE = "COMPLETE";
    /**
     * 触发器状态 - 阻塞中：触发器触发，任务在执行中，这时，触发器不再被触发，直到任务完成。
     */
    public static final String TRIGGER_STATE_BLOCKED = "BLOCKED";
    /**
     * 触发器状态 - 错误：触发器本身配置有问题.比如说无法获得任务Job
     */
    public static final String TRIGGER_STATE_ERROR = "ERROR";
    /**
     * 触发器状态 - 暂停：触发器被暂停。。处于当前状态后，触发器再也不会触发。
     */
    public static final String TRIGGER_STATE_PAUSED = "PAUSED";
    /**
     * 触发器状态 - 暂停 + 阻塞：暂时阻塞状态中的任务。任务不会马上停止，而是将触发器设置成`PAUSED_BLOCKED`，直到任务完成后，设置成`PAUSED`
     */
    public static final String TRIGGER_STATE_PAUSED_BLOCKED = "PAUSED_BLOCKED";
    /**
     * 触发器状态 - 删除：该状态实际不存在。
     */
    public static final String TRIGGER_STATE_DELETED = "DELETED";

    public static final LinkedHashMap<String, String> statusStrMap = new LinkedHashMap();

    static {
        statusStrMap.put(TRIGGER_STATE_WAITING, "等待中");
        statusStrMap.put(TRIGGER_STATE_ACQUIRED, "获得中");
        statusStrMap.put(TRIGGER_STATE_BLOCKED, "阻塞中");
        statusStrMap.put(TRIGGER_STATE_COMPLETE, "完成");
        statusStrMap.put(TRIGGER_STATE_PAUSED, "暂停运行");
        statusStrMap.put(TRIGGER_STATE_PAUSED_BLOCKED, "阻塞暂停");
        statusStrMap.put(TRIGGER_STATE_ERROR, "错误");
    }

    /**
     * 集群名称
     */
    @JSONField(name = "SCHED_NAME")
    private String schedName;
    /**
     * 触发器组
     */
    @JSONField(name = "TRIGGER_GROUP")
    private String triggerGroup;
    /**
     * 触发器名字
     */
    @JSONField(name = "TRIGGER_NAME")
    private String triggerName;
    /**
     * 任务名（映射服务名）
     */
    @JSONField(name = "JOB_NAME")
    private String jobName;
    /**
     * 任务分组
     */
    @JSONField(name = "JOB_GROUP")
    private String jobGroup;
    /**
     * 任务别名（中文描述）
     */
    @JSONField(name = "DESCRIPTION")
    private String description;
    /**
     * 次任务执行时间
     */
    @JSONField(name = "NEXT_FIRE_TIME")
    private Long nextFireTime;
    /**
     * 上次任务执行时间
     */
    @JSONField(name = "PREV_FIRE_TIME")
    private Long prevFireTime;
    /**
     * 触发器状态
     */
    @JSONField(name = "TRIGGER_STATE")
    private String triggerState;
    /**
     * 触发器表达式类型
     */
    @JSONField(name = "TRIGGER_TYPE")
    private String triggerType;
    /**
     * 表达式
     */
    @JSONField(name = "CRON_EXPRESSION")
    private String cronExpression;
    /**
     * 重复间隔
     */
    @JSONField(name = "REPEAT_INTERVAL")
    private Long repeatInterval;
    /**
     * 触发器表达式
     */
    private String triggerExpression;
    /**
     * 状态字符
     */
    private String statusStr;
    /**
     * 下次点火时间字符
     */
    private String nextFireTimeStr;
    /**
     * 上次点火时间字符
     */
    private String prevFireTimeStr;
    /**
     * 开始时间字符
     */
    private String startTimeStr;
    /**
     * 集群名
     */
    private String clusterName;
    /**
     * 报警ids
     */
    private String alarmContactsIds;
    /**
     * 报警人名称
     */
    private String alarmContactsNames;
    /**
     * 最大执行时间
     */
    private Integer maxExecuteTime;


    public Long getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(Long repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public String getSchedName() {
        return schedName;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getTriggerGroup() {
        return triggerGroup;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(Long nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public Long getPrevFireTime() {
        return prevFireTime;
    }

    public void setPrevFireTime(Long prevFireTime) {
        this.prevFireTime = prevFireTime;
    }

    public String getTriggerState() {
        return triggerState;
    }

    public void setTriggerState(String triggerState) {
        this.triggerState = triggerState;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public String getNextFireTimeStr() {
        return nextFireTimeStr;
    }

    public void setNextFireTimeStr(String nextFireTimeStr) {
        this.nextFireTimeStr = nextFireTimeStr;
    }

    public String getPrevFireTimeStr() {
        return prevFireTimeStr;
    }

    public void setPrevFireTimeStr(String prevFireTimeStr) {
        this.prevFireTimeStr = prevFireTimeStr;
    }

    public String getStartTimeStr() {
        return startTimeStr;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public String getTriggerExpression() {
        return triggerExpression;
    }

    public void setTriggerExpression(String triggerExpression) {
        this.triggerExpression = triggerExpression;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getAlarmContactsIds() {
        return alarmContactsIds;
    }

    public void setAlarmContactsIds(String alarmContactsIds) {
        this.alarmContactsIds = alarmContactsIds;
    }

    public String getAlarmContactsNames() {
        return alarmContactsNames;
    }

    public void setAlarmContactsNames(String alarmContactsNames) {
        this.alarmContactsNames = alarmContactsNames;
    }

    public Integer getMaxExecuteTime() {
        return maxExecuteTime;
    }

    public void setMaxExecuteTime(Integer maxExecuteTime) {
        this.maxExecuteTime = maxExecuteTime;
    }

}
