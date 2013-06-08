package com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors;

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectEntitySettings;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectFieldSettings;
import com.jtbdevelopment.e_eye_o.entities.reflection.IdObjectInterfaceResolver;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters.LocalDateTimeDateConverter;
import com.vaadin.event.FieldEvents;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
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

    public GeneratedEditorDialogWindow(final Class<T> entityType, final float width, final Unit widthUnit, final float height, final Unit heightUnit) {
        super(entityType, width, widthUnit, height, heightUnit);
    }

    protected String getDefaultField() {
        return getFieldRows().get(0).get(0);
    }

    protected List<List<String>> getFieldRows() {
        List<List<String>> rows = new LinkedList<>();
        List<String> currentRow = new LinkedList<>();

        for (String field : entityType.getAnnotation(IdObjectEntitySettings.class).editFieldOrder()) {
            if (IdObjectEntitySettings.SECTION_BREAK.equals(field)) {
                rows.add(currentRow);
                currentRow = new LinkedList<>();
            } else {
                currentRow.add(field);
            }
        }
        if (currentRow.size() > 0) {
            rows.add(currentRow);
        }
        return rows;
    }

    @SuppressWarnings("unused")
    protected void addDataSourceToSelectField(final String fieldName, final AbstractSelect select) {
    }

    protected Component getCustomField(final String fieldName) {
        return null;
    }

    @Override
    protected Focusable getInitialFocusComponent() {
        return defaultFocus;
    }

    @Override
    protected Layout buildEditorLayout() {
        Map<String, IdObjectFieldSettings> fields = interfaceResolver.getAllFieldPreferences(entityType);
        AbstractOrderedLayout rowContainerLayout = getRowContainerLayout();
        for (List<String> fieldRow : getFieldRows()) {
            Layout editorRow = getEditorRow();
            for (String fieldName : fieldRow) {
                IdObjectFieldSettings preferences = fields.get(fieldName);
                if (preferences == null) {
                    continue;
                }
                Label label = new Label(preferences.label() + ":");
                Component component = generateField(fieldName, preferences);
                if (component != null) {
                    editorRow.addComponent(label);
                    editorRow.addComponent(component);
                    if (getDefaultField().equals(fieldName) && component instanceof Focusable) {
                        defaultFocus = (Focusable) component;
                    }
                }
            }
            rowContainerLayout.addComponent(editorRow);
            rowContainerLayout.setComponentAlignment(editorRow, Alignment.MIDDLE_CENTER);
        }
        return rowContainerLayout;
    }

    protected AbstractOrderedLayout getRowContainerLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSpacing(true);
        return verticalLayout;
    }

    protected Layout getEditorRow() {
        HorizontalLayout editorRow = new HorizontalLayout();
        editorRow.setSpacing(true);
        return editorRow;
    }

    private Component generateField(final String fieldName, final IdObjectFieldSettings preferences) {
        Component component = null;
        switch (preferences.fieldType()) {
            case CHECKBOX:
                CheckBox checkBox = new CheckBox();
                entityBeanFieldGroup.bind(checkBox, fieldName);
                component = checkBox;
                break;
            case LOCAL_DATE_TIME:
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
            case CUSTOM:
                component = getCustomField(fieldName);
                break;
            default:
                break;
        }
        return component;
    }
}
