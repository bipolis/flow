/*
 * Copyright 2000-2014 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.tests.fieldgroup;

import java.util.Iterator;
import java.util.Map;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.shared.util.SharedUtil;
import com.vaadin.tests.components.AbstractTestUIWithLog;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

@Theme("valo")
public abstract class AbstractBasicCrud extends AbstractTestUIWithLog {

    protected AbstractForm form;
    protected static String[] columns = new String[] { "firstName", "lastName",
            "gender", "birthDate", "age", "alive", "address.streetAddress",
            "address.postalCode", "address.city", "address.country" };
    protected BeanItemContainer<ComplexPerson> container = ComplexPerson
            .createContainer(100);;

    {
        container.addNestedContainerBean("address");
    }

    protected ComboBox formType;

    /*
     * (non-Javadoc)
     *
     * @see com.vaadin.tests.components.AbstractTestUI#setup(com.vaadin.server.
     * VaadinRequest)
     */
    @Override
    protected void setup(VaadinRequest request) {
        getLayout().setSizeFull();
        getLayout().setSpacing(true);
        getContent().setSizeFull();
        form = new CustomForm();

        formType = new ComboBox();
        formType.setNullSelectionAllowed(false);
        formType.setWidth("300px");
        formType.addItem(form);
        formType.setValue(form);
        formType.addItem(new AutoGeneratedForm(TextField.class));
        formType.addItem(new AutoGeneratedForm(Field.class));
        Iterator<?> iterator = formType.getItemIds().iterator();
        formType.setItemCaption(iterator.next(), "TextField based form");
        formType.setItemCaption(iterator.next(),
                "Auto generated form (TextFields)");
        formType.setItemCaption(iterator.next(),
                "Auto generated form (Any fields)");
        formType.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                AbstractForm oldForm = form;
                form = (AbstractForm) formType.getValue();
                replaceComponent(oldForm, form);
            }
        });

        addComponent(formType);

    }

    public class CustomForm extends AbstractForm {

        private TextField firstName = new TextField("First name");
        private TextField lastName = new TextField("Last name");
        private TextField gender = new TextField("Gender");
        private TextField birthDate = new TextField("Birth date");
        private TextField age = new TextField("Age");
        private CheckBox alive = new CheckBox("Alive");
        private Label errorLabel = new Label((String) null, ContentMode.HTML);

        @PropertyId("address.streetAddress")
        private TextField address_streetAddress = new TextField(
                "Street address");
        @PropertyId("address.postalCode")
        private TextField address_postalCode = new TextField("Postal code");
        @PropertyId("address.city")
        private TextField address_city = new TextField("City");
        @PropertyId("address.country")
        private TextField address_country = new TextField("Country");

        public CustomForm() {
            fieldGroup.bindMemberFields(this);

            // Last name editing is disabled through property readonly.
            // Postal code editing is disabled through disabling field.
            /*
             * Currently only sets the initial state because of
             * https://dev.vaadin.com/ticket/17847
             *
             * Must set lastName state initially as BeanFieldGroup can't tell it
             * should be read-only before setting an item data source
             */
            lastName.setReadOnly(true);
            address_postalCode.setEnabled(false);

            age.addValidator(new IntegerRangeValidator(
                    "Must be between 0 and 100", 0, 100));

            setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
            addComponents(firstName, lastName, gender, birthDate, age, alive,
                    address_streetAddress, address_postalCode, address_city,
                    address_country);

            errorLabel.addStyleName(ValoTheme.LABEL_COLORED);
            setRows(3);
            addComponent(errorLabel, 0, 2, getColumns() - 1, 2);

            HorizontalLayout hl = new HorizontalLayout(save, cancel);
            hl.setSpacing(true);
            addComponent(hl);

        }

        @Override
        protected void handleCommitException(CommitException e) {
            String message = "";
            // Produce error message in the order in which the fields are in the
            // layout
            for (Component c : this) {
                if (!(c instanceof Field)) {
                    continue;
                }
                Field<?> f = (Field<?>) c;
                Map<Field<?>, InvalidValueException> exceptions = e
                        .getInvalidFields();
                if (exceptions.containsKey(f)) {
                    message += f.getCaption() + ": "
                            + exceptions.get(f).getLocalizedMessage()
                            + "<br/>\n";
                }
            }

            errorLabel.setValue(message);
        }

        @Override
        protected void discard() {
            super.discard();
            errorLabel.setValue(null);
        }
    }

    protected abstract void deselectAll();

    public class AbstractForm extends GridLayout {
        protected Button save = new Button("Save");
        protected Button cancel = new Button("Cancel");

        protected BeanFieldGroup<ComplexPerson> fieldGroup = new BeanFieldGroup<ComplexPerson>(
                ComplexPerson.class) {
            @Override
            protected void configureField(com.vaadin.ui.Field<?> field) {
                super.configureField(field);
                if (field.getCaption().equals("Postal code")) {
                    // Last name editing is disabled through property.
                    // Postal code editing is disabled through field.
                    /*
                     * This is needed because of
                     * https://dev.vaadin.com/ticket/17847
                     */
                    field.setEnabled(false);
                }
            };
        };

        public AbstractForm() {
            super(5, 1);
            setSpacing(true);
            setId("form");
            save.addClickListener(new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    try {
                        fieldGroup.commit();
                        log("Saved " + fieldGroup.getItemDataSource());
                    } catch (CommitException e) {
                        handleCommitException(e);
                        log("Commit failed: " + e.getMessage());
                    }
                }
            });
            cancel.addClickListener(new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    log("Discarded " + fieldGroup.getItemDataSource());
                    discard();
                }
            });
        }

        protected void discard() {
            deselectAll();
        }

        protected void handleCommitException(CommitException e) {
            String message = "";
            for (Object propertyId : e.getInvalidFields().keySet()) {
                Field<?> f = e.getFieldGroup().getField(propertyId);
                message += f.getCaption() + ": "
                        + e.getInvalidFields().get(propertyId);
            }

            if (!message.isEmpty()) {
                Notification.show(message, Type.ERROR_MESSAGE);
            }
        }

        public void edit(BeanItem<ComplexPerson> item) {
            fieldGroup.setItemDataSource(item);
        }
    }

    public class AutoGeneratedForm extends AbstractForm {

        public AutoGeneratedForm(Class<? extends Field> class1) {
            for (String p : columns) {
                Field f = fieldGroup.getFieldFactory()
                        .createField(container.getType(p), class1);
                f.setCaption(SharedUtil.propertyIdToHumanFriendly(p));
                fieldGroup.bind(f, p);
                addComponent(f);
            }

            HorizontalLayout hl = new HorizontalLayout(save, cancel);
            hl.setSpacing(true);
            addComponent(hl);
        }

    }
}
