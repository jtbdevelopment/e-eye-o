package com.jtbdevelopment.e_eye_o.entities.validation;

import com.jtbdevelopment.e_eye_o.entities.Observation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Date: 12/13/12
 * Time: 10:13 PM
 */
public class ObservationFollowUpValidator implements ConstraintValidator<ObservationFollowUpCheck, Observation> {
    @Override
    public void initialize(final ObservationFollowUpCheck observationFollowUpCheck) {

    }

    @Override
    public boolean isValid(final Observation observation, final ConstraintValidatorContext constraintValidatorContext) {
        return observation.getFollowUpObservation() == null ||
                !observation.getId().equals(observation.getFollowUpObservation().getId());
    }
}
