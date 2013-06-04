package com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors;

import com.jtbdevelopment.e_eye_o.entities.ClassList;
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
public class ClassListEditorDialogWindow extends GeneratedEditorDialogWindow<ClassList> {

    public ClassListEditorDialogWindow() {
        //  height seems to be ignored as less than some minimum it wants
        super(ClassList.class, 50, 2);
    }

    protected String getDefaultField() {
        return "description";
    }

    protected List<List<String>> getFieldRows() {
        List<List<String>> fieldRows = new LinkedList<>();
        fieldRows.add(Arrays.asList("description"));
        return fieldRows;
    }

}
