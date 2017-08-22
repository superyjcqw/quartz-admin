package com.gk.quartzAdmin.service;

import com.gk.quartzAdmin.base.QuartzClusterDataSourcePool;
import com.gk.quartzAdmin.dao.QuartzClusterDao;
import com.gk.quartzAdmin.entity.QuartzCluster;
import com.gk.quartzAdmin.entity.SchedulerState;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * quartz集群管理Service
 * Date:  17/7/11 上午11:27
 */
@Service
public class QuartzClusterService {

    @Autowired
    private QuartzClusterDao quartzClusterDao;

    @Autowired
    private QuartzClusterDataSourcePool quartzClusterDataSourcePool;

    @Autowired
    private QuartzSchedulerStateService quartzSchedulerStateService;

    private static List<QuartzCluster> cacheList = new ArrayList<>();

    private static Map<String, QuartzCluster> cacheMap = new HashMap<>();

    /**
     * 初始化
     */
    public void init() {
        cacheList = list();
        Map<String, QuartzCluster> clusterHashMap = new HashMap<>();
        cacheList.forEach(quartzCluster ->
                clusterHashMap.put(quartzCluster.getName(), quartzCluster)
        );
        cacheMap = clusterHashMap;
    }

    /**
     * 获取缓存集群集合
     * @return 集群集合
     */
    public static List<QuartzCluster> getCacheList() {
        return cacheList;
    }

    /**
     * 获取缓存集群map
     * @return 集群map
     */
    public static Map<String, QuartzCluster> getCacheMap() {
        return cacheMap;
    }

    /**
     * 获取集群集合
     * @return
     */
    public List<QuartzCluster> list() {
        List<QuartzCluster> quartzClusterList = quartzClusterDao.list();
        return quartzClusterList;
    }

    /**
     * 集群分页集合
     * @param pageIndex 分页索引
     * @param pageSize 每页显示数量
     * @return 集群分页集合
     */
    public List<QuartzCluster> page(Integer pageIndex, Integer pageSize) {
        Integer beginIndex = (pageIndex - 1) * pageSize;
        List<QuartzCluster> quartzClusterList = quartzClusterDao.list(beginIndex, pageSize);
        Map<String, Integer> nodeNumMap = findNodeNum(quartzClusterList);
        //处理节点数
        for (QuartzCluster quartzCluster : quartzClusterList) {
            if (StringUtils.isNotBlank(quartzCluster.getName())) {
                Integer nodeNum = nodeNumMap.get(quartzCluster.getName());
                quartzCluster.setNodeNum(nodeNum == null ? 0 : nodeNum);
            }
            quartzCluster.setDataSource(quartzCluster.getDataSource().replace("\n", "<br/>"));
        }
        return quartzClusterList;
    }

    /**
     * 集群总数
     * @return 集群总数
     */
    public int count() {
        return quartzClusterDao.count();
    }

    /**
     * 获取集群的节点数
     * @param quartzClusterList
     * @return
     */
    private Map<String, Integer> findNodeNum(List<QuartzCluster> quartzClusterList) {
        Map<String, Integer> nodeNum = new HashMap<>();
        //根据集群名称获取对应的节点数
        for (QuartzCluster quartzCluster : quartzClusterList) {
            List<SchedulerState> schedulerStateList = quartzSchedulerStateService.list(quartzCluster.getName(), quartzCluster.getInstanceName());
            //处理集群节点数
            Set<String> nameNodeSet = new HashSet<>();
            for (SchedulerState schedulerState : schedulerStateList) {
                String nodeName = schedulerState.getInstanceName();
                if (StringUtils.isNotBlank(nodeName)) {
                    nodeName = StringUtils.substring(nodeName, 0, nodeName.length() - String.valueOf(new Date().getTime()).length());
                    nameNodeSet.add(nodeName);
                }
            }
            //记录该集群对应的节点数
            nodeNum.put(quartzCluster.getName(), nameNodeSet.size());
        }
        return nodeNum;
    }

    /**
     * 添加集群
     * @param quartzCluster 集群对象
     * @return 集群id
     */
    public Integer insert(QuartzCluster quartzCluster) {
        quartzCluster.setStatus(QuartzCluster.STATUS_USE);
        Integer id = quartzClusterDao.insert(quartzCluster);
        quartzClusterDataSourcePool.addOrUpdateDataSourcePool(quartzCluster.getName(), quartzCluster.getDataSource());
        init();
        return id;
    }

    /**
     * 检查集群名是否唯一
     * @param name 集群名
     * @param ignoredId 忽略的集群id
     * @return 集群名是否唯一
     */
    public boolean checkNameIfUnique(String name, Integer ignoredId) {
        return quartzClusterDao.countByName(name, ignoredId) > 0;
    }

    /**
     * 获取详情
     * @param id 集群id
     * @return 集群对象
     */
    public QuartzCluster detail(Integer id) {
        if (id == null) {
            return new QuartzCluster();
        }
        QuartzCluster quartzCluster = quartzClusterDao.detail(id);
        List<QuartzCluster> quartzClusters = new ArrayList<>();
        quartzClusters.add(quartzCluster);
        Map<String, Integer> nodeNum = findNodeNum(quartzClusters);
        quartzCluster.setNodeNum(nodeNum.get(quartzCluster.getName()));
        return quartzCluster;
    }

    /**
     * 根据集群名获取集群详情
     * @param name 集群名
     * @return 集群对象
     */
    public QuartzCluster findDetailByName(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        return quartzClusterDao.findDetailByName(name);
    }

    /**
     * 修改集群
     * @param quartzCluster 集群对象
     */
    public void update(QuartzCluster quartzCluster) {
        quartzClusterDao.update(quartzCluster);
        quartzClusterDataSourcePool.addOrUpdateDataSourcePool(quartzCluster.getName(), quartzCluster.getDataSource());
        init();
    }

    /**
     * 删除集群
     * @param id 集群id
     */
    public void delete(Integer id) {
        //删除集群
        QuartzCluster quartzCluster = detail(id);
        quartzClusterDao.delete(id);
        quartzClusterDataSourcePool.delete(quartzCluster.getName());
        init();
    }

}
