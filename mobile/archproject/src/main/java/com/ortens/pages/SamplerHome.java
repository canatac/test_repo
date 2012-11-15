/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ortens.pages;

/**
 *
 * @author canatac
 */
import com.ortens.TouchKitApplication;
import com.ortens.TouchLayout;
import com.ortens.TouchMenu;
import com.ortens.TouchMenu.TouchCommand;
import com.ortens.TouchMenu.TouchMenuItem;
import com.ortens.pages.basics.UiBasics;
import com.ortens.pages.forms.NormalForms;
import com.ortens.pages.gridstrees.GridsAndTrees;
import com.ortens.pages.layoutscontainers.LayoutsAndContainers;
import com.ortens.pages.valueinput.ValueInputComponents;
import com.vaadin.ui.Label;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

@SuppressWarnings("serial")
public class SamplerHome extends TouchLayout {

    private TouchKitApplication app;

    public TouchKitApplication getApp() {
        return app;
    }
    
    public SamplerHome() {
    	initLayout();
    }
    
    public SamplerHome(TouchKitApplication app) {

        this.app = app;
        
        initLayout();
        

    }

    private void initLayout() {
        System.out.println("initLayout de SamplerHome");
        //Subject currentUser = SecurityUtils.getSubject();
        
        
//        setCaption("Vaadin Sampler current User : "+currentUser);
        //setCaption("Vaadin Sampler current User : "+getApp().getUsername());
        System.out.println("initLayout de SamplerHome 2");
        addComponent(new Label("<p>This is the mobile version of the Vaadin "
                + "sampler. It showcases some of the Vaadin components "
                + "that are suited for use in mobile applications. </p>"
                + "<p>You can add the application to you home screen to make "
                + "the application feel more native. </p>", Label.CONTENT_XHTML));
        System.out.println("initLayout de SamplerHome 3");
        TouchMenu menu = new TouchMenu("Samples");
        System.out.println("initLayout de SamplerHome 4");
        menu.addItem("UI Basics", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new UiBasics());
            }
        });
        System.out.println("initLayout de SamplerHome 5");
        menu.addItem("Value Input Components", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new ValueInputComponents());
            }
        });
        System.out.println("initLayout de SamplerHome 6");
        menu.addItem("Forms and Data Model", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                // TODO: Redirected to normal form page, because the iframe
                // in the login form does not render properly on the iPhone
                getParent().navigateTo(new NormalForms());
            }
        });
        System.out.println("initLayout de SamplerHome 6");
        menu.addItem("Grids and Trees", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new GridsAndTrees());
            }
        });
        System.out.println("initLayout de SamplerHome 7");
        menu.addItem("Layouts and Component Containers", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new LayoutsAndContainers());
            }
        });
        System.out.println("initLayout de SamplerHome 8");
        addComponent(menu);
        System.out.println("initLayout de SamplerHome 9");
    }

    
}

