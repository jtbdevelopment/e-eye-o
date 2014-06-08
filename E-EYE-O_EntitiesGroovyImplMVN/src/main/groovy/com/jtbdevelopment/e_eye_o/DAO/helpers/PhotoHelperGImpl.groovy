package com.jtbdevelopment.e_eye_o.DAO.helpers

import com.jtbdevelopment.e_eye_o.entities.Photo
import groovy.transform.CompileStatic
import org.imgscalr.Scalr
import org.springframework.stereotype.Component

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

/**
 * Date: 12/16/13
 * Time: 2:45 PM
 */
@CompileStatic
@Component
class PhotoHelperGImpl implements PhotoHelper {
    @Override
    void setPhotoImages(final Photo photo, final byte[] imageData) {
        photo.setImageData(imageData);
        resizeWithMimeType(photo);
    }

    @Override
    void reprocessForMimeType(final Photo photo) {
        resizeWithMimeType(photo);
    }

    @Override
    boolean isMimeTypeSupported(final String mimeType) {
        if (!mimeType) {
            return false
        }
        if (!mimeType.startsWith("image/")) {
            return false
        }
        return ImageIO.getImageWritersByMIMEType(mimeType).hasNext()
    }

    private void resizeWithMimeType(final Photo photo) {
        if (photo.getMimeType() && photo.getImageData()) {
            standardizePrimaryImage(photo);
            updateThumbnailImage(photo);
        }
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

            byte[] data = photo.getImageData()
            image = ImageIO.read(new ByteArrayInputStream(data));
            int maxDimension = Math.max(image.getHeight(), image.getWidth());
            int resize = Math.min(maxDimension, resizeTo);
            resized = Scalr.resize(image, resize);

            String type = ImageIO.getImageWritersByMIMEType(mimeType).next().getOriginatingProvider().getFormatNames()[0];
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

}
