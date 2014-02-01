package com.jtbdevelopment.e_eye_o.DAO

import com.jtbdevelopment.e_eye_o.entities.AppUser
import org.testng.annotations.Test

import javax.validation.ConstraintViolationException

/**
 * Date: 1/8/14
 * Time: 10:32 PM
 */
abstract class AbstractValidationIntegration extends AbstractIntegration {

    @Test(groups = ["integration"], expectedExceptions = ConstraintViolationException)
    public void testValidationIsActiveOnCreate() {
        createUser("")
    }

    @Test(groups = ["integration"], expectedExceptions = ConstraintViolationException)
    public void testValidationIsActiveOnUpdate() {
        AppUser user = createUser("validate@update")
        user.setEmailAddress("");
        rwDAO.update(user, user);
    }

    @Test(groups = ["integration"], expectedExceptions = ConstraintViolationException)
    public void testValidationIsActiveOnTrustedUpdate() {
        AppUser user = createUser("validate@trustedupdate")
        user.setEmailAddress("");
        rwDAO.trustedUpdate(user);
    }

    @Test(groups = ["integration"], expectedExceptions = ConstraintViolationException)
    public void testValidationIsActiveOnTrustedUpdates() {
        AppUser user = createUser("validate@trustedupdates")
        user.setEmailAddress("");
        rwDAO.trustedUpdates([user]);
    }
}
