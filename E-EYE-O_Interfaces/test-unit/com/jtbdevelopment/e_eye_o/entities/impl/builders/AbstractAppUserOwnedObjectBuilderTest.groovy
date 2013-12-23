package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.AppUser
import groovy.mock.interceptor.MockFor
import org.testng.annotations.Test

/**
 * Date: 12/1/13
 * Time: 7:43 PM
 */
abstract class AbstractAppUserOwnedObjectBuilderTest extends AbstractIdObjectBuilderTest {
    @Test
    void testWithAppUser() {
        testField("appUser", new MockFor(AppUser.class).proxyInstance())
    }

    @Test
    void testWithArchiveFlag() {
        testField("archived", true)
    }
}
