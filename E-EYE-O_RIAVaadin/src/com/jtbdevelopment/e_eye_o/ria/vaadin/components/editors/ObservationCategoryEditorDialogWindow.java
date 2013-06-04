package com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors;

import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 3/16/13
 * Time: 6:36 PM
 */
@org.springframework.stereotype.Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ObservationCategoryEditorDialogWindow extends GeneratedEditorDialogWindow<ObservationCategory> {
    public ObservationCategoryEditorDialogWindow() {
        //  height seems to be ignored as less than some minimum it wants
        super(ObservationCategory.class, 50, 2);
    }

    @Override
    protected String getDefaultField() {
        return "shortName";
    }

    @Override
    protected List<List<String>> getFieldRows() {
        List<List<String>> fieldRows = new LinkedList<>();
        fieldRows.add(Arrays.asList("shortName", "description"));
        return fieldRows;
    }

}
