package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.ria.vaadin.events.IdObjectChanged;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.AllItemsBeanItemContainer;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.BooleanToYesNoConverter;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.DateTimeStringConverter;
import com.jtbdevelopment.e_eye_o.ria.vaadin.widgets.AppUserOwnedActionGeneratedColumn;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.*;
import org.jsoup.helper.StringUtil;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 3/16/13
 * Time: 7:11 PM
 */
public abstract class IdObjectTable<T extends AppUserOwnedObject> extends CustomComponent {
    public static class HeaderInfo {
        private final String property;
        private final String description;
        private final Table.Align align;

        public HeaderInfo(final String property, final String description, final Table.Align align) {
            this.description = description;
            this.property = property;
            this.align = align;
        }
    }

    private final Class<T> entityType;
    protected final Table entityTable = new Table();
    protected final EventBus eventBus;
    protected final AllItemsBeanItemContainer<T> entities;
    protected final ReadWriteDAO readWriteDAO;
    protected final AppUser appUser;
    protected final IdObjectFactory idObjectFactory;
    private final TextField searchFor = new TextField();
    private Container.Filter currentFilter;

    // TODO make preference
    private int maxSize = 10;

    protected abstract List<HeaderInfo> getHeaderInfo();

    protected abstract void showEntityEditor(final T entity);

    protected abstract void handleClickEvent(final T entity);

    public IdObjectTable(final Class<T> entityType, final ReadWriteDAO readWriteDAO, final IdObjectFactory idObjectFactory, final EventBus eventBus, final AppUser appUser, final AllItemsBeanItemContainer<T> entities) {
        this.entityType = entityType;
        this.eventBus = eventBus;
        this.entities = entities;
        this.readWriteDAO = readWriteDAO;
        this.appUser = appUser;
        this.idObjectFactory = idObjectFactory;

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setImmediate(true);

        mainLayout.addComponent(buildActionRow());

        entityTable.setContainerDataSource(entities);
        addGeneratedColumns(true);

        entityTable.setSortEnabled(true);
        entityTable.setSelectable(true);
        entityTable.setSizeFull();
        entityTable.setNullSelectionAllowed(false);

        List<String> properties = new LinkedList<>();
        List<String> headers = new LinkedList<>();
        List<Table.Align> aligns = new LinkedList<>();
        for (HeaderInfo header : getHeaderInfo()) {
            properties.add(header.property);
            headers.add(header.description);
            aligns.add(header.align);
        }
        entityTable.setVisibleColumns(properties.toArray(new String[properties.size()]));
        entityTable.setColumnHeaders(headers.toArray(new String[headers.size()]));
        entityTable.setSortContainerPropertyId(getDefaultSortField(properties));
        entityTable.setSortAscending(getDefaultSortAscending());
        entityTable.setColumnAlignments(aligns.toArray(new Table.Align[aligns.size()]));
        addColumnConverters();

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
                handleClickEvent(entity);
                if (event.isDoubleClick()) {
                    showEntityEditor(entity);
                }
            }
        });
        refreshSizeAndSort();
        mainLayout.addComponent(entityTable);

        eventBus.register(this);
        setCompositionRoot(mainLayout);
    }

    protected Container.Filter generateFilter(final String searchFor) {
        return new SimpleStringFilter("viewableDescription", searchFor, true, false);
    }

    protected String getDefaultSortField(final List<String> properties) {
        //  TODO - make preference
        return properties.get(0);
    }

    protected boolean getDefaultSortAscending() {
        return true;
    }

    protected void addColumnConverters() {
        entityTable.setConverter("archived", new BooleanToYesNoConverter());
        entityTable.setConverter("modificationTimestamp", new DateTimeStringConverter());
    }

    protected void addGeneratedColumns(final boolean horizontal) {
        entityTable.addGeneratedColumn("actions", new AppUserOwnedActionGeneratedColumn<>(readWriteDAO, eventBus, entities, horizontal));
    }

    @SuppressWarnings("unused")
    protected void addCustomFilters(final HorizontalLayout filterSection) {
        //  None by default
    }

    private HorizontalLayout buildActionRow() {
        HorizontalLayout actionRow = new HorizontalLayout();
        HorizontalLayout buttonSection = buildActionButtons();
        actionRow.addComponent(buttonSection);
        actionRow.setComponentAlignment(buttonSection, Alignment.BOTTOM_LEFT);

        HorizontalLayout filterSection = buildFilterOptions();
        actionRow.addComponent(filterSection);
        actionRow.setComponentAlignment(filterSection, Alignment.BOTTOM_RIGHT);
        actionRow.setWidth(100, Unit.PERCENTAGE);
        actionRow.setExpandRatio(buttonSection, 1);
        actionRow.setExpandRatio(filterSection, 3);
        return actionRow;
    }

    private HorizontalLayout buildFilterOptions() {
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

        final CheckBox activeCB = new CheckBox("Active");
        final CheckBox archivedCB = new CheckBox("Archived");
        activeCB.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                setActiveArchiveFilters(activeCB.getValue(), archivedCB.getValue());
            }
        });
        archivedCB.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                setActiveArchiveFilters(activeCB.getValue(), archivedCB.getValue());
            }
        });
        //  TODO - configurable
        activeCB.setValue(Boolean.TRUE);
        //  TODO - configurable
        archivedCB.setValue(Boolean.FALSE);
        filterSection.addComponent(activeCB);
        filterSection.addComponent(archivedCB);
        filterSection.setComponentAlignment(activeCB, Alignment.BOTTOM_RIGHT);
        filterSection.setComponentAlignment(archivedCB, Alignment.BOTTOM_RIGHT);


        addCustomFilters(filterSection);

        Label showSizeLabel = new Label("How Many?");
        filterSection.addComponent(showSizeLabel);
        filterSection.setComponentAlignment(showSizeLabel, Alignment.BOTTOM_LEFT);

        final NativeSelect showSize = new NativeSelect("", Arrays.asList(1, 5, 10, 25, 50));
        showSize.setImmediate(true);
        showSize.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
                maxSize = ((Integer) showSize.getValue());
                entityTable.setPageLength(calculateTableRows());
            }
        });
        showSize.setValue(maxSize);
        showSize.setWidth(4, Unit.EM);
        filterSection.addComponent(showSize);
        filterSection.setComponentAlignment(showSize, Alignment.BOTTOM_LEFT);
        return filterSection;
    }

    private void setActiveArchiveFilters(final boolean active, final boolean archived) {
        entities.removeContainerFilters("archived");
        if (active && archived) {
            return;
        }
        if (active) {
            entities.addContainerFilter("archived", "false", false, true);
        } else {
            entities.addContainerFilter("archived", "true", false, true);
        }
    }

    private HorizontalLayout buildActionButtons() {
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

    private int calculateTableRows() {
        return Math.min(maxSize, entities.getUnfilteredSize());
    }

    public void refreshSizeAndSort() {
        entityTable.setPageLength(calculateTableRows());
        entityTable.sort();
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
            refreshSizeAndSort();
        }
    }

    public TextField getSearchFor() {
        return searchFor;
    }
}
