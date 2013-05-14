package com.jtbdevelopment.e_eye_o.ria.vaadin.components.photoalbum;

import com.google.common.eventbus.Subscribe;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.Photo;
import com.jtbdevelopment.e_eye_o.ria.events.IdObjectChanged;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors.IdObjectEditorDialogWindow;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors.PhotoEditorDialogWindow;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.IdObjectFilterableDisplay;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.generatedcolumns.ArchiveAndDeleteButtons;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.PhotoThumbnailResource;
import com.vaadin.data.Container;
import com.vaadin.event.LayoutEvents;
import com.vaadin.event.MouseEvents;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Runo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Date: 3/23/13
 * Time: 8:17 PM
 */
@org.springframework.stereotype.Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//  TODO - paginatation
//  TODO - some sort of ordering
//  TODO - text filter should go deeper into photo for summary desc perhaps
public class PhotoAlbum extends IdObjectFilterableDisplay<Photo> {
    private AppUserOwnedObject defaultPhotoFor;

    @Autowired
    private PhotoEditorDialogWindow photoEditorDialogWindow;

    private final CssLayout photoLayout = new CssLayout();

    public PhotoAlbum() {
        super(Photo.class);
    }

    @Override
    protected Component buildMainDisplay() {
        photoLayout.setSizeFull();

        photoLayout.setImmediate(true);
        photoLayout.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            private com.vaadin.ui.Component lastSelected = null;

            @Override
            public void layoutClick(final LayoutEvents.LayoutClickEvent event) {
                if (lastSelected != null) {
                    lastSelected.removeStyleName(Runo.CSSLAYOUT_SELECTABLE_SELECTED);
                }
                lastSelected = event.getChildComponent();
                if (lastSelected != null) {
                    lastSelected.addStyleName(Runo.CSSLAYOUT_SELECTABLE_SELECTED);
                }
                if (event.isDoubleClick()) {
                    com.vaadin.ui.Component clicked = event.getClickedComponent();
                    while (clicked instanceof CssLayout) {
                        clicked = ((CssLayout) clicked).getComponent(0);
                    }
                    if (clicked instanceof Embedded) {
                        Embedded embedded = (Embedded) clicked;
                        getUI().addWindow(photoEditorDialogWindow);
                        photoEditorDialogWindow.setEntity(((PhotoThumbnailResource) embedded.getSource()).getPhoto());
                    }
                }
            }
        });
        entities.addItemSetChangeListener(new Container.ItemSetChangeListener() {
            @Override
            public void containerItemSetChange(Container.ItemSetChangeEvent event) {
                refreshPhotos();
            }
        });
        return photoLayout;

    }

    @Override
    public IdObjectEditorDialogWindow<Photo> showEntityEditor(final Photo entity) {
        if (entity.getPhotoFor() == null) {
            entity.setPhotoFor(defaultPhotoFor);
        }
        photoEditorDialogWindow.setEntity(entity);
        getUI().addWindow(photoEditorDialogWindow);
        return photoEditorDialogWindow;
    }

    @Override
    protected void refreshSize() {
        //  TODO
    }

    @Override
    protected void refreshSort() {
        //  TODO
    }

    @Override
    public void setDisplayDriver(final IdObject driver) {
        super.setDisplayDriver(driver);
        //  TODO - show all for a student?
        if (driver instanceof AppUserOwnedObject) {
            entities.addAll(readWriteDAO.getAllPhotosForEntity((AppUserOwnedObject) driver));
            setDefaultPhotoFor((AppUserOwnedObject) driver);
        }
        refreshPhotos();
    }

    private void refreshPhotos() {
        while (photoLayout.getComponentCount() > 0) {
            photoLayout.removeComponent(photoLayout.getComponent(0));
        }
        for (final Photo p : entities.getItemIds()) {
            final Embedded photo = new Embedded(null, new PhotoThumbnailResource(p));
            photo.setAlternateText(p.getDescription());  // TODO - maybe filename?
            photo.setSizeUndefined();

            VerticalLayout photoAndText = new VerticalLayout();
            photoAndText.addComponent(photo);
            Label text = new Label(p.getDescription());
            text.setSizeUndefined();
            photoAndText.addComponent(text);
            photoAndText.setComponentAlignment(photo, Alignment.MIDDLE_CENTER);
            photoAndText.setComponentAlignment(text, Alignment.MIDDLE_CENTER);
            HorizontalLayout buttons = new HorizontalLayout();
            Embedded editButton = new Embedded(null, EDIT);
            editButton.addClickListener(new MouseEvents.ClickListener() {
                @Override
                public void click(final MouseEvents.ClickEvent event) {
                    showEntityEditor(p);
                }
            });
            buttons.addComponent(editButton);
            buttons.addComponent(new Embedded(null, p.isArchived() ? NOT_X : IS_X));
            ArchiveAndDeleteButtons<Photo> actionButtons = new ArchiveAndDeleteButtons<>(readWriteDAO, eventBus, p);
            buttons.addComponent(actionButtons);
            buttons.setSpacing(true);
            photoAndText.addComponent(buttons);
            photoAndText.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);
            photoAndText.setSizeUndefined();

            CssLayout photoInnerLayout = new CssLayout();
            photoInnerLayout.addComponent(photoAndText);
            photoInnerLayout.setSizeUndefined();
            photoInnerLayout.addStyleName("photo");

            CssLayout select = new CssLayout();
            select.addStyleName(Runo.CSSLAYOUT_SELECTABLE);
            select.addStyleName("photo");
            select.addComponent(photoInnerLayout);
            select.setSizeUndefined();

            photoLayout.addComponent(select);
        }
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void handleIdObjectChanged(final IdObjectChanged msg) {
        if (msg.getEntity() instanceof Photo) {
            //  TODO - better
            setDisplayDriver(displayDriver);
        }
    }

    public void setDefaultPhotoFor(AppUserOwnedObject defaultPhotoFor) {
        this.defaultPhotoFor = defaultPhotoFor;
    }
}
