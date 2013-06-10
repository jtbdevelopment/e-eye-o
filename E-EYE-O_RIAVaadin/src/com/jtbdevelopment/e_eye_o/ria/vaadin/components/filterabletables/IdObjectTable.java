package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.AppUserSettings;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.generatedcolumns.ArchiveAndDeleteButtonsGenerator;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.sorter.CompositeItemSorter;
import com.vaadin.data.Property;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.MouseEvents;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Runo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Date: 3/16/13
 * Time: 7:11 PM
 */
public abstract class IdObjectTable<T extends AppUserOwnedObject> extends IdObjectFilterableDisplay<T> {
    private static final Logger logger = LoggerFactory.getLogger(IdObjectTable.class);

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
        return settings.getSettingAsString(baseConfigSetting + ".defaultSortField", entitySettings.defaultSortField());
    }

    protected boolean getDefaultSortAscending() {
        AppUserSettings settings = getSession().getAttribute(AppUserSettings.class);
        return settings.getSettingAsBoolean(baseConfigSetting + ".defaultSortAscending", entitySettings.defaultSortAscending());
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
        entityTable.addGeneratedColumn("actions", new ArchiveAndDeleteButtonsGenerator<>(readWriteDAO, eventBus, entities));
    }

    @Override
    protected void refreshSize(final int maxSize) {
        entityTable.setPageLength(Math.min(maxSize, entities.size()));
    }

    @Override
    protected void refreshSort() {
        entityTable.sort();
    }

}
