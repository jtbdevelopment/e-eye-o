package com.jtbdevelopment.e_eye_o.entities.validation;

import com.jtbdevelopment.e_eye_o.entities.Observation;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.validation.ConstraintValidatorContext;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Date: 12/29/12
 * Time: 4:48 PM
 */
public class NoObservationFollowUpForSelfValidatorTest {
    private Mockery context;
    private ConstraintValidatorContext validatorContext;
    private Observation o1;
    private Observation o2;
    final private NoObservationFollowUpForSelfValidator check = new NoObservationFollowUpForSelfValidator();

    @BeforeMethod
    public void beforeTest() {
        context = new Mockery();
        validatorContext = context.mock(ConstraintValidatorContext.class);
        o1 = context.mock(Observation.class, "o1");
        o2 = context.mock(Observation.class, "o2");
    }

    @Test
    public void testNullFollowUpIsValid() {
        context.checking(new Expectations() {{
            allowing(o1).getId();
            will(returnValue("1"));
            allowing(o1).getFollowUpForObservation();
            will(returnValue(null));
        }});
        assertTrue(check.isValid(o1, validatorContext));
    }

    @Test
    public void testNonNullFollowUpIsValidWithNullId() {
        context.checking(new Expectations() {{
            allowing(o1).getId();
            will(returnValue(null));
            allowing(o1).getFollowUpForObservation();
            will(returnValue(o2));
            one(o2).getId();
            will(returnValue("2"));

        }});
        assertTrue(check.isValid(o1, validatorContext));
    }

    @Test
    public void testFollowUpIsValidWithDifferentIds() {
        context.checking(new Expectations() {{
            allowing(o1).getId();
            will(returnValue("1"));
            allowing(o1).getFollowUpForObservation();
            will(returnValue(o2));
            one(o2).getId();
            will(returnValue("2"));
        }});
        assertTrue(check.isValid(o1, validatorContext));
    }

    @Test
    public void testFollowUpIsInValidWithSameIds() {
        context.checking(new Expectations() {{
            allowing(o1).getId();
            will(returnValue("1"));
            allowing(o1).getFollowUpForObservation();
            will(returnValue(o2));
            one(o2).getId();
            will(returnValue("1"));
        }});
        assertFalse(check.isValid(o1, validatorContext));
    }
}
