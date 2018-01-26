package com.fortis.jsf.shiro.web.service;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;


public class DefaultRealm extends AuthorizingRealm{

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String userName = (String) principals.fromRealm(getName()).iterator().next();
        if (userName.equalsIgnoreCase("admin")) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            info.addStringPermission("pages/admin.xhtml");
            info.addStringPermission("pages/index.xhtml");
            info.addRole("admin");
            return info;
        } else if (userName.equalsIgnoreCase("hexy")) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            info.addStringPermission("pages/index.xhtml");
            info.addRole("test");
            return info;
        }else {
            return null;
        }
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        if (token.getUsername().equalsIgnoreCase("admin")) {
            return new SimpleAuthenticationInfo(token.getUsername(), token.getPassword(), getName());
        } if (token.getUsername().equalsIgnoreCase("hexy")) {
            return new SimpleAuthenticationInfo(token.getUsername(), token.getPassword(), getName());
        }else {
            return null;
        }
    }
}
