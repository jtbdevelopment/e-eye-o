package com.jtbdevelopment.e_eye_o.entities.validation;

import com.jtbdevelopment.e_eye_o.entities.Observation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Date: 3/31/13
 * Time: 1:56 PM
 */
//  TODO - test
public class ObservationFollowUpSubjectValidator implements ConstraintValidator<ObservationFollowUpSubjectCheck, Observation> {
    @Override
    public void initialize(final ObservationFollowUpSubjectCheck observationFollowUpSubjectCheck) {
    }

    @Override
    public boolean isValid(final Observation observation, final ConstraintValidatorContext constraintValidatorContext) {
        return observation == null ||
                observation.getFollowUpForObservation() == null ||
                observation.getObservationSubject() == null || // assume something else will get this
                observation.getObservationSubject().equals(observation.getFollowUpForObservation().getObservationSubject());
    }
}
