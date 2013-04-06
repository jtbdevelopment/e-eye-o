package com.jtbdevelopment.e_eye_o.entities.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Date: 4/5/13
 * Time: 9:14 PM
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface PreferredDescription {
    String singular() default "";

    String plural() default "";
}
