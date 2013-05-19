package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.annotations.PreferredDescription;
import com.jtbdevelopment.e_eye_o.entities.reflection.IdObjectInterfaceResolver;
import com.jtbdevelopment.e_eye_o.ria.events.AppUserOwnedObjectChanged;
import com.jtbdevelopment.e_eye_o.ria.events.IdObjectChanged;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors.IdObjectEditorDialogWindow;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.AllItemsBeanItemContainer;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.ComponentUtils;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Runo;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * Date: 4/24/13
 * Time: 6:40 AM
 */
//  TODO - this class is too big
public abstract class IdObjectFilterableDisplay<T extends AppUserOwnedObject> extends CustomComponent {
    protected static final ThemeResource EDIT = new ThemeResource("icons/16/edit.png");
    protected static final ThemeResource NOT_X = new ThemeResource("icons/16/no.png");
    protected static final ThemeResource IS_X = new ThemeResource("icons/16/yes.png");
    private static final Logger logger = LoggerFactory.getLogger(IdObjectFilterableDisplay.class);

    public interface ClickedOnListener<T> {
        void handleClickEvent(final T entity);
    }

    @Autowired
    protected IdObjectInterfaceResolver interfaceResolver;
    @Autowired
    protected IdObjectFactory idObjectFactory;
    // TODO make preference
    protected int maxSize = 10;
    private Container.Filter currentFilter;

    protected final Class<T> entityType;
    protected final TextField searchFor = new TextField();
    @Autowired
    protected EventBus eventBus;
    @Autowired
    protected ReadWriteDAO readWriteDAO;
    protected ClickedOnListener clickedOnListener;
    protected AllItemsBeanItemContainer<T> entities;
    protected AppUser appUser;
    protected IdObject displayDriver;

    protected abstract Component buildMainDisplay();

    public abstract IdObjectEditorDialogWindow<T> showEntityEditor(final T entity);

    protected abstract void refreshSize();

    protected abstract void refreshSort();

    @PostConstruct
    protected void initialize() {
        this.entities = new AllItemsBeanItemContainer<>(entityType);

        Panel panel = new Panel();
        panel.addStyleName(Runo.PANEL_LIGHT);
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.addComponent(buildActionRow());
        mainLayout.addComponent(buildMainDisplay());
        AbstractLayout footer = buildDisplayFooter();
        if (footer != null) {
            mainLayout.addComponent(footer);
        }
        refreshSizeAndSort();

        panel.setContent(mainLayout);
        setCompositionRoot(panel);
        ComponentUtils.setImmediateForAll(this, true);
    }

    protected Container.Filter generateFilter(final String searchFor) {
        return new SimpleStringFilter("summaryDescription", searchFor, true, false);
    }

    protected Layout buildActionRow() {
        HorizontalLayout actionRow = new HorizontalLayout();
        Layout buttonSection = buildActionButtons();
        actionRow.addComponent(buttonSection);
        actionRow.setComponentAlignment(buttonSection, Alignment.BOTTOM_LEFT);

        Layout filterSection = buildFilterOptions();
        actionRow.addComponent(filterSection);
        actionRow.setComponentAlignment(filterSection, Alignment.BOTTOM_RIGHT);
        actionRow.setWidth(100, Unit.PERCENTAGE);
        actionRow.setExpandRatio(filterSection, 1.0f);
        return actionRow;
    }

    private Layout buildFilterOptions() {
        HorizontalLayout filterSection = new HorizontalLayout();
        filterSection.setWidth(null);
        filterSection.setSpacing(true);

        Label searchForLabel = new Label("Search For:");
        filterSection.addComponent(searchForLabel);
        filterSection.setComponentAlignment(searchForLabel, Alignment.BOTTOM_LEFT);
        searchFor.setWidth(12, Unit.EM);
        searchFor.addTextChangeListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(final FieldEvents.TextChangeEvent event) {
                if (currentFilter != null) {
                    entities.removeContainerFilter(currentFilter);
                }

                final String searchValue = event.getText();
                logger.trace(getSession().getAttribute(AppUser.class).getId() + ": search text now " + searchValue);
                if (!StringUtil.isBlank(searchValue)) {
                    currentFilter = generateFilter(searchValue);
                    entities.addContainerFilter(currentFilter);
                }
                refreshSizeAndSort();
            }
        });
        filterSection.addComponent(searchFor);
        filterSection.setComponentAlignment(searchFor, Alignment.BOTTOM_LEFT);

        Label showWhichLabel = new Label("Show:");
        filterSection.addComponent(showWhichLabel);
        filterSection.setComponentAlignment(showWhichLabel, Alignment.BOTTOM_LEFT);

        final CheckBox activeCB = new CheckBox("Active");
        final CheckBox archivedCB = new CheckBox("Archived");
        //  TODO - configurable
        activeCB.setValue(Boolean.TRUE);
        //  TODO - configurable
        archivedCB.setValue(Boolean.FALSE);
        activeCB.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                logger.trace(getSession().getAttribute(AppUser.class).getId() + ": changing active/archived to " + activeCB.getValue() + "/" + archivedCB.getValue());
                setActiveArchiveFilters(activeCB.getValue(), archivedCB.getValue());
            }
        });
        archivedCB.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                logger.trace(getSession().getAttribute(AppUser.class).getId() + ": changing active/archived to " + activeCB.getValue() + "/" + archivedCB.getValue());
                setActiveArchiveFilters(activeCB.getValue(), archivedCB.getValue());
            }
        });
        filterSection.addComponent(activeCB);
        filterSection.addComponent(archivedCB);
        filterSection.setComponentAlignment(activeCB, Alignment.BOTTOM_LEFT);
        filterSection.setComponentAlignment(archivedCB, Alignment.BOTTOM_LEFT);


        addCustomFilters(filterSection);

        Label showSizeLabel = new Label("How Many?");
        filterSection.addComponent(showSizeLabel);
        filterSection.setComponentAlignment(showSizeLabel, Alignment.BOTTOM_LEFT);

        final NativeSelect showSize = new NativeSelect(null, Arrays.asList(1, 5, 10, 25, 50));
        showSize.setValue(maxSize);
        showSize.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
                logger.trace(getSession().getAttribute(AppUser.class).getId() + ": changing max size to " + showSize.getValue());
                maxSize = ((Integer) showSize.getValue());
                refreshSize();
            }
        });
        showSize.setWidth(4, Unit.EM);
        filterSection.addComponent(showSize);
        filterSection.setComponentAlignment(showSize, Alignment.BOTTOM_LEFT);
        return filterSection;
    }

    private void setActiveArchiveFilters(final boolean active, final boolean archived) {
        entities.removeContainerFilters("archived");
        if (!(active && archived)) {
            if (active) {
                entities.addContainerFilter("archived", "false", false, true);
            } else {
                entities.addContainerFilter("archived", "true", false, true);
            }
        }
        refreshSizeAndSort();
    }

    private Layout buildActionButtons() {
        //  Buttons
        HorizontalLayout buttonSection = new HorizontalLayout();
        buttonSection.setWidth(null);
        buttonSection.setSpacing(true);

        Button newEntityButton = new Button("New " + interfaceResolver.getIdObjectInterfaceForClass(entityType).getAnnotation(PreferredDescription.class).singular());
        newEntityButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                logger.trace(getSession().getAttribute(AppUser.class).getId() + ": press new entity button for " + entityType.getSimpleName());
                showEntityEditor(idObjectFactory.newAppUserOwnedObject(entityType, appUser));
            }
        });
        buttonSection.addComponent(newEntityButton);
        buttonSection.setComponentAlignment(newEntityButton, Alignment.BOTTOM_LEFT);
        return buttonSection;
    }

    @SuppressWarnings("unchecked")
    protected T handleSelectionChange(final Object item) {
        T entity;
        if (item instanceof BeanItem) {
            entity = ((BeanItem<T>) item).getBean();
        } else if (item != null && entityType.isAssignableFrom(item.getClass())) {
            entity = (T) item;
        } else {
            logger.warn(getSession().getAttribute(AppUser.class).getId() + ": clicked on item and didn't know what to do with it " + (item == null ? null : item.getClass().getSimpleName()));
            return null;
        }
        logger.trace(getSession().getAttribute(AppUser.class).getId() + ": clicked on " + entity.getId());
        if (clickedOnListener != null) {
            clickedOnListener.handleClickEvent(entity);
        }
        return entity;
    }

    @Subscribe
    @SuppressWarnings({"unused", "unchecked"})
    public void handleIdObjectChange(final AppUserOwnedObjectChanged msg) {
        if (!getSession().getAttribute(AppUser.class).equals(msg.getEntity().getAppUser())) {
            return;
        }
        if (entityType.isAssignableFrom(msg.getEntityType())) {
            T entity = (T) msg.getEntity();
            final T idForBean = entities.getBeanIdResolver().getIdForBean(entity);
            if (idForBean != null) {
                entities.removeItem(idForBean);
            }
            if (!IdObjectChanged.ChangeType.DELETED.equals(msg.getChangeType())) {
                boolean relatedToTableDriver = false;
                if (displayDriver instanceof AppUser) {
                    relatedToTableDriver = displayDriver.equals(entity.getAppUser());
                } else {
                    if (displayDriver instanceof ClassList && entity instanceof Student) {
                        relatedToTableDriver = ((Student) entity).getClassLists().contains(displayDriver);
                    }
                    if ((displayDriver instanceof ClassList || displayDriver instanceof Student) && entity instanceof Observation) {
                        relatedToTableDriver = ((Observation) entity).getObservationSubject().equals(displayDriver);
                    }
                    if ((displayDriver instanceof ClassList || displayDriver instanceof Observation) && entity instanceof Photo) {
                        relatedToTableDriver = ((Photo) entity).getPhotoFor().equals(displayDriver);
                    }
                    if (displayDriver instanceof ObservationCategory && entity instanceof Observation) {
                        relatedToTableDriver = ((Observation) entity).getCategories().contains(displayDriver);
                    }
                }
                if (relatedToTableDriver) {
                    entities.addBean(entity);
                }
            }
            refreshSizeAndSort();
        }
    }

    public void setDisplayDriver(final IdObject displayDriver) {
        logger.trace(getSession().getAttribute(AppUser.class).getId() + ": display driver changed to " + displayDriver.getId());
        this.displayDriver = displayDriver;
        entities.removeAllItems();
        if (displayDriver instanceof AppUser) {
            if (displayDriver.equals(appUser)) {
                entities.addAll(readWriteDAO.getEntitiesForUser(entityType, appUser));
                refreshSizeAndSort();
            }
        }
    }

    public void refreshSizeAndSort() {
        refreshSize();
        refreshSort();
    }

    public IdObjectFilterableDisplay(final Class<T> entityType) {
        this.entityType = entityType;
    }

    public void setClickedOnListener(ClickedOnListener<T> clickedOnListener) {
        this.clickedOnListener = clickedOnListener;
    }

    @Override
    public void attach() {
        super.attach();
        appUser = getSession().getAttribute(AppUser.class);
        eventBus.register(this);
    }

    @Override
    public void detach() {
        eventBus.unregister(this);
        super.detach();
    }

    @SuppressWarnings("unused")
    protected void addCustomFilters(final HorizontalLayout filterSection) {
        //  None by default
    }

    public TextField getSearchFor() {
        return searchFor;
    }

    protected AbstractLayout buildDisplayFooter() {
        return null;
    }
}
