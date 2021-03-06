package com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors;

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.Observable;
import com.jtbdevelopment.e_eye_o.entities.Observation;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.AbstractSelect;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Date: 3/16/13
 * Time: 6:36 PM
 */
@org.springframework.stereotype.Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ObservationEditorDialogWindow extends GeneratedEditorDialogWindow<Observation> {
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
            potentialCategories.addAll(readWriteDAO.getEntitiesForUser(ObservationCategory.class, observation.getAppUser(), 0, 0));
        } else {
            potentialCategories.addAll(readWriteDAO.getActiveEntitiesForUser(ObservationCategory.class, observation.getAppUser(), 0, 0));
        }
        potentialCategories.sort(new String[]{"shortName"}, new boolean[]{true});

        potentialSubjects.removeAllItems();
        boolean showArchivedSubjects = observation.getObservationSubject() != null && observation.getObservationSubject().isArchived();
        if (showArchivedSubjects) {
            potentialSubjects.addAll(readWriteDAO.getEntitiesForUser(Observable.class, observation.getAppUser(), 0, 0));
        } else {
            potentialSubjects.addAll(readWriteDAO.getActiveEntitiesForUser(Observable.class, observation.getAppUser(), 0, 0));
        }
        potentialSubjects.sort(new String[]{"summaryDescription"}, new boolean[]{true});

        super.setEntity(observation);
    }

    @Override
    protected void addDataSourceToSelectField(final String fieldName, final AbstractSelect select) {
        switch (fieldName) {
            case "categories":
                select.setItemCaptionPropertyId("shortName");
                select.setContainerDataSource(potentialCategories);
                break;
            case "observationSubject":
                select.setContainerDataSource(potentialSubjects);
                select.setItemCaptionPropertyId("summaryDescription");
                break;
        }

        super.addDataSourceToSelectField(fieldName, select);
    }
}
