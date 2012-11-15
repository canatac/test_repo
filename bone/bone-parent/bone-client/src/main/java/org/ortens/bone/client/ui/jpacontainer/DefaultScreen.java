package org.ortens.bone.client.ui.jpacontainer;

import com.vaadin.addon.jpacontainer.EntityContainer;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.fieldfactory.FieldFactory;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import javax.naming.NamingException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.ortens.bone.client.ui.component.DataTable;
import org.ortens.bone.core.bean.ChangementEntityProvider;
import org.ortens.bone.core.model.Changement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Application's "default" screen once authenticated
 *
 * @author Can ATAC <can.atac@ortens.com>
 * @version 0.1
 * @since 2012-11-01
 */
public class DefaultScreen extends VerticalLayout {

    private Logger logger = LoggerFactory.getLogger(DefaultScreen.class);

    public Logger getLogger() {
        return logger;
    }
    private VaadinJPAContainerApplication app;
    private Panel mainPanel;
    private Panel contentPanel;
    private JPAContainer<Changement> changements;
    private ChangementEntityProvider changementEjbEntityProvider;
    private FieldFactory fieldFactory;

    public DefaultScreen(VaadinJPAContainerApplication app) throws NamingException {
        this.app = app;
        this.init();
        initJPAContainer();
        initFieldFactory();

    }

    private void init() throws NamingException {
        Subject currentUser = SecurityUtils.getSubject();

        mainPanel = new Panel("Logged in as " + currentUser.getPrincipal().toString());
        contentPanel = new Panel(" ");

        final MenuBar menuBar = new MenuBar();
        Command myCommand = new MyCommand();

        MenuItem menuItem = menuBar.addItem("General", null, null);
        menuItem.addItem("import", null, null);
        menuItem.addItem("export", null, null);
        menuItem.addItem("print", null, null);
        menuItem.addItem(" ---  ", null, null);
        menuItem.addItem("logout", null, myCommand);

        MenuItem browser = menuBar.addItem("Browse", null, null);
        browser.addItem("data", null, myCommand);
        browser.addItem("actions", null, myCommand);

        MenuItem admin = menuBar.addItem("Admin", null, null);
        admin.addItem("users", null, myCommand);
        admin.addItem("localization", null, myCommand);

        MenuItem business = menuBar.addItem("Business", null, null);
        business.addItem("revenue", null, myCommand);

        mainPanel.addComponent(menuBar);
        this.addComponent(mainPanel);
        this.addComponent(contentPanel);

    }

    private class MyCommand implements Command {

        @Override
        public void menuSelected(MenuItem selectedItem) {
            if ("data".equals(selectedItem.getText())) {
                DefaultScreen.this.contentPanel.removeAllComponents();
                try {
                    DefaultScreen.this.contentPanel.addComponent(new DataTable(changements));
                } catch (NamingException e) {
                    getLogger().info("NamingException : " + e);
                }
            } else if ("logout".equals(selectedItem.getText())) {
                DefaultScreen.this.app.logout();
            }
        }
    }

    private void initFieldFactory() {
        fieldFactory = new FieldFactory() {

            @Override
            protected JPAContainer<?> createJPAContainerFor(
                    EntityContainer<?> containerForProperty, Class<?> type, boolean buffered) {
                JPAContainer c = new JPAContainer(type);
                if (type == Changement.class) {
                    c.setEntityProvider(changementEjbEntityProvider);
                }
                return c;
            }
        };
    }

    protected FieldFactory getFieldFactory() {
        return fieldFactory;
    }

    private void initJPAContainer() throws NamingException {
        changements = JPAContainerFactory.makeJndi(Changement.class);
    }
}
