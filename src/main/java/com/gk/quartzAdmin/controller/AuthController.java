package com.gk.quartzAdmin.controller;

import com.gk.quartzAdmin.entity.User;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 首页Controller
 * Date:  17/7/10 下午5:11
 */
@Controller
public class AuthController {

    /**
     * 首页
     * @param modelMap
     * @return
     */
    @RequestMapping("/")
    public String login(ModelMap modelMap) {
        modelMap.put("username", ((User) SecurityUtils.getSubject().getPrincipal()).getUsername());
        return "index";
    }

    /**
     * 登录页面
     * @return page
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        if (SecurityUtils.getSubject().isAuthenticated()) {
            return "redirect:/";
        }
        return "login";
    }

    /**
     * 登录
     * @param modelMap
     * @param username 用户名
     * @param password 密码
     * @param rememberMe 记住我
     * @param request
     * @return code
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(ModelMap modelMap,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam("rememberMe") Boolean rememberMe,
                        HttpServletRequest request) {
        if (SecurityUtils.getSubject().isAuthenticated()) {
            return "redirect:/";
        }
        String exceptionClazz = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        if (UnknownAccountException.class.getName().equals(exceptionClazz)) { // 帐号不存在
            modelMap.addAttribute("code", 1);
        } else if (IncorrectCredentialsException.class.getName().equals(exceptionClazz)) { // 密码不正确
            modelMap.addAttribute("code", 2);
        } else if (DisabledAccountException.class.getName().equals(exceptionClazz)) {
            modelMap.addAttribute("code", 3);
        } else {
            modelMap.addAttribute("code", 4);
        }
        return "login";
    }

    /**
     * 未授权
     * @return page
     */
    @RequestMapping("/unauthorized")
    public String unauthorized() {
        return "403";
    }

    /**
     * 登出
     * @return page
     */
    @RequestMapping("/logout")
    public String logout() {
        SecurityUtils.getSubject().logout();
        return "redirect:/";
    }

    /**
     * 健康检查url
     * @return
     */
    @RequestMapping("/health-check/status")
    @ResponseBody
    public String healthCheck() {
        return "ok";
    }

}
