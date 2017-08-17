package com.gk.quartzAdmin.controller;

import com.gk.quartzAdmin.entity.AlarmContacts;
import com.gk.quartzAdmin.service.AlarmContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 报警人Controller
 * Date:  17/7/22 下午2:30
 */
@Controller
@RequestMapping(value = "alarm-contacts")
public class AlarmContactsController {

    @Autowired
    private AlarmContactsService alarmContactsService;

    /**
     * 查询所有报警人
     * @param modelMap
     * @param pageIndex 分页索引
     * @param pageSize  每页显示数量
     * @return page
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(ModelMap modelMap, @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex, @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        List<AlarmContacts> listAlarmContacts = alarmContactsService.list(pageIndex, pageSize);
        modelMap.addAttribute("list", listAlarmContacts);
        modelMap.put("currIndex", pageIndex);
        int totalCount = alarmContactsService.count();
        int pages = totalCount % pageSize;
        modelMap.put("pages", pages == 0 ? totalCount / pageSize : (totalCount / pageSize + 1));
        modelMap.put("pageSize", pageSize);
        return "alarmContacts/list";
    }

    /**
     * 跳到创建页面
     * @return page
     */
    @RequestMapping("to-create")
    public String toCreate() {
        return "alarmContacts/add";
    }

    /**
     * 创建报警人
     * @param alarmContacts 报警人对象
     * @return code
     */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public Integer create(AlarmContacts alarmContacts) {
        alarmContactsService.add(alarmContacts);
        return 0;
    }

    /**
     * 跳到修改页面
     * @param modelMap
     * @param id 报警人id
     * @return 页面
     */
    @RequestMapping(value = "to-update", method = RequestMethod.GET)
    public String toUpdate(ModelMap modelMap, @RequestParam(value = "id") Integer id) {
        modelMap.addAttribute("alarmContacts", alarmContactsService.findById(id));
        return "alarmContacts/edit";
    }

    /**
     * 修改
     * @param alarmContacts 报警人对象
     * @return code
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public Integer update(AlarmContacts alarmContacts) {
        alarmContactsService.update(alarmContacts);
        return 0;
    }

    /**
     * 删除
     * @param id 报警人id
     * @return code
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public Integer delete(Integer id) {
        alarmContactsService.delete(id);
        return 0;
    }
}
