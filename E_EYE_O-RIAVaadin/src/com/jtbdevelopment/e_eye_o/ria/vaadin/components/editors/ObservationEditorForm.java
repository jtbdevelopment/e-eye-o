package com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors;

import com.google.common.eventbus.EventBus;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters.LocalDateDateConverter;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters.LocalDateTimeDateConverter;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Date: 3/17/13
 * Time: 6:39 PM
 */
@org.springframework.stereotype.Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ObservationEditorForm extends IdObjectEditorForm<Observation> {
    private TextArea commentField;
    private BeanItemContainer<ObservationCategory> potentialCategories;
    private BeanItemContainer<AppUserOwnedObject> potentialSubjects;

    @Autowired
    public ObservationEditorForm(final ReadWriteDAO readWriteDAO, final EventBus eventBus) {
        super(Observation.class, readWriteDAO, eventBus);
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
        boolean showArchivedSubjects = observation.getObservationSubject().isArchived();
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
        commentField = new TextArea();
        commentField.setRows(4);
        commentField.setWidth(40, Unit.EM);
        //  TODO - need to disable/re-enable enter key capture on Default button for text area.
        entityBeanFieldGroup.bind(commentField, "comment");
        row.addComponent(commentField);

        row.addComponent(new Label("Categories:"));
        potentialCategories = new BeanItemContainer<>(ObservationCategory.class);
        TwinColSelect categories = new TwinColSelect();
        categories.setRows(4);
        categories.setImmediate(true);
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
        observationFor.setImmediate(true);
        potentialSubjects = new BeanItemContainer<>(AppUserOwnedObject.class);
        observationFor.setContainerDataSource(potentialSubjects);
        observationFor.setItemCaptionPropertyId("summaryDescription");
        entityBeanFieldGroup.bind(observationFor, "observationSubject");
        row.addComponent(observationFor);

        row.addComponent(new Label("Significant?"));
        CheckBox significant = new CheckBox();
        entityBeanFieldGroup.bind(significant, "significant");
        row.addComponent(significant);

        row.addComponent(new Label("Follow Up?"));
        CheckBox followUp = new CheckBox();
        entityBeanFieldGroup.bind(followUp, "followUpNeeded");
        row.addComponent(followUp);

        row.addComponent(new Label("Reminder?"));
        DateField followUpReminder = new DateField();
        followUpReminder.setConverter(new LocalDateDateConverter());
        followUpReminder.setResolution(Resolution.DAY);
        entityBeanFieldGroup.bind(followUpReminder, "followUpReminder");
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
