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

    ;

    public enum PreferredUIFieldType {DEFAULT, TEXT, PASSWORD, MULTI_LINE_TEXT, SINGLE_SELECT_LIST, MULTI_SELECT_PICKER, DATE, DATE_TIME, CHECKBOX, CUSTOM}

    public enum PreferredAlignment {DEFAULT, LEFT, MIDDLE, RIGHT}

    boolean displayable() default true;

    EditableBy editableBy() default EditableBy.USER;

    int width() default 10;  //  Assumed to be in EM unit

    int height() default 1;  //  Dependent on display type

    String defautlLabel() default "";

    String tableLabel() default "";  // use default label

    String editLabel() default "";  //  Use default label

    PreferredUIFieldType uiFieldType() default PreferredUIFieldType.DEFAULT;

    PreferredUIFieldType tableFieldType() default PreferredUIFieldType.DEFAULT;  // use uifieldtype

    PreferredUIFieldType editFieldType() default PreferredUIFieldType.DEFAULT;  // use uifieldtype

    PreferredAlignment uiAlignment() default PreferredAlignment.DEFAULT;

    PreferredAlignment tableAlignment() default PreferredAlignment.DEFAULT;

    PreferredAlignment editableAlignment() default PreferredAlignment.DEFAULT;
}
