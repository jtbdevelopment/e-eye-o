package com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors;

import com.jtbdevelopment.e_eye_o.DAO.helpers.PhotoHelper;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.PhotoImageResource;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.Sizeable;
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
public class PhotoEditorDialogWindow extends GeneratedEditorDialogWindow<Photo> {
    private static final Logger logger = LoggerFactory.getLogger(PhotoEditorDialogWindow.class);

    @Autowired
    private PhotoHelper photoHelper;

    private AbstractSelect photoFor;
    private BeanItemContainer<AppUserOwnedObject> potentialPhotoFors = new BeanItemContainer<>(AppUserOwnedObject.class);
    private ByteArrayOutputStream uploadedStream;

    private Embedded bigPhoto = new Embedded();
    private TextField description = new TextField();
    private Panel photoPanel;

    private String mimeType;
    private Upload upload = new Upload();

    public PhotoEditorDialogWindow() {
        super(Photo.class, 100, Sizeable.Unit.PERCENTAGE, 100, Sizeable.Unit.PERCENTAGE);
    }

    @Override
    protected void addDataSourceToSelectField(final String fieldName, final AbstractSelect select) {
        switch (fieldName) {
            case "photoFor":
                photoFor = select;
                select.setContainerDataSource(potentialPhotoFors);
                select.setItemCaptionPropertyId("summaryDescription");
                break;
        }
        super.addDataSourceToSelectField(fieldName, select);
    }

    @Override
    protected Component getCustomField(final String fieldName) {
        switch (fieldName) {
            case "imageData":
                HorizontalLayout photoRow = new HorizontalLayout();
                photoRow.setSpacing(true);

                photoPanel = new Panel();
                bigPhoto.setSizeUndefined();
                photoPanel.setContent(bigPhoto);
                photoPanel.addStyleName(Runo.PANEL_LIGHT);
                photoPanel.setSizeUndefined();
                photoRow.addComponent(photoPanel);

                upload.setButtonCaption("Pick...");
                photoRow.addComponent(upload);

                upload.addSucceededListener(new Upload.SucceededListener() {
                    @Override
                    public void uploadSucceeded(Upload.SucceededEvent event) {
                        logger.trace(getSession().getAttribute(AppUser.class).getId() + ": upload completed");
                        Photo photo = entityBeanFieldGroup.getItemDataSource().getBean();
                        photo.setMimeType(PhotoEditorDialogWindow.this.mimeType);
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
                        PhotoEditorDialogWindow.this.mimeType = mimeType;
                        return uploadedStream;
                    }
                });
                return photoRow;
        }
        return super.getCustomField(fieldName);
    }

    @Override
    public void setEntity(final Photo entity) {
        if (StringUtils.hasLength(entity.getMimeType())) {
            bigPhoto.setSource(new PhotoImageResource(entity));
            bigPhoto.setAlternateText(entity.getDescription());
            mimeType = entity.getMimeType();
            upload.setVisible(false);
        } else {
            bigPhoto = new Embedded();
            photoPanel.setContent(bigPhoto);
            mimeType = "";
            upload.setVisible(true);
        }
        potentialPhotoFors.removeAllItems();
        if (entity.isArchived()) {
            potentialPhotoFors.addAll(readWriteDAO.getEntitiesForUser(Observation.class, entity.getAppUser(), 0, 0));
            potentialPhotoFors.addAll(readWriteDAO.getEntitiesForUser(ClassList.class, entity.getAppUser(), 0, 0));
        } else {
            potentialPhotoFors.addAll(readWriteDAO.getActiveEntitiesForUser(Observation.class, entity.getAppUser(), 0, 0));
            potentialPhotoFors.addAll(readWriteDAO.getActiveEntitiesForUser(ClassList.class, entity.getAppUser(), 0, 0));
        }
        potentialPhotoFors.sort(new String[]{"summaryDescription"}, new boolean[]{true});
        super.setEntity(entity);
    }
}
