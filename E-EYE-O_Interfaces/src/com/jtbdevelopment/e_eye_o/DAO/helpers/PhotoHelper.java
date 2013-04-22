package com.jtbdevelopment.e_eye_o.DAO.helpers;

import com.jtbdevelopment.e_eye_o.entities.Photo;

/**
 * Date: 4/14/13
 * Time: 8:25 PM
 */
public interface PhotoHelper {
    void setPhotoImages(final Photo photo, final byte[] imageData);

    boolean isMimeTypeSupported(final String mimeType);
}
