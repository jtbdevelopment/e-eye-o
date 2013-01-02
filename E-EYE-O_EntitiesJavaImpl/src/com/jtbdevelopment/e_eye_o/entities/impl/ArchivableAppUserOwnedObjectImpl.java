package com.jtbdevelopment.e_eye_o.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.ArchivableAppUserOwnedObject;

/**
 * Date: 11/28/12
 * Time: 11:05 PM
 */
public abstract class ArchivableAppUserOwnedObjectImpl extends AppUserOwnedObjectImpl implements ArchivableAppUserOwnedObject {
    private boolean archived = false;

    protected ArchivableAppUserOwnedObjectImpl() {
    }

    protected ArchivableAppUserOwnedObjectImpl(final AppUser appUser) {
        super(appUser);
    }

    @Override
    public boolean isArchived() {
        return archived;
    }

    @Override
    public <T extends ArchivableAppUserOwnedObject> T setArchived(boolean archived) {
        this.archived = archived;
        return (T) this;
    }
}
