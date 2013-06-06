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
public @interface IdObjectDisplayPreferences {
    // Since we cannot put multi-dimensional arrays into annotations, we have to provide break indicator
    public static final String SECTION_BREAK = "SECTION_BREAK";

    String singular() default "";

    String plural() default "";

    int[] pageSizes() default {5, 10, 25, 50};

    int defaultPageSize() default 10;

    String defaultSortField();

    boolean defaultSortAscending() default true;

    boolean viewable() default true;

    boolean editable() default true;

    String[] viewFieldOrder() default {};

    String[] editFieldOrder() default {};
}
