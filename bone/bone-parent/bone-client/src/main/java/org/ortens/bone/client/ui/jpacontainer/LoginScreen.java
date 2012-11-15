package org.ortens.bone.client.ui.jpacontainer;

import com.vaadin.ui.LoginForm.LoginEvent;
import com.vaadin.ui.*;
import com.vaadin.ui.Window.Notification;
import org.apache.shiro.authc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Application's "login" screen
 *
 * @author Can ATAC <can.atac@ortens.com>
 * @version 0.1
 * @since 2012-11-01
 */
public class LoginScreen extends VerticalLayout {

    private Logger logger = LoggerFactory.getLogger(LoginScreen.class);

    private Logger getLogger() {
        return logger;
    }
    private static final long serialVersionUID = 1L;
    private VaadinJPAContainerApplication app;

    private VaadinJPAContainerApplication getApp() {
        return app;
    }

    public LoginScreen(VaadinJPAContainerApplication vaadinJPAContainerApplication) {
        this.app = vaadinJPAContainerApplication;

        // The application caption is shown in the caption bar of the
        // browser window.
        getApp().getMainWindow().setCaption("Vaadin Shiro JPAContainer");
        getLogger().info("4");
        setSizeFull();
        Panel loginPanel = new Panel("Login");
        loginPanel.setWidth("400px");

        LoginForm loginForm = new LoginForm();
        loginForm.setPasswordCaption("Password");
        loginForm.setUsernameCaption("User");
        loginForm.setLoginButtonCaption("Go Client Go 222!!!");

        loginForm.setHeight("100px");
        loginForm.addListener(new LoginListener(this.app, loginForm));
        loginPanel.addComponent(loginForm);

        addComponent(loginPanel);
        setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);

        HorizontalLayout footer = new HorizontalLayout();
        footer.setHeight("50px");
        addComponent(footer);
        getLogger().info("5");
    }

    private static class LoginListener implements LoginForm.LoginListener {

        private Logger logger = LoggerFactory.getLogger(LoginListener.class);

        public Logger getLogger() {
            return logger;
        }
        private static final long serialVersionUID = 1L;
        private VaadinJPAContainerApplication app;

        public VaadinJPAContainerApplication getApp() {
            return app;
        }
        private LoginForm loginForm;
        private static final String UNKNOWN_ACCOUNT          = "Unknown Account";
        private static final String INCORRECT_CREDENTIALS    = "Incorrect Credentials";
        private static final String LOCKED_ACCOUNT           = "Locked Account";
        private static final String EXCESSIVE_ATTEMPTS       = "Excessive Attempts";
        private static final String INVALID_AUTHENTICATION   = "Invalid Authentication";

        public LoginListener(VaadinJPAContainerApplication app, LoginForm loginForm) {
            this.app = app;
            this.loginForm = loginForm;
        }

        @Override
        public void onLogin(LoginEvent event) {
            String username = event.getLoginParameter("username");
            String password = event.getLoginParameter("password");

            try {
                getLogger().info("5-1");
                VaadinJPAContainerApplication.getInstance().login(username, password);
                getLogger().info("5-2");
                // Switch to the protected view
                getApp().getMainWindow().setContent(new DefaultScreen(getApp()));
                getLogger().info("5-3");
            } catch (UnknownAccountException uae) {
                this.loginForm.getWindow().showNotification(UNKNOWN_ACCOUNT, Notification.TYPE_ERROR_MESSAGE);
            } catch (IncorrectCredentialsException ice) {
                this.loginForm.getWindow().showNotification(INCORRECT_CREDENTIALS, Notification.TYPE_ERROR_MESSAGE);
            } catch (LockedAccountException lae) {
                this.loginForm.getWindow().showNotification(LOCKED_ACCOUNT, Notification.TYPE_ERROR_MESSAGE);
            } catch (ExcessiveAttemptsException eae) {
                this.loginForm.getWindow().showNotification(EXCESSIVE_ATTEMPTS, Notification.TYPE_ERROR_MESSAGE);
            } catch (AuthenticationException ae) {
                this.loginForm.getWindow().showNotification(INVALID_AUTHENTICATION, Notification.TYPE_ERROR_MESSAGE);
            } catch (Exception ex) {
                this.loginForm.getWindow().showNotification("Exception " + ex.getMessage(), Notification.TYPE_ERROR_MESSAGE);
            }
        }
    }
}
