package org.arquillian.example;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.jboss.logging.Logger;

@Named
@SessionScoped
public class LoginController implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String SUCCESS_MESSAGE = "Welcome";
    private static final String FAILURE_MESSAGE =
        "Incorrect username and password combination";

    private User currentUser;
    
    @Inject
    private Credentials credentials;
    
    @Inject
    private Logger logger;
    
    public String login() {
        if ("demo".equals(credentials.getUsername()) &&
            "demo".equals(credentials.getPassword())) {
            currentUser = new User("demo");
            logger.info(currentUser.getUsername() + " has signed in.");
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(SUCCESS_MESSAGE));
            return "home.xhtml";
        }

        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_WARN,
                FAILURE_MESSAGE, FAILURE_MESSAGE));
        return null;
    }
    
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    @Produces
    @Named
    public User getCurrentUser() {
        return currentUser;
    }
}