package com.fortis.shiro.jsf.util;

import com.fortis.shiro.jsf.Constant;
import com.fortis.shiro.jsf.model.IUser;
import com.fortis.shiro.jsf.service.UserService;
import com.fortis.shiro.jsf.shiro.UserRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.authz.permission.WildcardPermissionResolver;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SecurityUtil {

    private static Logger logger = LoggerFactory.getLogger(SecurityUtil.class);
    /**
     * 注册配置
     */
    public static void init(){
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        SecurityUtils.setSecurityManager(securityManager);

        //设置authenticator 认证器
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        securityManager.setAuthenticator(authenticator);

        //设置authorizer 授权器
        ModularRealmAuthorizer authorizer = new ModularRealmAuthorizer();
        authorizer.setPermissionResolver(new WildcardPermissionResolver());
        securityManager.setAuthorizer(authorizer);

        //域，Shiro 从从 Realm 获取安全数据（如用户、角色、权限）
        UserRealm userRealm = new UserRealm();
        securityManager.setRealms(Arrays.asList((Realm) userRealm));
    }

    public static void registerService(UserService userService){
        DefaultSecurityManager defaultSecurityManager = (DefaultSecurityManager) SecurityUtils.getSecurityManager();
        for (Realm realm : defaultSecurityManager.getRealms()){
            if (realm instanceof UserRealm){
                ((UserRealm) realm).setUserService(userService);
                break;
            }
        }
    }

    /**
     * 登陆
     * @param user 用户信息
     * @param rememberMe 是否记录账号
     * @return 是否登陆
     */
    public static boolean login(IUser user, boolean rememberMe) {
        UsernamePasswordToken token = new UsernamePasswordToken(user.getAccount(), user.getPassword(), rememberMe);
        token.setRememberMe(rememberMe);
        try {
            getUserObject().login(token);
        } catch (UnknownAccountException ex) {
            FacesUtil.addInfoMessage("Unknown account",null);
            logger.error(ex.getMessage(), ex);
        }
        catch (IncorrectCredentialsException ex) {
            FacesUtil.addInfoMessage("Wrong password",null);
            logger.error(ex.getMessage(), ex);
        }
        catch (LockedAccountException ex) {
            FacesUtil.addInfoMessage("Locked account",null);
            logger.error(ex.getMessage(), ex);
        }
        catch (AuthenticationException ex) {
            FacesUtil.addInfoMessage("Unknown error: " + ex.getMessage(),null);
            logger.error(ex.getMessage(), ex);
        }
        finally {
            token.clear();
        }
        if (getUserObject().isAuthenticated()){
            return true;
        }
        return false;

    }

    public static boolean logout(){
        Subject subject = SecurityUtil.getUserObject();
        if(subject.isAuthenticated()){
            subject.logout();
        }
        return true;
    }

    public static Subject getUserObject(){
        return SecurityUtils.getSubject();
    }

    public static Session getUserSession(){
        return SecurityUtils.getSubject().getSession(true);
    }


    public static boolean hasRole(String role){
        Subject subject = getUserObject();
        return subject.hasRole(role);
    }

    private static final String salt = "6k2w9we825afasf23";

    /**
     * MD5登录密码加密
     */
    private  static String EncodeByMd5(String string) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        //一次MD5容易破解
        String newStr = base64Encoder.encode(md5.digest(string.getBytes("utf-8")));
        return newStr;
    }

    /**
     * 新旧密码验证
     */
    public static boolean checkpassword(String newPassword, String oldpassword){
        if(sercretByString(newPassword).equals(oldpassword)){
            return true;
        } else {
            return false;
        }
    }

    /**
     * 加密字符串
     */
    public static String sercretByString(String string) {
        String result = null;
        try{
            String encode = EncodeByMd5(string);
            result = EncodeByMd5(encode + salt);
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


    public String redirectUrl(String url){

        String rUrl = null;
        if(url != null && !url.contains("?faces-redirect=true")){
            rUrl = url + "?faces-redirect=true";
        }
        return rUrl;
    }

    /**
     * 返回页面所在元素的编码权限
     * @param supUrl 所在页面URL
     * @param elementUrl 元素权限
     * @return
     */
    public static String encodeElementUrl(String supUrl,String elementUrl){
        if (supUrl == null || elementUrl == null){
            return null;
        }

        return supUrl + Constant.DefaultSplitString + elementUrl;
    }

}
