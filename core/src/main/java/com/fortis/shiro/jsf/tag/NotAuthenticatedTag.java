package com.fortis.shiro.jsf.tag;

import javax.faces.view.facelets.TagConfig;

public class NotAuthenticatedTag extends AuthenticatedTag{
    public NotAuthenticatedTag(TagConfig config) {
        super(config);
    }

    @Override
    protected boolean checkAuthentication() {
        return !super.checkAuthentication();
    }
}
