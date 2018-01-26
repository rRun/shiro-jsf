package com.fortis.shiro.jsf.tag;

import org.apache.shiro.subject.PrincipalCollection;

import javax.faces.context.FacesContext;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Modifier;


public class PrincipalTag extends SecureComponent{

    /**
     * 类型
     */
    private String type;
    /**
     * 属性
     */
    private String property;
    /**
     * 默认值
     */
    private String defaultValue;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    // ----------------------------------------------------- 开始编码
    @Override
    protected void doEncodeAll(FacesContext ctx) throws IOException {
        String strValue = null;

        try {
            if (getSubject() != null) {
                // 获取权限
                Object principal;

                if (type == null) {
                    principal = getSubject().getPrincipal();
                } else {
                    principal = getPrincipalFromClassName();
                }

                // 根据属性名获取属性
                if (principal != null) {
                    if (property == null) {
                        strValue = principal.toString();
                    } else {
                        strValue = getPrincipalProperty(principal, property);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error getting principal type [" + type + "], property [" + property + "]: " + e.getMessage(), e);
        }

        if (strValue == null) {
            strValue = defaultValue;
        }

        // 如果str不会空，则输出标签
        if (strValue != null) {
            try {
                ctx.getResponseWriter().write(strValue);
            } catch (IOException e) {
                throw new IOException("Error writing [" + strValue + "] to output.");
            }
        }
    }

    private Object getPrincipalFromClassName() {
        Object principal = null;

        try {
            Class cls = Class.forName(type);
            PrincipalCollection principals = getSubject().getPrincipals();
            if (principals != null) {
                principal = principals.oneByType(cls);
            }
        } catch (ClassNotFoundException e) {
            if (log.isErrorEnabled()) {
                log.error("Unable to find class for name [" + type + "]");
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("Unknown error while getting principal for type [" + type + "]: " + e.getMessage(), e);
            }
        }
        return principal;
    }

    private String getPrincipalProperty(Object principal, String property) throws IOException {
        String strValue = null;

        try {
            BeanInfo bi = Introspector.getBeanInfo(principal.getClass());

            // 遍历属性 找到对应的属性
            boolean foundProperty = false;
            for (PropertyDescriptor pd : bi.getPropertyDescriptors()) {
                if (pd.getName().equals(property) && (Modifier.isPublic(pd.getReadMethod().getModifiers()))) {
                    Object value = null;
                    try {
                        pd.getReadMethod().setAccessible(true);
                        value = pd.getReadMethod().invoke(principal, (Object[]) null);
                    } finally {
                        pd.getReadMethod().setAccessible(false);
                    }
                    strValue = String.valueOf(value);
                    foundProperty = true;
                    break;
                }
            }

            if (!foundProperty) {
                final String message = "Property [" + property + "] not found in principal of type [" + principal.getClass().getName() + "]";
                if (log.isErrorEnabled()) {
                    log.error(message);
                }
                throw new IOException(message);
            }

        } catch (Exception e) {
            final String message = "Error reading property [" + property + "] from principal of type [" + principal.getClass().getName() + "]";
            if (log.isErrorEnabled()) {
                log.error(message, e);
            }
            throw new IOException(message);
        }

        return strValue;
    }

    // ----------------------------------------------------- 状态保存
    private Object[] values;

    @Override
    public Object saveState(FacesContext context) {
        if (values == null) {
            values = new Object[4];
        }
        values[0] = super.saveState(context);
        values[1] = type;
        values[2] = property;
        values[3] = defaultValue;
        return values;
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        values = (Object[]) state;
        super.restoreState(context, values[0]);
        type = (String) values[1];
        property = (String) values[2];
        defaultValue = (String) values[3];
    }
}
