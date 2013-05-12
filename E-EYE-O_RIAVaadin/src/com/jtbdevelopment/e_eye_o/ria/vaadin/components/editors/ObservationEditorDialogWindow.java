package com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors;

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.Observable;
import com.jtbdevelopment.e_eye_o.entities.Observation;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import com.jtbdevelopment.e_eye_o.ria.events.IdObjectChanged;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters.LocalDateTimeDateConverter;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents;
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
        super(Observation.class, 78, 21);
    }

    @Override
    public void setEntity(final Observation observation) {
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
            potentialSubjects.addAll(readWriteDAO.getEntitiesForUser(Observable.class, observation.getAppUser()));
        } else {
            potentialSubjects.addAll(readWriteDAO.getActiveEntitiesForUser(Observable.class, observation.getAppUser()));
        }

        super.setEntity(observation);
    }

    @Override
    protected Layout buildEditorLayout() {
        VerticalLayout outerLayout = new VerticalLayout();
        outerLayout.setSpacing(true);
        outerLayout.setSizeUndefined();

        HorizontalLayout row;

        //

        row = new HorizontalLayout();
        row.setSpacing(true);

        row.addComponent(new Label("Comment:"));
        commentField.setRows(9);
        commentField.setWidth(40, Unit.EM);
        commentField.addFocusListener(new FieldEvents.FocusListener() {
            @Override
            public void focus(FieldEvents.FocusEvent event) {
                disableDefaultEnterKey();
            }
        });
        commentField.addBlurListener(new FieldEvents.BlurListener() {
            @Override
            public void blur(FieldEvents.BlurEvent event) {
                enableDefaultEnterKey();
            }
        });
        entityBeanFieldGroup.bind(commentField, "comment");
        row.addComponent(commentField);

        row.addComponent(new Label("Categories:"));
        TwinColSelect categories = new TwinColSelect();
        categories.setRows(7);
        categories.setItemCaptionPropertyId("shortName");
        categories.setContainerDataSource(potentialCategories);
        entityBeanFieldGroup.bind(categories, "categories");
        row.addComponent(categories);

        outerLayout.addComponent(row);
        outerLayout.setComponentAlignment(row, Alignment.MIDDLE_CENTER);

        //

        row = new HorizontalLayout();
        row.setSpacing(true);

        row.addComponent(new Label("Observation For:"));
        ComboBox observationFor = new ComboBox();
        observationFor.setFilteringMode(FilteringMode.CONTAINS);
        observationFor.setNewItemsAllowed(false);
        observationFor.setTextInputAllowed(true);
        observationFor.setContainerDataSource(potentialSubjects);
        observationFor.setItemCaptionPropertyId("summaryDescription");
        entityBeanFieldGroup.bind(observationFor, "observationSubject");
        row.addComponent(observationFor);
        outerLayout.addComponent(row);
        outerLayout.setComponentAlignment(row, Alignment.MIDDLE_CENTER);

        row.addComponent(new Label("Significant?"));
        CheckBox significant = new CheckBox();
        entityBeanFieldGroup.bind(significant, "significant");
        row.addComponent(significant);

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

    @Override
    protected Observation save() throws FieldGroup.CommitException {
        Observation entity = super.save();
        if (entity != null) {
            Observable observationSubject = readWriteDAO.get(Observable.class, entity.getObservationSubject().getId());
            eventBus.post(new IdObjectChanged<>(IdObjectChanged.ChangeType.MODIFIED, observationSubject));
        }
        return entity;
    }
}
