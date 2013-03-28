package com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors;

import com.google.common.eventbus.EventBus;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.Observation;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters.LocalDateDateConverter;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters.LocalDateTimeDateConverter;
import com.vaadin.data.util.BeanItemContainer;
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

    @Autowired
    public ObservationEditorForm(final ReadWriteDAO readWriteDAO, final EventBus eventBus) {
        super(Observation.class, readWriteDAO, eventBus);
    }

    @Override
    public void setEntity(Observation entity) {
        super.setEntity(entity);
        boolean hasArchived = false;
        Observation observation = entityBeanFieldGroup.getItemDataSource().getBean();
        potentialCategories.removeAllItems();
        for (ObservationCategory category : observation.getCategories()) {
            if (category.isArchived()) {
                hasArchived = true;
            }
        }
        if (hasArchived) {
            potentialCategories.addAll(readWriteDAO.getEntitiesForUser(ObservationCategory.class, observation.getAppUser()));
        } else {
            potentialCategories.addAll(readWriteDAO.getActiveEntitiesForUser(ObservationCategory.class, observation.getAppUser()));
        }
    }

    @Override
    protected Layout buildEditorLayout() {
        //  TODO - observationSubject, followUpObservation
        VerticalLayout outerLayout = new VerticalLayout();
        outerLayout.setSpacing(true);
        outerLayout.setSizeUndefined();

        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.setSpacing(true);

        firstRow.addComponent(new Label("Comment:"));
        commentField = new TextArea();
        commentField.setRows(4);
        commentField.setWidth(40, Unit.EM);
        //  TODO - need to disable/re-enable enter key capture on Default button for text area.
        entityBeanFieldGroup.bind(commentField, "comment");
        firstRow.addComponent(commentField);

        firstRow.addComponent(new Label("Categories:"));
        potentialCategories = new BeanItemContainer<>(ObservationCategory.class);
        TwinColSelect categories = new TwinColSelect();
        categories.setRows(4);
        categories.setImmediate(true);
        categories.setItemCaptionPropertyId("shortName");
        categories.setContainerDataSource(potentialCategories);
        entityBeanFieldGroup.bind(categories, "categories");
        firstRow.addComponent(categories);

        outerLayout.addComponent(firstRow);
        outerLayout.setComponentAlignment(firstRow, Alignment.MIDDLE_CENTER);

        HorizontalLayout secondRow = new HorizontalLayout();
        secondRow.setSpacing(true);

        secondRow.addComponent(new Label("Significant?"));
        CheckBox significant = new CheckBox();
        entityBeanFieldGroup.bind(significant, "significant");
        secondRow.addComponent(significant);

        secondRow.addComponent(new Label("Follow Up?"));
        CheckBox followUp = new CheckBox();
        entityBeanFieldGroup.bind(followUp, "followUpNeeded");
        secondRow.addComponent(followUp);

        secondRow.addComponent(new Label("Reminder?"));
        DateField followUpReminder = new DateField();
        followUpReminder.setConverter(new LocalDateDateConverter());
        followUpReminder.setResolution(Resolution.DAY);
        entityBeanFieldGroup.bind(followUpReminder, "followUpReminder");
        secondRow.addComponent(followUpReminder);

        secondRow.addComponent(new Label("Observation Time"));
        DateField observationTimestamp = new DateField();
        observationTimestamp.setResolution(Resolution.MINUTE);
        observationTimestamp.setConverter(new LocalDateTimeDateConverter());
        entityBeanFieldGroup.bind(observationTimestamp, "observationTimestamp");
        secondRow.addComponent(observationTimestamp);

        outerLayout.addComponent(secondRow);
        outerLayout.setComponentAlignment(secondRow, Alignment.MIDDLE_CENTER);

        return outerLayout;
    }

    @Override
    protected Focusable getInitialFocusComponent() {
        return commentField;
    }
}
