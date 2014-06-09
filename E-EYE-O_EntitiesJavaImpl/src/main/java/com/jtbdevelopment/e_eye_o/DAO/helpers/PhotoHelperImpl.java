package com.jtbdevelopment.e_eye_o.DAO.helpers;

import com.jtbdevelopment.e_eye_o.entities.Photo;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * Date: 4/14/13
 * Time: 8:26 PM
 */
@Component
@SuppressWarnings("unused")
public class PhotoHelperImpl implements PhotoHelper {
    private final static Logger logger = LoggerFactory.getLogger(PhotoHelperImpl.class);

    public void setPhotoImages(final Photo photo, final byte[] imageData) {
        photo.setImageData(imageData);
        resizeWithMimeType(photo);
    }

    @Override
    public void reprocessForMimeType(final Photo photo) {
        resizeWithMimeType(photo);
    }

    private void resizeWithMimeType(final Photo photo) {
        if (StringUtils.hasLength(photo.getMimeType()) && photo.getImageData() != null && photo.getImageData().length > 0) {
            standardizePrimaryImage(photo);
            updateThumbnailImage(photo);
        }
    }

    @Override
    public boolean isMimeTypeSupported(final String mimeType) {
        if (!StringUtils.hasLength(mimeType)) {
            logger.error("mimeType is null or blank " + mimeType);
            return false;
        }

        if (!mimeType.startsWith("image/")) {
            logger.error("Only image mime types are supported.  Not " + mimeType);
            return false;
        }
        Iterator<ImageWriter> writers = getImageWritersByMIMEType(mimeType);
        if (!writers.hasNext()) {
            logger.warn("No image support for mimeType = " + mimeType);
            return false;
        }
        return true;
    }

    private void standardizePrimaryImage(final Photo photo) {
        photo.setImageData(resizePhoto(photo, Photo.STANDARD_SIZE));
    }

    private void updateThumbnailImage(final Photo photo) {
        photo.setThumbnailImageData(resizePhoto(photo, Photo.THUMBNAIL_SIZE));
    }

    private byte[] resizePhoto(final Photo photo, final int resizeTo) {
        BufferedImage image = null, resized = null;
        try {
            final String mimeType = photo.getMimeType();
            if (!isMimeTypeSupported(mimeType)) {
                throw new RuntimeException("Error with mimeType on photo" + mimeType);
            }

            image = ImageIO.read(new ByteArrayInputStream(photo.getImageData()));
            int maxDimension = Math.max(image.getHeight(), image.getWidth());
            int resize = Math.min(maxDimension, resizeTo);
            resized = Scalr.resize(image, resize);

            String type = getImageWritersByMIMEType(mimeType).next().getOriginatingProvider().getFormatNames()[0];
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(resized, type, os);
            os.close();
            image.flush();
            resized.flush();
            return os.toByteArray();
        } catch (IOException e) {
            if (image != null) {
                image.flush();
            }
            if (resized != null) {
                resized.flush();
            }
            throw new RuntimeException("Error creating thumbnail.", e);
        }
    }

    private Iterator<ImageWriter> getImageWritersByMIMEType(String mimeType) {
        return ImageIO.getImageWritersByMIMEType(mimeType);
    }
}
