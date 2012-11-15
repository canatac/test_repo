package com.ortens.pages.forms;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.StringLengthValidator;
import com.ortens.TouchLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class NormalForms extends TouchLayout {

    Person person;

    public NormalForms() {

        setCaption("Forms and Data Model");

        addComponent(new Label(
                "A Form is most useful when connected to a data "
                        + "source, and provides buffering and customization features to "
                        + "support that scenario. A Form can easily be used as a POJO or "
                        + "Bean editor by wrapping the bean using BeanItem. The basic "
                        + "functionality only requires a couple of lines of code, then "
                        + "Validators and other customizations can be applied to taste. "
                        + "Enter something and try discarding or applying."));

        person = new Person(); // a person POJO
        BeanItem<Person> personItem = new BeanItem<Person>(person); // item from
        // POJO

        // Create the Form
        final Form personForm = new Form();
        personForm.setWriteThrough(false); // we want explicit 'apply'
        personForm.setInvalidCommitted(false); // no invalid values in datamodel

        // FieldFactory for customizing the fields and adding validators
        personForm.setFormFieldFactory(new PersonFieldFactory());
        personForm.setItemDataSource(personItem); // bind to POJO via BeanItem

        // Determines which properties are shown, and in which order:
        personForm
                .setVisibleItemProperties(Arrays.asList(new String[] {
                        "firstName", "lastName", "password", "birthdate",
                        "shoesize" }));

        // Add form to layout
        addComponent(personForm);

        // The cancel / apply buttons
        CssLayout buttons = new CssLayout();
        NativeButton discardChanges = new NativeButton("Discard changes",
                new Button.ClickListener() {
                    public void buttonClick(ClickEvent event) {
                        personForm.discard();
                    }
                });
        buttons.addComponent(discardChanges);

        NativeButton apply = new NativeButton("Apply",
                new Button.ClickListener() {
                    public void buttonClick(ClickEvent event) {
                        try {
                            personForm.commit();
                        } catch (Exception e) {
                            // Ingnored, we'll let the Form handle the errors
                        }
                    }
                });
        buttons.addComponent(apply);

        // button for showing the internal state of the POJO
        NativeButton showPojoState = new NativeButton(
                "Show POJO internal state", new Button.ClickListener() {
                    public void buttonClick(ClickEvent event) {
                        showPojoState();
                    }
                });
        buttons.addComponent(showPojoState);

        addComponent(buttons);
    }

    private void showPojoState() {
        Window.Notification n = new Window.Notification("POJO state",
                Window.Notification.TYPE_TRAY_NOTIFICATION);
        n.setPosition(Window.Notification.POSITION_CENTERED);
        n.setDescription("First name: " + person.getFirstName()
                + "<br/>Last name: " + person.getLastName()
                + "<br/>Birthdate: " + person.getBirthdate()
                + "<br/>Shoe size: " + +person.getShoesize()
                + "<br/>Password: " + person.getPassword());
        getWindow().showNotification(n);
    }

    private class PersonFieldFactory extends DefaultFieldFactory {

        @Override
        public Field createField(Item item, Object propertyId,
                Component uiContext) {
            Field f = super.createField(item, propertyId, uiContext);
            if ("firstName".equals(propertyId)) {
                TextField tf = (TextField) f;
                tf.setRequired(true);
                tf.setRequiredError("Please enter a First Name");
                tf.addValidator(new StringLengthValidator(
                        "First Name must be 3-25 characters", 3, 25, false));
            } else if ("lastName".equals(propertyId)) {
                TextField tf = (TextField) f;
                tf.setRequired(true);
                tf.setRequiredError("Please enter a Last Name");
                tf.addValidator(new StringLengthValidator(
                        "Last Name must be 3-50 characters", 3, 50, false));
            } else if ("password".equals(propertyId)) {
                TextField tf = (TextField) f;
                tf.setSecret(true);
                tf.setRequired(true);
                tf.setRequiredError("Please enter a password");
                tf.addValidator(new StringLengthValidator(
                        "Password must be 6-20 characters", 6, 20, false));
            } else if ("shoesize".equals(propertyId)) {
                TextField tf = (TextField) f;
                tf.addValidator(new IntegerValidator(
                        "Shoe size must be an Integer"));
            }

            return f;
        }
    }

    public class Person implements Serializable {

        private String firstName = "";
        private String lastName = "";
        private Date birthdate;
        private int shoesize = 42;
        private String password = "";

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public Date getBirthdate() {
            return birthdate;
        }

        public void setBirthdate(Date birthdate) {
            this.birthdate = birthdate;
        }

        public int getShoesize() {
            return shoesize;
        }

        public void setShoesize(int shoesize) {
            this.shoesize = shoesize;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

    }

    public class IntegerValidator implements Validator {

        private final String message;

        public IntegerValidator(String message) {
            this.message = message;
        }

        public boolean isValid(Object value) {
            if (value == null || !(value instanceof String)) {
                return false;
            }
            try {
                Integer.parseInt((String) value);
            } catch (Exception e) {
                return false;
            }
            return true;
        }

        public void validate(Object value) throws InvalidValueException {
            if (!isValid(value)) {
                throw new InvalidValueException(message);
            }
        }

    }

}
