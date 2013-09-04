package com.jtbdevelopment.e_eye_o.ria.vaadin.utils;

import com.jtbdevelopment.e_eye_o.entities.Photo;

/**
 * Date: 4/16/13
 * Time: 8:00 PM
 */
public class PhotoThumbnailResource extends PhotoResource {

    public PhotoThumbnailResource(final Photo photo) {
        super(photo);
    }

    @Override
    public String getFilename() {
        return "TN" + super.getFilename();
    }

    @Override
    protected byte[] getData() {
        return photo.getThumbnailImageData();
    }

}
