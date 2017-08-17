package com.gk.quartzAdmin.controller;

import com.gk.quartzAdmin.entity.User;
import com.gk.quartzAdmin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 用户管理Controller
 * Author: liuhuan
 * Date:  17/7/17 下午3:32
 */
@Controller
@RequestMapping(value = "user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 分页列表
     * @param modelMap
     * @param pageIndex 分页索引
     * @param pageSize 每页显示数量
     * @return page
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(ModelMap modelMap, @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex, @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        List<User> listUser = userService.list(pageIndex, pageSize);
        modelMap.addAttribute("listUser", listUser);
        modelMap.put("currIndex", pageIndex);
        int totalCount = userService.count();
        int pages = totalCount % pageSize;
        modelMap.put("pages", pages == 0 ? totalCount / pageSize : (totalCount / pageSize + 1));
        modelMap.put("pageSize", pageSize);
        return "user/list";
    }

    /**
     * 跳到创建页面
     * @return page
     */
    @RequestMapping("to-create")
    public String toCreate() {
        return "user/add";
    }

    /**
     * 创建用户
     * @param user 用户对象
     * @return code
     */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public Integer create(User user) {
        if (userService.checkLoginNameIfUnique(user.getLoginName(), null)) {
            return 1;
        }
        userService.addUser(user);
        return 0;
    }

    /**
     * 跳到修改页面
     * @param modelMap
     * @param id 用户id
     * @return page
     */
    @RequestMapping(value = "to-update", method = RequestMethod.GET)
    public String toUpdate(ModelMap modelMap, @RequestParam(value = "id") Integer id) {
        modelMap.addAttribute("user", userService.findUserById(id));
        return "user/edit";
    }

    /**
     * 修改用户
     * @param user 用户对象
     * @return code
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public Integer update(User user) {
        if (userService.checkLoginNameIfUnique(user.getLoginName(), user.getId())) {
            return 1;
        }
        userService.updateUser(user);
        return 0;
    }

    /**
     * 跳到修改密码页面
     * @param modelMap
     * @param id 用户id
     * @return page
     */
    @RequestMapping(value = "to-update-password", method = RequestMethod.GET)
    public String toUpdatePwd(ModelMap modelMap, @RequestParam(value = "id") Integer id) {
        modelMap.addAttribute("user", userService.findUserById(id));
        return "user/updatePwd";
    }

    /**
     * 修改密码
     * @param newPassword 新密码
     * @param confirmNewPassword 确认新密码
     * @param id 用户id
     * @return code
     */
    @RequestMapping(value = "update-password", method = RequestMethod.POST)
    @ResponseBody
    public Integer updatePassword(@RequestParam(value = "newPassword") String newPassword,
                                  @RequestParam(value = "confirmNewPassword") String confirmNewPassword,
                                  @RequestParam(value = "id") Integer id) {
        return userService.updatePassword(id, newPassword, confirmNewPassword);
    }

    /**
     * 用户详情
     * @param model
     * @param id 用户id
     * @return page
     */
    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public String detail(ModelMap model, @RequestParam(value = "id") Integer id) {
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        return "user/detail";
    }

    /**
     * 删除用户
     * @param id 用户id
     * @return code
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public Integer delete(@RequestParam(value = "id") Integer id) {
        userService.deleteUser(id);
        return 0;
    }


}

