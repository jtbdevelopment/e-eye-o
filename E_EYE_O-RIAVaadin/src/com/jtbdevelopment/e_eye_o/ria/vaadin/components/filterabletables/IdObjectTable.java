package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables;

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters.DateTimeStringConverter;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.generatedcolumns.ArchiveAndDeleteButtons;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.sorter.CompositeItemSorter;
import com.vaadin.data.Property;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Runo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 3/16/13
 * Time: 7:11 PM
 */
public abstract class IdObjectTable<T extends AppUserOwnedObject> extends IdObjectFilterableDisplay<T> {
    //  TODO - diff icons
    protected static final ThemeResource NOT_X = new ThemeResource("../runo/icons/16/cancel.png");
    protected static final ThemeResource IS_X = new ThemeResource("../runo/icons/16/ok.png");

    //  TODO - this should just drive off of annotations it would seem off of interface
    public static class HeaderInfo {
        private final String property;
        private final String description;
        private final Table.Align align;
        private final boolean forceGeneratedSort;

        public HeaderInfo(final String property, final String description, final Table.Align align) {
            this.description = description;
            this.property = property;
            this.align = align;
            this.forceGeneratedSort = false;
        }

        public HeaderInfo(final String property, final String description, final Table.Align align, final boolean forceGeneratedSort) {
            this.description = description;
            this.property = property;
            this.align = align;
            this.forceGeneratedSort = forceGeneratedSort;
        }
    }

    protected static final List<HeaderInfo> headers;

    static {
        headers = Arrays.asList(
                new HeaderInfo("modificationTimestamp", "Last Update", Table.Align.CENTER),
                new HeaderInfo("archived", "Active", Table.Align.CENTER, true),
                new HeaderInfo("actions", "Actions", Table.Align.RIGHT)
        );
    }

    @Autowired
    protected DateTimeStringConverter dateTimeStringConverter;

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
        List<String> generatedProperties = new LinkedList<>();
        for (HeaderInfo headerInfo : getHeaderInfo()) {
            if (headerInfo.forceGeneratedSort) {
                generatedProperties.add(headerInfo.property);
            }
            properties.add(headerInfo.property);
            headers.add(headerInfo.description);
            aligns.add(headerInfo.align);
        }
        entities.setAdditionalSortableProperties(generatedProperties);

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
        return properties.get(0);
    }

    protected boolean getDefaultSortAscending() {
        return true;
    }

    protected void addColumnConverters() {
        entityTable.setConverter("modificationTimestamp", dateTimeStringConverter);
    }

    protected void addGeneratedColumns() {
        entityTable.addGeneratedColumn("actions", new ArchiveAndDeleteButtons<>(readWriteDAO, eventBus, entities));
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
