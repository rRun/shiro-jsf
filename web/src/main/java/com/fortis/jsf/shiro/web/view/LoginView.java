package com.fortis.jsf.shiro.web.view;

import com.fortis.jsf.shiro.web.service.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class LoginView {
    private User user;

    @PostConstruct
    public void init(){
        user = new User();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String logout(){
        Subject currentUser = SecurityUtils.getSubject();
        try {
            currentUser.logout();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "login";
    }

    public String login(){
        // Example using most common scenario of username/password pair:
        UsernamePasswordToken token = new UsernamePasswordToken(user.getAccount(),
                user.getPassword());

// "Remember Me" built-in:
        token.setRememberMe(user.isRememberMe());

        Subject currentUser = SecurityUtils.getSubject();


        try {
            currentUser.login(token);
        } catch (AuthenticationException e) {
            // Could catch a subclass of AuthenticationException if you like
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage("Login Failed: " + e.getMessage(), e
                            .toString()));
            return "/login";
        }
        return "index";
    }
}
