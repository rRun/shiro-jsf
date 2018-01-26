package com.fortis.shiro.jsf.tag;

import javax.faces.component.UIComponent;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagConfig;
import java.io.IOException;

/**
 * 检查权限的基础taghandler
 */
public abstract class AuthenticationTagHandler extends SecureTagHandler{
    public AuthenticationTagHandler(TagConfig config) {
        super(config);
    }

    @Override
    public void apply(FaceletContext ctx, UIComponent parent) throws IOException {
        if (showTagBody()) {
            this.nextHandler.apply(ctx, parent);
        }
    }

    protected abstract boolean checkAuthentication();

    protected boolean showTagBody() {
        if (checkAuthentication()) {
            if (log.isTraceEnabled()) {
                log.trace("Authentication successfully verified.  " + "Tag body will be evaluated.");
            }
            return true;
        } else {
            if (log.isTraceEnabled()) {
                log.trace("Authentication verification failed.  " + "Tag body will not be evaluated.");
            }
            return false;
        }
    }
}
