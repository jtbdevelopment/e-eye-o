package com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectEntitySettings;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.ComponentUtils;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Runo;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * Date: 3/20/13
 * Time: 8:13 PM
 */
//  TODO - be nice to recompute the caption at top as we go
public abstract class IdObjectEditorDialogWindow<T extends AppUserOwnedObject> extends Window {
    private static final Logger logger = LoggerFactory.getLogger(IdObjectEditorDialogWindow.class);

    @Autowired
    protected ReadWriteDAO readWriteDAO;

    protected final Class<T> entityType;

    protected final BeanFieldGroup<T> entityBeanFieldGroup;

    protected final VerticalLayout mainLayout = new VerticalLayout();
    private Button saveButton;
    protected IdObjectEntitySettings entitySettings;

    protected abstract Layout buildEditorLayout();

    protected abstract Focusable getInitialFocusComponent();

    public IdObjectEditorDialogWindow(final Class<T> entityType, final float width, final float height) {
        this(entityType, width, Unit.EM, height, Unit.EM);
    }

    public IdObjectEditorDialogWindow(final Class<T> entityType, final float width, final Unit widthUnit, final float height, final Unit heightUnit) {
        this.entityBeanFieldGroup = new BeanFieldGroup<>(entityType);
        this.entityType = entityType;
        entitySettings = entityType.getAnnotation(IdObjectEntitySettings.class);
        setSizeUndefined();
        setModal(true);
        addStyleName(Runo.WINDOW_DIALOG);
        setWidth(width, widthUnit);
        setHeight(height, heightUnit);
        setContent(mainLayout);
    }

    @PostConstruct
    public void init() {
        buildMainLayout();
        ComponentUtils.setImmediateForAll(this, true);
    }

    protected void buildMainLayout() {
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);

        Layout editorRow = buildEditorLayout();
        mainLayout.addComponent(editorRow);
        mainLayout.setComponentAlignment(editorRow, Alignment.MIDDLE_CENTER);

        Layout buttons = buildButtonLayout();
        mainLayout.addComponent(buttons);
        mainLayout.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);
    }

    private Layout buildButtonLayout() {
        HorizontalLayout buttons = new HorizontalLayout();
        saveButton = new Button("Save");
        saveButton.addStyleName(Runo.BUTTON_DEFAULT);
        enableDefaultEnterKey();
        buttons.addComponent(saveButton);
        saveButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                logger.trace(getSession().getAttribute(AppUser.class).getId() + ": clicked save on " + entityBeanFieldGroup.getItemDataSource().getBean().getId());
                try {
                    save();
                } catch (FieldGroup.CommitException e) {
                    if (e.getCause() instanceof Validator.InvalidValueException) {
                        Notification.show("Please check that all entries are correct.", Notification.Type.WARNING_MESSAGE);
                    } else {
                        Notification.show("There was a problem trying to save this.  Refresh, check and try again please.", Notification.Type.ERROR_MESSAGE);
                    }
                }
            }
        });
        final Button cancel = new Button("Cancel");
        cancel.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        cancel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                logger.trace(getSession().getAttribute(AppUser.class).getId() + ": clicked cancel on " + entityBeanFieldGroup.getItemDataSource().getBean().getId());
                entityBeanFieldGroup.discard();
                closeWindow();
            }
        });
        buttons.addComponent(cancel);

        final Button reset = new Button("Reset");
        reset.setClickShortcut(ShortcutAction.KeyCode.R, ShortcutAction.ModifierKey.ALT);
        reset.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                logger.trace(getSession().getAttribute(AppUser.class).getId() + ": clicked reset on " + entityBeanFieldGroup.getItemDataSource().getBean().getId());
                entityBeanFieldGroup.discard();
            }
        });
        buttons.addComponent(reset);
        return buttons;
    }

    protected void enableDefaultEnterKey() {
        saveButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    }

    protected void disableDefaultEnterKey() {
        saveButton.removeClickShortcut();
    }

    protected T save() throws FieldGroup.CommitException {
        entityBeanFieldGroup.commit();
        T entity = entityBeanFieldGroup.getItemDataSource().getBean();
        if (entity == null) {
            return null;
        }
        if (StringUtil.isBlank(entity.getId())) {
            entity = writeNewObjectToDAO(entity);
        } else {
            entity = writeUpdateObjectToDAO(entity);
        }
        closeWindow();
        return entity;
    }

    protected T writeUpdateObjectToDAO(final T entity) {
        return readWriteDAO.update(getSession().getAttribute(AppUser.class), entity);
    }

    protected T writeNewObjectToDAO(final T entity) {
        return readWriteDAO.create(entity);
    }

    private void closeWindow() {
        entityBeanFieldGroup.discard();
        getUI().removeWindow(this);
    }

    @Override
    public void attach() {
        super.attach();
        ComponentUtils.clearAllErrors(this);
        getUI().setFocusedComponent(getInitialFocusComponent());
    }

    public void setEntity(final T entity) {
        setCaption(entity.getSummaryDescription());
        ComponentUtils.removeAllValidators(this);
        mainLayout.setComponentError(null);
        entityBeanFieldGroup.setItemDataSource(entity);
    }
}
