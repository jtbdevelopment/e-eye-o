package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.IdObject
import com.jtbdevelopment.e_eye_o.entities.Observation
import com.jtbdevelopment.e_eye_o.entities.Photo
import org.joda.time.LocalDateTime
import org.testng.annotations.Test

/**
 * Date: 11/30/13
 * Time: 12:32 PM
 */
abstract class AbstractPhotoTest extends AbstractAppUserOwnedObjectTest {

    abstract Observation createObservation()

    @Test
    public void testPhotoFor() {
        testNonStringFieldWithNullValidationError("photoFor", null, createObservation(), Photo.PHOTO_PHOTO_FOR_CANNOT_BE_NULL_ERROR)
    }

    @Test
    public void testTimestamp() {
        LocalDateTime before = new LocalDateTime();
        Photo photo = createObjectUnderTest()
        LocalDateTime after = new LocalDateTime();
        assert before.compareTo(photo.timestamp) <= 0
        assert after.compareTo(photo.timestamp) >= 0

        testNonStringFieldWithNullValidationError("timestamp", objectUnderTest.timestamp, new LocalDateTime(), Photo.PHOTO_TIMESTAMP_CANNOT_BE_NULL_ERROR)
    }

    @Test
    public void testMimeType() {
        testStringFieldExpectingErrorForNullOrBlank("mimeType", "", Photo.PHOTO_MIME_TYPE_CANNOT_BE_BLANK_OR_NULL)
        //  TODO - mimeType
    }


    @Test
    public void testDescription() {
        testStringFieldExpectingErrorForNullOrBlank("description", "", Photo.PHOTO_DESCRIPTION_CANNOT_BE_BLANK_OR_NULL_ERROR)
        testStringFieldSize("description", IdObject.MAX_DESCRIPTION_SIZE, Photo.PHOTO_DESCRIPTION_SIZE_ERROR)
    }

    @Test
    public void testImageData() {
        String field = "imageData"
        testImageField(field, Photo.PHOTO_IMAGE_DATA_CANNOT_BE_BLANK_OR_NULL)
    }

    @Test
    public void testThumbnailImageData() {
        String field = "thumbnailImageData"
        testImageField(field, Photo.PHOTO_THUMBNAIL_IMAGE_DATA_CANNOT_BE_BLANK_OR_NULL)
    }

    private void testImageField(final String field, final String error) {
        byte[] bytes = [0x1, 0x2, 0x3]
        testNonStringFieldWithNullValidationError(field, null, bytes, error)

        assert !bytes.is(objectUnderTest."$field")
        assert !objectUnderTest."$field".is(objectUnderTest."$field")
    }

    @Test
    public void testSummaryDescription() {
        LocalDateTime now = LocalDateTime.now();
        final String desc = "Some description";
        objectUnderTest.description = " " + desc + " ";
        objectUnderTest.timestamp = now;
        assert desc + " " + now.toString("MMM dd") == objectUnderTest.summaryDescription
    }
}
