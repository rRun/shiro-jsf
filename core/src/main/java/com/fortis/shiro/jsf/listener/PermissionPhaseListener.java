package com.fortis.shiro.jsf.listener;

import com.fortis.shiro.jsf.util.FacesUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.session.ExpiredSessionException;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import java.util.Map;


public class PermissionPhaseListener implements PhaseListener{

    private static String lastViewIdKey = "lastViewId";
    private String loginUrl = null;
    private String errorUrl = null;
    private String successUrl = null;

    public PermissionPhaseListener(String loginUrl,String successUrl,String errorUrl){
        this.loginUrl = loginUrl;
        this.errorUrl = errorUrl;
        this.successUrl = successUrl;
    }

    @Override
    public void afterPhase(PhaseEvent event) {

        UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();

        if (event.getPhaseId() == PhaseId.RESTORE_VIEW){

            //TODO: 鉴权代码
            String lastViewId = (String) SecurityUtils.getSubject().getSession().getAttribute(lastViewIdKey);
            if (lastViewId == null) {
                SecurityUtils.getSubject().getSession().setAttribute(lastViewIdKey,viewId==null?loginUrl:viewId);
            }
            //如果还未认证
                if (!SecurityUtils.getSubject().isAuthenticated() && viewId.indexOf(loginUrl)>=0) {
                    return;
                }else if (!SecurityUtils.getSubject().isAuthenticated() && viewId.indexOf(loginUrl)<0){
                    FacesUtil.gotoNoPermissionPage(loginUrl);
                    viewRoot.setViewId(loginUrl);
                    return;
                }else if (SecurityUtils.getSubject().isAuthenticated() && viewId.indexOf(loginUrl)>=0){
                    FacesUtil.gotoNoPermissionPage(lastViewId);
                    viewRoot.setViewId(lastViewId);
                    return;
                }

//                /systemManager/systemMenu/index.xhtml


            ///systemManager/systemMenu/add.xhtml
            ///systemManager/systemMenu/modify.xhtml

            Map<String,Integer> rules = new HashedMap();rules.put("/systemManager/systemMenu/" ,0);

            rules.put("/systemManager/ddddddd/" ,1);


//            /systemManager/systemMenu/

                try {
                    SecurityUtils.getSubject().checkPermission(viewId);
                    SecurityUtils.getSubject().getSession().setAttribute(lastViewIdKey,viewId);
                } catch (AuthorizationException authorizationException) {//role权限不够
                    FacesUtil.addInfoMessage("没有权限！", null);
                    FacesUtil.gotoNoPermissionPage(errorUrl);

                } catch (ExpiredSessionException exSessionException){
                    FacesUtil.addInfoMessage("认证已过期！", null);
                    FacesUtil.gotoNoPermissionPage(loginUrl);
                }
        }

    }


    @Override
    public void beforePhase(PhaseEvent event) {

    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }


}
