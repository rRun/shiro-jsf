package com.fortis.shiro.jsf.service;

import com.fortis.shiro.jsf.model.IPermission;
import com.fortis.shiro.jsf.model.IRole;
import com.fortis.shiro.jsf.model.IUser;
import java.util.Set;

public interface UserService {
  /**
   * 创建用户.
   * @param user 用户
   * @return 用户
   */
  IUser createUser(IUser user);

  /**
   * 修改密码.
   * @param oldUser 原用户
   * @param newPassword 新密码
   */
  void changePassword(IUser oldUser, String newPassword);

  /**
   * 根据账号查找用户.
   * @param account 账号
   * @return 用户
   */
  IUser findByAccount(String account);

  /**
   * 根据账号查找用户.
   * @param account 账号
   * @return 角色列表
   */
  Set<IRole> findRoles(String account);

  /**
   * 根据用户名查找其权限.
   * @param account 账号
   * @return 权限列表
   */
  Set<IPermission> findPermissions(String account);

  Set<IPermission> ingorePermissions();
}
