package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables;

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.Observation;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import com.jtbdevelopment.e_eye_o.ria.events.IdObjectChanged;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors.ObservationEditorDialogWindow;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters.LocalDateStringConverter;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters.LocalDateTimeStringConverter;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters.StringObservationCategorySetConverter;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Runo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.vaadin.dialogs.ConfirmDialog;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Date: 3/17/13
 * Time: 2:02 PM
 */
@org.springframework.stereotype.Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ObservationTable extends IdObjectTable<Observation> {
    private AppUserOwnedObject defaultObservationSubject;

    @Autowired
    private ObservationEditorDialogWindow observationEditorDialogWindow;

    @Autowired
    private LocalDateStringConverter localDateStringConverter;

    @Autowired
    private LocalDateTimeStringConverter localDateTimeStringConverter;

    @Autowired
    private StringObservationCategorySetConverter stringObservationCategorySetConverter;

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
                new HeaderInfo("archived", "Archived?", Table.Align.CENTER),
                new HeaderInfo("modificationTimestamp", "Last Update", Table.Align.CENTER),
                new HeaderInfo("actions", "Actions", Table.Align.RIGHT, true)                       //  Generated
        );
    }

    @Override
    protected List<HeaderInfo> getHeaderInfo() {
        return headers;
    }

    @Override
    protected void showEntityEditor(final Observation entity) {
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
                //  TODO - search on categories doesn't work as it searches actual object toString representation
                new SimpleStringFilter("categories", searchFor, true, false)
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
        entityTable.setConverter("categories", stringObservationCategorySetConverter);
        //  TODO
        entityTable.setConverter("comment", new Converter<String, String>() {
            @Override
            public String convertToModel(final String value, final Locale locale) throws ConversionException {
                return null;
            }

            @Override
            public String convertToPresentation(final String value, final Locale locale) throws ConversionException {
                String shortenedComment = value.replace("\n", "<br>");
                if (shortenedComment.length() > 50) {
                    shortenedComment = shortenedComment.substring(0, 47) + "...";
                }
                return shortenedComment;
            }

            @Override
            public Class<String> getModelType() {
                return String.class;
            }

            @Override
            public Class<String> getPresentationType() {
                return String.class;
            }
        });
    }

    @Override
    protected void addGeneratedColumns() {
        super.addGeneratedColumns();
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
        entityTable.addGeneratedColumn("showFollowUp", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(final Table source, final Object itemId, final Object columnId) {
                final Observation entity = entities.getItem(itemId).getBean();
                GridLayout layout = new GridLayout(2, 1);
                layout.setSizeUndefined();
                if (entity.getFollowUpObservation() != null) {
                    Button showFollowUpButton = new Button("See Follow Up");
                    showFollowUpButton.addStyleName(Runo.BUTTON_SMALL);
                    showFollowUpButton.addClickListener(new Button.ClickListener() {
                        @Override
                        public void buttonClick(final Button.ClickEvent event) {
                            showEntityEditor(entity);
                        }
                    });
                    layout.addComponent(showFollowUpButton, 0, 0);
                    Button breakLink = new Button("Break Link");
                    breakLink.addStyleName(Runo.BUTTON_SMALL);
                    breakLink.setDescription("This will NOT delete the follow up observation, just break the link between the two observations and reset the follow-up flag for this observation.");
                    breakLink.addClickListener(new Button.ClickListener() {
                        @Override
                        public void buttonClick(final Button.ClickEvent event) {
                            ConfirmDialog.show(getUI(), "Change " + entity.getFollowUpObservation().getSummaryDescription() + " to not be a follow-up for this observation?", new ConfirmDialog.Listener() {
                                @Override
                                public void onClose(final ConfirmDialog dialog) {
                                    if (dialog.isConfirmed()) {
                                        entity.setFollowUpNeeded(true);
                                        entity.setFollowUpObservation(null);
                                        Observation updatedEntity = readWriteDAO.update(entity);
                                        eventBus.post(new IdObjectChanged<>(IdObjectChanged.ChangeType.MODIFIED, updatedEntity));
                                    }
                                }
                            });
                        }
                    });
                    layout.addComponent(breakLink, 1, 0);
                } else {
                    Button addFollowUp = new Button("New");
                    addFollowUp.setDescription("This starts a new observation.");
                    addFollowUp.addStyleName(Runo.BUTTON_SMALL);
                    addFollowUp.addClickListener(new Button.ClickListener() {
                        @Override
                        public void buttonClick(final Button.ClickEvent event) {
                            Notification.show("TODO:  add new observation and link", Notification.Type.HUMANIZED_MESSAGE);
//                            showEntityEditor(idObjectFactory.newObservation(appUser));
                            //  TODO - link created observation to this observation and set follow up to false and reminder to null.
                        }
                    });
                    layout.addComponent(addFollowUp, 0, 0);
                    Button linkFollowUp = new Button("Link");
                    linkFollowUp.setDescription("This lets you pick an existing observation as the follow-up.");
                    linkFollowUp.addStyleName(Runo.BUTTON_SMALL);
                    linkFollowUp.addClickListener(new Button.ClickListener() {
                        @Override
                        public void buttonClick(final Button.ClickEvent event) {
                            Notification.show("TODO:  link existing observation", Notification.Type.HUMANIZED_MESSAGE);
                            //  TODO - show observations for entity and let them pick one to link and set follow up to false and reminder to null.
                        }
                    });
                    layout.addComponent(linkFollowUp, 1, 0);
                }
                return layout;
            }
        });

        //  TODO - this better
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
            } else {
                entities.addAll(readWriteDAO.getAllObservationsForEntity((AppUserOwnedObject) tableDriver));
                setDefaultObservationSubject((AppUserOwnedObject) tableDriver);
            }
            refreshSizeAndSort();
        }
    }

    public void setDefaultObservationSubject(final AppUserOwnedObject defaultObservationSubject) {
        this.defaultObservationSubject = defaultObservationSubject;
    }

}
