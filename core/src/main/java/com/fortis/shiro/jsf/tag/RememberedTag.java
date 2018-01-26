package com.fortis.shiro.jsf.tag;

import javax.faces.view.facelets.TagConfig;

public class RememberedTag extends AuthenticationTagHandler{
    public RememberedTag(TagConfig config) {
        super(config);
    }

    @Override
    protected boolean checkAuthentication() {
        return (getSubject() != null && getSubject().isRemembered());
    }
}
