package com.fortis.shiro.jsf.tag;

import org.apache.shiro.subject.Subject;

import javax.faces.view.facelets.TagConfig;

public class HasAnyRolesTag extends PermissionTagHandler{

    private static final String ROLE_NAMES_DELIMETER = ",";

    public HasAnyRolesTag(TagConfig config) {
        super(config);
    }

    @Override
    protected boolean showTagBody(String roleNames) {
        boolean hasAnyRole = false;

        Subject subject = getSubject();

        if (subject != null) {
            for (String role : roleNames.split(ROLE_NAMES_DELIMETER)) {
                if (subject.hasRole(role.trim())) {
                    hasAnyRole = true;
                    break;
                }
            }
        }

        return hasAnyRole;
    }
}
