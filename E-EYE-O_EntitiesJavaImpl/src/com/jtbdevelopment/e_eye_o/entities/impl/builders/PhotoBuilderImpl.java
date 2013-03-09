package com.jtbdevelopment.e_eye_o.entities.impl.builders;

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.Photo;
import com.jtbdevelopment.e_eye_o.entities.builders.PhotoBuilder;
import org.joda.time.LocalDateTime;

/**
 * Date: 3/9/13
 * Time: 12:10 PM
 */
public class PhotoBuilderImpl extends AppUserOwnedObjectBuilderImpl<Photo> implements PhotoBuilder {
    public PhotoBuilderImpl(final Photo entity) {
        super(entity);
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
}
