package com.jtbdevelopment.e_eye_o.ria.vaadin.components;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.ria.vaadin.events.IdObjectChanged;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.AllItemsBeanItemContainer;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.BooleanToYesNoConverter;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.DateTimeConverter;
import com.jtbdevelopment.e_eye_o.ria.vaadin.widgets.AppUserOwnedActionGeneratedColumn;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.*;
import org.jsoup.helper.StringUtil;

import java.util.Arrays;

/**
 * Date: 3/16/13
 * Time: 7:11 PM
 */
public abstract class IdObjectTable<T extends AppUserOwnedObject> extends CustomComponent {
    private static final String ALL = "All";
    private static final String ACTIVE_ONLY = "Active Only";
    private static final String ARCHIVED_ONLY = "Archived Only";

    private final Class<T> entityType;
    private final Table entityTable = new Table();
    private final EventBus eventBus;
    private final AllItemsBeanItemContainer<T> entities;
    private final TextField searchFor = new TextField();
    private Container.Filter currentFilter;

    // TODO make preference
    private int maxSize = 10;

    abstract void showEntityEditor(final T entity);

    abstract void handleClickEvent(final ItemClickEvent event, final T entity);

    abstract Container.Filter generateFilter(final String searchFor);

    public IdObjectTable(final Class<T> entityType, final ReadWriteDAO readWriteDAO, final IdObjectFactory idObjectFactory, final EventBus eventBus, final AppUser appUser, final AllItemsBeanItemContainer<T> entities) {
        this.entityType = entityType;
        this.eventBus = eventBus;
        this.entities = entities;

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setImmediate(true);

        mainLayout.addComponent(buildActionRow(entityType, idObjectFactory, appUser, entities));

        entityTable.setContainerDataSource(entities);
        entityTable.setSortEnabled(true);
        entityTable.setSelectable(true);
        entityTable.setPageLength(getTableRows());
        entityTable.setSizeFull();
        entityTable.setNullSelectionAllowed(false);
        entityTable.addGeneratedColumn("actions", new AppUserOwnedActionGeneratedColumn<>(readWriteDAO, eventBus, entities));
        entityTable.setVisibleColumns(new String[]{"firstName", "lastName", "modificationTimestamp", "archived", "actions"});
        entityTable.setColumnHeaders(new String[]{"First Name", "Last Name", "Last Update", "Archived?", "Actions"});
        //  TODO - make preference
        entityTable.setSortContainerPropertyId("firstName");
        entityTable.setColumnAlignments(Table.Align.LEFT, Table.Align.LEFT, Table.Align.CENTER, Table.Align.CENTER, Table.Align.RIGHT);
        entityTable.setConverter("archived", new BooleanToYesNoConverter());
        entityTable.setConverter("modificationTimestamp", new DateTimeConverter());

        entityTable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void itemClick(final ItemClickEvent event) {
                Object item = event.getItem();
                T entity;
                if (item instanceof BeanItem) {
                    entity = ((BeanItem<T>) item).getBean();
                } else {
                    Notification.show("Not sure what this is - " + (item == null ? "null" : item.toString()));
                    return;
                }
                handleClickEvent(event, entity);
                if (event.isDoubleClick()) {
                    showEntityEditor(entity);
                }
            }
        });
        mainLayout.addComponent(entityTable);

        eventBus.register(this);
        setCompositionRoot(mainLayout);
    }

    private HorizontalLayout buildActionRow(final Class<T> entityType, final IdObjectFactory idObjectFactory, final AppUser appUser, final AllItemsBeanItemContainer<T> entities) {
        HorizontalLayout actionRow = new HorizontalLayout();
        HorizontalLayout buttonSection = buildActionButtons(entityType, idObjectFactory, appUser);
        actionRow.addComponent(buttonSection);
        actionRow.setComponentAlignment(buttonSection, Alignment.BOTTOM_LEFT);

        HorizontalLayout filterSection = buildFilterOptions(entities);
        actionRow.addComponent(filterSection);
        actionRow.setComponentAlignment(filterSection, Alignment.BOTTOM_RIGHT);
        actionRow.setWidth(100, Unit.PERCENTAGE);
        return actionRow;
    }

    private HorizontalLayout buildFilterOptions(final AllItemsBeanItemContainer<T> entities) {
        HorizontalLayout filterSection = new HorizontalLayout();
        filterSection.setWidth(null);
        filterSection.setSpacing(true);

        Label searchForLabel = new Label("Search For:");
        filterSection.addComponent(searchForLabel);
        filterSection.setComponentAlignment(searchForLabel, Alignment.BOTTOM_LEFT);
        searchFor.setWidth(12, Unit.EM);
        searchFor.setImmediate(true);
        searchFor.addTextChangeListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(final FieldEvents.TextChangeEvent event) {
                if (currentFilter != null) {
                    entities.removeContainerFilter(currentFilter);
                }

                final String searchValue = event.getText();
                if (!StringUtil.isBlank(searchValue)) {
                    currentFilter = generateFilter(searchValue);
                    entities.addContainerFilter(currentFilter);
                }
            }
        });
        filterSection.addComponent(searchFor);
        filterSection.setComponentAlignment(searchFor, Alignment.BOTTOM_LEFT);

        Label showWhichLabel = new Label("Show:");
        filterSection.addComponent(showWhichLabel);
        filterSection.setComponentAlignment(showWhichLabel, Alignment.BOTTOM_LEFT);
        final OptionGroup showWhich = new OptionGroup("", Arrays.asList(ALL, ACTIVE_ONLY, ARCHIVED_ONLY));
        showWhich.addStyleName("horizontal");
        showWhich.setImmediate(true);
        showWhich.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
                entities.removeContainerFilters("archived");
                switch ((String) showWhich.getValue()) {
                    case ACTIVE_ONLY:
                        entities.addContainerFilter("archived", "false", false, true);
                        break;
                    case ARCHIVED_ONLY:
                        entities.addContainerFilter("archived", "true", false, false);
                        break;
                }
            }
        });
        //  TODO - make preference
        showWhich.setValue(ACTIVE_ONLY);
        filterSection.addComponent(showWhich);
        filterSection.setComponentAlignment(showWhich, Alignment.BOTTOM_LEFT);

        Label showSizeLabel = new Label("How Many?");
        filterSection.addComponent(showSizeLabel);
        filterSection.setComponentAlignment(showSizeLabel, Alignment.BOTTOM_LEFT);

        final ComboBox showSize = new ComboBox("", Arrays.asList(1, 5, 10, 25, 50));
        showSize.setImmediate(true);
        showSize.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
                maxSize = ((Integer) showSize.getValue());
                entityTable.setPageLength(getTableRows());
            }
        });
        showSize.setValue(maxSize);
        showSize.setWidth(4, Unit.EM);
        filterSection.addComponent(showSize);
        filterSection.setComponentAlignment(showSize, Alignment.BOTTOM_LEFT);
        return filterSection;
    }

    private HorizontalLayout buildActionButtons(final Class<T> entityType, final IdObjectFactory idObjectFactory, final AppUser appUser) {
        //  Buttons
        HorizontalLayout buttonSection = new HorizontalLayout();
        buttonSection.setWidth(null);
        buttonSection.setSpacing(true);

        Button newEntityButton = new Button("New " + entityType.getSimpleName());
        newEntityButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                showEntityEditor(idObjectFactory.newAppUserOwnedObject(entityType, appUser));
            }
        });
        buttonSection.addComponent(newEntityButton);
        buttonSection.setComponentAlignment(newEntityButton, Alignment.BOTTOM_LEFT);
        return buttonSection;
    }

    private int getTableRows() {
        return Math.min(maxSize, entities.getUnfilteredSize());
    }

    @Override
    public void attach() {
        super.attach();
        eventBus.register(this);
    }

    @Override
    public void detach() {
        eventBus.unregister(this);
        super.detach();
    }

    @Subscribe
    @SuppressWarnings({"unused", "unchecked"})
    public void handleIdObjectChange(final IdObjectChanged msg) {
        if (entityType.isAssignableFrom(msg.getEntityType())) {
            T entity = (T) msg.getEntity();
            final T idForBean = entities.getBeanIdResolver().getIdForBean(entity);
            if (idForBean != null) {
                entities.removeItem(idForBean);
            }
            if (!IdObjectChanged.ChangeType.DELETED.equals(msg.getChangeType())) {
                entities.addBean(entity);
            }
            entityTable.sort();
            entityTable.setPageLength(getTableRows());
        }
    }

    public TextField getSearchFor() {
        return searchFor;
    }
}
