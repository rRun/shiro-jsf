package com.fortis.shiro.jsf.tag;

import javax.faces.view.facelets.TagConfig;

public class HasPermissionTag extends PermissionTagHandler{

    public HasPermissionTag(TagConfig config) {
        super(config);
    }

    @Override
    protected boolean showTagBody(String p) {
        return isPermitted(p);
    }
}
