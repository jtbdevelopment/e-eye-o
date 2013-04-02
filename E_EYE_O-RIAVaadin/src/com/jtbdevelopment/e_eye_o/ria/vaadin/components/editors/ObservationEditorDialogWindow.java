package com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.Observable;
import com.jtbdevelopment.e_eye_o.entities.Observation;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import com.jtbdevelopment.e_eye_o.ria.events.IdObjectChanged;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters.LocalDateDateConverter;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters.LocalDateTimeDateConverter;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;

/**
 * Date: 3/16/13
 * Time: 6:36 PM
 */
//  TODO - entity level checks (observation self reference etc) don't validate nicely...
@org.springframework.stereotype.Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ObservationEditorDialogWindow extends IdObjectEditorDialogWindow<Observation> {
    private final TextArea commentField = new TextArea();
    private Observation initialFollowUpFor;
    private final BeanItemContainer<ObservationCategory> potentialCategories = new BeanItemContainer<>(ObservationCategory.class);
    private final BeanItemContainer<AppUserOwnedObject> potentialSubjects = new BeanItemContainer<>(AppUserOwnedObject.class);
    private final BeanItemContainer<Observation> potentialFollowUpsFor = new BeanItemContainer<>(Observation.class);

    public ObservationEditorDialogWindow() {
        super(Observation.class, 90, 25);
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

        //  TODO - potentials should update to correct sublist whenever observation subject is changed
        potentialFollowUpsFor.removeAllItems();
        boolean showArchivedObservations = observation.isArchived() || (observation.getFollowUpForObservation() != null && observation.getFollowUpForObservation().isArchived());
        boolean hasSubject = observation.getObservationSubject() != null;
        Collection<Observation> potential;
        if (hasSubject) {
            if (showArchivedObservations) {
                potential = readWriteDAO.getAllObservationsForEntity(observation.getObservationSubject());
            } else {
                //  TODO - shows archived
                potential = readWriteDAO.getAllObservationsForEntity(observation.getObservationSubject());
            }
        } else {
            if (showArchivedObservations) {
                potential = readWriteDAO.getEntitiesForUser(Observation.class, observation.getAppUser());
            } else {
                potential = readWriteDAO.getActiveEntitiesForUser(Observation.class, observation.getAppUser());
            }
        }

        potentialFollowUpsFor.addAll(Collections2.filter(potential, new Predicate<Observation>() {
            //  TODO- also filter on matching categories
            @Override
            public boolean apply(@Nullable final Observation input) {
                return input != null
                        && !input.equals(observation)
                        && input.getObservationTimestamp().compareTo(observation.getObservationTimestamp()) <= 0
                        && !Sets.intersection(input.getCategories(), observation.getCategories()).isEmpty();
            }
        }));
        potentialFollowUpsFor.sort(
                new String[]{"observationSubject.summaryDescription", "followUpNeeded", "observationTimestamp"},  //  TODO - check if first one works
                new boolean[]{true, true, false}  // TODO - check if middle one is correct
        );
        initialFollowUpFor = observation.getFollowUpForObservation();
        super.setEntity(observation);
    }

    public void setFollowUpFor(final Observation initialObservation) {
        Observation observation = entityBeanFieldGroup.getItemDataSource().getBean();
        observation.setFollowUpForObservation(initialObservation);
        observation.setCategories(initialObservation.getCategories());
        setEntity(observation);
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
        row.addComponent(new Label("Follow Up For:"));
        ComboBox followUpForObservation = new ComboBox();
        followUpForObservation.setContainerDataSource(potentialFollowUpsFor);
        followUpForObservation.setItemCaptionPropertyId("summaryDescription");
        entityBeanFieldGroup.bind(followUpForObservation, "followUpForObservation");
        row.addComponent(followUpForObservation);
        outerLayout.addComponent(row);
        outerLayout.setComponentAlignment(row, Alignment.MIDDLE_CENTER);

        //
        row = new HorizontalLayout();
        row.setSpacing(true);

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

    @Override
    protected Observation save() throws FieldGroup.CommitException {
        Observation entity = super.save();
        if (entity != null) {
            Observable observationSubject = readWriteDAO.get(Observable.class, entity.getObservationSubject().getId());
            eventBus.post(new IdObjectChanged<>(IdObjectChanged.ChangeType.MODIFIED, observationSubject));
            if (entity.getFollowUpForObservation() != null) {
                Observation initialObservation = readWriteDAO.get(Observation.class, entity.getFollowUpForObservation().getId());
                eventBus.post(new IdObjectChanged<>(IdObjectChanged.ChangeType.MODIFIED, initialObservation));
            }
            if (initialFollowUpFor != null && !initialFollowUpFor.equals(entity.getFollowUpForObservation())) {
                Observation initialObservation = readWriteDAO.get(Observation.class, initialFollowUpFor.getId());
                eventBus.post(new IdObjectChanged<>(IdObjectChanged.ChangeType.MODIFIED, initialObservation));
            }
        }
        return entity;
    }

    @Override
    protected Observation writeUpdateObjectToDAO(final Observation entity) {
        if (entity.getFollowUpForObservation() == null && initialFollowUpFor == null) {
            return super.writeUpdateObjectToDAO(entity);
        } else {
            if (initialFollowUpFor == null || initialFollowUpFor.equals(entity.getFollowUpForObservation())) {
                return readWriteDAO.linkFollowUpObservation(entity.getFollowUpForObservation(), entity);
            } else {
                //  This is only follow up for initial - reset it to need follow up
                if (readWriteDAO.getAllObservationFollowups(initialFollowUpFor).size() == 1) {   //  Still includes this one
                    initialFollowUpFor.setFollowUpNeeded(true);
                    Collection<Observation> updated = readWriteDAO.update(Arrays.asList(initialFollowUpFor, entity));
                    for (Observation update : updated) {
                        if (update.equals(entity)) {
                            return update;
                        }
                    }
                    return null;
                } else {
                    return super.writeUpdateObjectToDAO(entity);
                }
            }
        }
    }

    @Override
    protected Observation writeNewObjectToDAO(final Observation entity) {
        if (entity.getFollowUpForObservation() == null) {
            return super.writeNewObjectToDAO(entity);
        } else {
            return readWriteDAO.createAndLinkFollowUpObservation(entity.getFollowUpForObservation(), entity);
        }
    }
}
