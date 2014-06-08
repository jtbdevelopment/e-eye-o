package com.jtbdevelopment.e_eye_o.entities.builders

import com.jtbdevelopment.e_eye_o.entities.AppUserSettingsGImpl

/**
 * Date: 12/1/13
 * Time: 9:02 PM
 */
class AppUserSettingsBuilderGImplTest extends AbstractAppUserSettingsBuilderTest {
    @Override
    def createEntity() {
        return new AppUserSettingsGImpl()
    }

    @Override
    def createBuilder(final entity) {
        return new AppUserSettingsBuilderGImpl(entity: entity)
    }
}
