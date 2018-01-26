package com.fortis.shiro.jsf.tag;

import org.apache.shiro.subject.Subject;

import javax.faces.view.facelets.TagConfig;

public class HasRoleTag extends PermissionTagHandler{

    public HasRoleTag(TagConfig config) {
        super(config);
    }

    @Override
    protected boolean showTagBody(String roleName) {
        return getSubject() != null && getSubject().hasRole(roleName);
    }
}
