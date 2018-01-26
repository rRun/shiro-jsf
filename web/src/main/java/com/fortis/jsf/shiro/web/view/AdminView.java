package com.fortis.jsf.shiro.web.view;

import org.apache.shiro.web.filter.authc.PassThruAuthenticationFilter;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class AdminView {
    public String logout() {
        return "logout";

    }
}
