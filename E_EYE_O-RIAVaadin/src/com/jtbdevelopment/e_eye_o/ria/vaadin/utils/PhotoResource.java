package com.jtbdevelopment.e_eye_o.ria.vaadin.utils;

import com.jtbdevelopment.e_eye_o.entities.Photo;
import com.vaadin.server.ConnectorResource;
import com.vaadin.server.DownloadStream;

import java.io.ByteArrayInputStream;

/**
 * Date: 4/16/13
 * Time: 8:10 PM
 */
public abstract class PhotoResource implements ConnectorResource {
    protected final Photo photo;

    public PhotoResource(final Photo photo) {
        this.photo = photo;
    }

    @Override
    public DownloadStream getStream() {
        return new DownloadStream(new ByteArrayInputStream(getData()), getMIMEType(), getFilename());
    }

    abstract protected byte[] getData();

    @Override
    public String getFilename() {
        //  TODO - should we add filename to photo?
        return photo.getDescription();
    }

    @Override
    public String getMIMEType() {
        return photo.getMimeType();
    }

    public Photo getPhoto() {
        return photo;
    }
}
