package com.fortis.shiro.jsf.tag;

import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;

import com.fortis.shiro.jsf.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * tag_handler的基础类
 */
public abstract class SecureTagHandler extends TagHandler{
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    public SecureTagHandler(TagConfig config) {
        super(config);
    }

    protected Subject getSubject() {
        return SecurityUtil.getUserObject();
    }
}
