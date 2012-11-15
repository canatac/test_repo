package org.ortens.bone.client.ui.jpacontainer;

import com.vaadin.addon.beanvalidation.BeanValidationForm;
import com.vaadin.data.Item;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.*;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.ortens.bone.core.model.Changement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChangementEditor extends Window implements Button.ClickListener,
FormFieldFactory{

    private Logger logger = LoggerFactory.getLogger(ChangementEditor.class);

    public Logger getLogger() {
        return logger;
    }
    
    private final Item changementItem;
    private Form editorForm;
    private Button saveButton;
    private Button cancelButton;

    public ChangementEditor(Item changementItem) {
        this.changementItem = changementItem;
        editorForm = new BeanValidationForm<Changement>(Changement.class);
        editorForm.setFormFieldFactory(this);
        editorForm.setWriteThrough(false);
        editorForm.setImmediate(true);
        editorForm.setItemDataSource(changementItem, Arrays.asList("title", "description"));

        saveButton = new Button("Save", this);
        cancelButton = new Button("Cancel", this);

        editorForm.getFooter().addComponent(saveButton);
        editorForm.getFooter().addComponent(cancelButton);
        getContent().setSizeUndefined();
        addComponent(editorForm);
        setCaption(buildCaption());
    }

    /**
     * @return the caption of the editor window
     */
    private String buildCaption() {
        return String.format("%s %s", changementItem.getItemProperty("title")
                .getValue(), changementItem.getItemProperty("description").getValue());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
     * ClickEvent)
     */
    @Override
    public void buttonClick(ClickEvent event) {
        if (event.getButton() == saveButton) {
            editorForm.commit();
            fireEvent(new EditorSavedEvent(this, changementItem));
        } else if (event.getButton() == cancelButton) {
            editorForm.discard();
        }
        close();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.ui.FormFieldFactory#createField(com.vaadin.data.Item,
     * java.lang.Object, com.vaadin.ui.Component)
     */
    @Override
    public Field createField(Item item, Object propertyId, Component uiContext) {

        Field field = DefaultFieldFactory.get().createField(item, propertyId,
                uiContext);
        if (field instanceof TextField) {
            ((TextField) field).setNullRepresentation("");
        }
        return field;
    }

    public void addListener(EditorSavedListener editorSavedListener) {
        try {
            Method method = EditorSavedListener.class.getDeclaredMethod(
                    "editorSaved", new Class[] { EditorSavedEvent.class });
            addListener(EditorSavedEvent.class, editorSavedListener, method);
        } catch (final java.lang.NoSuchMethodException e) {
            // This should never happen
            getLogger().error("Internal error, editor saved method not found");
        }
    }

    public void removeListener(EditorSavedListener listener) {
        removeListener(EditorSavedEvent.class, listener);
    }

    public static class EditorSavedEvent extends Component.Event {
        
        private Item savedItem;

        public EditorSavedEvent(Component source, Item savedItem) {
            super(source);
            this.savedItem = savedItem;
        }

        public Item getSavedItem() {
            return savedItem;
        }
    }

    public interface EditorSavedListener extends Serializable {
        void editorSaved(EditorSavedEvent event);
    }


}
