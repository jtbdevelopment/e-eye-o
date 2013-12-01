package com.jtbdevelopment.e_eye_o.entities.impl;

import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Date: 6/14/13
 * Time: 2:48 PM
 */
public class AppUserSettingsImplTest extends AbstractAppUserOwnedObjectTest<AppUserSettingsImpl> {

    public static final Map<String, Object> SAMPLE_VALUES = new HashMap<String, Object>() {{
        put("string", "astring");
        put("object", new Object());
        put("int", 1);
        put("boolean", true);
    }};

    public AppUserSettingsImplTest() {
        super(AppUserSettingsImpl.class);
    }

    @Test
    public void testConstructorsForNewObjects() {
        checkDefaultAndAppUserConstructorTests();
    }

    @Test
    public void testGetSetSettings() throws Exception {
        AppUserSettingsImpl impl = new AppUserSettingsImpl(null);
        assertTrue(impl.getSettings().isEmpty());
        Map<String, String> valuesAsStrings = new HashMap<>();
        for (Map.Entry<String, Object> entry : SAMPLE_VALUES.entrySet()) {
            valuesAsStrings.put(entry.getKey(), entry.getValue().toString());
            impl.setSetting(entry.getKey(), entry.getValue());
        }

        Map<String, String> getResult = impl.getSettings();
        assertEquals(valuesAsStrings.size(), getResult.size());
        for (Map.Entry<String, String> entry : getResult.entrySet()) {
            assertTrue(valuesAsStrings.containsKey(entry.getKey()));
            assertEquals(valuesAsStrings.get(entry.getKey()), entry.getValue());
        }
    }

    @Test
    public void testUpdateSettings() throws Exception {
        AppUserSettingsImpl impl = new AppUserSettingsImpl(null);
        assertTrue(impl.getSettings().isEmpty());
        impl.setSetting("initialValue", "don't lose me");
        impl.setSetting("boolean", false); //  will be lost
        Map<String, String> valuesAsStrings = new HashMap<>();
        for (Map.Entry<String, Object> entry : SAMPLE_VALUES.entrySet()) {
            valuesAsStrings.put(entry.getKey(), entry.getValue().toString());
        }
        valuesAsStrings.put("initialValue", "don't lose me");

        impl.updateSettings(SAMPLE_VALUES);
        Map<String, String> getResult = impl.getSettings();
        assertEquals(valuesAsStrings.size(), getResult.size());
        for (Map.Entry<String, String> entry : getResult.entrySet()) {
            assertTrue(valuesAsStrings.containsKey(entry.getKey()));
            assertEquals(valuesAsStrings.get(entry.getKey()), entry.getValue());
        }

    }

    @Test
    public void testGetSettingAsString() throws Exception {
        AppUserSettingsImpl impl = new AppUserSettingsImpl(null);
        assertTrue(impl.getSettings().isEmpty());
        impl.setSetting("string", "string2");
        assertEquals("string2", impl.getSettingAsString("string", ""));
        assertEquals("default", impl.getSettingAsString("default", "default"));
    }

    @Test
    public void testGetSettingAsInt() throws Exception {
        AppUserSettingsImpl impl = new AppUserSettingsImpl(null);
        assertTrue(impl.getSettings().isEmpty());
        impl.setSetting("int", 11);
        assertEquals("11", impl.getSettingAsString("int", ""));
        assertEquals(11, impl.getSettingAsInt("int", 12));
        assertEquals(12, impl.getSettingAsInt("default", 12));
    }

    @Test
    public void testGetSettingAsBoolean() throws Exception {
        AppUserSettingsImpl impl = new AppUserSettingsImpl(null);
        assertTrue(impl.getSettings().isEmpty());
        impl.setSetting("boolean", false);
        assertEquals("false", impl.getSettingAsString("boolean", ""));
        assertEquals(false, impl.getSettingAsBoolean("boolean", true));
        assertEquals(true, impl.getSettingAsBoolean("default", true));
    }
}
