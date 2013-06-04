package com.jtbdevelopment.e_eye_o.entities.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Date: 4/27/13
 * Time: 3:01 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface IdObjectTableDisplayPreferences {
    int[] displaySizes() default {5, 10, 25, 50};

    int defaultDisplaySize() default 10;

    String defaultSortField();

    boolean defaultSortAscending() default true;

    boolean displayable() default true;
}
