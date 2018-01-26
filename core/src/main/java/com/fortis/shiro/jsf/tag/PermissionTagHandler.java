package com.fortis.shiro.jsf.tag;

import com.fortis.shiro.jsf.Constant;
import com.fortis.shiro.jsf.util.FacesUtil;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import java.io.IOException;

public abstract class PermissionTagHandler extends SecureTagHandler{

    private final TagAttribute name;
    public PermissionTagHandler(TagConfig config) {
        super(config);
        this.name = this.getRequiredAttribute("name");
    }

    private String getAttrValue(FaceletContext ctx, TagAttribute attr) {
        String value;
        if (attr.isLiteral()) {
            value = attr.getValue(ctx);
        } else {
            ValueExpression expression = attr.getValueExpression(ctx, String.class);
            value = (String) expression.getValue(ctx);
        }
        return value;
    }

    protected boolean isPermitted(String p) {
        String id = FacesUtil.getCurrentViewRoot().getViewId()+ Constant.DefaultSplitString+p.trim();
        return getSubject() != null && getSubject().isPermitted(id);
    }

    @Override
    public void apply(FaceletContext ctx, UIComponent parent) throws IOException {
        String perm;
        perm = getAttrValue(ctx, name);
        if (showTagBody(perm)) {
            this.nextHandler.apply(ctx, parent);
        }
    }


    protected abstract boolean showTagBody(String p);
}
