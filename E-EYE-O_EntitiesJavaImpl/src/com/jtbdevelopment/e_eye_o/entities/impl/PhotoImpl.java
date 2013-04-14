package com.jtbdevelopment.e_eye_o.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.Photo;
import org.joda.time.LocalDateTime;

/**
 * Date: 11/18/12
 * Time: 12:56 AM
 */
public class PhotoImpl extends AppUserOwnedObjectImpl implements Photo {
    private String description = "";
    private AppUserOwnedObject photoFor;
    private LocalDateTime timestamp = new LocalDateTime();
    private String mimeType;
    private byte[] imageData;


    PhotoImpl(final AppUser appUser) {
        super(appUser);
    }

    @Override
    public AppUserOwnedObject getPhotoFor() {
        return photoFor;
    }

    @Override
    public void setPhotoFor(final AppUserOwnedObject photoFor) {
        this.photoFor = photoFor;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(final LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String getSummaryDescription() {
        return (description + " " + timestamp.toString("MMM dd")).trim();
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
