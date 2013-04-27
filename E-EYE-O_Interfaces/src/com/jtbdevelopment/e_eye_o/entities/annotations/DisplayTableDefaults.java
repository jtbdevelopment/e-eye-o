package com.jtbdevelopment.e_eye_o.entities.annotations;

/**
 * Date: 4/27/13
 * Time: 3:01 PM
 */
public @interface DisplayTableDefaults {
    int defaultDisplaySize() default 10;

    String defaultSortField();

    boolean defaultSortAscending() default true;
}
