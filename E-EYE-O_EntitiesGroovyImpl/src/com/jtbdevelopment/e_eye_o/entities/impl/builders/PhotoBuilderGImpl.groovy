package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.DAO.helpers.PhotoHelper
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject
import com.jtbdevelopment.e_eye_o.entities.Photo
import com.jtbdevelopment.e_eye_o.entities.builders.PhotoBuilder
import org.joda.time.LocalDateTime

/**
 * Date: 12/1/13
 * Time: 3:37 PM
 */
class PhotoBuilderGImpl extends AppUserOwnedObjectBuilderGImpl<Photo> implements PhotoBuilder {
    private PhotoHelper photoHelper

    @Override
    PhotoBuilder withPhotoFor(final AppUserOwnedObject photoFor) {
        entity.photoFor = photoFor
        return this
    }

    @Override
    PhotoBuilder withDescription(final String description) {
        entity.description = description
        return this
    }

    @Override
    PhotoBuilder withTimestamp(final LocalDateTime timestamp) {
        entity.timestamp = timestamp
        return this
    }

    @Override
    PhotoBuilder withMimeType(final String mimeType) {
        entity.mimeType = mimeType
        photoHelper.reprocessForMimeType(entity)
        return this
    }

    @Override
    PhotoBuilder withImageData(final byte[] imageData) {
        photoHelper.setPhotoImages(entity, imageData)
        return this
    }
}
