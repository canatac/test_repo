package com.ortens;

import com.ortens.pages.SamplerHome;
import com.ortens.pages.forms.LoginForms;
import com.vaadin.Application;
import com.vaadin.service.ApplicationContext;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

//public class TouchKitApplication extends Application {
public class TouchKitApplication extends Application implements ApplicationContext.TransactionListener {

    private static final long serialVersionUID = 5474522369804563317L;
    private static ThreadLocal<TouchKitApplication> currentApplication = new ThreadLocal<TouchKitApplication>();
    private String username ="";

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username   =   username;
    }
    @Override
    public void init() {
        final Window mainWindow = new Window("Sampler");
        final TouchPanel panel = new TouchPanel();

        panel.navigateTo(new SamplerHome());
//        panel.navigateTo(new LoginForms(this));
        mainWindow.setContent(panel);

        setMainWindow(mainWindow);
        setTheme("touch");
    }

    public void login(String username, String password) {
        System.out.println("TouchKitApplication login()");
        setUsername(username);
        UsernamePasswordToken token;

        token = new UsernamePasswordToken(username, password);
        System.out.println("TouchKitApplication login() 2");
        // Remember Me built-in, just do this:
        token.setRememberMe(true);
        System.out.println("TouchKitApplication login() 3");

        // With most of Shiro, you'll always want to make sure you're working with the currently executing user,
        // referred to as the subject
        Subject currentUser = SecurityUtils.getSubject();
        System.out.println("TouchKitApplication login() 4");

        // Authenticate
        currentUser.login(token);
        System.out.println("TouchKitApplication login() 5");
    }

    public void logout() {
        getMainWindow().getApplication().close();

        Subject currentUser = SecurityUtils.getSubject();

        if (currentUser.isAuthenticated()) {
            currentUser.logout();
        }
    }

    public static TouchKitApplication getInstance() {
        System.out.println("TouchKitApplication getInstance()");
        return TouchKitApplication.currentApplication.get();
    }

    @Override
    public void transactionStart(Application application, Object transactionData) {
        System.out.println("TouchKitApplication transactionStart()");
        System.out.println("application : "+ application.toString());
        if (application.equals(TouchKitApplication.this)) {
            TouchKitApplication.currentApplication.set(this);
        }
    }

    @Override
    public void transactionEnd(Application application, Object transactionData) {
        System.out.println("TouchKitApplication transactionEnd()");
        if (application.equals(TouchKitApplication.this)) {
            TouchKitApplication.currentApplication.set(null);

            TouchKitApplication.currentApplication.remove();
        }
    }
    // Logout Listener is defined for the application

    public static class LogoutListener implements Button.ClickListener {

        private static final long serialVersionUID = 1L;
        private TouchKitApplication app;

        public LogoutListener(TouchKitApplication app) {
            this.app = app;
        }

        @Override
        public void buttonClick(Button.ClickEvent event) {
            this.app.logout();
        }
    }
}
