package com.jtbdevelopment.e_eye_o.entities

import groovy.transform.CompileStatic
import org.joda.time.LocalDateTime

/**
 * Date: 11/30/13
 * Time: 12:25 PM
 */
@CompileStatic
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
        return ((byte[]) imageData?.clone())
    }

    void setImageData(final byte[] imageData) {
        this.imageData = imageData?.clone()
    }

    byte[] getThumbnailImageData() {
        return ((byte[]) thumbnailImageData?.clone())
    }

    void setThumbnailImageData(final byte[] thumbnailImageData) {
        this.thumbnailImageData = thumbnailImageData?.clone()
    }
}
