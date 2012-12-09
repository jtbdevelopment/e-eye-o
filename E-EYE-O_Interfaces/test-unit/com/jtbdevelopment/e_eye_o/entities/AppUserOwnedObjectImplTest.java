package com.jtbdevelopment.e_eye_o.entities;

import org.testng.annotations.Test;
import sun.plugin.dom.exception.InvalidStateException;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static org.testng.Assert.assertEquals;

/**
 * Date: 12/8/12
 * Time: 2:18 PM
 */
public class AppUserOwnedObjectImplTest extends AbstractIdObjectTest {
    private final AppUser user1 = (AppUser) new AppUserImpl().setId("123");
    private final AppUser user2 = (AppUser) new AppUserImpl().setId("456");

    private class LocalEntity extends AppUserOwnedObjectImpl {
        public LocalEntity() {
            super();
        }

        public LocalEntity(final AppUser appUser) {
            super(appUser);
        }

        public LocalEntity addAnotherUserObject(final LocalEntity other) {
            validateSameAppUser(other);
            return this;
        }

        public LocalEntity addAnotherUserObjectCollection(final Collection<LocalEntity> others) {
            validateSameAppUsers(others);
            return this;
        }
    }

    @Test
    public void testGetAppUserForNewObjects() throws Exception {
        assertEquals(user1, new LocalEntity(user1).getAppUser());
        assertEquals(null, new LocalEntity().getAppUser());
        assertEquals(user2, new LocalEntity().setAppUser(user2).getAppUser());
    }

    @Test
    public void testAssigningDifferentOwnerSameIDOK() {
        AppUserImpl userCopy = new AppUserImpl();
        userCopy.setId(user1.getId());

        assertEquals(userCopy, new LocalEntity(user1).setAppUser(userCopy).getAppUser());
    }

    @Test(expectedExceptions = InvalidParameterException.class)
    public void testSetAppUserNullExceptionsOnConstructor() {
        new LocalEntity(null);

    }

    @Test(expectedExceptions = InvalidParameterException.class)
    public void testSetAppUserNullExceptionsOnSet() {
        new LocalEntity().setAppUser(null);
    }

    @Test(expectedExceptions = InvalidParameterException.class)
    public void testSetNewAppUserExceptions() {
        new LocalEntity(user1).setAppUser(user2);
    }

    @Test
    public void testValidateSameAppUserMatches() {
        new LocalEntity(user1).addAnotherUserObject(new LocalEntity(user1));
    }

    @Test(expectedExceptions = InvalidStateException.class)
    public void testValidateSameAppUserExceptionOnUnownedThis() {
        new LocalEntity().addAnotherUserObject(new LocalEntity());
    }

    @Test(expectedExceptions = InvalidStateException.class)
    public void testValidateSameAppUserExceptionsOnUnownedOther() {
        new LocalEntity(user1).addAnotherUserObject(new LocalEntity());
    }

    @Test(expectedExceptions = InvalidParameterException.class)
    public void testValidateSameAppUserExceptionsOnDifferentOwners() {
        new LocalEntity(user1).addAnotherUserObject(new LocalEntity(user2));
    }

    @Test(expectedExceptions = InvalidParameterException.class)
    public void testValidatingSameAppUserMultipleObjects() {
        int size = 20;
        List<LocalEntity> others = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            others.add(new LocalEntity());
        }
        int wrongOne = new Random().nextInt(size - 5) + 5;  //  make sure bad one is more towards back
        for (int i = 0; i < size; ++i) {
            if (i != wrongOne) {
                others.get(i).setAppUser(user1);
            } else {
                others.get(i).setAppUser(user2);
            }
        }

        new LocalEntity(user1).addAnotherUserObjectCollection(others);
    }
}
