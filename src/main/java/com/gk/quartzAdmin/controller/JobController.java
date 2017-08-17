package com.gk.quartzAdmin.controller;

import com.gk.quartzAdmin.entity.DeletedJob;
import com.gk.quartzAdmin.entity.FiredTrigger;
import com.gk.quartzAdmin.entity.QuartzCluster;
import com.gk.quartzAdmin.entity.QuartzJob;
import com.gk.quartzAdmin.service.AlarmContactsService;
import com.gk.quartzAdmin.service.QuartzClusterService;
import com.gk.quartzAdmin.service.QuartzJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * job管理Controller
 * Date:  17/7/11 下午5:28
 */
@Controller
@RequestMapping("job")
public class JobController {

    @Autowired
    private QuartzJobService quartzJobService;

    /**
     * 分页列表
     * @param modelMap
     * @param clusterName 集群名
     * @param triggerState 触发器状态
     * @param jobName 任务名
     * @param pageIndex 分页索引
     * @param pageSize 每页显示数量
     * @return page
     */
    @RequestMapping("list")
    public String list(ModelMap modelMap,
                       @RequestParam(value = "clusterName", required = false) String clusterName,
                       @RequestParam(value = "triggerState", required = false) String triggerState,
                       @RequestParam(value = "jobName", required = false) String jobName,
                       @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        List<QuartzCluster> quartzClusters = QuartzClusterService.getCacheList();
        if (!CollectionUtils.isEmpty(quartzClusters)) {
            if (clusterName == null) {
                clusterName = quartzClusters.get(0).getName();
            }
            List<QuartzJob> quartzJobList = quartzJobService.list(clusterName, triggerState, jobName, pageIndex, pageSize);
            modelMap.put("list", quartzJobList);
            modelMap.put("currIndex", pageIndex);
            int totalCount = quartzJobService.count(clusterName, triggerState, jobName);
            int pages = totalCount % pageSize;
            modelMap.put("pages", pages == 0 ? totalCount / pageSize : (totalCount / pageSize + 1));
            modelMap.put("pageSize", pageSize);
            modelMap.put("clusterList", quartzClusters);
            modelMap.put("clusterName", clusterName);
            modelMap.put("triggerState", triggerState);
            modelMap.put("jobName", jobName);
            modelMap.put("triggerStateMap", QuartzJob.statusStrMap);
        }
        return "job/list";
    }

    /**
     * 跳到创建页面
     * @param modelMap
     * @param clusterName 集群名
     * @return page
     */
    @RequestMapping("to-create")
    public String toCreate(ModelMap modelMap, @RequestParam("clusterName") String clusterName) {
        modelMap.put("clusterName", clusterName);
        modelMap.put("alarmContactsList", AlarmContactsService.getCacheAlarmContactsList());
        return "job/add";
    }

    /**
     * 创建job
     * @param quartzJob job实例
     * @return boolean
     */
    @RequestMapping("create")
    @ResponseBody
    public Boolean create(QuartzJob quartzJob) {
        return quartzJobService.add(quartzJob);
    }

    /**
     * 跳到修改页面
     * @param modelMap
     * @param clusterName 集群名
     * @param jobName 任务名
     * @return page
     */
    @RequestMapping("to-update")
    public String toUpdate(ModelMap modelMap, @RequestParam("clusterName") String clusterName, @RequestParam("jobName") String jobName) {
        QuartzJob quartzJob = quartzJobService.findOne(clusterName, jobName);
        modelMap.put("job", quartzJob);
        modelMap.put("clusterName", clusterName);
        modelMap.put("alarmContactsList", quartzJobService.getCheckedAlarmContacts(quartzJob.getAlarmContactsIds()));
        return "job/edit";
    }

    /**
     * 修改
     * @param quartzJob job实例
     * @return boolean
     */
    @RequestMapping("update")
    @ResponseBody
    public Boolean update(QuartzJob quartzJob) {
        return quartzJobService.edit(quartzJob);
    }

    /**
     * 删除
     * @param quartzJob job实例
     * @return boolean
     */
    @RequestMapping("delete")
    @ResponseBody
    public Boolean delete(QuartzJob quartzJob) {
        return quartzJobService.delete(quartzJob);
    }

    /**
     * 暂停
     * @param quartzJob job实例
     * @return
     */
    @RequestMapping("pause")
    @ResponseBody
    public Boolean pause(QuartzJob quartzJob) {
        return quartzJobService.pause(quartzJob);
    }

    /**
     * 恢复
     * @param quartzJob job实例
     * @return
     */
    @RequestMapping("recover")
    @ResponseBody
    public Boolean recover(QuartzJob quartzJob) {
        return quartzJobService.recover(quartzJob);
    }

    /**
     * 立即执行
     * @param quartzJob job实例
     * @return
     */
    @RequestMapping("execute")
    @ResponseBody
    public Boolean execute(QuartzJob quartzJob) {
        return quartzJobService.execute(quartzJob);
    }

    /**
     * 正在运行的任务列表
     * @param modelMap
     * @param clusterName 集群名
     * @return page
     */
    @RequestMapping("running/list")
    public String runningList(ModelMap modelMap, @RequestParam(value = "clusterName", required = false) String clusterName) {
        List<QuartzCluster> quartzClusters = QuartzClusterService.getCacheList();
        if (!CollectionUtils.isEmpty(quartzClusters)) {
            if (clusterName == null) {
                clusterName = quartzClusters.get(0).getName();
            }
            List<FiredTrigger> firedTriggers = quartzJobService.firedTriggerList(clusterName);
            modelMap.put("list", firedTriggers);
            modelMap.put("clusterList", quartzClusters);
            modelMap.put("clusterName", clusterName);
        }
        return "job/runningList";
    }

    /**
     * 已删除的任务列表
     * @param modelMap
     * @param pageIndex 分页索引
     * @param pageSize 每页显示数量
     * @param clusterName 集群名
     * @return page
     */
    @RequestMapping(value = "deleted/list", method = RequestMethod.GET)
    public String deletedList(ModelMap modelMap, @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                       @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                       @RequestParam(value = "clusterName", required = false) String clusterName) {
        List<QuartzCluster> quartzClusters = QuartzClusterService.getCacheList();
        if (!CollectionUtils.isEmpty(quartzClusters)) {
            if (clusterName == null) {
                clusterName = quartzClusters.get(0).getName();
            }
            List<DeletedJob> list = quartzJobService.deletedJobList(clusterName, pageIndex, pageSize);
            modelMap.addAttribute("list", list);
            modelMap.put("currIndex", pageIndex);
            int totalCount = quartzJobService.deletedJobCount(clusterName);
            int pages = totalCount % pageSize;
            modelMap.put("pages", pages == 0 ? totalCount / pageSize : (totalCount / pageSize + 1));
            modelMap.put("pageSize", pageSize);
            modelMap.put("clusterList", quartzClusters);
            modelMap.put("clusterName", clusterName);
        }
        return "job/deletedList";
    }
}
