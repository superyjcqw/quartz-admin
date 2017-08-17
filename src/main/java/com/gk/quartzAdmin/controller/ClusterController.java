package com.gk.quartzAdmin.controller;

import com.gk.quartzAdmin.entity.QuartzCluster;
import com.gk.quartzAdmin.service.QuartzClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * quartz集群管理Controller
 * Date:  17/7/11 上午11:41
 */
@Controller
@RequestMapping("cluster")
public class ClusterController {

    @Autowired
    private QuartzClusterService quartzClusterService;

    /**
     * 列表
     * @param modelMap
     * @return
     */
    @RequestMapping("list")
    public String list(ModelMap modelMap, @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex, @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        modelMap.put("quartzClusterList", quartzClusterService.page(pageIndex, pageSize));
        modelMap.put("currIndex", pageIndex);
        int totalCount = quartzClusterService.count();
        int pages = totalCount % pageSize;
        modelMap.put("pages", pages == 0 ? totalCount / pageSize : (totalCount / pageSize + 1));
        modelMap.put("pageSize", pageSize);
        return "cluster/list";
    }

    /**
     * 跳到新增
     * @return page
     */
    @RequestMapping("to-create")
    public String toCreate() {
        return "cluster/add";
    }

    /**
     * 新增
     * @param quartzCluster 集群对象
     * @return code
     */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public Integer create(QuartzCluster quartzCluster) {
        if (quartzClusterService.checkNameIfUnique(quartzCluster.getName(), null)) {
            return 1;
        }
        quartzClusterService.insert(quartzCluster);
        return 0;
    }

    /**
     * 跳到修改
     * @param modelMap
     * @param id 集群id
     * @return page
     */
    @RequestMapping("to-update")
    public String toUpdate(ModelMap modelMap, @RequestParam("id") Integer id) {
        QuartzCluster quartzCluster = quartzClusterService.detail(id);
        modelMap.put("quartzCluster", quartzCluster);
        return "cluster/edit";
    }

    /**
     * 更新
     * @param quartzCluster 集群对象
     * @return code
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public Integer update(QuartzCluster quartzCluster) {
        if (quartzClusterService.checkNameIfUnique(quartzCluster.getName(), quartzCluster.getId())) {
            return 1;
        }
        quartzClusterService.update(quartzCluster);
        return 0;
    }

    /**
     * 详情
     * @param modelMap
     * @param id 集群id
     * @return page
     */
    @RequestMapping("detail")
    public String detail(ModelMap modelMap, @RequestParam("id") Integer id) {
        QuartzCluster quartzCluster = quartzClusterService.detail(id);
        modelMap.put("quartzCluster", quartzCluster);
        return "cluster/detail";
    }

    /**
     * 删除集群，并释放数据连接
     * @param id 集群id
     * @return code
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public Integer delete(@RequestParam("id") Integer id) {
        quartzClusterService.delete(id);
        return 0;
    }

}
