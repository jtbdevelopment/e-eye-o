package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.impl.AppUserGImpl
import org.joda.time.DateTime
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * Date: 12/1/13
 * Time: 8:58 PM
 */
class AppUserBuilderGImplTest extends IdObjectBuilderGImplTest {
    @BeforeMethod
    def setUp() {
        entity = new AppUserGImpl()
        builder = new AppUserBuilderGImpl(entity: entity)
    }

    @Test
    void testWithFirstName() {
        testStringField("firstName")
    }

    @Test
    void testWithLastName() {
        testStringField("lastName")
    }

    @Test
    void testWithEmailAddress() {
        testStringField("emailAddress")
    }

    @Test
    void testWithPassword() {
        testStringField("password")
    }

    @Test
    void testWithLastLogout() {
        testField("lastLogout", DateTime.now())
    }

    @Test
    void testWithActivated() {
        testField("activated", true)
    }

    @Test
    void testWithActive() {
        testField("active", false)
    }

    @Test
    void testWithAdmin() {
        testField("admin", true)
    }
}
