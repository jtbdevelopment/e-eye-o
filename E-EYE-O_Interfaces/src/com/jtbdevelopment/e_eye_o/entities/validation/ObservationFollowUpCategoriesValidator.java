package com.jtbdevelopment.e_eye_o.entities.validation;

import com.google.common.collect.Sets;
import com.jtbdevelopment.e_eye_o.entities.Observation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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
        return observation == null
                || observation.getFollowUpForObservation() == null
                || observation.getFollowUpForObservation().getCategories().isEmpty()
                || Sets.intersection(observation.getCategories(), observation.getFollowUpForObservation().getCategories()).size() > 0;
    }
}
