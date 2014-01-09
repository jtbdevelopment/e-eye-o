package com.jtbdevelopment.e_eye_o.DAO

import org.testng.annotations.Test

import javax.validation.ConstraintViolationException

/**
 * Date: 1/8/14
 * Time: 10:32 PM
 */
abstract class AbstractValidationIntegration extends AbstractIntegration {

    @Test(groups = ["integration"], expectedExceptions = ConstraintViolationException)
    public void testValidationIsActive() {
        createUser("")
    }
}
