package com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors;

import com.jtbdevelopment.e_eye_o.DAO.helpers.PhotoHelper;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters.LocalDateTimeDateConverter;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.ComponentUtils;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.PhotoImageResource;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Runo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * Date: 4/19/13
 * Time: 6:50 AM
 */
@org.springframework.stereotype.Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PhotoEditorDialogWindow extends IdObjectEditorDialogWindow<Photo> {
    private static final Logger logger = LoggerFactory.getLogger(PhotoEditorDialogWindow.class);

    @Autowired
    private PhotoHelper photoHelper;

    private ByteArrayOutputStream uploadedStream;
    private Embedded bigPhoto = new Embedded();
    private BeanItemContainer<AppUserOwnedObject> potentialPhotoFors = new BeanItemContainer<>(AppUserOwnedObject.class);
    private Label mimeType;
    private TextField description = new TextField();
    private Panel photoPanel;

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
        description.setWidth(30, Unit.EM);
        entityBeanFieldGroup.bind(description, "description");
        details.addComponent(description);

        details.addComponent(new Label("Photo For:"));
        final ComboBox photoFor = new ComboBox();
        photoFor.setNewItemsAllowed(false);
        photoFor.setContainerDataSource(potentialPhotoFors);
        photoFor.setItemCaptionPropertyId("summaryDescription");
        entityBeanFieldGroup.bind(photoFor, "photoFor");
        details.addComponent(photoFor);
        details.addComponent(new Label("Taken:"));
        final DateField dateField = new DateField();
        dateField.setResolution(Resolution.SECOND);
        dateField.setConverter(new LocalDateTimeDateConverter());
        entityBeanFieldGroup.bind(dateField, "timestamp");
        details.addComponent(dateField);

        details.addComponent(new Label("Image Type:"));
        mimeType = new Label();
        mimeType.setReadOnly(true);
        details.addComponent(mimeType);

        HorizontalLayout photoRow = new HorizontalLayout();
        photoRow.setSpacing(true);
        final Upload upload = new Upload();
        upload.setButtonCaption("Pick...");
        photoRow.addComponent(upload);

        photoPanel = new Panel();
        bigPhoto.setSizeUndefined();
        photoPanel.setContent(bigPhoto);
        photoPanel.addStyleName(Runo.PANEL_LIGHT);
        photoPanel.setSizeUndefined();
        photoRow.addComponent(photoPanel);
        verticalLayout.addComponent(photoRow);
        verticalLayout.setComponentAlignment(photoRow, Alignment.MIDDLE_CENTER);

        upload.addSucceededListener(new Upload.SucceededListener() {
            @Override
            public void uploadSucceeded(Upload.SucceededEvent event) {
                logger.trace(getSession().getAttribute(AppUser.class).getId() + ": upload completed");
                Photo photo = entityBeanFieldGroup.getItemDataSource().getBean();
                photo.setMimeType(PhotoEditorDialogWindow.this.mimeType.getValue());
                photoHelper.setPhotoImages(photo, uploadedStream.toByteArray());
                photo.setDescription(description.getValue());
                photo.setPhotoFor((AppUserOwnedObject) photoFor.getValue());
                setEntity(photo);
            }
        });
        upload.setReceiver(new Upload.Receiver() {
            @Override
            public OutputStream receiveUpload(String filename, String mimeType) {
                logger.trace(getSession().getAttribute(AppUser.class).getId() + ": upload started " + filename + " (" + mimeType + ")");
                if (!photoHelper.isMimeTypeSupported(mimeType)) {
                    Notification.show("Sorry - this file type is not supported.", Notification.Type.ERROR_MESSAGE);
                    return null;
                }
                upload.setComponentError(null);
                uploadedStream = new ByteArrayOutputStream();
                if (!StringUtils.hasLength(description.getValue())) {
                    description.setValue(filename);
                }
                PhotoEditorDialogWindow.this.mimeType.setValue(mimeType);
                return uploadedStream;
            }
        });

        ComponentUtils.setImmediateForAll(verticalLayout, true);
        return verticalLayout;
    }

    @Override
    protected Focusable getInitialFocusComponent() {
        return description;
    }

    @Override
    public void setEntity(final Photo entity) {
        if (StringUtils.hasLength(entity.getMimeType())) {
            bigPhoto.setSource(new PhotoImageResource(entity));
            bigPhoto.setAlternateText(entity.getDescription());
            mimeType.setValue(entity.getMimeType());
        } else {
            bigPhoto = new Embedded();
            photoPanel.setContent(bigPhoto);
            mimeType.setValue("");
        }
        potentialPhotoFors.removeAllItems();
        if (entity.isArchived()) {
            potentialPhotoFors.addAll(readWriteDAO.getEntitiesForUser(Observation.class, entity.getAppUser()));
            potentialPhotoFors.addAll(readWriteDAO.getEntitiesForUser(ClassList.class, entity.getAppUser()));
        } else {
            potentialPhotoFors.addAll(readWriteDAO.getActiveEntitiesForUser(Observation.class, entity.getAppUser()));
            potentialPhotoFors.addAll(readWriteDAO.getActiveEntitiesForUser(ClassList.class, entity.getAppUser()));
        }
        super.setEntity(entity);
    }
}
