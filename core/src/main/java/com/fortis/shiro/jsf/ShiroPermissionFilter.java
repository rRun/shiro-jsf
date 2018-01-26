package com.fortis.shiro.jsf;

import com.fortis.shiro.jsf.listener.PermissionPhaseListener;
import com.fortis.shiro.jsf.util.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.env.IniWebEnvironment;
import org.apache.shiro.web.env.WebEnvironment;
import org.apache.shiro.web.servlet.ShiroFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Created by ft on 2018/1/5.
 */
public class ShiroPermissionFilter extends ShiroFilter{
    @Override
    protected void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws ServletException, IOException {
        super.doFilterInternal(servletRequest, servletResponse, chain);
    }

    @Override
    public void init() throws Exception {
        super.init();

        //初始化SHIRO管理器
        ServletContext sc = getServletContext();
        WebEnvironment env = WebUtils.getRequiredWebEnvironment(sc);
        SecurityUtils.setSecurityManager(env.getWebSecurityManager());

        //TODO:修复奔溃1
        //获取配置的参数
        IniWebEnvironment iniWebEnvironment = (IniWebEnvironment) env;
        String loginUrl = iniWebEnvironment.getIni().get("main").get("authc.loginUrl");
        String errorUrl = iniWebEnvironment.getIni().get("main").get("perms.unauthorizedUrl");
        String successUrl = iniWebEnvironment.getIni().get("main").get("authc.successUrl");
        String libaries = sc.getInitParameter("javax.faces.FACELETS_LIBRARIES");

        //配置TAG标签，增加使用自定义tag
        if (libaries == null){
            String filepath = "shiro-faces.taglib.xml";
            try{
                filepath = this.getClass().getResource("shiro-faces.taglib.xml").getPath();
                System.out.println("paty");
            }catch (Exception ex){
                System.out.println(ex.getLocalizedMessage());
            }
            sc.setInitParameter("javax.faces.FACELETS_LIBRARIES",filepath);
        }
        this.getApplication().addComponent("com.fortis.shiro.jsf.tag.PrincipalTag","com.fortis.shiro.jsf.tag.PrincipalTag");

        //增加JSF自定义的权限拦截器
        this.getLifecycle().addPhaseListener(new PermissionPhaseListener(loginUrl,successUrl,errorUrl));
    }

    /***
     * 获取JSF的APPLICATION
     * @return Application
     */
    private Application getApplication(){
        ApplicationFactory applicationFactory = (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        Application application = applicationFactory.getApplication();
        return application;
    }

    /***
     * 获取JSF的生命周期
     * @return Lifecycle
     */
    public Lifecycle getLifecycle(){
        LifecycleFactory lifecycleFactory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        Lifecycle lifecycle= lifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
        return lifecycle;
    }
}
