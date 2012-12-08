package com.jtbdevelopment.e_eye_o.entities;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Date: 12/8/12
 * Time: 2:13 PM
 */
public class ArchivableAppUserOwnedObjectTest {
    private class LocalArchivableObject extends ArchivableAppUserOwnedObjectImpl {
        public LocalArchivableObject() {
            super(new AppUserImpl());
        }
    }

    @Test
    public void testDefaultArchiveFlagIsFalse() {
        assertEquals(false, new LocalArchivableObject().isArchived());
    }

    @Test
    public void testGetSetArchived() throws Exception {
        final LocalArchivableObject entity = new LocalArchivableObject();
        assertEquals(true, entity.setArchived(true).isArchived());
        assertEquals(false, entity.setArchived(false).isArchived());
    }
}
