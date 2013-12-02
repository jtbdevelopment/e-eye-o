package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.impl.AppUserSettingsGImpl
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * Date: 12/1/13
 * Time: 9:02 PM
 */
class AppUserSettingsBuilderGImplTest extends AppUserOwnedObjectBuilderGImplTest {
    @BeforeMethod
    def setUp() {
        entity = new AppUserSettingsGImpl()
        builder = new AppUserSettingsBuilderGImpl(entity: entity)
    }

    @Test
    void testWithSetting() {
        builder.withSetting("Key", "Value")
        assert "Value" == entity.getSettingAsString("Key", "")
    }
}
