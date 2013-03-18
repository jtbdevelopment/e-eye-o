package com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors;

import com.google.common.eventbus.EventBus;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.Observation;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.LocalDateDateConverter;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.LocalDateTimeDateConverter;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;

/**
 * Date: 3/17/13
 * Time: 6:39 PM
 */
public class ObservationEditorForm extends IdObjectEditorForm<Observation> {
    private TextArea commentField;

    public ObservationEditorForm(final ReadWriteDAO readWriteDAO, final EventBus eventBus, final Observation entity) {
        super(Observation.class, readWriteDAO, eventBus, entity);
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
        beanFieldGroup.bind(commentField, "comment");
        firstRow.addComponent(commentField);
        firstRow.addComponent(new Label("Categories:"));
        boolean hasArchived = false;
        int longestCategory = 0;
        for (ObservationCategory category : entity.getCategories()) {
            if (category.isArchived()) {
                hasArchived = true;
            }
            longestCategory = Math.max(longestCategory, category.getShortName().length());
        }
        BeanItemContainer<ObservationCategory> potentialCategories = new BeanItemContainer<>(ObservationCategory.class);
        if (hasArchived) {
            potentialCategories.addAll(readWriteDAO.getEntitiesForUser(ObservationCategory.class, entity.getAppUser()));
        } else {
            potentialCategories.addAll(readWriteDAO.getActiveEntitiesForUser(ObservationCategory.class, entity.getAppUser()));
        }
        TwinColSelect categories = new TwinColSelect();
        categories.setRows(4);
        categories.setContainerDataSource(potentialCategories);
        categories.setItemCaptionPropertyId("shortName");
        beanFieldGroup.bind(categories, "categories");
        firstRow.addComponent(categories);

        outerLayout.addComponent(firstRow);
        outerLayout.setComponentAlignment(firstRow, Alignment.MIDDLE_CENTER);

        HorizontalLayout secondRow = new HorizontalLayout();
        secondRow.setSpacing(true);

        secondRow.addComponent(new Label("Significant?"));
        CheckBox significant = new CheckBox();
        beanFieldGroup.bind(significant, "significant");
        secondRow.addComponent(significant);

        secondRow.addComponent(new Label("Follow Up?"));
        CheckBox followUp = new CheckBox();
        beanFieldGroup.bind(followUp, "followUpNeeded");
        secondRow.addComponent(followUp);

        secondRow.addComponent(new Label("Reminder?"));
        DateField followUpReminder = new DateField();
        followUpReminder.setConverter(new LocalDateDateConverter());
        followUpReminder.setResolution(Resolution.DAY);
        beanFieldGroup.bind(followUpReminder, "followUpReminder");
        secondRow.addComponent(followUpReminder);

        secondRow.addComponent(new Label("Observation Time"));
        DateField observationTimestamp = new DateField();
        observationTimestamp.setResolution(Resolution.MINUTE);
        observationTimestamp.setConverter(new LocalDateTimeDateConverter());
        beanFieldGroup.bind(observationTimestamp, "observationTimestamp");
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
