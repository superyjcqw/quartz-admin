package com.gk.quartzAdmin.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigInteger;

/**
 * 集群实例状态
 * Date:  17/7/35 下午3:03
 */
public class SchedulerState {

    /**
     * 任务名
     */
    @JSONField(name = "SCHED_NAME")
    private String schedName;
    /**
     * 实例名
     */
    @JSONField(name = "INSTANCE_NAME")
    private String instanceName;
    /**
     * 最后一次checkin时间
     */
    @JSONField(name = "LAST_CHECKIN_TIME")
    private BigInteger lastCheckinTime;
    /**
     * 每次checkin间隔(毫秒)
     */
    @JSONField(name = "CHECKIN_INTERVAL")
    private BigInteger checkinInterval;

    public String getSchedName() {
        return schedName;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public BigInteger getLastCheckinTime() {
        return lastCheckinTime;
    }

    public void setLastCheckinTime(BigInteger lastCheckinTime) {
        this.lastCheckinTime = lastCheckinTime;
    }

    public BigInteger getCheckinInterval() {
        return checkinInterval;
    }

    public void setCheckinInterval(BigInteger checkinInterval) {
        this.checkinInterval = checkinInterval;
    }
}
