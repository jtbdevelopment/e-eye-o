package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.generatedcolumns.ArchiveAndDeleteButtonsGenerator;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.sorter.CompositeItemSorter;
import com.vaadin.data.Property;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.MouseEvents;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Runo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * Date: 3/16/13
 * Time: 7:11 PM
 */
public abstract class IdObjectTable<T extends AppUserOwnedObject> extends IdObjectFilterableDisplay<T> {
    private static final Logger logger = LoggerFactory.getLogger(IdObjectTable.class);
    protected static final HeaderInfo EDIT_HEADER = new HeaderInfo("edit", "", Table.Align.CENTER);

    //  TODO - this should just drive off of annotations it would seem off of interface
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

    protected final Table entityTable = new Table();

    protected abstract List<HeaderInfo> getHeaderInfo();

    public IdObjectTable(final Class<T> entityType) {
        super(entityType);
    }

    @Override
    protected AbstractLayout buildDisplayFooter() {
        CssLayout spacer = new CssLayout();
        spacer.addStyleName("table-spacer-row");
        return spacer;
    }

    @Override
    protected Component buildMainDisplay() {
        List<String> properties = new LinkedList<>();
        List<String> headers = new LinkedList<>();
        List<Table.Align> aligns = new LinkedList<>();

        //  Force a header info list with edit at front
        List<HeaderInfo> headerInfos = new LinkedList<>();
        headerInfos.add(EDIT_HEADER);
        headerInfos.addAll(getHeaderInfo());
        for (HeaderInfo headerInfo : headerInfos) {
            properties.add(headerInfo.property);
            headers.add(headerInfo.description);
            aligns.add(headerInfo.align);
        }

        entityTable.setContainerDataSource(entities);
        entityTable.addStyleName(Runo.TABLE_SMALL);
        addGeneratedColumns();

        entityTable.setSortEnabled(true);
        entityTable.setSelectable(true);
        entityTable.setSizeFull();
        entityTable.setNullSelectionAllowed(false);
        entityTable.setMultiSelect(false);

        entityTable.setVisibleColumns(properties.toArray(new String[properties.size()]));
        entityTable.setColumnHeaders(headers.toArray(new String[headers.size()]));
        entityTable.setSortContainerPropertyId(getDefaultSortField(properties));
        entityTable.setSortAscending(getDefaultSortAscending());
        entityTable.setColumnAlignments(aligns.toArray(new Table.Align[aligns.size()]));
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

    protected String getDefaultSortField(final List<String> properties) {
        //  TODO - make preference
        return properties.get(1);
    }

    protected boolean getDefaultSortAscending() {
        return true;
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
        entityTable.addGeneratedColumn("archived", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                T entity = entities.getItem(itemId).getBean();
                return new Embedded(null, entity.isArchived() ? NOT_X : IS_X);
            }
        });
    }

    @Override
    protected void refreshSize() {
        entityTable.setPageLength(Math.min(maxSize, entities.size()));
    }

    @Override
    protected void refreshSort() {
        entityTable.sort();
    }

}
