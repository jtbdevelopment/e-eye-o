package com.jtbdevelopment.e_eye_o.DAO.helpers

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.IdObject
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectFieldSettings
import com.jtbdevelopment.e_eye_o.entities.reflection.IdObjectReflectionHelper
import org.jmock.Expectations
import org.jmock.Mockery
import org.joda.time.DateTime
import org.testng.annotations.Test

/**
 * Date: 12/13/13
 * Time: 11:29 PM
 */
class IdObjectUpdateHelperGImplTest extends GroovyTestCase {
    private static final String ORIGINAL_VALUE = "A"
    private static final String UPDATED_VALUE = "B"
    private static class LocalClass implements IdObject {
        String id
        DateTime modificationTimestamp
        String summaryDescription

        String userLevel
        String adminLevel
        String controlledLevel
        String noneLevel
    }

    private LocalClass original = new LocalClass()
    private LocalClass update = new LocalClass()
    private Mockery context = new Mockery()
    private IdObjectReflectionHelper helper = context.mock(IdObjectReflectionHelper.class)
    private AppUser user = [isAdmin: { false }] as AppUser;
    private AppUser admin = [isAdmin: { true }] as AppUser;
    private IdObjectUpdateHelperGImpl updateHelperGImpl = new IdObjectUpdateHelperGImpl(reflectionHelper: helper)


    @Test
    public void testUserLevelAsUser() {
        testScenario(user, IdObjectFieldSettings.EditableBy.USER, "userLevel", UPDATED_VALUE)
    }

    @Test
    public void testUserLevelAsAdmin() {
        testScenario(admin, IdObjectFieldSettings.EditableBy.USER, "userLevel", UPDATED_VALUE)
    }

    @Test
    public void testAdminLevelAsUser() {
        testScenario(user, IdObjectFieldSettings.EditableBy.ADMIN, "adminLevel", ORIGINAL_VALUE)
    }

    @Test
    public void testAdminLevelAsAdmin() {
        testScenario(admin, IdObjectFieldSettings.EditableBy.ADMIN, "adminLevel", UPDATED_VALUE)
    }

    @Test
    public void testNoneLevelAsUser() {
        testScenario(user, IdObjectFieldSettings.EditableBy.NONE, "noneLevel", ORIGINAL_VALUE)
    }

    @Test
    public void testNoneLevelAsAdmin() {
        testScenario(admin, IdObjectFieldSettings.EditableBy.NONE, "noneLevel", ORIGINAL_VALUE)
    }

    @Test
    public void testControlledLevelAsUser() {
        testScenario(user, IdObjectFieldSettings.EditableBy.CONTROLLED, "controlledLevel", ORIGINAL_VALUE)
    }

    @Test
    public void testControlledLevelAsAdmin() {
        testScenario(admin, IdObjectFieldSettings.EditableBy.CONTROLLED, "controlledLevel", ORIGINAL_VALUE)
    }

    private void testScenario(final AppUser user, final IdObjectFieldSettings.EditableBy editableBy, final String field, final String expectedValue) {
        IdObjectFieldSettings setting = [editableBy: { editableBy }] as IdObjectFieldSettings
        original."$field" = ORIGINAL_VALUE
        update."$field" = UPDATED_VALUE
        context.checking(new Expectations() {
            {
                one(helper).getAllFieldPreferences(LocalClass.class)
                will(returnValue([(field): setting]))
            }
        })
        updateHelperGImpl.vetInvalidFieldUpdates(user, original, update)
        assert original."$field" == ORIGINAL_VALUE
        assert update."$field" == expectedValue
    }
}
