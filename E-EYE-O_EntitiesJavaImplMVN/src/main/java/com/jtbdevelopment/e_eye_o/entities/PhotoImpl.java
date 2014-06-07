package com.jtbdevelopment.e_eye_o.entities;

import org.joda.time.LocalDateTime;

/**
 * Date: 11/18/12
 * Time: 12:56 AM
 */
public class PhotoImpl extends AppUserOwnedObjectImpl implements Photo {
    private String description = "";
    private AppUserOwnedObject photoFor;
    private LocalDateTime timestamp = new LocalDateTime();
    private String mimeType = "";
    private byte[] imageData;
    private byte[] thumbnailImageData;


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
        return description.trim() + " " + timestamp.toString("MMM dd");
    }

    public byte[] getImageData() {
        return imageData != null ? imageData.clone() : null;
    }

    public void setImageData(final byte[] imageData) {
        this.imageData = imageData != null ? imageData.clone() : imageData;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(final String mimeType) {
        this.mimeType = mimeType;
    }

    public byte[] getThumbnailImageData() {
        return thumbnailImageData != null ? thumbnailImageData.clone() : null;
    }

    public void setThumbnailImageData(final byte[] thumbnailImageData) {
        this.thumbnailImageData = thumbnailImageData != null ? thumbnailImageData.clone() : null;
    }
}
