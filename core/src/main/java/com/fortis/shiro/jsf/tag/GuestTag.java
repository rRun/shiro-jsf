package com.fortis.shiro.jsf.tag;

import javax.faces.view.facelets.TagConfig;

public class GuestTag extends AuthenticationTagHandler{
    public GuestTag(TagConfig config) {
        super(config);
    }

    @Override
    protected boolean checkAuthentication() {
        return (getSubject() == null || getSubject().getPrincipal() == null);
    }
}
