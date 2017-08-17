package com.gk.quartzAdmin.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 集群
 * @date 2017/7/11 15:53.
 */
public class QuartzCluster {

    /**
     * 状态-正常
     */
    public static final int STATUS_USE = 1;
    /**
     * 状态-停用
     */
    public static final int STATUS_DELETE = 2;

    /**
     * id 主键
     */
    @JSONField(name = "id")
    private Integer id;
    /**
     * 集群名称
     */
    @JSONField(name = "name")
    private String name;
    /**
     * 实例名称
     */
    @JSONField(name = "instance_name")
    private String instanceName;
    /**
     * 数据源
     */
    @JSONField(name = "datasource")
    private String dataSource;
    /**
     * 节点数
     */
    private Integer nodeNum;
    /**
     * 备注
     */
    @JSONField(name = "memo")
    private String memo;
    /**
     * 集群状态
     */
    @JSONField(name = "status")
    private Integer status;
    /**
     * 远程集群节点
     */
    @JSONField(name = "remote_node_host")
    private String remoteNodeHost;

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

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public Integer getNodeNum() {
        return nodeNum;
    }

    public void setNodeNum(Integer nodeNum) {
        this.nodeNum = nodeNum;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemoteNodeHost() {
        return remoteNodeHost;
    }

    public void setRemoteNodeHost(String remoteNodeHost) {
        this.remoteNodeHost = remoteNodeHost;
    }
}
