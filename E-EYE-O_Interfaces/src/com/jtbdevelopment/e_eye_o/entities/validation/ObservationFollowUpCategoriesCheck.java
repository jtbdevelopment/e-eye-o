package com.jtbdevelopment.e_eye_o.entities.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Date: 12/13/12
 * Time: 10:11 PM
 */
@Target(value = {ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ObservationFollowUpCategoriesValidator.class)
public @interface ObservationFollowUpCategoriesCheck {
    String message() default "{com.jtbdevelopment.e_eye_o.entities.validation.observationfollowupcategoriescheck}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
