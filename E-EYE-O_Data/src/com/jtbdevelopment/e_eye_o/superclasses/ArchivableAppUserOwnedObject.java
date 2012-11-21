package com.jtbdevelopment.e_eye_o.superclasses;

import com.jtbdevelopment.e_eye_o.entities.AppUser;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Date: 11/19/12
 * Time: 10:40 PM
 */
@MappedSuperclass
public class ArchivableAppUserOwnedObject extends AppUserOwnedObject {
    private boolean archived = false;

    @SuppressWarnings("unused")
    protected ArchivableAppUserOwnedObject() {
        //  for hibernate
    }

    public ArchivableAppUserOwnedObject(final AppUser appUser) {
        super(appUser);
    }

    @Column(nullable = false)
    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }
}
