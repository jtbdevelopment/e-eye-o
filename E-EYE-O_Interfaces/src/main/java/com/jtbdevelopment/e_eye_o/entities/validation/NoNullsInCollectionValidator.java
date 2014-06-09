package com.jtbdevelopment.e_eye_o.entities.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Date: 12/13/12
 * Time: 10:49 PM
 */
public class NoNullsInCollectionValidator implements ConstraintValidator<NoNullsInCollectionCheck, Iterable<?>> {
    @Override
    public void initialize(final NoNullsInCollectionCheck noNullsInCollectionCheck) {

    }

    @Override
    public boolean isValid(final Iterable<?> iterable, final ConstraintValidatorContext constraintValidatorContext) {
        for (Object object : iterable) {
            if (object == null) {
                return false;
            }
        }
        return true;
    }
}
