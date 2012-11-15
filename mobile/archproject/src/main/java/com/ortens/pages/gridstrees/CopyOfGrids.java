package com.ortens.pages.gridstrees;

import com.vaadin.addon.jpacontainer.EntityContainer;
import com.vaadin.addon.jpacontainer.fieldfactory.FieldFactory;

import java.util.HashSet;
import java.util.Set;

import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.Action;
import com.vaadin.terminal.ThemeResource;
import com.ortens.TouchLayout;
import com.ortens.sampler.ExampleUtil;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.CellStyleGenerator;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.ui.CheckBox;
import org.ortens.bone.core.model.Changement;
import org.ortens.bone.core.bean.ChangementEntityProvider;

@SuppressWarnings("serial")
public class CopyOfGrids extends TouchLayout implements Button.ClickListener{

    private JPAContainer<Changement> changements;
    private Table contactList;
    private ChangementEntityProvider changementEjbEntityProvider;
    private FieldFactory fieldFactory;
    
    Table table = new Table("ISO-3166 Country Codes and flags");

    HashSet<Object> markedRows = new HashSet<Object>();

    static final Action ACTION_MARK = new Action("Mark");
    static final Action ACTION_UNMARK = new Action("Unmark");
    static final Action ACTION_LOG = new Action("Save");
    static final Action[] ACTIONS_UNMARKED = new Action[] { ACTION_MARK,
            ACTION_LOG };
    static final Action[] ACTIONS_MARKED = new Action[] { ACTION_UNMARK,
            ACTION_LOG };

    public CopyOfGrids() {

    	changements = JPAContainerFactory.makeJndi(Changement.class);
    	
    	
    	contactList = new Table("The New Changements", changements);
    	
        setCaption("Grids");

        addComponent(new Label(
                "A Table, also known as a (Data)Grid, can be "
                        + "used to show data in a tabular fashion. It's well suited for "
                        + "showing large datasets."));

        // Label to indicate current selection
        final Label selected = new Label("No selection");
        addComponent(selected);

//        addComponent(table);
        addComponent(contactList);

        // set a style name, so we can style rows and cells
        table.setStyleName("iso3166");

        // size
        table.setWidth("100%");
        table.setPageLength(10);

        // selectable
        table.setSelectable(true);
        table.setMultiSelect(true);
        table.setImmediate(true); // react at once when something is selected

        // connect data source
        table.setContainerDataSource(ExampleUtil.getISO3166Container());

        // turn on column reordering and collapsing
        table.setColumnReorderingAllowed(true);
        table.setColumnCollapsingAllowed(true);

        // set column headers
        table.setColumnHeaders(new String[] { "Country", "Code", "Icon file" });

        // Icons for column headers
        table.setColumnIcon(ExampleUtil.iso3166_PROPERTY_FLAG,
                new ThemeResource("../sampler/icons/action_save.gif"));
        table.setColumnIcon(ExampleUtil.iso3166_PROPERTY_NAME,
                new ThemeResource("../sampler/icons/icon_get_world.gif"));
        table.setColumnIcon(ExampleUtil.iso3166_PROPERTY_SHORT,
                new ThemeResource("../sampler/icons/page_code.gif"));

        // Column alignment
        table.setColumnAlignment(ExampleUtil.iso3166_PROPERTY_SHORT,
                Table.ALIGN_CENTER);

        // Column width
        table.setColumnExpandRatio(ExampleUtil.iso3166_PROPERTY_NAME, 1);
        table.setColumnWidth(ExampleUtil.iso3166_PROPERTY_SHORT, 70);

        // Collapse one column - the user can make it visible again
        try {
            table.setColumnCollapsed(ExampleUtil.iso3166_PROPERTY_FLAG, true);
        } catch (Exception e){
            System.err.println(e);
        }
//        catch (IllegalAccessException e) {
//            // Not critical, but strange
//            System.err.println(e);
//        }

        // show row header w/ icon
        table.setRowHeaderMode(Table.ROW_HEADER_MODE_ICON_ONLY);
        table.setItemIconPropertyId(ExampleUtil.iso3166_PROPERTY_FLAG);

        // Actions (a.k.a context menu)
        table.addActionHandler(new Action.Handler() {
            public Action[] getActions(Object target, Object sender) {
                if (markedRows.contains(target)) {
                    return ACTIONS_MARKED;
                } else {
                    return ACTIONS_UNMARKED;
                }
            }

            public void handleAction(Action action, Object sender, Object target) {
                if (ACTION_MARK.equals(action)) {
                    markedRows.add(target);
                    table.requestRepaint();
                } else if (ACTION_UNMARK.equals(action)) {
                    markedRows.remove(target);
                    table.requestRepaint();
                } else if (ACTION_LOG.equals(action)) {
                    Item item = table.getItem(target);
                    addComponent(new Label("Saved: "
                            + target
                            + ", "
                            + item.getItemProperty(
                                    ExampleUtil.iso3166_PROPERTY_NAME)
                                    .getValue()));
                }

            }

        });

        // style generator
        table.setCellStyleGenerator(new CellStyleGenerator() {
            public String getStyle(Object itemId, Object propertyId) {
                if (propertyId == null) {
                    // no propertyId, styling row
                    return (markedRows.contains(itemId) ? "marked" : null);
                } else if (ExampleUtil.iso3166_PROPERTY_NAME.equals(propertyId)) {
                    return "bold";
                } else {
                    // no style
                    return null;
                }

            }

        });

        // listen for valueChange, a.k.a 'select' and update the label
        table.addListener(new Table.ValueChangeListener() {
            public void valueChange(ValueChangeEvent event) {
                // in multiselect mode, a Set of itemIds is returned,
                // in singleselect mode the itemId is returned directly
                Set<?> value = (Set<?>) event.getProperty().getValue();
                if (null == value || value.size() == 0) {
                    selected.setValue("No selection");
                } else {
                    selected.setValue("Selected: " + table.getValue());
                }
            }
        });

        
        CheckBox switchEditable = new CheckBox("Editable");
        switchEditable.setImmediate(true);
        switchEditable.addListener(this); // react to clicks

        addComponent(switchEditable);
//        switchEditable.addListener(new Property.ValueChangeListener() {
//
//            @Override
//            public void valueChange(ValueChangeEvent event) {
//                contactList.setEditable(((Boolean) event.getProperty().getValue()).booleanValue());
//            }
//        });
//        switchEditable.setImmediate(true);
        contactList.setSelectable(true);
        contactList.setImmediate(true);
//        contactList.addListener(new Property.ValueChangeListener() {
//
//            @Override
//            public void valueChange(ValueChangeEvent event) {
//                setModificationsEnabled(event.getProperty().getValue() != null);
//            }
//
//            private void setModificationsEnabled(boolean b) {
//                deleteButton.setEnabled(b);
//                editButton.setEnabled(b);
//            }
//        });
        contactList.setSizeFull();
//        contactList.addListener(new ItemClickListener() {
//
//            @Override
//            public void itemClick(ItemClickEvent event) {
//                if (event.isDoubleClick()) {
//                    contactList.select(event.getItemId());
//                }
//            }
//        });

        contactList.setVisibleColumns(new Object[]{"title", "description"});
    }
    /*
     * Shows a notification when a checkbox is clicked.
     */
    public void buttonClick(ClickEvent event) {
        boolean enabled = event.getButton().booleanValue();
        getWindow().showNotification("click detected");      
       	contactList.setEditable(enabled);
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
}
