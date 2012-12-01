package com.jtbdevelopment.e_eye_o.entities;

import org.joda.time.LocalDateTime;

/**
 * Date: 11/18/12
 * Time: 12:56 AM
 */
public class PhotoImpl extends ArchivableAppUserOwnedObjectImpl implements Photo {
    private String description = "";
    private LocalDateTime timestamp;
    //  TODO - actual photo

    public PhotoImpl() {
    }

    public PhotoImpl(final AppUser appUser) {
        super(appUser);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Photo setDescription(final String description) {
        validateNonNullValue(description);
        this.description = description;
        return this;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public Photo setTimestamp(final LocalDateTime timestamp) {
        validateNonNullValue(timestamp);
        this.timestamp = timestamp;
        return this;
    }
}
