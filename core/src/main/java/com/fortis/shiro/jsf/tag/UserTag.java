package com.fortis.shiro.jsf.tag;

import javax.faces.view.facelets.TagConfig;

public class UserTag extends GuestTag{
    public UserTag(TagConfig config) {
        super(config);
    }

    @Override
    protected boolean checkAuthentication() {
        return !super.checkAuthentication();
    }
}
