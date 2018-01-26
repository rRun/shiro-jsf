package com.fortis.shiro.jsf.tag;

import javax.faces.view.facelets.TagConfig;

public class LacksPermissionTag extends PermissionTagHandler{
    public LacksPermissionTag(TagConfig config) {
        super(config);
    }
    @Override
    protected boolean showTagBody(String p) {
        return !isPermitted(p);
    }
}
