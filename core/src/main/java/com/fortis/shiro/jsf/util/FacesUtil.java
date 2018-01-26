package com.fortis.shiro.jsf.util;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.servlet.ServletContext;
import java.lang.reflect.Method;

public class FacesUtil {

    public static boolean isPostback() {
        return FacesContext.getCurrentInstance().isPostback();
    }

    public static boolean isNotPostback() {
        return !isPostback();
    }

    public static void addErrorMessage(String message,String clientId) {
        FacesContext.getCurrentInstance()
                .addMessage(
                        clientId,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, message,
                                message));
    }

    public static void addInfoMessage(String message,String clientId) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, message, message));
    }

    public static UIViewRoot getCurrentViewRoot(){
        FacesContext currentContext = FacesContext.getCurrentInstance();
        UIViewRoot uiv =currentContext.getViewRoot()!=null?currentContext.getViewRoot():new UIViewRoot();
        return uiv;
    }

    public static ServletContext getCurrentServletContext(){
        FacesContext currentContext = FacesContext.getCurrentInstance();
        return (ServletContext)currentContext.getExternalContext().getContext();
    }

    /**
     * 转入无权限声明页面
     */
    public static void gotoNoPermissionPage(String nextpage) {

        FacesContext context = FacesContext.getCurrentInstance();
        Application application = context.getApplication();
        NavigationHandler navHandler = application.getNavigationHandler();
        navHandler.handleNavigation(context, null, nextpage);
        context.renderResponse();
    }


    public static Class getClassz(String managedBean){
        FacesContext context =FacesContext.getCurrentInstance();
        ValueBinding binding =
                context.getApplication().createValueBinding("#{glBranchFormulaBB}");
        return null;
    }

    public static Method getMethod(Class classz,String methodName){
        return null;
    }

}
