package com.jtbdevelopment.e_eye_o.events

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.Photo
import groovy.mock.interceptor.MockFor
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * Date: 12/8/13
 * Time: 8:15 AM
 */
abstract class AbstractEventFactoryTest {
    EventFactory eventFactory

    abstract EventFactory createFactory();

    @BeforeMethod
    public void setUp() {
        eventFactory = createFactory()
    }

    @Test
    public void testNewIdObject() {
        MockFor context = new MockFor(AppUser.class)
        AppUser user = context.proxyInstance()
        IdObjectChanged.ChangeType changeType = IdObjectChanged.ChangeType.ADDED

        IdObjectChanged<AppUser> event = eventFactory.newIdObjectChanged(changeType, user)
        assert user == event.getEntity()
        assert changeType == event.changeType
    }

    @Test
    public void testNewAppUserObject() {
        MockFor context = new MockFor(Photo.class)
        Photo photo = context.proxyInstance()
        IdObjectChanged.ChangeType changeType = IdObjectChanged.ChangeType.DELETED

        AppUserOwnedObjectChanged<Photo> event = eventFactory.newAppUserOwnedObjectChanged(changeType, photo)
        assert photo == event.getEntity()
        assert changeType == event.changeType
    }
}
