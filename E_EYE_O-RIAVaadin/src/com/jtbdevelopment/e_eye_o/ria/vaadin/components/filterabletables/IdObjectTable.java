package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.ria.events.IdObjectChanged;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters.BooleanToYesNoConverter;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters.DateTimeStringConverter;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.generatedcolumns.AppUserOwnedActionGeneratedColumn;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.AllItemsBeanItemContainer;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.ItemSorter;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Runo;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Date: 3/16/13
 * Time: 7:11 PM
 */
//  TODO - figure out how to get rid of excess spacing at top of action row
public abstract class IdObjectTable<T extends AppUserOwnedObject> extends CustomComponent {
    //  TODO - this should just drive off of annotations it would seem off of interface
    public static class HeaderInfo {
        private final String property;
        private final String description;
        private final Table.Align align;
        private final boolean generated;

        public HeaderInfo(final String property, final String description, final Table.Align align) {
            this.description = description;
            this.property = property;
            this.align = align;
            this.generated = false;
        }

        public HeaderInfo(final String property, final String description, final Table.Align align, final boolean generated) {
            this.description = description;
            this.property = property;
            this.align = align;
            this.generated = generated;
        }
    }

    public interface ClickedOnListener<T> {
        void handleClickEvent(final T entity);
    }

    @Autowired
    protected IdObjectFactory idObjectFactory;

    @Autowired
    protected EventBus eventBus;

    @Autowired
    protected ReadWriteDAO readWriteDAO;

    @Autowired
    protected BooleanToYesNoConverter booleanToYesNoConverter;

    @Autowired
    protected DateTimeStringConverter dateTimeStringConverter;

    protected ClickedOnListener<T> clickedOnListener;

    protected IdObject tableDriver;

    private final Class<T> entityType;

    protected final Table entityTable = new Table();

    protected AllItemsBeanItemContainer<T> entities;
    protected AppUser appUser;

    private final TextField searchFor = new TextField();
    private Container.Filter currentFilter;

    // TODO make preference
    private int maxSize = 10;

    protected abstract List<HeaderInfo> getHeaderInfo();

    protected abstract void showEntityEditor(final T entity);


    public IdObjectTable(final Class<T> entityType) {
        this.entityType = entityType;
    }

    @PostConstruct
    private void initialize() {
        List<String> properties = new LinkedList<>();
        List<String> headers = new LinkedList<>();
        List<Table.Align> aligns = new LinkedList<>();
        List<String> generatedProperties = new LinkedList<>();
        for (HeaderInfo headerInfo : getHeaderInfo()) {
            if (headerInfo.generated) {
                generatedProperties.add(headerInfo.property);
            }
            properties.add(headerInfo.property);
            headers.add(headerInfo.description);
            aligns.add(headerInfo.align);
        }
        this.entities = new AllItemsBeanItemContainer<>(entityType, generatedProperties);
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setImmediate(true);

        mainLayout.addComponent(buildActionRow());

        buildEntityTable(properties, headers, aligns);
        mainLayout.addComponent(entityTable);

        eventBus.register(this);
        setCompositionRoot(mainLayout);
    }

    private void buildEntityTable(List<String> properties, List<String> headers, List<Table.Align> aligns) {
        entityTable.setContainerDataSource(entities);
        entityTable.addStyleName(Runo.TABLE_SMALL);
        addGeneratedColumns(true);

        entityTable.setSortEnabled(true);
        entityTable.setSelectable(true);
        entityTable.setSizeFull();
        entityTable.setImmediate(true);
        entityTable.setNullSelectionAllowed(false);

        entityTable.setVisibleColumns(properties.toArray(new String[properties.size()]));
        entityTable.setColumnHeaders(headers.toArray(new String[headers.size()]));
        entityTable.setSortContainerPropertyId(getDefaultSortField(properties));
        entityTable.setSortAscending(getDefaultSortAscending());
        entityTable.setColumnAlignments(aligns.toArray(new Table.Align[aligns.size()]));
        addColumnConverters();

        entityTable.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                handleValueChange(entityTable.getValue());
            }
        });
        entityTable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void itemClick(final ItemClickEvent event) {
                T entity = handleValueChange(event.getItem());
                if (event.isDoubleClick() && entity != null) {
                    showEntityEditor(entity);
                }
            }
        });
        entities.setItemSorter(new ItemSorter() {
            private Object[] propertyIds;
            private Table.ColumnGenerator[] generators;
            private Converter<String, Object>[] converters;
            private boolean[] ascending;

            @Override
            public void setSortProperties(Container.Sortable container, Object[] propertyId, boolean[] ascending) {
                this.propertyIds = propertyId;
                this.ascending = ascending;
                generators = new Table.ColumnGenerator[propertyId.length];
                converters = new Converter[propertyId.length];
                for (int i = 0; i < propertyIds.length; ++i) {
                    Object property = propertyIds[i];
                    generators[i] = entityTable.getColumnGenerator(property);
                    converters[i] = entityTable.getConverter(property);
                }
            }

            @Override
            public int compare(Object itemId1, Object itemId2) {
                Locale locale = getUI().getLocale();
                for (int i = 0; i < propertyIds.length; ++i) {
                    if (converters[i] != null) {
                        Object object1 = entityTable.getContainerProperty(itemId1, propertyIds[i]).getValue();
                        Object object2 = entityTable.getContainerProperty(itemId2, propertyIds[i]).getValue();
                        String value1 = converters[i].convertToPresentation(object1, locale);
                        String value2 = converters[i].convertToPresentation(object2, locale);
                        if (value1 != null) {
                            int compare = value1.compareTo(value2);
                            if (compare != 0) {
                                return compare * (ascending[i] ? 1 : -1);
                            }
                        }
                    } else if (generators[i] != null) {
                        T entity1 = entities.getItem(itemId1).getBean();
                        T entity2 = entities.getItem(itemId2).getBean();
                        Object object1 = generators[i].generateCell(entityTable, entity1, propertyIds[i]);
                        Object object2 = generators[i].generateCell(entityTable, entity2, propertyIds[i]);
                        if (object1 instanceof Comparable && object2 instanceof Comparable) {
                            int compare = ((Comparable) object1).compareTo(object2);
                            if (compare != 0) {
                                return compare * (ascending[i] ? 1 : -1);
                            }
                        }
                        //  TODO - log / notify
                    } else {
                        Object object1 = entityTable.getContainerProperty(itemId1, propertyIds[i]).getValue();
                        Object object2 = entityTable.getContainerProperty(itemId2, propertyIds[i]).getValue();
                        if (object1 instanceof Comparable && object2 instanceof Comparable) {
                            int compare = ((Comparable) object1).compareTo(object2);
                            if (compare != 0) {
                                return compare * (ascending[i] ? 1 : -1);
                            }
                        }
                        //  TODO - log / notify
                    }
                }
                return 0;
            }
        });
        refreshSizeAndSort();
    }

    private T handleValueChange(final Object item) {
        T entity;
        if (item instanceof BeanItem) {
            entity = ((BeanItem<T>) item).getBean();
        } else if (entityType.isAssignableFrom(item.getClass())) {
            entity = (T) item;
        } else {
            Notification.show("Not sure what this is - " + (item == null ? "null" : item.toString()));
            return null;
        }
        if (clickedOnListener != null) {
            clickedOnListener.handleClickEvent(entity);
        }
        return entity;
    }

    protected Container.Filter generateFilter(final String searchFor) {
        return new SimpleStringFilter("summaryDescription", searchFor, true, false);
    }

    protected String getDefaultSortField(final List<String> properties) {
        //  TODO - make preference
        return properties.get(0);
    }

    protected boolean getDefaultSortAscending() {
        return true;
    }

    protected void addColumnConverters() {
        entityTable.setConverter("archived", booleanToYesNoConverter);
        entityTable.setConverter("modificationTimestamp", dateTimeStringConverter);
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
        filterSection.setComponentAlignment(activeCB, Alignment.BOTTOM_LEFT);
        filterSection.setComponentAlignment(archivedCB, Alignment.BOTTOM_LEFT);


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

        //  TODO - doesn't work for classlist
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
        appUser = getSession().getAttribute(AppUser.class);
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

    public void setTableDriver(final IdObject tableDriver) {
        this.tableDriver = tableDriver;
        entities.removeAllItems();
        if (tableDriver instanceof AppUser) {
            if (!tableDriver.equals(appUser)) {
                //  TODO - log notify exception
            } else {
                entities.addAll(readWriteDAO.getEntitiesForUser(entityType, appUser));
                refreshSizeAndSort();
            }
        }
    }

    public TextField getSearchFor() {
        return searchFor;
    }

    public void setClickedOnListener(ClickedOnListener<T> clickedOnListener) {
        this.clickedOnListener = clickedOnListener;
    }
}
