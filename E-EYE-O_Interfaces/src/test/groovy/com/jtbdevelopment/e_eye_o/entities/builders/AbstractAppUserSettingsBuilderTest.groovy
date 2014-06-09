package com.jtbdevelopment.e_eye_o.entities.builders

import org.testng.annotations.Test

/**
 * Date: 12/1/13
 * Time: 9:02 PM
 */
abstract class AbstractAppUserSettingsBuilderTest extends AbstractAppUserOwnedObjectBuilderTest {
    @Test
    void testWithSetting() {
        builder.withSetting("Key", "Value")
        assert "Value" == entity.getSettingAsString("Key", "")
    }
}
