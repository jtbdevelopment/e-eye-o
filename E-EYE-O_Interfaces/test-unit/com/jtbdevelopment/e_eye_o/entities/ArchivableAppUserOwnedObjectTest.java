package com.jtbdevelopment.e_eye_o.entities;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Date: 12/8/12
 * Time: 2:13 PM
 */
public class ArchivableAppUserOwnedObjectTest extends AbstractIdObjectTest {
    public static class LocalArchivableObject extends ArchivableAppUserOwnedObjectImpl {
        public LocalArchivableObject() {
            super();
        }

        public LocalArchivableObject(final AppUser appUser) {
            super(appUser);
        }
    }

    @Test
    public void testDefaultGetSetArchived() throws Exception {
        booleanSetGetAndDefaultCheck(LocalArchivableObject.class, "archived", false);
    }
}
