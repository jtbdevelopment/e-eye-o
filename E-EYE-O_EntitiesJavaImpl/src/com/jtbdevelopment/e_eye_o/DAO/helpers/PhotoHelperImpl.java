package com.jtbdevelopment.e_eye_o.DAO.helpers;

import com.jtbdevelopment.e_eye_o.entities.Photo;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Date: 4/14/13
 * Time: 8:26 PM
 */
//  TODO - inject
public class PhotoHelperImpl {
    public static byte[] createThumbnailImage(final Photo photo) {
        BufferedImage image = null, resized = null;
        try {
            image = ImageIO.read(new ByteArrayInputStream(photo.getImageData()));
            resized = Scalr.resize(image, Photo.THUMBNAIL_SIZE);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(resized, "jpg", os);
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
