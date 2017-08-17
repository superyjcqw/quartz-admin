package com.gk.quartzAdmin.controller;

import com.gk.quartzAdmin.entity.DispatchLog;
import com.gk.quartzAdmin.entity.QuartzCluster;
import com.gk.quartzAdmin.service.DispatchLogService;
import com.gk.quartzAdmin.service.QuartzClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 调度日志Controller
 * Date:  17/7/23 下午3:28
 */
@Controller
@RequestMapping("dispatch/log")
public class DispatchLogController {

    @Autowired
    private DispatchLogService dispatchLogService;

    /**
     * 列表
     * @param modelMap
     * @param pageIndex 分页索引
     * @param pageSize 每页显示数量
     * @param clusterName 集群名
     * @param jobName 任务名
     * @param status 任务状态
     * @return page
     */
    @RequestMapping("list")
    public String list(ModelMap modelMap, @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                       @RequestParam(value = "clusterName", required = false) String clusterName,
                       @RequestParam(value = "jobName", required = false) String jobName,
                       @RequestParam(value = "status", required = false) Integer status) {
        List<QuartzCluster> quartzClusters = QuartzClusterService.getCacheList();
        if (!CollectionUtils.isEmpty(quartzClusters)) {
            if (clusterName == null) {
                clusterName = quartzClusters.get(0).getName();
            }
            List<DispatchLog> quartzJobList = dispatchLogService.list(clusterName, jobName, status, pageIndex, pageSize);
            modelMap.put("list", quartzJobList);
            modelMap.put("currIndex", pageIndex);
            int totalCount = dispatchLogService.count(clusterName, jobName, status);
            int pages = totalCount % pageSize;
            modelMap.put("pages", pages == 0 ? totalCount / pageSize : (totalCount / pageSize + 1));
            modelMap.put("pageSize", pageSize);
            modelMap.put("clusterList", quartzClusters);
            modelMap.put("clusterName", clusterName);
            modelMap.put("jobName", jobName);
            modelMap.put("status", status);
            modelMap.put("statusMap", DispatchLog.statusStrMap);
        }
        return "dispatchLog/list";
    }
}
