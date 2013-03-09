package com.jtbdevelopment.e_eye_o.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import org.testng.annotations.Test;

import java.security.InvalidParameterException;

import static org.testng.Assert.assertEquals;

/**
 * Date: 12/8/12
 * Time: 2:18 PM
 */
public class AppUserOwnedObjectImplTest extends AbstractAppUserOwnedObjectTest<AppUserOwnedObjectImplTest.LocalEntity> {

    public static class LocalEntity extends AppUserOwnedObjectImpl {

        public LocalEntity(final AppUser appUser) {
            super(appUser);
        }
    }

    public AppUserOwnedObjectImplTest() {
        super(LocalEntity.class);
    }

    @Test
    public void testDefaultGetSetArchived() throws Exception {
        checkBooleanDefaultAndSetGet("archived", false);
    }

    @Test
    public void testConstructorsForNewObjects() {
        checkDefaultAndAppUserConstructorTests();
    }

    @Test
    public void testAssigningDifferentOwnerSameIDisOK() {
        AppUserImpl userCopy = new AppUserImpl();
        userCopy.setId(USER1.getId());

        final LocalEntity localEntity = new LocalEntity(USER1);
        localEntity.setAppUser(userCopy);
        assertEquals(userCopy, localEntity.getAppUser());
    }

    @Test(expectedExceptions = InvalidParameterException.class)
    public void testChangeAppUserExceptions() {
        new LocalEntity(USER1).setAppUser(USER2);
    }

}
