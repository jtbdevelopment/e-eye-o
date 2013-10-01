package com.jtbdevelopment.e_eye_o.ria.vaadin.components.photoalbum;

import com.google.common.eventbus.Subscribe;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.Photo;
import com.jtbdevelopment.e_eye_o.entities.events.AppUserOwnedObjectChanged;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors.IdObjectEditorDialogWindow;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors.PhotoEditorDialogWindow;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.IdObjectFilterableDisplay;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.generatedcolumns.ArchiveAndDeleteButtons;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.PhotoThumbnailResource;
import com.vaadin.data.Container;
import com.vaadin.data.Validator;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.LayoutEvents;
import com.vaadin.event.MouseEvents;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Runo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
//  TODO - no ordering but default for now
//  TODO - no text search for now
//  TODO - if any more pagination screens are built, some base functionality here
public class PhotoAlbum extends IdObjectFilterableDisplay<Photo> {
    private static final Logger logger = LoggerFactory.getLogger(PhotoAlbum.class);
    static final Resource leftArrow = new ThemeResource("../runo/icons/16/arrow-left.png");
    static final Resource rightArrow = new ThemeResource("../runo/icons/16/arrow-right.png");

    private AppUserOwnedObject defaultPhotoFor;

    private int currentPage = 1;
    private int maxPages = 1;

    @Autowired
    private PhotoEditorDialogWindow photoEditorDialogWindow;

    private final CssLayout photoLayout = new CssLayout();
    private Label maxPagesLabel;
    private TextField currentPageField;
    private Embedded leftButton;
    private Embedded rightButton;

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
        recomputePages();
        jumpToPage(currentPage);
        setDisplayDriver(displayDriver);
        refreshPhotos();
    }

    private void recomputePages() {
        int total;
        int maxSize = ((Integer) showSize.getValue());
        if (displayDriver == null) {
            return;
        }
        if (displayDriver instanceof AppUserOwnedObject) {
            if (activeCB.getValue() && archivedCB.getValue()) {
                total = readWriteDAO.getAllPhotosForEntityCount((AppUserOwnedObject) displayDriver);
            } else if (activeCB.getValue()) {
                total = readWriteDAO.getActivePhotosForEntityCount((AppUserOwnedObject) displayDriver);
            } else {
                total = readWriteDAO.getArchivedPhotosForEntityCount((AppUserOwnedObject) displayDriver);
            }
        } else if (displayDriver instanceof AppUser) {
            if (activeCB.getValue() && archivedCB.getValue()) {
                total = readWriteDAO.getEntitiesForUserCount(entityType, this.appUser);
            } else if (activeCB.getValue()) {
                total = readWriteDAO.getActiveEntitiesForUserCount(entityType, this.appUser);
            } else {
                total = readWriteDAO.getArchivedEntitiesForUserCount(entityType, this.appUser);
            }
        } else {
            return;
        }
        int newMaxPages = total / maxSize;
        if (total % maxSize > 0) {
            ++newMaxPages;
        }
        newMaxPages = Math.max(newMaxPages, 1);
        int newCurrentPage = currentPage;
        if (newCurrentPage > newMaxPages) {
            newCurrentPage = newMaxPages;
        }
        currentPage = newCurrentPage;
        maxPages = newMaxPages;
        updateCurrentPage();
        updateMaxPagesLabel();
    }

    @Override
    protected void refreshSort() {
    }

    @Override
    protected void updateSearchFilter(String searchValue) {
    }

    @Override
    protected void updateActiveArchiveFilters(boolean active, boolean archived) {
        refreshSize();
    }

    @Override
    protected void buildSearchFilter(HorizontalLayout filterSection) {
    }

    @Override
    public void setDisplayDriver(final IdObject driver) {
        if (driver == null) {
            return;
        }
        AppUser appUser = getSession().getAttribute(AppUser.class);
        if (appUser == null) {
            return;
        }
        logger.trace(appUser.getId() + ": display driver changed to " + driver.getId());
        this.displayDriver = driver;
        entities.removeAllItems();
        Collection<Photo> entitiesForUser = java.util.Collections.emptySet();
        if (displayDriver instanceof AppUser) {
            if (displayDriver.equals(this.appUser)) {
                if (activeCB.getValue() && archivedCB.getValue()) {
                    entitiesForUser = readWriteDAO.getEntitiesForUser(entityType, this.appUser, getFirstResult(), getMaxResults());
                } else if (activeCB.getValue()) {
                    entitiesForUser = readWriteDAO.getActiveEntitiesForUser(entityType, this.appUser, getFirstResult(), getMaxResults());
                } else {
                    entitiesForUser = readWriteDAO.getArchivedEntitiesForUser(entityType, this.appUser, getFirstResult(), getMaxResults());
                }
            }
        }
        if (driver instanceof AppUserOwnedObject) {
            setDefaultPhotoFor((AppUserOwnedObject) driver);
            AppUserOwnedObject userOwnedObject = (AppUserOwnedObject) driver;
            if (activeCB.getValue() && archivedCB.getValue()) {
                entitiesForUser = readWriteDAO.getAllPhotosForEntity(userOwnedObject, getFirstResult(), getMaxResults());
            } else if (activeCB.getValue()) {
                entitiesForUser = readWriteDAO.getActivePhotosForEntity(userOwnedObject, getFirstResult(), getMaxResults());
            } else {
                entitiesForUser = readWriteDAO.getArchivedPhotosForEntity(userOwnedObject, getFirstResult(), getMaxResults());
            }
        }
        entities.addAll(entitiesForUser);
        recomputePages();
        refreshPhotos();
    }

    @Override
    protected int getMaxResults() {
        return ((Integer) showSize.getValue());
    }

    @Override
    protected int getFirstResult() {
        return (currentPage - 1) * getMaxResults();
    }

    @Override
    protected void addCustomFilters(HorizontalLayout filterSection) {
        leftButton = new Embedded(null, leftArrow);
        leftButton.setEnabled(false);
        leftButton.addClickListener(new MouseEvents.ClickListener() {
            @Override
            public void click(MouseEvents.ClickEvent event) {
                decreasePage();
            }
        });
        filterSection.addComponent(leftButton);

        currentPageField = new TextField();
        currentPageField.setConverter(new StringToIntegerConverter());
        currentPageField.addStyleName("right-align");
        currentPageField.addValidator(new Validator() {
            @Override
            public void validate(Object value) throws InvalidValueException {
                if (value instanceof Integer) {
                    if (((Integer) value) > 0) {
                        return;
                    }
                }
                throw new InvalidValueException("Positive Numbers Only.");
            }
        });
        currentPageField.setWidth(2, Unit.EM);
        currentPageField.addTextChangeListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(FieldEvents.TextChangeEvent event) {
                jumpToPage(Integer.parseInt(event.getText()));
            }
        });

        filterSection.addComponent(currentPageField);

        maxPagesLabel = new Label();
        filterSection.addComponent(maxPagesLabel);

        rightButton = new Embedded(null, rightArrow);
        rightButton.setEnabled(false);
        rightButton.addClickListener(new MouseEvents.ClickListener() {
            @Override
            public void click(MouseEvents.ClickEvent event) {
                increasePage();
            }
        });
        filterSection.addComponent(rightButton);

        updateMaxPagesLabel();
        updateCurrentPage();
    }

    private void updateCurrentPage() {
        currentPageField.setValue("" + currentPage);
        if (currentPage == 1) {
            leftButton.setEnabled(false);
        } else {
            leftButton.setEnabled(true);
        }
        if (currentPage == maxPages) {
            rightButton.setEnabled(false);
        } else {
            rightButton.setEnabled(true);
        }
    }

    private void updateMaxPagesLabel() {
        maxPagesLabel.setValue(" of " + maxPages);
    }

    private void jumpToPage(final int page) {
        currentPage = page;
        updateCurrentPage();
        setDisplayDriver(displayDriver);
    }

    private void decreasePage() {
        --currentPage;
        jumpToPage(currentPage);
    }

    private void increasePage() {
        ++currentPage;
        jumpToPage(currentPage);
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
    public void handleIdObjectChanged(final AppUserOwnedObjectChanged msg) {
        if (!getSession().getAttribute(AppUser.class).equals(msg.getEntity().getAppUser())) {
            return;
        }
        if (msg.getEntity() instanceof Photo) {
            //  TODO - better
            setDisplayDriver(displayDriver);
        }
    }

    public void setDefaultPhotoFor(AppUserOwnedObject defaultPhotoFor) {
        this.defaultPhotoFor = defaultPhotoFor;
    }
}
