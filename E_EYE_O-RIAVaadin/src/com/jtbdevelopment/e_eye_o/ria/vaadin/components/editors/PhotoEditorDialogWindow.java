package com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors;

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.ClassList;
import com.jtbdevelopment.e_eye_o.entities.Observation;
import com.jtbdevelopment.e_eye_o.entities.Photo;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters.LocalDateTimeDateConverter;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.PhotoImageResource;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Runo;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Date: 4/19/13
 * Time: 6:50 AM
 */
@org.springframework.stereotype.Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PhotoEditorDialogWindow extends IdObjectEditorDialogWindow<Photo> {
    private Embedded bigPhoto = new Embedded();
    private BeanItemContainer<AppUserOwnedObject> potentialPhotoFors = new BeanItemContainer<>(AppUserOwnedObject.class);
    private Label mimeType;

    public PhotoEditorDialogWindow() {
        super(Photo.class, 100, Unit.PERCENTAGE, 100, Unit.PERCENTAGE);
    }

    @Override
    protected Layout buildEditorLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();

        HorizontalLayout details = new HorizontalLayout();
        verticalLayout.addComponent(details);
        verticalLayout.setComponentAlignment(details, Alignment.MIDDLE_CENTER);
        details.setMargin(true);
        details.setSpacing(true);

        details.addComponent(new Label("Description:"));
        TextField description = new TextField();
        entityBeanFieldGroup.bind(description, "description");
        details.addComponent(description);

        details.addComponent(new Label("Photo For:"));
        ComboBox photoFor = new ComboBox();
        photoFor.setNewItemsAllowed(false);
        photoFor.setContainerDataSource(potentialPhotoFors);
        photoFor.setItemCaptionPropertyId("summaryDescription");
        entityBeanFieldGroup.bind(photoFor, "photoFor");
        details.addComponent(photoFor);
        details.addComponent(new Label("Taken:"));
        DateField dateField = new DateField();
        dateField.setResolution(Resolution.SECOND);
        dateField.setConverter(new LocalDateTimeDateConverter());
        entityBeanFieldGroup.bind(dateField, "timestamp");
        details.addComponent(dateField);

        details.addComponent(new Label("Image Type:"));
        mimeType = new Label();
        mimeType.setReadOnly(true);
        details.addComponent(mimeType);

        Panel panel = new Panel();
        bigPhoto.setSizeUndefined();
        panel.setContent(bigPhoto);
        panel.addStyleName(Runo.PANEL_LIGHT);
        panel.setSizeUndefined();
        verticalLayout.addComponent(panel);
        verticalLayout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
        return verticalLayout;
    }

    @Override
    protected Focusable getInitialFocusComponent() {
        return null;
    }

    @Override
    public void setEntity(final Photo entity) {
        bigPhoto.setSource(new PhotoImageResource(entity));
        bigPhoto.setAlternateText(entity.getDescription());
        potentialPhotoFors.removeAllItems();
        if (entity.isArchived()) {
            potentialPhotoFors.addAll(readWriteDAO.getEntitiesForUser(Observation.class, entity.getAppUser()));
            potentialPhotoFors.addAll(readWriteDAO.getEntitiesForUser(ClassList.class, entity.getAppUser()));
        } else {
            potentialPhotoFors.addAll(readWriteDAO.getActiveEntitiesForUser(Observation.class, entity.getAppUser()));
            potentialPhotoFors.addAll(readWriteDAO.getActiveEntitiesForUser(ClassList.class, entity.getAppUser()));
        }
        mimeType.setCaption(entity.getMimeType());
        super.setEntity(entity);
    }
}
