package com.jtbdevelopment.e_eye_o.entities.builders

import com.jtbdevelopment.e_eye_o.entities.AppUserGImpl

/**
 * Date: 12/1/13
 * Time: 8:58 PM
 */
class AppUserBuilderGImplTest extends AbstractAppUserBuilderTest {
    @Override
    def createEntity() {
        return new AppUserGImpl()
    }

    @Override
    def createBuilder(final entity) {
        return new AppUserBuilderGImpl(entity: entity)
    }
}
