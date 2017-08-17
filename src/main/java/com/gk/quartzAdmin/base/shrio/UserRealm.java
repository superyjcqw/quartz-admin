package com.gk.quartzAdmin.base.shrio;

import com.gk.quartzAdmin.entity.User;
import com.gk.quartzAdmin.service.UserService;
import com.gk.quartzAdmin.util.BeanManager;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 * 用户认证Realm
 * Date:  17/7/28 下午2:27
 */
public class UserRealm extends AuthorizingRealm {

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return new SimpleAuthorizationInfo();
    }

    /**
     * 认证
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        User user = BeanManager.getBean(UserService.class).findUserByLoginName(usernamePasswordToken.getUsername());
        if (user == null) {
            throw new UnknownAccountException();
        }
        if (user.getStatus().equals(User.USER_DISABLE)) {
            throw new DisabledAccountException();
        }
        return new SimpleAuthenticationInfo(
                user, user.getPassword(), ByteSource.Util.bytes(user.getLoginName()), getName());
    }


}
