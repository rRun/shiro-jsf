package com.fortis.shiro.jsf.tag;

import javax.faces.view.facelets.TagConfig;

public class AuthenticatedTag extends AuthenticationTagHandler{
    public AuthenticatedTag(TagConfig config) {
        super(config);
    }

    @Override
    protected boolean checkAuthentication() {
        return (getSubject() != null && getSubject().isAuthenticated());
    }
}
