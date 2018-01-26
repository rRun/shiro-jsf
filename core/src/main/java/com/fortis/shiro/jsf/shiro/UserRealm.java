package com.fortis.shiro.jsf.shiro;

import com.fortis.shiro.jsf.model.IPermission;
import com.fortis.shiro.jsf.model.IRole;
import com.fortis.shiro.jsf.model.IUser;
import com.fortis.shiro.jsf.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Set;

public class UserRealm extends AuthorizingRealm {

    /**
     * 用于验证身份
     * @param token
     * @return AuthorizationInfo 身份认证 / 登录，验证用户是不是拥有相应的身份
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String)token.getPrincipal();
        IUser user = userService.findByAccount(username);
        if(user == null) {
            throw new UnknownAccountException();//没找到帐号
        }
//        if(Boolean.TRUE.equals(user.getLocked())) {
//            throw new LockedAccountException(); //帐号锁定
//        }
        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getAccount(), //账号
                user.getPassword(), //密码
                getName()  //realm name
        );
        return authenticationInfo;
    }

    /**
     * 登录之后用于授权
     * @param principals
     * @return AuthorizationInfo 身份认证 / 登录，验证用户是不是拥有相应的身份
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        //获取授权信息
        String account = (String) principals.fromRealm(getName()).iterator().next();
        if (account != null) {
            Set<IRole> roles = userService.findRoles(account);
            Set<IPermission> pers = userService.findPermissions(account);
            Set<IPermission> ignorePers = userService.ingorePermissions();

            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            if (pers != null && !pers.isEmpty()) {
                for (IPermission each : pers) {
                    //将权限资源添加到用户信息中
                    if (each.getPermission() != null && each.getPermission().length()>0)
                        info.addStringPermission(each.getPermission());
                }
            }
            if(ignorePers != null && !ignorePers.isEmpty()){
                for (IPermission each : ignorePers) {
                    //将权限资源添加到用户信息中
                    if (each.getPermission() != null && each.getPermission().length()>0)
                        info.addStringPermission(each.getPermission());
                }
            }
            if (roles!= null && !roles.isEmpty()){
                for (IRole each : roles) {
                    //将角色资源添加到用户信息中
                    if (each.getRole() != null&& each.getRole().length()>0)
                        info.addRole(each.getRole());
                }
            }

            return info;
        }
        return null;
    }

    /**用户的业务类**/
    private UserService userService;
    public UserService getUserService() {
        return userService;
    }
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
