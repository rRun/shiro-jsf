package com.fortis.shiro.jsf.tag;

import javax.faces.view.facelets.TagConfig;

public class LacksRoleTag extends PermissionTagHandler{
    public LacksRoleTag(TagConfig config) {
        super(config);
    }

    @Override
    protected boolean showTagBody(String roleName) {
        boolean hasRole = getSubject() != null && getSubject().hasRole(roleName);
        return !hasRole;
    }
}
