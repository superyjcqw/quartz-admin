package com.gk.quartzAdmin.service;

import com.gk.quartzAdmin.dao.UserDao;
import com.gk.quartzAdmin.entity.User;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 用户管理Service
 * Date:  17/7/17 下午7:03
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    /**
     * 用户分页集合
     * @param pageIndex 分页索引
     * @param pageSize 每页显示数量
     * @return 用户分页集合
     */
    public List<User> list(Integer pageIndex, Integer pageSize) {
        Integer beginIndex = (pageIndex - 1) * pageSize;
        return userDao.list(beginIndex, pageSize);
    }

    /**
     * 用户总数
     * @return 用户总数
     */
    public int count() {
        return userDao.count();
    }

    /**
     * 查检用户登录名是否唯一
     * @param loginName 登录名
     * @param ignoredId 忽略的用户id
     * @return 用户登录名是否唯一
     */
    public boolean checkLoginNameIfUnique(String loginName, Integer ignoredId) {
        return userDao.countByLoginName(loginName, ignoredId) > 0;
    }

    /**
     * 根据id查询用户
     * @param id 用户id
     * @return 用户对象
     */
    public User findUserById(Integer id) {
        User user = userDao.findUserById(id);
        user.setPassword(null);
        return user;
    }

    /**
     * 根据登录名查询用户
     * @param loginName 登录名
     * @return 用户对象
     */
    public User findUserByLoginName(String loginName) {
        return userDao.findUserByLoginName(loginName);
    }

    /**
     * 删除用户
     * @param id 用户id
     */
    public void deleteUser(Integer id) {
        userDao.updateUserStatus(id);
    }

    /**
     * 修改用户基本信息
     * @param user 用户对象
     */
    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    /**
     * 修改用户密码
     * @param id 用户id
     * @param newPassword 新密码
     * @param confirmNewPassword 确认新密码
     * @return code
     */
    public int updatePassword(Integer id, String newPassword, String confirmNewPassword) {
        //判断当前用户权限
        if (!StringUtils.isEmpty(newPassword)) {
            if (!newPassword.equals(confirmNewPassword)) {
                return 1;//两次密码不一致
            }
            User user = findUserById(id);
            userDao.updatePassword(id, new Md5Hash(newPassword, user.getLoginName(), 2).toString());
        }
        return 0;
    }

    /**
     * 添加用户
     * @param user 用户对象
     */
    public void addUser(User user) {
        user.setPassword(new Md5Hash(user.getPassword(), user.getLoginName(), 2).toString());
        user.setStatus(User.USER_ENABLE);
        user.setCreateTime(new Date());
        userDao.insertUser(user);
    }
}
