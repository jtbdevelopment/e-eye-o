package com.jtbdevelopment.e_eye_o.entities.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Date: 4/5/13
 * Time: 9:14 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface IdObjectPreferredDescription {
    String singular() default "";

    String plural() default "";
}
