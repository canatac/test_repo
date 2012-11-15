package com.ortens.pages.valueinput;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.ortens.TouchLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class TextInput extends TouchLayout {

    public TextInput() {

        setCaption("Text Input");

        addComponent(new Label(
                "Text inputs are probably the most needed "
                        + "components in any application that require user input or editing."));

        // Normal text field
        final TextField tf = new TextField("Echo this:");
        tf.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                getWindow().showNotification((String) tf.getValue());
            }
        });
        tf.setImmediate(true);

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeUndefined();
        layout.setCaption("Text field");
        layout.addComponent(tf);
        addComponent(layout);

        // Password field
        final TextField pw = new TextField("Enter password:");
        pw.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                getWindow().showNotification((String) pw.getValue());
            }
        });
        pw.setImmediate(true);
        pw.setSecret(true);

        layout = new VerticalLayout();
        layout.setSizeUndefined();
        layout.setCaption("Password field");
        layout.addComponent(pw);
        addComponent(layout);

        // Prompt
        final TextField prompt = new TextField();
        prompt.setInputPrompt("Enter text");
        prompt.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                getWindow().showNotification((String) prompt.getValue());
            }
        });
        prompt.setImmediate(true);

        layout = new VerticalLayout();
        layout.setSizeUndefined();
        layout.setCaption("Field with input prompt");
        layout.addComponent(prompt);
        addComponent(layout);

        // Text Area
        final TextField ta = new TextField(null,
                "The quick brown fox jumps over the lazy dog.");
        ta.setRows(15);
        ta.setColumns(22);
        ta.setImmediate(true);
        ta.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                getWindow().showNotification((String) ta.getValue());
            }
        });

        layout = new VerticalLayout();
        layout.setSizeUndefined();
        layout.setCaption("Text area");
        layout.addComponent(ta);
        addComponent(layout);

    }

}
