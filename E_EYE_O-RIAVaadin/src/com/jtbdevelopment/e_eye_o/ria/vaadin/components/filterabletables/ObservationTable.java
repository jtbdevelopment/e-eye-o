package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables;

import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors.ObservationEditorDialogWindow;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters.LocalDateStringConverter;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters.LocalDateTimeStringConverter;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters.ObservationCategorySetStringConverter;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters.ShortenedCommentConverter;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.filters.ObservationCategoryFilter;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.generatedcolumns.ObservationFollowUpButtons;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.Arrays;
import java.util.List;

/**
 * Date: 3/17/13
 * Time: 2:02 PM
 */
@org.springframework.stereotype.Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ObservationTable extends IdObjectTable<Observation> {
    private Observable defaultObservationSubject;

    @Autowired
    private ObservationEditorDialogWindow observationEditorDialogWindow;

    @Autowired
    private LocalDateStringConverter localDateStringConverter;

    @Autowired
    private LocalDateTimeStringConverter localDateTimeStringConverter;

    @Autowired
    private ObservationCategorySetStringConverter observationCategorySetStringConverter;

    @Autowired
    private ShortenedCommentConverter shortenedCommentConverter;

    public ObservationTable() {
        super(Observation.class);
    }

    protected static final List<HeaderInfo> headers;

    static {
        headers = Arrays.asList(
                new HeaderInfo("observationTimestamp", "Time", Table.Align.LEFT),
                new HeaderInfo("comment", "Comment", Table.Align.LEFT),
                new HeaderInfo("categories", "Categories", Table.Align.LEFT),
                new HeaderInfo("significant", "Significant", Table.Align.CENTER),
                new HeaderInfo("followUpNeeded", "Follow Up?", Table.Align.CENTER),
                new HeaderInfo("followUpReminder", "Reminder?", Table.Align.CENTER),
                new HeaderInfo("showFollowUp", "Follow Up", Table.Align.CENTER, true),              //  Generated
                new HeaderInfo("modificationTimestamp", "Last Update", Table.Align.CENTER),
                new HeaderInfo("archived", "Archived?", Table.Align.CENTER),
                new HeaderInfo("actions", "Actions", Table.Align.RIGHT, true)                       //  Generated
        );
    }

    @Override
    protected List<HeaderInfo> getHeaderInfo() {
        return headers;
    }

    @Override
    public void showEntityEditor(final Observation entity) {
        if (entity.getObservationSubject() == null) {
            entity.setObservationSubject(defaultObservationSubject);
        }
        getUI().addWindow(observationEditorDialogWindow);
        observationEditorDialogWindow.setEntity(entity);
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
        entityTable.setConverter("significant", booleanToYesNoConverter);
        entityTable.setConverter("followUpNeeded", booleanToYesNoConverter);
        entityTable.setConverter("followUpReminder", localDateStringConverter);
        entityTable.setConverter("observationTimestamp", localDateTimeStringConverter);
        entityTable.setConverter("categories", observationCategorySetStringConverter);
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
    protected void addGeneratedColumns() {
        super.addGeneratedColumns();
        entityTable.addGeneratedColumn("showFollowUp", new ObservationFollowUpButtons(readWriteDAO, eventBus, this, entities));

        //  TODO - do his better
        entityTable.setColumnExpandRatio("observationTimestamp", 0.10f);
        entityTable.setColumnExpandRatio("categories", 0.10f);
        entityTable.setColumnExpandRatio("modificationTimestamp", 0.10f);
        entityTable.setColumnExpandRatio("comment", 0.4f);
        entityTable.setColumnExpandRatio("archived", 0.10f);
        entityTable.setColumnExpandRatio("significant", 0.10f);
        entityTable.setColumnExpandRatio("followUpReminder", 0.10f);
    }

    @Override
    protected void addCustomFilters(final HorizontalLayout filterSection) {
        super.addCustomFilters(filterSection);
        final CheckBox significantOnly = new CheckBox("Significant Only?");
        significantOnly.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
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

        final CheckBox followUpOnly = new CheckBox("Follow Up Only?");
        followUpOnly.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (followUpOnly.getValue()) {
                    entities.addContainerFilter("followUpNeeded", "true", false, true);
                } else {
                    entities.removeContainerFilters("followUpNeeded");
                }
                refreshSizeAndSort();
            }
        });
        //  TODO - make preference
        followUpOnly.setValue(false);
        filterSection.addComponent(followUpOnly);
        filterSection.setComponentAlignment(followUpOnly, Alignment.BOTTOM_LEFT);
    }

    @Override
    public void setTableDriver(final IdObject tableDriver) {
        super.setTableDriver(tableDriver);
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
