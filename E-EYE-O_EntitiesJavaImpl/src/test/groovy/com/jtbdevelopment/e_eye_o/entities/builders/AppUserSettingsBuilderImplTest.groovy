package com.jtbdevelopment.e_eye_o.entities.builders

import com.jtbdevelopment.e_eye_o.entities.AppUserSettings
import com.jtbdevelopment.e_eye_o.entities.AppUserSettingsImpl

/**
 * Date: 12/1/13
 * Time: 9:02 PM
 */
class AppUserSettingsBuilderImplTest extends AbstractAppUserSettingsBuilderTest {
    @Override
    def createEntity() {
        return new AppUserSettingsImpl()
    }

    @Override
    def createBuilder(final entity) {
        return new AppUserSettingsBuilderImpl((AppUserSettings) entity)
    }
}
