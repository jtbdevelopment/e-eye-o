package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.DAO.helpers.PhotoHelper
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject
import com.jtbdevelopment.e_eye_o.entities.impl.PhotoGImpl
import groovy.mock.interceptor.MockFor
import org.joda.time.LocalDateTime
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * Date: 12/2/13
 * Time: 6:52 AM
 */
class PhotoBuilderGImplTest extends AppUserOwnedObjectBuilderGImplTest {
    MockFor photoHelperMockContext;
    GroovyObject photoHelper
    static byte[] SOME_BYTES = [0x1, 0x2]

    @BeforeMethod
    def setUp() {
        entity = new PhotoGImpl()
        photoHelperMockContext = new MockFor(PhotoHelper)
        photoHelperMockContext.demand.setPhotoImages { a, b -> assert a.is(entity); assert b == SOME_BYTES }
        photoHelper = photoHelperMockContext.proxyInstance()
        builder = new PhotoBuilderGImpl(entity: entity, photoHelper: photoHelper)
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
    }

    @Test
    void testWithImageData() {
        assert (builder.is(builder.withImageData(SOME_BYTES)))

        photoHelperMockContext.verify photoHelper
    }
}
