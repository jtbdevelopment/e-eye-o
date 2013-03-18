package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.eventbus.EventBus;
import com.google.gwt.thirdparty.guava.common.base.Function;
import com.google.gwt.thirdparty.guava.common.collect.Collections2;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors.ObservationEditorDialogWindow;
import com.jtbdevelopment.e_eye_o.ria.vaadin.events.IdObjectChanged;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.AllItemsBeanItemContainer;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.BooleanToYesNoConverter;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.LocalDateStringConverter;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.LocalDateTimeStringConverter;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.MouseEvents;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Runo;
import org.vaadin.dialogs.ConfirmDialog;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Date: 3/17/13
 * Time: 2:02 PM
 */
public abstract class ObservationTable extends IdObjectTable<Observation> {
    private static final String SIGNIFICANT_ONLY = "Significant";
    private static final String FOLLOWUP_ONLY = "Follow Up";

    private AppUserOwnedObject defaultObservationSubject;

    public ObservationTable(final ReadWriteDAO readWriteDAO, final IdObjectFactory idObjectFactory, final EventBus eventBus, final AppUser appUser, final AllItemsBeanItemContainer<Observation> entities) {
        super(Observation.class, readWriteDAO, idObjectFactory, eventBus, appUser, entities);
    }

    protected static final List<HeaderInfo> headers;

    static {
        headers = Arrays.asList(
                new HeaderInfo("observationTimestamp", "Time", Table.Align.LEFT),
                new HeaderInfo("commentPanel", "Comment", Table.Align.LEFT),  // Generated
                new HeaderInfo("categories", "Categories", Table.Align.LEFT),
                new HeaderInfo("significant", "Significant", Table.Align.CENTER),
                new HeaderInfo("followUpNeeded", "Follow Up?", Table.Align.CENTER),
                new HeaderInfo("followUpReminder", "Reminder?", Table.Align.CENTER),
                new HeaderInfo("showFollowUp", "Follow Up", Table.Align.CENTER),              //  Generated
                new HeaderInfo("archived", "Archived?", Table.Align.CENTER),
                new HeaderInfo("modificationTimestamp", "Last Update", Table.Align.CENTER),
                new HeaderInfo("actions", "Actions", Table.Align.RIGHT)                       //  Generated
        );
    }

    @Override
    protected List<HeaderInfo> getHeaderInfo() {
        return headers;
    }

    @Override
    protected void showEntityEditor(final Observation entity) {
        if (entity.getObservationSubject() == null) {
            if (defaultObservationSubject == null) {
                // TODO - implement a picker here or on dialog.
                Notification.show("Need to pick something to observe first", Notification.Type.HUMANIZED_MESSAGE);
                return;
            }
            entity.setObservationSubject(defaultObservationSubject);
        }
        getUI().addWindow(new ObservationEditorDialogWindow(readWriteDAO, eventBus, entity));
    }

    @Override
    protected Container.Filter generateFilter(final String searchFor) {
        return new Or(
                new SimpleStringFilter("comment", searchFor, true, false),
                new SimpleStringFilter("categories", searchFor, true, false)
        );
    }

    @Override
    protected void addColumnConverters() {
        super.addColumnConverters();
        entityTable.setConverter("significant", new BooleanToYesNoConverter());
        entityTable.setConverter("followUpNeeded", new BooleanToYesNoConverter());
        entityTable.setConverter("followUpReminder", new LocalDateStringConverter());
        entityTable.setConverter("observationTimestamp", new LocalDateTimeStringConverter());
        entityTable.setConverter("categories", new Converter<String, Set>() {
            @Override
            public Set<ObservationCategory> convertToModel(final String value, final Locale locale) throws ConversionException {
                Set<ObservationCategory> results = new HashSet<>();

                //  TODO - need injection working here - have ObservationCategoryHelper for just this purpose
                Set<ObservationCategory> loaded = readWriteDAO.getEntitiesForUser(ObservationCategory.class, appUser);
                Map<String, ObservationCategory> map = new HashMap<>();
                for (ObservationCategory category : loaded) {
                    map.put(category.getShortName(), category);
                }
                for (String shortCode : Splitter.on(",").trimResults().omitEmptyStrings().split(value)) {
                    ObservationCategory category = map.get(shortCode);
                    if (category != null) {
                        results.add(category);
                    } else {
                        //  TODO - notify/log
                    }
                }
                return results;
            }

            @Override
            @SuppressWarnings("unchecked")
            public String convertToPresentation(final Set value, final Locale locale) throws ConversionException {
                Set<ObservationCategory> ocs = (Set<ObservationCategory>) value;
                return Joiner.on(", ").skipNulls().join(Collections2.transform(ocs, new Function<ObservationCategory, String>() {
                    @Override
                    public String apply(@Nullable final ObservationCategory observationCategory) {
                        return observationCategory == null ? null : observationCategory.getShortName();
                    }
                }));
            }

            @Override
            public Class<Set> getModelType() {
                return Set.class;
            }

            @Override
            public Class<String> getPresentationType() {
                return String.class;
            }
        });
    }

    @Override
    protected void addGeneratedColumns(final boolean horizontal) {
        super.addGeneratedColumns(false);  //  Ignore
        entityTable.addGeneratedColumn("commentPanel", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(final Table source, final Object itemId, final Object columnId) {
                final Observation entity = entities.getItem(itemId).getBean();
                Label comment = new Label(entity.getComment());
                Panel panel = new Panel(comment);
                panel.addStyleName(Runo.PANEL_LIGHT);
                panel.setHeight(5, Unit.EM);
                panel.setWidth(30, Unit.EM);
                panel.addClickListener(new MouseEvents.ClickListener() {
                    @Override
                    public void click(final MouseEvents.ClickEvent event) {
                        handleClickEvent(entity);
                        entityTable.setValue(entity);
                        if (event.isDoubleClick()) {
                            showEntityEditor(entity);
                        }
                    }
                });
                return panel;
            }
        });
        entityTable.addGeneratedColumn("showFollowUp", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(final Table source, final Object itemId, final Object columnId) {
                final Observation entity = entities.getItem(itemId).getBean();
                GridLayout layout = new GridLayout(1, 2);
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
                            ConfirmDialog.show(getUI(), "Change " + entity.getFollowUpObservation().getViewableDescription() + " to not be a follow-up for this observation?", new ConfirmDialog.Listener() {
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
                    layout.addComponent(breakLink, 0, 1);
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
                    layout.addComponent(linkFollowUp, 0, 1);
                }
                return layout;
            }
        });
    }

    @Override
    protected void addCustomFilters(final HorizontalLayout filterSection) {
        super.addCustomFilters(filterSection);

        final OptionGroup significant = new OptionGroup("", Arrays.asList(ALL, SIGNIFICANT_ONLY));
        significant.addStyleName("horizontal");
        significant.setImmediate(true);
        significant.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
                entities.removeContainerFilters("significant");
                switch ((String) significant.getValue()) {
                    case SIGNIFICANT_ONLY:
                        entities.addContainerFilter("significant", "true", false, true);
                        break;
                }
            }
        });
        //  TODO - make preference
        significant.setValue(ALL);
        filterSection.addComponent(significant);
        filterSection.setComponentAlignment(significant, Alignment.BOTTOM_LEFT);

        final OptionGroup followup = new OptionGroup("", Arrays.asList(ALL, FOLLOWUP_ONLY));
        followup.addStyleName("horizontal");
        followup.setImmediate(true);
        followup.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
                entities.removeContainerFilters("followUpNeeded");
                switch ((String) followup.getValue()) {
                    case FOLLOWUP_ONLY:
                        entities.addContainerFilter("followUpNeeded", "true", false, true);
                        break;
                }
            }
        });
        //  TODO - make preference
        followup.setValue(ALL);
        filterSection.addComponent(followup);
        filterSection.setComponentAlignment(followup, Alignment.BOTTOM_LEFT);
    }

    public void setDefaultObservationSubject(final AppUserOwnedObject defaultObservationSubject) {
        this.defaultObservationSubject = defaultObservationSubject;
    }
}
