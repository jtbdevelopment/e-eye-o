package com.jtbdevelopment.e_eye_o.entities.builders;

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.Photo;
import org.joda.time.LocalDateTime;

/**
 * Date: 3/9/13
 * Time: 11:18 AM
 */
public interface PhotoBuilder extends AppUserOwnedObjectBuilder<Photo> {
    PhotoBuilder withPhotoFor(final AppUserOwnedObject photoFor);

    PhotoBuilder withDescription(final String description);

    PhotoBuilder withTimestamp(final LocalDateTime timestamp);

    PhotoBuilder withMimeType(final String mimeType);

    PhotoBuilder withImageData(final byte[] imageData);
}
