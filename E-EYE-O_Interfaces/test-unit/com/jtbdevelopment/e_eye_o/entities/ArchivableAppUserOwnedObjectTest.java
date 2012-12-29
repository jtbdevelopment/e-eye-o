package com.jtbdevelopment.e_eye_o.entities;

import org.testng.annotations.Test;

/**
 * Date: 12/8/12
 * Time: 2:13 PM
 */
public class ArchivableAppUserOwnedObjectTest extends AbstractAppUserOwnedObjectTest<ArchivableAppUserOwnedObjectTest.LocalArchivableObject> {
    public static class LocalArchivableObject extends ArchivableAppUserOwnedObjectImpl {
        public LocalArchivableObject() {
            super();
        }

        public LocalArchivableObject(final AppUser appUser) {
            super(appUser);
        }
    }

    public ArchivableAppUserOwnedObjectTest() {
        super(LocalArchivableObject.class);
    }

    @Test
    public void testConstructorsForNewObjects() {
        checkDefaultAndAppUserConstructorTests();
    }

    @Test
    public void testDefaultGetSetArchived() throws Exception {
        checkBooleanDefaultAndSetGet("archived", false);
    }

}
