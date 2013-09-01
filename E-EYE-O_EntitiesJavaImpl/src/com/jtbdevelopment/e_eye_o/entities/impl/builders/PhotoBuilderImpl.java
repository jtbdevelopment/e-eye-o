package com.jtbdevelopment.e_eye_o.entities.impl.builders;

import com.jtbdevelopment.e_eye_o.DAO.helpers.PhotoHelper;
import com.jtbdevelopment.e_eye_o.DAO.helpers.PhotoHelperImpl;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.Photo;
import com.jtbdevelopment.e_eye_o.entities.builders.PhotoBuilder;
import org.joda.time.LocalDateTime;

/**
 * Date: 3/9/13
 * Time: 12:10 PM
 */
public class PhotoBuilderImpl extends AppUserOwnedObjectBuilderImpl<Photo> implements PhotoBuilder {
    private final PhotoHelper photoHelper;

    public PhotoBuilderImpl(final PhotoHelper photoHelper, final Photo entity) {
        super(entity);
        if (photoHelper != null) {
            this.photoHelper = photoHelper;
        } else {
            this.photoHelper = new PhotoHelperImpl();
        }
    }

    @Override
    public PhotoBuilder withPhotoFor(final AppUserOwnedObject photoFor) {
        entity.setPhotoFor(photoFor);
        return this;
    }

    @Override
    public PhotoBuilder withDescription(final String description) {
        entity.setDescription(description);
        return this;
    }

    @Override
    public PhotoBuilder withTimestamp(final LocalDateTime timestamp) {
        entity.setTimestamp(timestamp);
        return this;
    }

    @Override
    public PhotoBuilder withMimeType(final String mimeType) {
        entity.setMimeType(mimeType);
        return this;
    }

    @Override
    public PhotoBuilder withImageData(final byte[] imageData) {
        photoHelper.setPhotoImages(entity, imageData);
        return this;
    }
}
