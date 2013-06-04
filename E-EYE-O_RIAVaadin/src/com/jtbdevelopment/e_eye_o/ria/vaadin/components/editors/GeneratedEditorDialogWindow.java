package com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors;

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectFieldPreferences;
import com.jtbdevelopment.e_eye_o.entities.reflection.IdObjectInterfaceResolver;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters.LocalDateTimeDateConverter;
import com.vaadin.event.FieldEvents;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Date: 6/3/13
 * Time: 10:33 PM
 */
public abstract class GeneratedEditorDialogWindow<T extends AppUserOwnedObject> extends IdObjectEditorDialogWindow<T> {
    private Focusable defaultFocus;

    @Autowired
    private IdObjectInterfaceResolver interfaceResolver;

    public GeneratedEditorDialogWindow(Class<T> entityType, float width, float height) {
        super(entityType, width, height);
    }

    protected abstract String getDefaultField();

    protected abstract List<List<String>> getFieldRows();

    @SuppressWarnings("unused")
    protected void addDataSourceToSelectField(final String fieldName, final AbstractSelect select) {
    }

    @Override
    protected Focusable getInitialFocusComponent() {
        return defaultFocus;
    }

    @Override
    protected Layout buildEditorLayout() {
        Map<String, IdObjectFieldPreferences> fields = interfaceResolver.getAllFieldPreferences(entityType);
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSpacing(true);
        for (List<String> fieldRow : getFieldRows()) {
            HorizontalLayout editorRow = new HorizontalLayout();
            editorRow.setSpacing(true);
            for (String fieldName : fieldRow) {
                IdObjectFieldPreferences preferences = fields.get(fieldName);
                if (preferences == null || !preferences.displayable() || !IdObjectFieldPreferences.EditableBy.USER.equals(preferences.editableBy())) {
                    continue;
                }
                Label label = new Label(preferences.defautlLabel() + ":");
                Focusable component = generateField(fieldName, preferences);
                if (component != null) {
                    editorRow.addComponent(label);
                    editorRow.addComponent(component);
                    if (getDefaultField().equals(fieldName)) {
                        defaultFocus = component;
                    }
                }
            }
            verticalLayout.addComponent(editorRow);
            verticalLayout.setComponentAlignment(editorRow, Alignment.MIDDLE_CENTER);
        }
        return verticalLayout;
    }

    private Focusable generateField(final String fieldName, final IdObjectFieldPreferences preferences) {
        Focusable component = null;
        switch (preferences.uiFieldType()) {
            case CHECKBOX:
                CheckBox checkBox = new CheckBox();
                entityBeanFieldGroup.bind(checkBox, fieldName);
                component = checkBox;
                break;
            case DATE_TIME:
                DateField dateTimeField = new DateField();
                dateTimeField.setResolution(Resolution.MINUTE);
                dateTimeField.setConverter(new LocalDateTimeDateConverter());
                entityBeanFieldGroup.bind(dateTimeField, fieldName);
                component = dateTimeField;
                break;
            case SINGLE_SELECT_LIST:
                ComboBox singleSelect = new ComboBox();
                singleSelect.setFilteringMode(FilteringMode.CONTAINS);
                singleSelect.setNewItemsAllowed(false);
                singleSelect.setTextInputAllowed(true);
                addDataSourceToSelectField(fieldName, singleSelect);
                entityBeanFieldGroup.bind(singleSelect, fieldName);
                component = singleSelect;
                break;
            case MULTI_SELECT_PICKER:
                TwinColSelect multiSelect = new TwinColSelect();
                multiSelect.setRows(preferences.height());
                addDataSourceToSelectField(fieldName, multiSelect);
                entityBeanFieldGroup.bind(multiSelect, fieldName);
                component = multiSelect;
                break;
            case TEXT:
                TextField uiField = new TextField();
                uiField.setWidth(preferences.width(), Unit.EM);
                entityBeanFieldGroup.bind(uiField, fieldName);
                component = uiField;
                break;
            case MULTI_LINE_TEXT:
                TextArea textArea = new TextArea();
                textArea.setRows(preferences.height());
                textArea.setWidth(preferences.width(), Unit.EM);
                textArea.addFocusListener(new FieldEvents.FocusListener() {
                    @Override
                    public void focus(FieldEvents.FocusEvent event) {
                        disableDefaultEnterKey();
                    }
                });
                textArea.addBlurListener(new FieldEvents.BlurListener() {
                    @Override
                    public void blur(FieldEvents.BlurEvent event) {
                        enableDefaultEnterKey();
                    }
                });
                entityBeanFieldGroup.bind(textArea, fieldName);
                component = textArea;
                break;
            default:
                break;
        }
        return component;
    }
}
