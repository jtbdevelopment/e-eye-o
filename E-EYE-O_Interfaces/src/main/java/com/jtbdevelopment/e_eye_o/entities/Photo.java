package com.jtbdevelopment.e_eye_o.entities;

import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectEntitySettings;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectFieldSettings;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Date: 11/25/12
 * Time: 3:18 PM
 */
@IdObjectEntitySettings(defaultPageSize = 25, defaultSortField = "timestamp", defaultSortAscending = true, singular = "Photo", plural = "Photos",
        viewFieldOrder = {"description", "photoFo", "timestamp", "modificationTimestamp", "archived"},
        editFieldOrder = {"description", "photoFor", "timestamp", IdObjectEntitySettings.SECTION_BREAK, "imageData"})
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
    @IdObjectFieldSettings(label = "Photo For", fieldType = IdObjectFieldSettings.DisplayFieldType.SINGLE_SELECT_LIST, alignment = IdObjectFieldSettings.DisplayAlignment.LEFT)
    AppUserOwnedObject getPhotoFor();

    void setPhotoFor(final AppUserOwnedObject photoFor);

    @NotEmpty(message = PHOTO_DESCRIPTION_CANNOT_BE_BLANK_OR_NULL_ERROR)
    @Size(max = MAX_DESCRIPTION_SIZE, message = PHOTO_DESCRIPTION_SIZE_ERROR)
    @IdObjectFieldSettings(label = "Description", width = 30, fieldType = IdObjectFieldSettings.DisplayFieldType.TEXT, alignment = IdObjectFieldSettings.DisplayAlignment.LEFT)
    String getDescription();

    void setDescription(String description);

    @NotNull(message = PHOTO_TIMESTAMP_CANNOT_BE_NULL_ERROR)
    @IdObjectFieldSettings(label = "Taken", fieldType = IdObjectFieldSettings.DisplayFieldType.LOCAL_DATE_TIME, alignment = IdObjectFieldSettings.DisplayAlignment.CENTER)
    LocalDateTime getTimestamp();

    void setTimestamp(LocalDateTime timestamp);

    //  TODO - max size
    @NotEmpty(message = PHOTO_MIME_TYPE_CANNOT_BE_BLANK_OR_NULL)
    @IdObjectFieldSettings(editableBy = IdObjectFieldSettings.EditableBy.NONE)
    String getMimeType();

    void setMimeType(final String mimeType);

    //  TODO - max size
    @NotEmpty(message = PHOTO_IMAGE_DATA_CANNOT_BE_BLANK_OR_NULL)
    @IdObjectFieldSettings(label = "Picture", editableBy = IdObjectFieldSettings.EditableBy.NONE, fieldType = IdObjectFieldSettings.DisplayFieldType.CUSTOM)
    byte[] getImageData();

    void setImageData(final byte[] imageBytes);

    @NotEmpty(message = PHOTO_THUMBNAIL_IMAGE_DATA_CANNOT_BE_BLANK_OR_NULL)
    @IdObjectFieldSettings(label = "Picture", editableBy = IdObjectFieldSettings.EditableBy.NONE, fieldType = IdObjectFieldSettings.DisplayFieldType.CUSTOM)
    byte[] getThumbnailImageData();

    void setThumbnailImageData(final byte[] thumbnailBytes);
}
