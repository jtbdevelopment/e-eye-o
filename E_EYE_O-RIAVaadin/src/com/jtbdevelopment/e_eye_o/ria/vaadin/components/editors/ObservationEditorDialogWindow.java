package com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors;

import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters.LocalDateDateConverter;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters.LocalDateTimeDateConverter;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Date: 3/16/13
 * Time: 6:36 PM
 */
@org.springframework.stereotype.Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ObservationEditorDialogWindow extends IdObjectEditorDialogWindow<Observation> {
    private final TextArea commentField = new TextArea();
    private final BeanItemContainer<ObservationCategory> potentialCategories = new BeanItemContainer<>(ObservationCategory.class);
    private final BeanItemContainer<AppUserOwnedObject> potentialSubjects = new BeanItemContainer<>(AppUserOwnedObject.class);

    public ObservationEditorDialogWindow() {
        super(Observation.class, 90, 25);
    }

    @Override
    public void setEntity(final Observation entity) {
        super.setEntity(entity);
        Observation observation = entityBeanFieldGroup.getItemDataSource().getBean();
        potentialCategories.removeAllItems();
        boolean hasArchived = false;
        for (ObservationCategory category : observation.getCategories()) {
            if (category.isArchived()) {
                hasArchived = true;
                break;
            }
        }
        if (hasArchived) {
            potentialCategories.addAll(readWriteDAO.getEntitiesForUser(ObservationCategory.class, observation.getAppUser()));
        } else {
            potentialCategories.addAll(readWriteDAO.getActiveEntitiesForUser(ObservationCategory.class, observation.getAppUser()));
        }

        potentialSubjects.removeAllItems();
        boolean showArchivedSubjects = observation.getObservationSubject() != null && observation.getObservationSubject().isArchived();
        if (showArchivedSubjects) {
            potentialSubjects.addAll(readWriteDAO.getEntitiesForUser(ClassList.class, observation.getAppUser()));
            potentialSubjects.addAll(readWriteDAO.getEntitiesForUser(Student.class, observation.getAppUser()));
        } else {
            potentialSubjects.addAll(readWriteDAO.getActiveEntitiesForUser(ClassList.class, observation.getAppUser()));
            potentialSubjects.addAll(readWriteDAO.getActiveEntitiesForUser(Student.class, observation.getAppUser()));
        }
    }

    @Override
    protected Layout buildEditorLayout() {
        //  TODO - followUpObservation
        VerticalLayout outerLayout = new VerticalLayout();
        outerLayout.setSpacing(true);
        outerLayout.setSizeUndefined();

        HorizontalLayout row;

        //

        row = new HorizontalLayout();
        row.setSpacing(true);

        row.addComponent(new Label("Comment:"));
        commentField.setRows(4);
        commentField.setWidth(40, Unit.EM);
        //  TODO - need to disable/re-enable enter key capture on Default button for text area.
        entityBeanFieldGroup.bind(commentField, "comment");
        row.addComponent(commentField);

        row.addComponent(new Label("Categories:"));
        TwinColSelect categories = new TwinColSelect();
        categories.setRows(4);
        categories.setItemCaptionPropertyId("shortName");
        categories.setContainerDataSource(potentialCategories);
        entityBeanFieldGroup.bind(categories, "categories");
        row.addComponent(categories);

        outerLayout.addComponent(row);
        outerLayout.setComponentAlignment(row, Alignment.MIDDLE_CENTER);

        //

        row = new HorizontalLayout();
        row.setSpacing(true);

        row.addComponent(new Label("Observation for:"));
        ComboBox observationFor = new ComboBox();
        observationFor.setFilteringMode(FilteringMode.CONTAINS);
        observationFor.setNewItemsAllowed(false);
        observationFor.setTextInputAllowed(true);
        observationFor.setContainerDataSource(potentialSubjects);
        observationFor.setItemCaptionPropertyId("summaryDescription");
        entityBeanFieldGroup.bind(observationFor, "observationSubject");
        row.addComponent(observationFor);

        row.addComponent(new Label("Significant?"));
        CheckBox significant = new CheckBox();
        entityBeanFieldGroup.bind(significant, "significant");
        row.addComponent(significant);

        row.addComponent(new Label("Follow Up?"));
        final CheckBox followUp = new CheckBox();
        entityBeanFieldGroup.bind(followUp, "followUpNeeded");
        row.addComponent(followUp);

        row.addComponent(new Label("Reminder?"));
        final DateField followUpReminder = new DateField();
        followUpReminder.setConverter(new LocalDateDateConverter());
        followUpReminder.setResolution(Resolution.DAY);
        entityBeanFieldGroup.bind(followUpReminder, "followUpReminder");
        followUpReminder.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (followUpReminder.getValue() != null) {
                    followUp.setValue(true);
                }
            }
        });
        row.addComponent(followUpReminder);

        row.addComponent(new Label("Observation Time"));
        DateField observationTimestamp = new DateField();
        observationTimestamp.setResolution(Resolution.MINUTE);
        observationTimestamp.setConverter(new LocalDateTimeDateConverter());
        entityBeanFieldGroup.bind(observationTimestamp, "observationTimestamp");
        row.addComponent(observationTimestamp);

        outerLayout.addComponent(row);
        outerLayout.setComponentAlignment(row, Alignment.MIDDLE_CENTER);

        return outerLayout;
    }

    @Override
    protected Focusable getInitialFocusComponent() {
        return commentField;
    }
}
