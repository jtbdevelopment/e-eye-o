package com.jtbdevelopment.e_eye_o.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.Photo;
import org.joda.time.LocalDateTime;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Date: 12/8/12
 * Time: 2:58 PM
 */
public class PhotoImplTest extends AbstractAppUserOwnedObjectTest<PhotoImpl> {
    private static final byte SOME_BYTES[] = {0x1, 0x2, 0x3};
    private static final byte EMPTY_BYTE[] = {};

    public PhotoImplTest() {
        super(PhotoImpl.class);
    }

    @Test
    public void testConstructorsForNewObjects() {
        checkDefaultAndAppUserConstructorTests();
    }

    @Test
    public void testPhotoForDefaultAndValidation() {
        PhotoImpl photo = new PhotoImpl(USER1);
        assertNull(photo.getPhotoFor());
        validateExpectingError(photo, Photo.PHOTO_PHOTO_FOR_CANNOT_BE_NULL_ERROR);
    }

    @Test
    public void testPhotoForAssignment() {
        PhotoImpl photo = new PhotoImpl(USER1);
        photo.setPhotoFor(new StudentImpl(USER1));
        validateNotExpectingError(photo, Photo.PHOTO_PHOTO_FOR_CANNOT_BE_NULL_ERROR);
        photo.setPhotoFor(new ObservationImpl(USER1));
        validateNotExpectingError(photo, Photo.PHOTO_PHOTO_FOR_CANNOT_BE_NULL_ERROR);
        photo.setPhotoFor(new ClassListImpl(USER1));
        validateNotExpectingError(photo, Photo.PHOTO_PHOTO_FOR_CANNOT_BE_NULL_ERROR);
    }

    @Test
    public void testNewPhotoDefaultTimestamp() throws Exception {
        LocalDateTime before = new LocalDateTime();
        Thread.sleep(1);
        PhotoImpl photo = new PhotoImpl(USER1);
        Thread.sleep(1);
        LocalDateTime after = new LocalDateTime();
        assertTrue(before.compareTo(photo.getTimestamp()) < 0);
        assertTrue(after.compareTo(photo.getTimestamp()) > 0);
    }

    @Test
    public void testValidationOnNullTimestamp() {
        Photo photo = new PhotoImpl(USER1);
        photo.setTimestamp(null);
        validateExpectingError(photo, Photo.PHOTO_TIMESTAMP_CANNOT_BE_NULL_ERROR);
    }

    @Test
    public void testSetGetTimestamp() {
        LocalDateTime now = new LocalDateTime();
        final Photo photo = new PhotoImpl(USER1);
        photo.setTimestamp(now);
        assertEquals(now, photo.getTimestamp());
        validateNotExpectingError(photo, Photo.PHOTO_TIMESTAMP_CANNOT_BE_NULL_ERROR);
    }

    @Test
    public void testSetGetDescription() {
        checkStringSetGetsAndValidateNullsAndBlanksAsError("description", Photo.PHOTO_DESCRIPTION_CANNOT_BE_BLANK_OR_NULL_ERROR);
    }

    @Test
    public void testDescriptionSize() {
        checkStringSizeValidation("description", TOO_LONG_FOR_DESCRIPTION, Photo.PHOTO_DESCRIPTION_SIZE_ERROR);
    }

    @Test
    public void testNullImageDate() {
        validateExpectingError(new PhotoImpl(USER1), Photo.PHOTO_IMAGE_DATA_CANNOT_BE_BLANK_OR_NULL);
    }

    @Test
    public void testEmptyImageDate() {
        final PhotoImpl photo = new PhotoImpl(USER1);
        photo.setImageData(EMPTY_BYTE);
        validateExpectingError(photo, Photo.PHOTO_IMAGE_DATA_CANNOT_BE_BLANK_OR_NULL);
    }

    @Test
    public void testWithImageDate() {
        final PhotoImpl photo = new PhotoImpl(USER1);
        photo.setImageData(SOME_BYTES);
        assertNotSame(SOME_BYTES, photo.getImageData());
        assertEquals(SOME_BYTES, photo.getImageData());
        validateNotExpectingError(photo, Photo.PHOTO_IMAGE_DATA_CANNOT_BE_BLANK_OR_NULL);
    }

    @Test
    public void testNullThumbnailImageDate() {
        validateExpectingError(new PhotoImpl(USER1), Photo.PHOTO_THUMBNAIL_IMAGE_DATA_CANNOT_BE_BLANK_OR_NULL);
    }

    @Test
    public void testEmptyThumbnailImageDate() {
        final PhotoImpl photo = new PhotoImpl(USER1);
        photo.setThumbnailImageData(EMPTY_BYTE);
        validateExpectingError(photo, Photo.PHOTO_THUMBNAIL_IMAGE_DATA_CANNOT_BE_BLANK_OR_NULL);
    }

    @Test
    public void testWithThumbnailImageDate() {
        final PhotoImpl photo = new PhotoImpl(USER1);
        photo.setThumbnailImageData(SOME_BYTES);
        assertNotSame(SOME_BYTES, photo.getThumbnailImageData());
        assertEquals(SOME_BYTES, photo.getThumbnailImageData());
        validateNotExpectingError(photo, Photo.PHOTO_THUMBNAIL_IMAGE_DATA_CANNOT_BE_BLANK_OR_NULL);
    }

    @Test
    public void testSummaryDescription() {
        final PhotoImpl photo = new PhotoImpl(USER1);
        LocalDateTime now = LocalDateTime.now();
        final String desc = "Some description";
        photo.setDescription(" " + desc + " ");
        photo.setTimestamp(now);
        assertEquals(desc + " " + now.toString("MMM dd"), photo.getSummaryDescription());
    }
}
