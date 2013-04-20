package com.jtbdevelopment.e_eye_o.ria.vaadin.components.photoalbum;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.annotations.PreferredDescription;
import com.jtbdevelopment.e_eye_o.ria.events.IdObjectChanged;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors.PhotoEditorDialogWindow;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.PhotoThumbnailResource;
import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Runo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.Collection;

/**
 * Date: 3/23/13
 * Time: 8:17 PM
 */
@org.springframework.stereotype.Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//  TODO - more overlap with idobjectable than expected - refactor?
//  TODO - how to delete a photo?
//  TODO - some sort of ordering
public class PhotoAlbum extends CustomComponent {
    private AppUserOwnedObject defaultPhotoFor;

    @Autowired
    private EventBus eventBus;

    @Autowired
    private PhotoEditorDialogWindow photoEditorDialogWindow;

    @Autowired
    private ReadOnlyDAO readOnlyDAO;

    @Autowired
    private IdObjectFactory idObjectFactory;

    private final CssLayout photoLayout;

    private IdObject driver;

    public PhotoAlbum() {
        VerticalLayout overall = new VerticalLayout();
        overall.setSizeFull();

        HorizontalLayout actions = new HorizontalLayout();
        actions.setWidth(100, Unit.PERCENTAGE);
        overall.addComponent(actions);

        Button addPhoto = new Button("New " + Photo.class.getAnnotation(PreferredDescription.class).singular());
        addPhoto.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Photo newPhoto = idObjectFactory.newPhoto(getUI().getSession().getAttribute(AppUser.class));
                if (defaultPhotoFor != null) {
                    newPhoto.setPhotoFor(defaultPhotoFor);
                }
                photoEditorDialogWindow.setEntity(newPhoto);
                getUI().addWindow(photoEditorDialogWindow);
            }
        });
        actions.addComponent(addPhoto);
        photoLayout = new CssLayout();
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
        overall.addComponent(photoLayout);
        setCompositionRoot(overall);
        setSizeFull();
    }

    @Override
    public void attach() {
        super.attach();
        eventBus.register(this);
    }

    public void setAlbumDriver(final IdObject driver) {
        while (photoLayout.getComponentCount() > 0) {
            photoLayout.removeComponent(photoLayout.getComponent(0));
        }
        //  TODO - archived/active filters
        //  TODO - show all for a student?
        Collection<Photo> photos;
        if (driver instanceof AppUser) {
            photos = readOnlyDAO.getActiveEntitiesForUser(Photo.class, (AppUser) driver);
        } else if (driver instanceof AppUserOwnedObject) {
            photos = readOnlyDAO.getAllPhotosForEntity((AppUserOwnedObject) driver);
            setDefaultPhotoFor((AppUserOwnedObject) driver);
        } else {
            //  TODO - log/notify
            return;
        }
        for (Photo p : photos) {
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
        this.driver = driver;
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void handleIdObjectChanged(final IdObjectChanged msg) {
        if (msg.getEntity() instanceof Photo) {
            //  TODO - better
            setAlbumDriver(driver);
        }
    }

    public void setDefaultPhotoFor(AppUserOwnedObject defaultPhotoFor) {
        this.defaultPhotoFor = defaultPhotoFor;
    }
}
