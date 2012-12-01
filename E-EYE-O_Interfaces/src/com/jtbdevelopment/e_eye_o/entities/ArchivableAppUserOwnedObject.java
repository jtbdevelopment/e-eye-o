package com.jtbdevelopment.e_eye_o.entities;

/**
 * Date: 11/25/12
 * Time: 3:11 PM
 */
public interface ArchivableAppUserOwnedObject extends AppUserOwnedObject {
    boolean isArchived();

    ArchivableAppUserOwnedObject setArchived(boolean archived);
}
