package com.fortis.shiro.jsf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HasRoles {
  /**
  * 角色.
  * @return 角色数组
  */
  String[] value();
  /**
  * 权限间的逻辑关系
  * @return 权限逻辑
  */
  Logical logical() default Logical.AND;
}
