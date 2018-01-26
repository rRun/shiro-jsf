package com.fortis.shiro.jsf.service;

import com.fortis.shiro.jsf.model.IPermission;
import com.fortis.shiro.jsf.model.IRole;
import com.fortis.shiro.jsf.model.IUser;
import java.util.Set;

public interface UserService {

     IUser createUser(IUser user); //创建账户
     void changePassword(IUser oldUser, String newPassword);//修改密码
//    public void correlationRoles(User user, Set<Role> roles); //添加用户-角色关系
//    public void uncorrelationRoles(User user, Set<Role> roles);// 移除用户-角色关系

     IUser findByAccount(String account);// 根据账号查找用户
     Set<IRole> findRoles(String account);// 根据用户名查找其角色
     Set<IPermission> findPermissions(String account); //根据用户名查找其权限
     Set<IPermission> ingorePermissions();
}
