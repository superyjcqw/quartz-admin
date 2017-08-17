package com.gk.quartzAdmin.service;

import com.gk.quartzAdmin.dao.QuartzSchedulerStateDao;
import com.gk.quartzAdmin.entity.SchedulerState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 集群实例状态Service
 * Date:  17/7/14 下午7:01
 */
@Service
public class QuartzSchedulerStateService {

    @Autowired
    private QuartzSchedulerStateDao quartzSchedulerStateDao;

    /**
     * 根据实例名称查询该实例所对应的节点数
     * @param clusterName 集群名
     * @param instanceName 实例名
     * @return 集群实例状态集合
     */
    public List<SchedulerState> list(String clusterName, String instanceName) {
        return quartzSchedulerStateDao.list(clusterName, instanceName);
    }
}
