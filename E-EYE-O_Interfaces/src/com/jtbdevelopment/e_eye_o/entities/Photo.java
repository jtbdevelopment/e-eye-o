package com.jtbdevelopment.e_eye_o.entities;

import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectDisplayPreferences;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectFieldPreferences;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Date: 11/25/12
 * Time: 3:18 PM
 */
@IdObjectDisplayPreferences(defaultPageSize = 10, defaultSortField = "timestamp", defaultSortAscending = true, singular = "Photo", plural = "Photos",
        editFieldOrder = {"description", "photoFor", "taken", IdObjectDisplayPreferences.SECTION_BREAK, "imageData"})
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
    @IdObjectFieldPreferences(label = "Photo For", fieldType = IdObjectFieldPreferences.DisplayFieldType.SINGLE_SELECT_LIST, alignment = IdObjectFieldPreferences.DisplayAlignment.LEFT)
    AppUserOwnedObject getPhotoFor();

    void setPhotoFor(final AppUserOwnedObject photoFor);

    @NotEmpty(message = PHOTO_DESCRIPTION_CANNOT_BE_BLANK_OR_NULL_ERROR)
    @Size(max = MAX_DESCRIPTION_SIZE, message = PHOTO_DESCRIPTION_SIZE_ERROR)
    @IdObjectFieldPreferences(label = "Description", width = 30, fieldType = IdObjectFieldPreferences.DisplayFieldType.TEXT, alignment = IdObjectFieldPreferences.DisplayAlignment.LEFT)
    String getDescription();

    void setDescription(String description);

    @NotNull(message = PHOTO_TIMESTAMP_CANNOT_BE_NULL_ERROR)
    @IdObjectFieldPreferences(label = "Taken", fieldType = IdObjectFieldPreferences.DisplayFieldType.LOCAL_DATE_TIME, alignment = IdObjectFieldPreferences.DisplayAlignment.CENTER)
    LocalDateTime getTimestamp();

    void setTimestamp(LocalDateTime timestamp);

    @NotEmpty(message = PHOTO_MIME_TYPE_CANNOT_BE_BLANK_OR_NULL)
    @IdObjectFieldPreferences(editableBy = IdObjectFieldPreferences.EditableBy.NONE)
    String getMimeType();

    void setMimeType(final String mimeType);

    //  TODO - max size
    @NotEmpty(message = PHOTO_IMAGE_DATA_CANNOT_BE_BLANK_OR_NULL)
    @IdObjectFieldPreferences(label = "Picture", editableBy = IdObjectFieldPreferences.EditableBy.NONE, fieldType = IdObjectFieldPreferences.DisplayFieldType.CUSTOM)
    byte[] getImageData();

    void setImageData(final byte[] imageBytes);

    @NotEmpty(message = PHOTO_THUMBNAIL_IMAGE_DATA_CANNOT_BE_BLANK_OR_NULL)
    @IdObjectFieldPreferences(label = "Picture", editableBy = IdObjectFieldPreferences.EditableBy.NONE, fieldType = IdObjectFieldPreferences.DisplayFieldType.CUSTOM)
    byte[] getThumbnailImageData();

    void setThumbnailImageData(final byte[] thumbnailBytes);
}
