package com.jtbdevelopment.e_eye_o.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import org.testng.annotations.Test;

import java.security.InvalidParameterException;

import static org.testng.Assert.assertEquals;

/**
 * Date: 12/8/12
 * Time: 2:18 PM
 */
public class AppUserOwnedObjectImplTest extends AbstractAppUserOwnedObjectTest<AppUserOwnedObjectImplTest.LocalEntity> {

    public static class LocalEntity extends AppUserOwnedObjectImpl {

        public LocalEntity() {
            super();
        }

        public LocalEntity(final AppUser appUser) {
            super(appUser);
        }
    }

    public AppUserOwnedObjectImplTest() {
        super(LocalEntity.class);
    }

    @Test
    public void testConstructorsForNewObjects() {
        checkDefaultAndAppUserConstructorTests();
    }

    @Test
    public void testGetSetAppUserAfterConstruction() throws Exception {
        assertEquals(USER2, new LocalEntity().setAppUser(USER2).getAppUser());
    }

    @Test
    public void testAssigningDifferentOwnerSameIDisOK() {
        AppUserImpl userCopy = new AppUserImpl();
        userCopy.setId(USER1.getId());

        assertEquals(userCopy, new LocalEntity(USER1).setAppUser(userCopy).getAppUser());
    }

    @Test
    public void testSetAppUserNullValidation() {
        validateExpectingError(new LocalEntity(null), AppUserOwnedObject.APP_USER_CANNOT_BE_NULL_ERROR);
        validateExpectingError(new LocalEntity().setAppUser(null), AppUserOwnedObject.APP_USER_CANNOT_BE_NULL_ERROR);
    }

    @Test(expectedExceptions = InvalidParameterException.class)
    public void testChangeAppUserExceptions() {
        new LocalEntity(USER1).setAppUser(USER2);
    }

}
