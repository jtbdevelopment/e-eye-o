package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.impl.AppUserOwnedObjectGImpl
import groovy.mock.interceptor.MockFor
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * Date: 12/1/13
 * Time: 7:43 PM
 */
class AppUserOwnedObjectBuilderGImplTest extends IdObjectBuilderGImplTest {
    static class AppUserOwnedImpl extends AppUserOwnedObjectGImpl {
    }

    @BeforeMethod
    def setUp() {
        entity = new AppUserOwnedImpl()
        builder = new AppUserOwnedObjectBuilderGImpl<AppUserOwnedImpl>(entity: entity)
    }

    @Test
    void testWithAppUser() {
        testField("appUser", new MockFor(AppUser.class).proxyInstance())
    }

    @Test
    void testWithArchiveFlag() {
        testField("archived", true)
    }
}