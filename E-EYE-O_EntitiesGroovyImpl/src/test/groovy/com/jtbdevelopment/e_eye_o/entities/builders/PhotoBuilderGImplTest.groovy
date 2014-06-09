package com.jtbdevelopment.e_eye_o.entities.builders

import com.jtbdevelopment.e_eye_o.entities.PhotoGImpl

/**
 * Date: 12/2/13
 * Time: 6:52 AM
 */
class PhotoBuilderGImplTest extends AbstractPhotoBuilderTest {
    @Override
    def createEntity() {
        return new PhotoGImpl()
    }

    @Override
    def createBuilder(final entity) {
        return new PhotoBuilderGImpl(entity: entity, photoHelper: photoHelper)
    }
}
