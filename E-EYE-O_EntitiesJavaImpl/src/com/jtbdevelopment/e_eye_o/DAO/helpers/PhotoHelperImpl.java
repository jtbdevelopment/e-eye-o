package com.jtbdevelopment.e_eye_o.DAO.helpers;

import com.jtbdevelopment.e_eye_o.entities.Photo;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Date: 4/14/13
 * Time: 8:26 PM
 */
@Component
@SuppressWarnings("unused")
public class PhotoHelperImpl implements PhotoHelper {
    public void setPhotoImages(final Photo photo, final byte[] imageData) {
        photo.setImageData(imageData);
        updateThumbnailImage(photo);
        standardizePrimaryImage(photo);
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
            image = ImageIO.read(new ByteArrayInputStream(photo.getImageData()));
            resized = Scalr.resize(image, resizeTo);
            ByteArrayOutputStream os = new ByteArrayOutputStream();

            String type = ImageIO.getImageWritersByMIMEType(photo.getMimeType()).next().getOriginatingProvider().getFormatNames()[0];
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
