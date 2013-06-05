package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables;

import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors.ObservationEditorDialogWindow;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters.ObservationCategorySetStringConverter;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters.ShortenedCommentConverter;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.filters.ObservationCategoryFilter;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.LinkedList;
import java.util.List;

/**
 * Date: 3/17/13
 * Time: 2:02 PM
 */
//  TODO - add date range filter
@org.springframework.stereotype.Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ObservationTable extends GeneratedIdObjectTable<Observation> {
    private static final Logger logger = LoggerFactory.getLogger(ObservationTable.class);

    private Observable defaultObservationSubject;

    @Autowired
    private ObservationEditorDialogWindow observationEditorDialogWindow;

    @Autowired
    private ObservationCategorySetStringConverter observationCategorySetStringConverter;

    @Autowired
    private ShortenedCommentConverter shortenedCommentConverter;

    public ObservationTable() {
        super(Observation.class);
    }

    @Override
    public ObservationEditorDialogWindow showEntityEditor(final Observation entity) {
        if (entity.getObservationSubject() == null) {
            entity.setObservationSubject(defaultObservationSubject);
        }
        getUI().addWindow(observationEditorDialogWindow);
        observationEditorDialogWindow.setEntity(entity);
        return observationEditorDialogWindow;
    }

    @Override
    protected Container.Filter generateFilter(final String searchFor) {
        return new Or(
                new SimpleStringFilter("comment", searchFor, true, false),
                new ObservationCategoryFilter(searchFor)
        );
    }

    @Override
    protected boolean getDefaultSortAscending() {
        return false;
    }

    @Override
    protected void addColumnConverters() {
        super.addColumnConverters();
        entityTable.setConverter("categories", observationCategorySetStringConverter);
        entities.addAdditionalSortableProperty("categories");
        entityTable.setConverter("comment", shortenedCommentConverter);
        entityTable.setItemDescriptionGenerator(new AbstractSelect.ItemDescriptionGenerator() {
            @Override
            public String generateDescription(final Component source, final Object itemId, final Object propertyId) {
                if (itemId != null && propertyId != null && propertyId.equals("comment")) {
                    final Observation entity = entities.getItem(itemId).getBean();
                    return entity.getComment().replace("\n", "<br/>");
                }
                return null;
            }
        });
    }

    @Override
    protected List<String> getTableFields() {
        final List<String> s = super.getTableFields();
        return new LinkedList<String>() {{
            add("observationTimestamp");
            add("comment");
            add("categories");
            add("significant");
            addAll(s);
        }};
    }

    @Override
    protected void addGeneratedColumns() {
        super.addGeneratedColumns();
        entityTable.addGeneratedColumn("significant", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                Observation observation = entities.getItem(itemId).getBean();
                return observation.isSignificant() ? new Embedded(null, IS_X) : null;
            }
        });

        //  TODO - do his better
        entityTable.setColumnExpandRatio("observationTimestamp", 0.10f);
        entityTable.setColumnExpandRatio("categories", 0.10f);
        entityTable.setColumnExpandRatio("modificationTimestamp", 0.10f);
        entityTable.setColumnExpandRatio("comment", 0.60f);
        entityTable.setColumnExpandRatio("archived", 0.05f);
        entityTable.setColumnExpandRatio("significant", 0.05f);
    }

    @Override
    protected void addCustomFilters(final HorizontalLayout filterSection) {
        super.addCustomFilters(filterSection);
        final CheckBox significantOnly = new CheckBox("Significant Only");
        significantOnly.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                logger.trace(getSession().getAttribute(AppUser.class).getId() + ": significant only filter " + significantOnly.getValue());
                if (significantOnly.getValue()) {
                    entities.addContainerFilter("significant", "true", false, true);
                } else {
                    entities.removeContainerFilters("significant");
                }
                refreshSizeAndSort();
            }
        });
        //  TODO - make preference
        significantOnly.setValue(false);
        filterSection.addComponent(significantOnly);
        filterSection.setComponentAlignment(significantOnly, Alignment.BOTTOM_LEFT);
    }

    @Override
    public void setDisplayDriver(final IdObject tableDriver) {
        super.setDisplayDriver(tableDriver);
        if (tableDriver instanceof AppUserOwnedObject) {
            if (tableDriver instanceof ObservationCategory) {
                entities.addAll(readWriteDAO.getAllObservationsForObservationCategory((ObservationCategory) tableDriver));
            } else if (tableDriver instanceof Observable) {
                entities.addAll(readWriteDAO.getAllObservationsForEntity((Observable) tableDriver));
                setDefaultObservationSubject((Observable) tableDriver);
            }
            refreshSizeAndSort();
        }
    }

    public void setDefaultObservationSubject(final Observable defaultObservationSubject) {
        this.defaultObservationSubject = defaultObservationSubject;
    }

}
