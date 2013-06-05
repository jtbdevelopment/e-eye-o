package com.jtbdevelopment.e_eye_o.entities.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Date: 6/2/13
 * Time: 8:20 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface IdObjectFieldPreferences {
    public enum EditableBy {USER, ADMIN, NONE}

    public enum DisplayFieldType {DEFAULT, TEXT, PASSWORD, MULTI_LINE_TEXT, SINGLE_SELECT_LIST, MULTI_SELECT_PICKER, DATE_TIME, LOCAL_DATE_TIME, CHECKBOX, REVERSE_CHECKBOX, CUSTOM}

    public enum DisplayAlignment {DEFAULT, LEFT, CENTER, RIGHT}

    boolean viewable() default true;

    EditableBy editableBy() default EditableBy.USER;

    int width() default 10;  //  Assumed to be in EM unit

    int height() default 1;  //  Dependent on display type

    String label() default "";

    String tableLabel() default "";  // use default label

    String editLabel() default "";  //  Use default label

    DisplayFieldType fieldType() default DisplayFieldType.DEFAULT;

    DisplayFieldType tableFieldType() default DisplayFieldType.DEFAULT;

    DisplayFieldType editFieldType() default DisplayFieldType.DEFAULT;

    DisplayAlignment alignment() default DisplayAlignment.DEFAULT;

    DisplayAlignment tableAlignment() default DisplayAlignment.DEFAULT;

    DisplayAlignment editableAlignment() default DisplayAlignment.DEFAULT;
}
