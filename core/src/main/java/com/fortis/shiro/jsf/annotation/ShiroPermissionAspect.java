package com.fortis.shiro.jsf.annotation;

import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by ft on 2018/1/15.
 *
 */
@Aspect
public class ShiroPermissionAspect {

    private static final Logger logger = LoggerFactory.getLogger(ShiroPermissionAspect.class);

    /**
     * 定义Pointcut，Pointcut的名称，此方法不能有返回值，该方法只是一个标示
     */
    @Pointcut("@annotation(com.fortis.shiro.jsf.annotation.CheckGuest)")
    public void controllerAspect() {}
    @Pointcut("@annotation(com.fortis.shiro.jsf.annotation.CheckAuthentication)")
    public void controllerAspect1() {}
    @Pointcut("@annotation(com.fortis.shiro.jsf.annotation.HasPermissions)")
    public void controllerAspect2() {}
    @Pointcut("@annotation(com.fortis.shiro.jsf.annotation.HasRoles)")
    public void controllerAspect3() {}


    /**
     * 环绕通知（Around advice） ：包围一个连接点的通知，类似Web中Servlet规范中的Filter的doFilter方法。可以在方法的调用前后完成自定义的行为，也可以选择不执行。
     * @param joinPoint
     */
    @Around("controllerAspect()||controllerAspect1()||controllerAspect2()||controllerAspect3()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable
    {
        Object obj = null;
        if (checkPermission(joinPoint)){
            obj = joinPoint.proceed();
        }else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "没有权限！");
            FacesContext.getCurrentInstance().addMessage("null", msg);
        }
        return  obj;
    }

    /**
     * 权限判断
     *
     * @param joinPoint
     */
    private boolean checkPermission(JoinPoint joinPoint) {
        try {
            //获得注解
            Annotation[] annotations = getMethodAnnotations(joinPoint);
            for (Annotation annotation : annotations) {
                if (annotation instanceof CheckGuest) {
                    return !SecurityUtils.getSubject().isAuthenticated();
                } else if (annotation instanceof CheckAuthentication) {
                    return SecurityUtils.getSubject().isAuthenticated();
                } else if (annotation instanceof HasPermissions) {
                    HasPermissions hasPermissions = (HasPermissions) annotation;
                    boolean[] resultArray = SecurityUtils.getSubject().isPermitted(hasPermissions.value());
                    return readLogical(resultArray, hasPermissions.logical());
                } else if (annotation instanceof HasRoles) {
                    HasRoles hasRoles = (HasRoles) annotation;
                    boolean[] resultArray = SecurityUtils.getSubject().hasRoles(Arrays.asList(hasRoles.value()));
                    return readLogical(resultArray, hasRoles.logical());
                }

            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return true;
    }

    /**
     * 获得注解
     * @param joinPoint
     * @return
     * @throws Exception
     */
    private static Annotation[] getMethodAnnotations(JoinPoint joinPoint) throws Exception
    {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null)
        {
            return method.getAnnotations();
        }

        return null;
    }

    /**
     * 检查permission 或 role 获得权限
     * @param array
     * @param logical
     * @return
     */
    private boolean readLogical(boolean[] array, Logical logical){
        if (logical == Logical.AND) {
            boolean hasAll = true;
            for (boolean temp : array) {
                if (!temp) {
                    hasAll = false;
                    break;
                }
            }
            return hasAll;
        } else {
            boolean hasAny = false;
            for (boolean temp : array) {
                if (temp) {
                    hasAny = true;
                    break;
                }
            }
            return hasAny;
        }
    }
}
