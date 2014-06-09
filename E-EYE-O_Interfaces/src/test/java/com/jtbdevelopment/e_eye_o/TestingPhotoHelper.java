package com.jtbdevelopment.e_eye_o;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class TestingPhotoHelper {
    public static final String PNG = "image/png";
    public static byte[] simpleImageBytes;

    static {
        try {
            InputStream resourceAsStream = TestingPhotoHelper.class.getResourceAsStream("/simple.png");
            BufferedImage image = ImageIO.read(resourceAsStream);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image, "png", os);
            os.close();
            simpleImageBytes = os.toByteArray();
        } catch (IOException e) {
            simpleImageBytes = null;
        }
    }
}