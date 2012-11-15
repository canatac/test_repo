package com.ortens.pages.forms;


import com.ortens.TouchKitApplication;
import com.ortens.TouchLayout;
import com.ortens.TouchPanel;
import com.ortens.pages.SamplerHome;
import com.ortens.pages.valueinput.ValueInputComponents;
import com.vaadin.ui.*;
import com.vaadin.ui.LoginForm.LoginEvent;
import org.apache.shiro.authc.*;

@SuppressWarnings("serial")
public class LoginForms extends TouchLayout {

    private TouchKitApplication app;
    
    private TouchKitApplication getApp() {
        return app;
    }
    
    public LoginForms(){
        
        addComponent(new Label( 
                "Using normal Vaadin components to build a "
                        + "login form is sometimes sufficient, but in many cases you'll "
                        + "want the browser to remember the credentials later on. Using "
                        + "the LoginForm helps in that case. You can override methods "
                        + "from LoginForm if you wish to specify the generated HTML "
                        + "yourself."));

        VerticalLayout vl = new VerticalLayout();

        LoginForm login = new LoginForm();
        login.setSizeUndefined();
        login.addListener(new LoginForm.LoginListener() {
            public void onLogin(LoginEvent event) {
                getWindow().showNotification(
                        "New Login",
                        "Username: " + event.getLoginParameter("username")
                                + ", password: "
                                + event.getLoginParameter("password"));
            }
        });
        vl.addComponent(login);

        addComponent(vl);        
    }
    
        public LoginForms(TouchKitApplication app){
        this.app = app;
        addComponent(new Label( 
                "Using normal Vaadin components to build a "
                        + "login form is sometimes sufficient, but in many cases you'll "
                        + "want the browser to remember the credentials later on. Using "
                        + "the LoginForm helps in that case. You can override methods "
                        + "from LoginForm if you wish to specify the generated HTML "
                        + "yourself."));

        VerticalLayout vl = new VerticalLayout();

        LoginForm login = new LoginForm();
        login.setSizeUndefined();
        login.addListener(new LoginForm.LoginListener() {
            public void onLogin(LoginEvent event) {
                String username =   event.getLoginParameter("username");
                String password =   event.getLoginParameter("password");
                String UNKNOWN_ACCOUNT          = "Unknown Account";
                String INCORRECT_CREDENTIALS    = "Incorrect Credentials";
                String LOCKED_ACCOUNT           = "Locked Account";
                String EXCESSIVE_ATTEMPTS       = "Excessive Attempts";
                String INVALID_AUTHENTICATION   = "Invalid Authentication";
                getWindow().showNotification(
                        "Ortens Login",
                        "Username: " + username
                                + ", password: "
                                + password);
                try {
                                //getLogger().info("5-1");
                                System.out.println("5-1");
                                System.out.println("LoginForms, getApp().toString() : "+getApp().toString());
                                getApp().login(username, password);
                                //getLogger().info("5-2");
                                System.out.println("5-2");
                                // Switch to the protected view

                //                TouchPanel panel = new TouchPanel();
                                System.out.println("getParent() : "+getParent().toString());
                                System.out.println("getParent().getParent() : "+getParent().getParent().toString());
                                
                                //this.loginForm.getParent().getParent().getParent().getWindow().setContent(new SamplerHome(getApp()));
//                                getParent().setContent(new SamplerHome(getApp()));
                                getParent().navigateTo(new SamplerHome(getApp()));

                //                getApp().getMainWindow().setContent(panel);

                                System.out.println("5-3");
                                //getLogger().info("5-3");
                                } catch (UnknownAccountException uae) {
                                    getWindow().showNotification(UNKNOWN_ACCOUNT, Window.Notification.TYPE_ERROR_MESSAGE);
                                } catch (IncorrectCredentialsException ice) {
                                    getWindow().showNotification(INCORRECT_CREDENTIALS, Window.Notification.TYPE_ERROR_MESSAGE);
                                } catch (LockedAccountException lae) {
                                    getWindow().showNotification(LOCKED_ACCOUNT, Window.Notification.TYPE_ERROR_MESSAGE);
                                } catch (ExcessiveAttemptsException eae) {
                                    getWindow().showNotification(EXCESSIVE_ATTEMPTS, Window.Notification.TYPE_ERROR_MESSAGE);
                                } catch (AuthenticationException ae) {
                                    getWindow().showNotification(INVALID_AUTHENTICATION, Window.Notification.TYPE_ERROR_MESSAGE);
                                } catch (Exception ex) {
                                    getWindow().showNotification("Exception " + ex.getMessage(), Window.Notification.TYPE_ERROR_MESSAGE);
                                }
            }
        });
        vl.addComponent(login);

        addComponent(vl);        
    }
    

}
