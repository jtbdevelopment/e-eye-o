package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject
import com.jtbdevelopment.e_eye_o.entities.Photo
import org.joda.time.LocalDateTime

/**
 * Date: 11/30/13
 * Time: 12:25 PM
 */
class PhotoGImpl extends AppUserOwnedObjectGImpl implements Photo {
    AppUserOwnedObject photoFor
    String description = ""
    LocalDateTime timestamp = LocalDateTime.now()
    String mimeType = ""
    byte[] imageData
    byte[] thumbnailImageData

    @Override
    String getSummaryDescription() {
        return description.trim() + " " + timestamp.toString("MMM dd");
    }

    byte[] getImageData() {
        return imageData?.clone()
    }

    void setImageData(final byte[] imageData) {
        this.imageData = imageData?.clone()
    }

    byte[] getThumbnailImageData() {
        return thumbnailImageData?.clone()
    }

    void setThumbnailImageData(final byte[] thumbnailImageData) {
        this.thumbnailImageData = thumbnailImageData?.clone()
    }
}
