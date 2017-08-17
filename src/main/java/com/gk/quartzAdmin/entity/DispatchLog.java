package com.gk.quartzAdmin.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.LinkedHashMap;

/**
 * 定时器的执行Log
 * Date:  17/7/13 上午11:28
 */
public class DispatchLog {

    /**
     * 执行状态- 初始化
     */
    public static final Integer STATUS_BEGIN = 0;
    /**
     * 执行状态- 执行成功
     */
    public static final Integer STATUS_SUCCESS = 1;
    /**
     * 执行状态- 执行失败
     */
    public static final Integer STATUS_FAILURE = 2;
    /**
     * 异常通知状态-未通知
     */
    public static final Integer EXCEPTION_NOTICE_STATUS_NO = 1;
    /**
     * 异常通知状态-已通知
     */
    public static final Integer EXCEPTION_NOTICE_STATUS_YES = 2;
    /**
     * 慢执行通知状态-未通知
     */
    public static final Integer SLOW_EXECUTE_NOTICE_STATUS_NO = 1;
    /**
     * 慢执行通知状态-已通知
     */
    public static final Integer SLOW_EXECUTE_NOTICE_STATUS_YES = 2;

    public static final LinkedHashMap<Integer, String> statusStrMap = new LinkedHashMap();

    static {
        statusStrMap.put(0, "初始化");
        statusStrMap.put(1, "执行成功");
        statusStrMap.put(2, "执行失败");
    }

    /**
     * 编号
     */
    @JSONField(name = "id")
    private Long id;
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
     * 映射服务名
     */
    @JSONField(name = "job_name")
    private String jobName;
    /**
     * 执行开始时间
     */
    @JSONField(name = "begin_time")
    private Long beginTime;
    /**
     * 执行的结束时间
     */
    @JSONField(name = "end_time")
    private Long endTime;
    /**
     * 执行状态 0-初始化状态 1-成功 2-失败
     */
    @JSONField(name = "status")
    private Integer status;
    /**
     * 异常
     */
    @JSONField(name = "exception")
    private String exception;
    /**
     * 异常
     */
    @JSONField(name = "execute_duration")
    private Long executeDuration;
    /**
     * 异常通知状态
     */
    @JSONField(name = "exception_notice_status")
    private Integer exceptionNoticeStatus;
    /**
     * 慢执行通知状态
     */
    @JSONField(name = "slow_execute_notice_status")
    private Integer slowExecuteNoticeStatus;
    /**
     * 开始时间字符
     */
    private String beginTimeStr;
    /**
     * 结束时间字符
     */
    private String endTimeStr;
    /**
     * 异常通知状态字符
     */
    private String exceptionNoticeStatusStr;
    /**
     * 状态字符
     */
    private String statusStr;
    /**
     * 最大执行时间(非存储)
     */
    @JSONField(name = "max_execute_time")
    private String maxExecuteTime;

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public String getExceptionNoticeStatusStr() {
        return exceptionNoticeStatusStr;
    }

    public void setExceptionNoticeStatusStr(String exceptionNoticeStatusStr) {
        this.exceptionNoticeStatusStr = exceptionNoticeStatusStr;
    }

    public String getBeginTimeStr() {
        return beginTimeStr;
    }

    public void setBeginTimeStr(String beginTimeStr) {
        this.beginTimeStr = beginTimeStr;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Long beginTime) {
        this.beginTime = beginTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getExecuteDuration() {
        return executeDuration;
    }

    public void setExecuteDuration(Long executeDuration) {
        this.executeDuration = executeDuration;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public Integer getExceptionNoticeStatus() {
        return exceptionNoticeStatus;
    }

    public void setExceptionNoticeStatus(Integer exceptionNoticeStatus) {
        this.exceptionNoticeStatus = exceptionNoticeStatus;
    }

    public Integer getSlowExecuteNoticeStatus() {
        return slowExecuteNoticeStatus;
    }

    public void setSlowExecuteNoticeStatus(Integer slowExecuteNoticeStatus) {
        this.slowExecuteNoticeStatus = slowExecuteNoticeStatus;
    }

    public String getMaxExecuteTime() {
        return maxExecuteTime;
    }

    public void setMaxExecuteTime(String maxExecuteTime) {
        this.maxExecuteTime = maxExecuteTime;
    }
}