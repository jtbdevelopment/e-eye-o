package com.jtbdevelopment.e_eye_o.entities.impl

import org.joda.time.DateTime
import org.testng.annotations.Test

/**
 * Date: 12/1/13
 * Time: 8:18 AM
 */
abstract class AbstractAppUserSettingsTest extends AbstractAppUserOwnedObjectTest {
    static final Map<String, Object> SAMPLE_VALUES = ["string": "astring", "object": new Object(), "int": 1, "boolean": true]

    @Test
    public void testSettings() throws Exception {
        assert objectUnderTest.getSettings().isEmpty();
        Map<String, String> valuesAsStrings = [:]
        SAMPLE_VALUES.each { key, value -> valuesAsStrings += [(key): value.toString()]; objectUnderTest.setSetting(key, value) }
        assert valuesAsStrings == objectUnderTest.getSettings();
        assert !objectUnderTest.settings.is(objectUnderTest.settings)
    }

    @Test
    public void testSetSettings() throws Exception {
        assert objectUnderTest.getSettings().isEmpty();
        objectUnderTest.setSetting("initialValue", "lose me");  // will be lost
        objectUnderTest.setSetting("boolean", false); //  will be lost
        assert 2 == objectUnderTest.getSettings().size()
        def valuesAsStrings = SAMPLE_VALUES.collectEntries { key, value -> [(key): value.toString()]; }
        objectUnderTest.setSettings(SAMPLE_VALUES)
        assert valuesAsStrings == objectUnderTest.getSettings();
    }

    @Test
    public void testUpdateSettings() throws Exception {
        assert objectUnderTest.getSettings().isEmpty();
        objectUnderTest.setSetting("initialValue", "don't lose me");
        objectUnderTest.setSetting("boolean", false); //  will be lost
        Map<String, String> valuesAsStrings = [:]
        SAMPLE_VALUES.each { key, value -> valuesAsStrings += [(key): value.toString()]; objectUnderTest.setSetting(key, value) }
        valuesAsStrings += ["initialValue": "don't lose me"];

        objectUnderTest.updateSettings(SAMPLE_VALUES);
        assert valuesAsStrings == objectUnderTest.getSettings();
    }

    @Test
    public void testGetSettingAsString() throws Exception {
        assert objectUnderTest.getSettings().isEmpty();
        objectUnderTest.setSetting("string", "string2");
        assert "string2" == objectUnderTest.getSettingAsString("string", "");
        assert "default" == objectUnderTest.getSettingAsString("default", "default");
    }

    @Test
    public void testGetSettingAsInt() throws Exception {
        assert objectUnderTest.getSettings().isEmpty();
        objectUnderTest.setSetting("int", 11);
        assert "11" == objectUnderTest.getSettingAsString("int", "");
        assert 11 == objectUnderTest.getSettingAsInt("int", 12);
        assert 12 == objectUnderTest.getSettingAsInt("default", 12);
    }

    @Test
    public void testGetSettingAsLong() throws Exception {
        assert objectUnderTest.getSettings().isEmpty();
        DateTime now = DateTime.now()
        objectUnderTest.setSetting("long", now.millis);
        assert now.millis.toString() == objectUnderTest.getSettingAsString("long", "");
        assert now.millis == objectUnderTest.getSettingAsLong("long", Long.MAX_VALUE);
        assert Long.MAX_VALUE == objectUnderTest.getSettingAsLong("default", Long.MAX_VALUE);
    }

    @Test
    public void testGetSettingAsBoolean() throws Exception {
        assert objectUnderTest.getSettings().isEmpty();
        objectUnderTest.setSetting("boolean", false);
        assert "false" == objectUnderTest.getSettingAsString("boolean", "true");
        assert false == objectUnderTest.getSettingAsBoolean("boolean", true);
        assert true == objectUnderTest.getSettingAsBoolean("default", true);
    }
}
