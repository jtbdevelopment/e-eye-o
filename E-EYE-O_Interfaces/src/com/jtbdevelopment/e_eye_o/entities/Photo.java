package com.jtbdevelopment.e_eye_o.entities;

import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectFieldPreferences;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectPreferredDescription;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectTableDisplayPreferences;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Date: 11/25/12
 * Time: 3:18 PM
 */
@IdObjectPreferredDescription(singular = "Photo", plural = "Photos")
@IdObjectTableDisplayPreferences(defaultDisplaySize = 10, defaultSortField = "timestamp", defaultSortAscending = true)
public interface Photo extends AppUserOwnedObject {

    public static final int THUMBNAIL_SIZE = 150;
    public static final int STANDARD_SIZE = 1024;

    public static final String PHOTO_TIMESTAMP_CANNOT_BE_NULL_ERROR = "Photo.timestamp" + CANNOT_BE_NULL_ERROR;
    public static final String PHOTO_DESCRIPTION_SIZE_ERROR = "Photo.description" + DESCRIPTION_SIZE_ERROR;
    public static final String PHOTO_DESCRIPTION_CANNOT_BE_BLANK_OR_NULL_ERROR = "Photo.description" + CANNOT_BE_BLANK_OR_NULL_ERROR;
    public static final String PHOTO_PHOTO_FOR_CANNOT_BE_NULL_ERROR = "Photo.photoFor" + CANNOT_BE_NULL_ERROR;
    public static final String PHOTO_MIME_TYPE_CANNOT_BE_BLANK_OR_NULL = "Photo.mimeType" + CANNOT_BE_BLANK_OR_NULL_ERROR;
    public static final String PHOTO_IMAGE_DATA_CANNOT_BE_BLANK_OR_NULL = "Photo.imageData" + CANNOT_BE_BLANK_OR_NULL_ERROR;
    public static final String PHOTO_THUMBNAIL_IMAGE_DATA_CANNOT_BE_BLANK_OR_NULL = "Photo.thumbnail" + CANNOT_BE_BLANK_OR_NULL_ERROR;

    @NotNull(message = PHOTO_PHOTO_FOR_CANNOT_BE_NULL_ERROR)
    @IdObjectFieldPreferences(defautlLabel = "Photo For", uiFieldType = IdObjectFieldPreferences.PreferredUIFieldType.SINGLE_SELECT_LIST, uiAlignment = IdObjectFieldPreferences.PreferredAlignment.LEFT)
    AppUserOwnedObject getPhotoFor();

    void setPhotoFor(final AppUserOwnedObject photoFor);

    @NotEmpty(message = PHOTO_DESCRIPTION_CANNOT_BE_BLANK_OR_NULL_ERROR)
    @Size(max = MAX_DESCRIPTION_SIZE, message = PHOTO_DESCRIPTION_SIZE_ERROR)
    @IdObjectFieldPreferences(defautlLabel = "Description", width = 30, uiFieldType = IdObjectFieldPreferences.PreferredUIFieldType.TEXT, uiAlignment = IdObjectFieldPreferences.PreferredAlignment.LEFT)
    String getDescription();

    void setDescription(String description);

    @NotNull(message = PHOTO_TIMESTAMP_CANNOT_BE_NULL_ERROR)
    @IdObjectFieldPreferences(defautlLabel = "Taken", uiFieldType = IdObjectFieldPreferences.PreferredUIFieldType.DATE_TIME, uiAlignment = IdObjectFieldPreferences.PreferredAlignment.MIDDLE)
    LocalDateTime getTimestamp();

    void setTimestamp(LocalDateTime timestamp);

    @NotEmpty(message = PHOTO_MIME_TYPE_CANNOT_BE_BLANK_OR_NULL)
    @IdObjectFieldPreferences(displayable = false, editableBy = IdObjectFieldPreferences.EditableBy.NONE)
    String getMimeType();

    void setMimeType(final String mimeType);

    //  TODO - max size
    @NotEmpty(message = PHOTO_IMAGE_DATA_CANNOT_BE_BLANK_OR_NULL)
    @IdObjectFieldPreferences(displayable = false, defautlLabel = "Picture", uiFieldType = IdObjectFieldPreferences.PreferredUIFieldType.CUSTOM)
    byte[] getImageData();

    void setImageData(final byte[] imageBytes);

    @NotEmpty(message = PHOTO_THUMBNAIL_IMAGE_DATA_CANNOT_BE_BLANK_OR_NULL)
    @IdObjectFieldPreferences(displayable = true, defautlLabel = "Picture", editableBy = IdObjectFieldPreferences.EditableBy.NONE)
    byte[] getThumbnailImageData();

    void setThumbnailImageData(final byte[] thumbnailBytes);
}
