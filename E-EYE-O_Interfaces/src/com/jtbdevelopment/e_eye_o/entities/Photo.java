package com.jtbdevelopment.e_eye_o.entities;

import org.joda.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Date: 11/25/12
 * Time: 3:18 PM
 */
public interface Photo extends ArchivableAppUserOwnedObject {

    public static final String PHOTO_TIMESTAMP_CANNOT_BE_NULL_ERROR = "Photo.timestamp" + CANNOT_BE_NULL_ERROR;
    public static final String PHOTO_DESCRIPTION_SIZE_ERROR = "Photo.description" + DESCRIPTION_SIZE_ERROR;
    public static final String PHOTO_DESCRIPTION_CANNOT_BE_NULL_ERROR = "Photo.description" + CANNOT_BE_NULL_ERROR;
    public static final String PHOTO_PHOTO_FOR_CANNOT_BE_NULL_ERROR = "Photo.photoFor" + CANNOT_BE_NULL_ERROR;

    @NotNull(message = PHOTO_PHOTO_FOR_CANNOT_BE_NULL_ERROR)
    AppUserOwnedObject getPhotoFor();

    Photo setPhotoFor(final AppUserOwnedObject photoFor);

    @NotNull(message = PHOTO_DESCRIPTION_CANNOT_BE_NULL_ERROR)
    @Size(max = MAX_DESCRIPTION_SIZE, message = PHOTO_DESCRIPTION_SIZE_ERROR)
    String getDescription();

    Photo setDescription(String description);

    @NotNull(message = PHOTO_TIMESTAMP_CANNOT_BE_NULL_ERROR)
    LocalDateTime getTimestamp();

    Photo setTimestamp(LocalDateTime timestamp);
}
