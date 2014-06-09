package com.jtbdevelopment.e_eye_o.entities.builders

import com.jtbdevelopment.e_eye_o.DAO.helpers.PhotoHelper
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject
import groovy.mock.interceptor.MockFor
import org.joda.time.LocalDateTime
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * Date: 12/2/13
 * Time: 6:52 AM
 */
abstract class AbstractPhotoBuilderTest extends AbstractAppUserOwnedObjectBuilderTest {
    PhotoHelper photoHelper
    static byte[] SOME_BYTES = [0x1, 0x2]
    boolean photoHelperCalled

    @BeforeMethod
    @Override
    def setUp() {
        photoHelperCalled = false
        photoHelper = [
                setPhotoImages      : { a, b -> assert a.is(entity); assert b == SOME_BYTES; photoHelperCalled = true },
                reprocessForMimeType: { a -> assert a.is(entity); photoHelperCalled = true }
        ] as PhotoHelper
        super.setUp()
    }

    @Test
    void testWithPhotoFor() {
        testField("photoFor", new MockFor(AppUserOwnedObject).proxyInstance())
    }

    @Test
    void testWithDescription() {
        testStringField("description")
    }

    @Test
    void testWithTimestamp() {
        testField("timestamp", LocalDateTime.now())
    }

    @Test
    void testWithMimeType() {
        testStringField("mimeType")
        assert photoHelperCalled
    }

    @Test
    void testWithImageData() {
        assert (builder.is(builder.withImageData(SOME_BYTES)))
        assert photoHelperCalled
    }
}
