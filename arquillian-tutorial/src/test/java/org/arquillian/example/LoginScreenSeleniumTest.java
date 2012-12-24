package org.arquillian.example;

import java.io.File;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.thoughtworks.selenium.DefaultSelenium;
import java.net.URL;
import org.jboss.arquillian.test.api.ArquillianResource;

@RunWith(Arquillian.class)
public class LoginScreenSeleniumTest {
    private static final String WEBAPP_SRC = "src/main/webapp";
    
    @Deployment(testable = false)
    public static WebArchive createDeployment() {
    	MavenDependencyResolver resolver = DependencyResolvers
                .use(MavenDependencyResolver.class)
                .loadMetadataFromPom("pom.xml");
    	
        return ShrinkWrap.create(WebArchive.class, "login.war")
            .addClasses(Credentials.class, User.class, LoginController.class)
            .addAsLibraries(resolver
                .artifact("org.jboss.seam.solder:seam-solder")
                .resolveAsFiles())
            .addAsWebResource(new File(WEBAPP_SRC, "login.xhtml"))
            .addAsWebResource(new File(WEBAPP_SRC, "home.xhtml"))
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsWebInfResource(
                new StringAsset("<faces-config version=\"2.0\"/>"),
                "faces-config.xml");
    }
    
    @Drone
    DefaultSelenium browser;
    
    @ArquillianResource
    URL deploymentUrl;
    
    @Test
    public void should_login_successfully() {
        browser.open(deploymentUrl + "login.jsf");

        browser.type("id=loginForm:username", "demo");
        browser.type("id=loginForm:password", "demo");
        browser.click("id=loginForm:login");
        browser.waitForPageToLoad("15000");

        Assert.assertTrue("User should be logged in!",
            browser.isElementPresent("xpath=//li[contains(text(), 'Welcome')]"));
        Assert.assertTrue("Username should be shown!",
            browser.isElementPresent("xpath=//p[contains(text(), 'You are signed in as demo.')]"));
    } 
}