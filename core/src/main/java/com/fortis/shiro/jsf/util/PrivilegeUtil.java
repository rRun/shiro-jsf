package com.fortis.shiro.jsf.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrivilegeUtil {
    public static boolean matchParttern(String parttern,String code){
        if (parttern == null || code == null)
            return false;
        Pattern pattern = Pattern.compile(parttern.toLowerCase());
        Matcher matcher = pattern.matcher(code.toLowerCase());
        // 查找字符串中是否有匹配正则表达式的字符/字符串
        return matcher.find();
    }

}
