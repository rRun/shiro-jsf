package com.fortis.shiro.jsf.tag;

import com.fortis.shiro.jsf.model.IPermission;
import com.fortis.shiro.jsf.util.FacesUtil;
import org.apache.shiro.subject.Subject;

import javax.faces.view.facelets.TagConfig;

public class HasAnyPermissionTag extends PermissionTagHandler{
    private static final String PERMISSIONS_DELIMETER = ",";

    public HasAnyPermissionTag(TagConfig config) {
        super(config);
    }

    @Override
    protected boolean showTagBody(String permissions) {
        boolean hasAnyPermission = false;

        Subject subject = getSubject();

        if (subject != null) {
            // Iterate through permissions and check to see if the user has one of the permission
            for (String permission : permissions.split(PERMISSIONS_DELIMETER)) {
                String url = FacesUtil.getCurrentViewRoot().getViewId()+':'+permission.trim();
                if (subject.isPermitted(url)) {
                    hasAnyPermission = true;
                    break;
                }
            }
        }
        return hasAnyPermission;
    }
}
