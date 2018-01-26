package com.fortis.shiro.jsf.tag;

import com.fortis.shiro.jsf.util.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import java.io.IOException;


/**
 * JSF基础组件
 *
 */
public abstract class SecureComponent extends UIOutput {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    protected Subject getSubject() {
        return SecurityUtil.getUserObject();
    }

    @Override
    public void encodeAll(FacesContext ctx) throws IOException {
        verifyAttributes();
        doEncodeAll(ctx);
    }

    protected void verifyAttributes() throws IOException {
    }

    protected abstract void doEncodeAll(FacesContext ctx) throws IOException;
}
