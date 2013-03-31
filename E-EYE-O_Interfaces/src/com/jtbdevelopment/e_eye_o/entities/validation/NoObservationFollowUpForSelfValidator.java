package com.jtbdevelopment.e_eye_o.entities.validation;

import com.jtbdevelopment.e_eye_o.entities.Observation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Date: 12/13/12
 * Time: 10:13 PM
 */
public class NoObservationFollowUpForSelfValidator implements ConstraintValidator<NoObservationFollowUpForSelfCheck, Observation> {
    @Override
    public void initialize(final NoObservationFollowUpForSelfCheck noObservationFollowUpForSelfCheck) {
    }

    @Override
    public boolean isValid(final Observation observation, final ConstraintValidatorContext constraintValidatorContext) {
        return observation.getFollowUpForObservation() == null ||
                observation.getId() == null ||  //  Presume other validation will get it
                !observation.getId().equals(observation.getFollowUpForObservation().getId());
    }
}
