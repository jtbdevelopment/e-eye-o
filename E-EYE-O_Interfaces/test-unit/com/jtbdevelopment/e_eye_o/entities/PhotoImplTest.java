package com.jtbdevelopment.e_eye_o.entities;

import org.joda.time.LocalDateTime;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Date: 12/8/12
 * Time: 2:58 PM
 */
public class PhotoImplTest extends AbstractAppUserOwnedObjectTest<PhotoImpl> {
    public PhotoImplTest() {
        super(PhotoImpl.class);
    }

    @Test
    public void testConstructorsForNewObjects() {
        checkDefaultAndAppUserConstructorTests();
    }

    @Test
    public void testNewPhotoDefaultTimestamp() throws Exception {
        LocalDateTime before = new LocalDateTime();
        Thread.sleep(1);
        PhotoImpl photo = new PhotoImpl();
        Thread.sleep(1);
        LocalDateTime after = new LocalDateTime();
        assertTrue(before.compareTo(photo.getTimestamp()) < 0);
        assertTrue(after.compareTo(photo.getTimestamp()) > 0);
    }

    @Test
    public void testValidationOnNullTimestamp() {
        Photo photo = new PhotoImpl().setTimestamp(null);
        validateExpectingError(photo, Photo.PHOTO_TIMESTAMP_CANNOT_BE_NULL_ERROR);
    }

    @Test
    public void testSetGetTimestamp() {
        LocalDateTime now = new LocalDateTime();
        final Photo photo = new PhotoImpl().setTimestamp(now);
        assertEquals(now, photo.getTimestamp());
        validateNotExpectingError(photo, Photo.PHOTO_TIMESTAMP_CANNOT_BE_NULL_ERROR);
    }

    @Test
    public void testSetGetDescription() {
        checkStringSetGetsAndValidateNullsAsError("description", Photo.PHOTO_DESCRIPTION_CANNOT_BE_NULL_ERROR);
    }

    @Test
    public void testDescriptionSize() {
        checkStringSizeValidation("description", TOO_LONG_FOR_DESCRIPTION, Photo.PHOTO_DESCRIPTION_SIZE_ERROR);
    }

}
