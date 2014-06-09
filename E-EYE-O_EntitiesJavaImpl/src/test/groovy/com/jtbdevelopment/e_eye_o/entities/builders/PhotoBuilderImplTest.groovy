package com.jtbdevelopment.e_eye_o.entities.builders

import com.jtbdevelopment.e_eye_o.entities.Photo
import com.jtbdevelopment.e_eye_o.entities.PhotoImpl

/**
 * Date: 12/2/13
 * Time: 6:52 AM
 */
class PhotoBuilderImplTest extends AbstractPhotoBuilderTest {
    @Override
    def createEntity() {
        return new PhotoImpl()
    }

    @Override
    def createBuilder(final entity) {
        return new PhotoBuilderImpl(photoHelper, (Photo) entity)
    }
}
