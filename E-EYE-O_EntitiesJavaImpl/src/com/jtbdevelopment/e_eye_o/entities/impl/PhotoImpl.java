package com.jtbdevelopment.e_eye_o.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.Photo;
import org.joda.time.LocalDateTime;

/**
 * Date: 11/18/12
 * Time: 12:56 AM
 */
public class PhotoImpl extends ArchivableAppUserOwnedObjectImpl implements Photo {
    private String description = "";
    private AppUserOwnedObject photoFor;
    private LocalDateTime timestamp = new LocalDateTime();
    //  TODO - actual photo

    PhotoImpl() {
    }

    PhotoImpl(final AppUser appUser) {
        super(appUser);
    }

    @Override
    public AppUserOwnedObject getPhotoFor() {
        return photoFor;
    }

    @Override
    public Photo setPhotoFor(final AppUserOwnedObject photoFor) {
        this.photoFor = photoFor;
        return this;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Photo setDescription(final String description) {
        this.description = description;
        return this;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public Photo setTimestamp(final LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}