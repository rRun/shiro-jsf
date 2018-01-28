package com.fortis.shiro.jsf.model;

public interface IUser {
  /**
   * 账号.
   * @return 账号字段
   */
  String getAccount();

  /**
   * 密码.
   * @return 密码字段
   */
  String getPassword();
}
