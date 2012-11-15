package org.ortens.bone.client.ui.component;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.*;
import javax.naming.NamingException;
import org.ortens.bone.client.ui.jpacontainer.ChangementEditor;
import org.ortens.bone.client.ui.jpacontainer.ChangementEditor.EditorSavedEvent;
import org.ortens.bone.client.ui.jpacontainer.ChangementEditor.EditorSavedListener;
import org.ortens.bone.core.model.Changement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A custom component displaying data in a editable Table
 *
 * @author Can ATAC <can.atac@ortens.com>
 * @version 0.1
 * @since 2012-11-01
 */
public class DataTable extends CustomComponent {

    private Logger logger = LoggerFactory.getLogger(DataTable.class);

    public Logger getLogger() {
        return logger;
    }
    private static final long serialVersionUID = 1L;
    private JPAContainer<Changement> changements;
    private Table contactList;
    private Form contactEditor = new Form();
    private Panel panel;
    private Label label;
    private Button newButton = null;
    private Button deleteButton = null;
    private Button editButton = null;

    public DataTable(JPAContainer<Changement> changements) throws NamingException {
  
        this.changements = changements;
        label = new Label("totototo");
        label.setSizeUndefined();
        panel = new Panel("My Custom Component");

        initLayout();

        setCompositionRoot(panel);
    }

    private void initLayout() {

        
        TextField searchField;
        
        HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
        panel.addComponent(label);
        panel.setContent(splitPanel);
        VerticalLayout left = new VerticalLayout();

        contactList = new Table("The New Changements", changements);
        CheckBox switchEditable = new CheckBox("Editable");
        switchEditable.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                contactList.setEditable(((Boolean) event.getProperty().getValue()).booleanValue());
            }
        });
        switchEditable.setImmediate(true);
        contactList.setSelectable(true);
        contactList.setImmediate(true);
        contactList.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                setModificationsEnabled(event.getProperty().getValue() != null);
            }

            private void setModificationsEnabled(boolean b) {
                deleteButton.setEnabled(b);
                editButton.setEnabled(b);
            }
        });
        contactList.setSizeFull();
        contactList.addListener(new ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    contactList.select(event.getItemId());
                }
            }
        });

        contactList.setVisibleColumns(new Object[]{"title", "description"});

        HorizontalLayout toolbar = new HorizontalLayout();
        newButton = new Button("Add");
        newButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                final BeanItem<Changement> newChangementItem = new BeanItem<Changement>(
                        new Changement());
                ChangementEditor changementEditor = new ChangementEditor(newChangementItem);
                changementEditor.addListener(new EditorSavedListener() {

                    @Override
                    public void editorSaved(EditorSavedEvent event) {
                        changements.addEntity(newChangementItem.getBean());
                    }
                });
                getApplication().getMainWindow().addWindow(changementEditor);
            }
        });

        deleteButton = new Button("Delete");
        deleteButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                getLogger().info("contactList.getValue() : " + contactList.getValue());
                changements.removeItem(contactList.getValue());
                getLogger().info("REMOVE DONE");
            }
        });
        deleteButton.setEnabled(false);

        editButton = new Button("Edit");
        editButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                getApplication().getMainWindow().addWindow(
                        new ChangementEditor(contactList.getItem(contactList.getValue())));
            }
        });
        editButton.setEnabled(false);

        searchField = new TextField();
        searchField.setInputPrompt("Search by name");
        searchField.addListener(new TextChangeListener() {

            @Override
            public void textChange(TextChangeEvent event) {
                getLogger().info("TO DO");
            }
        });

        toolbar.addComponent(newButton);
        toolbar.addComponent(deleteButton);
        toolbar.addComponent(editButton);
        toolbar.addComponent(searchField);
        toolbar.setWidth("100%");
        toolbar.setExpandRatio(searchField, 1);
        toolbar.setComponentAlignment(searchField, Alignment.TOP_RIGHT);

        left.addComponent(toolbar);
        
        left.addComponent(contactList);
        left.addComponent(switchEditable);
        left.setExpandRatio(contactList, 1);
        splitPanel.addComponent(left);
        splitPanel.addComponent(contactEditor);
        contactEditor.setCaption("Contact details editor");
        contactEditor.setSizeFull();
        contactEditor.getLayout().setMargin(true);
        contactEditor.setImmediate(true);

    }
}
