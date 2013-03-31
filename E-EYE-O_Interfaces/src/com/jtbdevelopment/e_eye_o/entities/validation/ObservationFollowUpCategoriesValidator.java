package com.jtbdevelopment.e_eye_o.entities.validation;

import com.jtbdevelopment.e_eye_o.entities.Observation;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

/**
 * Date: 3/31/13
 * Time: 1:56 PM
 */
//  TODO - test
public class ObservationFollowUpCategoriesValidator implements ConstraintValidator<ObservationFollowUpCategoriesCheck, Observation> {
    @Override
    public void initialize(final ObservationFollowUpCategoriesCheck observationFollowUpCategoriesCheck) {
    }

    @Override
    public boolean isValid(final Observation observation, final ConstraintValidatorContext constraintValidatorContext) {
        if (observation == null ||
                observation.getFollowUpForObservation() == null ||
                observation.getFollowUpForObservation().getCategories().isEmpty())
            return true;

        Set<ObservationCategory> thisCategories = observation.getCategories();
        for (ObservationCategory category : observation.getFollowUpForObservation().getCategories()) {
            if (thisCategories.contains(category)) {
                return true;
            }
        }
        return false;
    }
}
