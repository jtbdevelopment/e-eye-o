package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.AppUserSettings;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.generatedcolumns.ArchiveAndDeleteButtonsGenerator;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.sorter.CompositeItemSorter;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.MouseEvents;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Runo;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Date: 3/16/13
 * Time: 7:11 PM
 */
public abstract class IdObjectTable<T extends AppUserOwnedObject> extends IdObjectFilterableDisplay<T> {
    private static final Logger logger = LoggerFactory.getLogger(IdObjectTable.class);
    public static final String DEFAULT_SORT_FIELD_SETTING = ".defaultSortField";
    public static final String DEFAULT_SORT_ASCENDING_SETTING = ".defaultSortAscending";

    private Container.Filter currentFilter;

    protected final Table entityTable = new Table();

    protected abstract String[] getVisibleColumns();

    protected abstract String[] getColumnHeaders();

    protected abstract Table.Align[] getColumnAlignments();

    public IdObjectTable(final Class<T> entityType) {
        super(entityType);
    }

    @Override
    public void attach() {
        entityTable.setSortContainerPropertyId(getDefaultSortField());
        entityTable.setSortAscending(getDefaultSortAscending());
        super.attach();
    }

    @Override
    protected AbstractLayout buildDisplayFooter() {
        CssLayout spacer = new CssLayout();
        spacer.addStyleName("table-spacer-row");
        return spacer;
    }

    @Override
    protected Component buildMainDisplay() {
        entityTable.setContainerDataSource(entities);
        entityTable.addStyleName(Runo.TABLE_SMALL);
        addGeneratedColumns();

        entityTable.setSortEnabled(true);
        entityTable.setSelectable(true);
        entityTable.setSizeFull();
        entityTable.setNullSelectionAllowed(false);
        entityTable.setMultiSelect(false);

        String[] visibleColumns = getVisibleColumns();
        entityTable.setVisibleColumns(visibleColumns);
        entityTable.setColumnHeaders(getColumnHeaders());
        entityTable.setColumnAlignments(getColumnAlignments());
        addColumnConverters();

        entityTable.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                handleSelectionChange(entityTable.getValue());
            }
        });
        entityTable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void itemClick(final ItemClickEvent event) {
                T entity = handleSelectionChange(event.getItem());
                if (event.isDoubleClick() && entity != null) {
                    logger.trace(getSession().getAttribute(AppUser.class).getId() + ": double clicked to edit " + entity.getId());
                    //  Get latest from dao in case stale
                    entity = readWriteDAO.get(entityType, entity.getId());
                    showEntityEditor(entity);
                }
            }
        });
        entities.setItemSorter(new CompositeItemSorter<>(entityTable, entities));

        return entityTable;
    }

    protected String getDefaultSortField() {
        AppUserSettings settings = getSession().getAttribute(AppUserSettings.class);
        return settings.getSettingAsString(baseConfigSetting + DEFAULT_SORT_FIELD_SETTING, entitySettings.defaultSortField());
    }

    protected boolean getDefaultSortAscending() {
        AppUserSettings settings = getSession().getAttribute(AppUserSettings.class);
        return settings.getSettingAsBoolean(baseConfigSetting + DEFAULT_SORT_ASCENDING_SETTING, entitySettings.defaultSortAscending());
    }

    protected void addColumnConverters() {
    }

    protected void addGeneratedColumns() {
        entityTable.addGeneratedColumn("edit", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(final Table source, final Object itemId, final Object columnId) {
                Embedded embedded = new Embedded(null, EDIT);
                embedded.addClickListener(new MouseEvents.ClickListener() {
                    @Override
                    public void click(final MouseEvents.ClickEvent event) {
                        T entity = entities.getItem(itemId).getBean();
                        showEntityEditor(entity);
                    }
                });
                return embedded;
            }
        });
        entityTable.addGeneratedColumn("actions", new ArchiveAndDeleteButtonsGenerator<>(archiveHelper, deletionHelper, entities));
    }

    @Override
    protected void refreshSize() {
        entityTable.setPageLength(Math.min((Integer) showSize.getValue(), entities.size()));
    }

    @Override
    protected void refreshSort() {
        entityTable.sort();
    }

    @Override
    protected void updateSearchFilter(final String searchValue) {
        if (currentFilter != null) {
            entities.removeContainerFilter(currentFilter);
        }

        logger.trace(getSession().getAttribute(AppUser.class).getId() + ": search text now " + searchValue);
        if (!StringUtil.isBlank(searchValue)) {
            currentFilter = generateFilter(searchValue);
            entities.addContainerFilter(currentFilter);
        }
        refreshSizeAndSort();
    }

    @Override
    protected void updateActiveArchiveFilters(final boolean active, final boolean archived) {
        logger.trace(getSession().getAttribute(AppUser.class).getId() + ": changing active/archived to " + activeCB.getValue() + "/" + archivedCB.getValue());
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


}
