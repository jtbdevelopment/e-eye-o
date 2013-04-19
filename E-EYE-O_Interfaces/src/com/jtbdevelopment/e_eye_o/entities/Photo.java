package com.jtbdevelopment.e_eye_o.entities;

import com.jtbdevelopment.e_eye_o.entities.annotations.PreferredDescription;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Date: 11/25/12
 * Time: 3:18 PM
 */
@PreferredDescription(singular = "Photo", plural = "Photos")
public interface Photo extends AppUserOwnedObject {

    public static final int THUMBNAIL_SIZE = 75;
    public static final int STANDARD_SIZE = 1024;

    public static final String PHOTO_TIMESTAMP_CANNOT_BE_NULL_ERROR = "Photo.timestamp" + CANNOT_BE_NULL_ERROR;
    public static final String PHOTO_DESCRIPTION_SIZE_ERROR = "Photo.description" + DESCRIPTION_SIZE_ERROR;
    public static final String PHOTO_DESCRIPTION_CANNOT_BE_BLANK_OR_NULL_ERROR = "Photo.description" + CANNOT_BE_BLANK_OR_NULL_ERROR;
    public static final String PHOTO_PHOTO_FOR_CANNOT_BE_NULL_ERROR = "Photo.photoFor" + CANNOT_BE_NULL_ERROR;
    public static final String PHOTO_MIME_TYPE_CANNOT_BE_BLANK_OR_NULL = "Photo.mimeType" + CANNOT_BE_BLANK_OR_NULL_ERROR;
    public static final String PHOTO_IMAGE_DATA_CANNOT_BE_BLANK_OR_NULL = "Photo.imageData" + CANNOT_BE_BLANK_OR_NULL_ERROR;
    public static final String PHOTO_THUMBNAIL_IMAGE_DATA_CANNOT_BE_BLANK_OR_NULL = "Photo.thumbnail" + CANNOT_BE_BLANK_OR_NULL_ERROR;

    @NotNull(message = PHOTO_PHOTO_FOR_CANNOT_BE_NULL_ERROR)
    AppUserOwnedObject getPhotoFor();

    void setPhotoFor(final AppUserOwnedObject photoFor);

    @NotEmpty(message = PHOTO_DESCRIPTION_CANNOT_BE_BLANK_OR_NULL_ERROR)
    @Size(max = MAX_DESCRIPTION_SIZE, message = PHOTO_DESCRIPTION_SIZE_ERROR)
    String getDescription();

    void setDescription(String description);

    @NotNull(message = PHOTO_TIMESTAMP_CANNOT_BE_NULL_ERROR)
    LocalDateTime getTimestamp();

    void setTimestamp(LocalDateTime timestamp);

    @NotEmpty(message = PHOTO_MIME_TYPE_CANNOT_BE_BLANK_OR_NULL)
    String getMimeType();

    void setMimeType(final String mimeType);

    //  TODO - max size
    @NotEmpty(message = PHOTO_IMAGE_DATA_CANNOT_BE_BLANK_OR_NULL)
    byte[] getImageData();

    void setImageData(final byte[] imageBytes);

    @NotEmpty(message = PHOTO_THUMBNAIL_IMAGE_DATA_CANNOT_BE_BLANK_OR_NULL)
    byte[] getThumbnailImageData();

    void setThumbnailImageData(final byte[] thumbnailBytes);
}
