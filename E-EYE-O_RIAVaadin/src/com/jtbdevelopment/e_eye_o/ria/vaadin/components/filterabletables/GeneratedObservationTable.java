package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables;

import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors.ObservationEditorDialogWindow;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters.LocalDateTimeDateConverter;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters.ObservationCategorySetStringConverter;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters.ShortenedCommentConverter;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.filters.ObservationCategoryFilter;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.Between;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Date: 6/5/13
 * Time: 10:22 PM
 */
public class GeneratedObservationTable extends GeneratedIdObjectTable<Observation> {
    private static final Logger logger = LoggerFactory.getLogger(ObservationWithoutSubjectTable.class);
    public static final String SIGNIFICANTONLY_DEFAULT = ".significantonly.default";
    public static final String MONTHSBACK_DEFAULT = ".monthsback.default";
    private Observable defaultObservationSubject;
    @Autowired
    private ObservationEditorDialogWindow observationEditorDialogWindow;
    @Autowired
    private ObservationCategorySetStringConverter observationCategorySetStringConverter;
    @Autowired
    private ShortenedCommentConverter shortenedCommentConverter;
    @Autowired
    private LocalDateTimeDateConverter localDateTimeDateConverter;

    private CheckBox significantOnly = new CheckBox("Significant Only");
    private DateField from = new DateField();
    private DateField to = new DateField();
    private Between dateRangeFilter;

    public GeneratedObservationTable() {
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
    protected void addGeneratedColumns() {
        super.addGeneratedColumns();

        //  TODO - do this better
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
        Label label = new Label("From");
        filterSection.addComponent(label);
        filterSection.setComponentAlignment(label, Alignment.BOTTOM_LEFT);
        from.setResolution(Resolution.DAY);
        from.setConverter(localDateTimeDateConverter);
        filterSection.addComponent(from);
        filterSection.setComponentAlignment(from, Alignment.BOTTOM_LEFT);
        from.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                logger.trace(getSession().getAttribute(AppUser.class).getId() + ": from filter " + from.getValue());
                updateDateRangeFilter();
            }
        });
        label = new Label("To");
        filterSection.addComponent(label);
        filterSection.setComponentAlignment(label, Alignment.BOTTOM_LEFT);
        to.setResolution(Resolution.DAY);
        to.setConverter(localDateTimeDateConverter);
        to.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                logger.trace(getSession().getAttribute(AppUser.class).getId() + ": to filter " + to.getValue());
                updateDateRangeFilter();
            }
        });
        filterSection.addComponent(to);
        filterSection.setComponentAlignment(to, Alignment.BOTTOM_LEFT);

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
        filterSection.addComponent(significantOnly);
        filterSection.setComponentAlignment(significantOnly, Alignment.BOTTOM_LEFT);
    }

    private void updateDateRangeFilter() {
        if (dateRangeFilter != null) {
            entities.removeContainerFilter(dateRangeFilter);
        }
        dateRangeFilter = new Between("observationTimestamp",
                new LocalDateTime(from.getConvertedValue()),
                new LocalDate(to.getConvertedValue()).toLocalDateTime(new LocalTime(23, 59, 59)));
        entities.addContainerFilter(dateRangeFilter);
        refreshSizeAndSort();
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

    @Override
    protected void initializeFilters() {
        super.initializeFilters();
        AppUserSettings settings = getSession().getAttribute(AppUserSettings.class);
        significantOnly.setValue(settings.getSettingAsBoolean(baseConfigSetting + SIGNIFICANTONLY_DEFAULT, false));
        from.setConvertedValue(new LocalDateTime().minusMonths(settings.getSettingAsInt(baseConfigSetting + MONTHSBACK_DEFAULT, 1)));
        to.setConvertedValue(new LocalDateTime());
    }

    public void setDefaultObservationSubject(final Observable defaultObservationSubject) {
        this.defaultObservationSubject = defaultObservationSubject;
    }
}
