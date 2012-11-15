/*
 * Copyright 2009 IT Mill Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.ortens.bone.client.ui.jpacontainer;

import com.vaadin.Application;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Application's "main" class
 *
 * @author Can ATAC <can.atac@ortens.com>
 * @version 0.1
 * @since 2012-11-01
 */
public class VaadinJPAContainerApplication extends Application implements ApplicationContext.TransactionListener {

    private Logger logger = LoggerFactory.getLogger(VaadinJPAContainerApplication.class);

    public Logger getLogger() {
        return logger;
    }
    private static final long serialVersionUID = 1L;
    private static ThreadLocal<VaadinJPAContainerApplication> currentApplication = new ThreadLocal<VaadinJPAContainerApplication>();
    private Window mainWindow;
    private String username ="";

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username   =   username;
    }

    /**
     * Application entry point.
     */
    @WebServlet(name = "Servlet",
    urlPatterns = "/*")
    public static class Servlet extends AbstractApplicationServlet {

        @Override
        protected Class<? extends Application> getApplicationClass() {
            return VaadinJPAContainerApplication.class;
        }

        @Override
        protected Application getNewApplication(HttpServletRequest request) throws ServletException {
            return new VaadinJPAContainerApplication();
        }
    }

    @Override
    public Window getMainWindow() {
        return mainWindow;
    }

    @Override
    public void init() {
        this.getContext().addTransactionListener(this);
        this.mainWindow = new Window("My TOP GUN Vaadin JPA Client");
        this.setMainWindow(mainWindow);
        mainWindow.setContent(new LoginScreen(this));
    }

    public void login(String username, String password) {
        setUsername(username);
        UsernamePasswordToken token;

        token = new UsernamePasswordToken(username, password);
        // ”Remember Me” built-in, just do this:
        token.setRememberMe(true);

        // With most of Shiro, you'll always want to make sure you're working with the currently executing user,
        // referred to as the subject
        Subject currentUser = SecurityUtils.getSubject();

        // Authenticate
        currentUser.login(token);
    }

    public void logout() {
        getMainWindow().getApplication().close();

        Subject currentUser = SecurityUtils.getSubject();

        if (currentUser.isAuthenticated()) {
            currentUser.logout();
        }
    }

    public static VaadinJPAContainerApplication getInstance() {
        return VaadinJPAContainerApplication.currentApplication.get();
    }

    @Override
    public void transactionStart(Application application, Object transactionData) {
        if (application.equals(VaadinJPAContainerApplication.this)) {
            VaadinJPAContainerApplication.currentApplication.set(this);
        }
    }

    @Override
    public void transactionEnd(Application application, Object transactionData) {
        if (application.equals(VaadinJPAContainerApplication.this)) {
            VaadinJPAContainerApplication.currentApplication.set(null);

            VaadinJPAContainerApplication.currentApplication.remove();
        }
    }

    // Logout Listener is defined for the application
    public static class LogoutListener implements Button.ClickListener {

        private static final long serialVersionUID = 1L;
        private VaadinJPAContainerApplication app;

        public LogoutListener(VaadinJPAContainerApplication app) {
            this.app = app;
        }

        @Override
        public void buttonClick(ClickEvent event) {
            this.app.logout();
        }
    }
}