package com.jtbdevelopment.e_eye_o.entities;

/**
 * Date: 11/28/12
 * Time: 11:05 PM
 */
public abstract class ArchivableAppUserOwnedObjectImpl extends AppUserOwnedObjectImpl implements ArchivableAppUserOwnedObject {
    private boolean archived = false;

    protected ArchivableAppUserOwnedObjectImpl() {
    }

    public ArchivableAppUserOwnedObjectImpl(final AppUser appUser) {
        super(appUser);
    }

    @Override
    public boolean isArchived() {
        return archived;
    }

    @Override
    public ArchivableAppUserOwnedObject setArchived(boolean archived) {
        this.archived = archived;
        return this;
    }
}
