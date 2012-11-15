package com.ortens.pages.forms;


import com.ortens.TouchLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.LoginForm;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.LoginForm.LoginEvent;

@SuppressWarnings("serial")
public class LoginFormsBackup extends TouchLayout {

    public LoginFormsBackup() {

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

}
