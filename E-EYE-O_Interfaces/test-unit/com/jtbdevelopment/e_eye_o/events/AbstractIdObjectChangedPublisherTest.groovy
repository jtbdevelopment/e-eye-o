package com.jtbdevelopment.e_eye_o.events

import com.google.common.eventbus.EventBus
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject
import com.jtbdevelopment.e_eye_o.entities.IdObject
import groovy.mock.interceptor.MockFor
import org.jmock.Expectations
import org.jmock.Mockery
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * Date: 1/11/14
 * Time: 9:18 PM
 */
abstract class AbstractIdObjectChangedPublisherTest {
    abstract IdObjectChangedPublisher createPublisher(final EventBus eventBus);

    Mockery context
    IdObjectChangedPublisher publisher
    public EventFactory eventFactory
    EventBus eventBus
    IdObjectChanged lastPublished;
    public IdObjectChanged createIdObject;
    public IdObjectChanged updateIdObject;
    public IdObjectChanged deleteIdObject;
    public AppUserOwnedObjectChanged createAUOC;
    public AppUserOwnedObjectChanged updateAUOC;
    public AppUserOwnedObjectChanged deleteAOUC;

    @BeforeMethod
    public void setUp() {
        eventBus = [
                post: {
                    a ->
                        if (a in IdObjectChanged) {
                            lastPublished = a
                        } else {
                            throw new RuntimeException("Wrong type")
                        }
                }
        ] as EventBus
        context = new Mockery()
        eventFactory = context.mock(EventFactory)
        createIdObject = context.mock(IdObjectChanged, "IC")
        deleteIdObject = context.mock(IdObjectChanged, "DC")
        updateIdObject = context.mock(IdObjectChanged, "UC")
        createAUOC = context.mock(AppUserOwnedObjectChanged, "AA")
        deleteAOUC = context.mock(AppUserOwnedObjectChanged, "DA")
        updateAUOC = context.mock(AppUserOwnedObjectChanged, "UA")
        lastPublished = null

        publisher = createPublisher(eventBus)
    }

    @Test
    public void testCreateWhenBusIsNull() {
        publisher = createPublisher(null)

        publisher.publishCreate((IdObject) new MockFor(IdObject.class).proxyInstance())
    }

    @Test
    public void testUpdateWhenBusIsNull() {
        publisher = createPublisher(null)

        publisher.publishUpdate((IdObject) new MockFor(IdObject.class).proxyInstance())
    }

    @Test
    public void testDeleteWhenBusIsNull() {
        publisher = createPublisher(null)

        publisher.publishDelete((IdObject) new MockFor(IdObject.class).proxyInstance())
    }

    @Test
    public void testCreateIdObject() {
        IdObject idObject = (IdObject) new MockFor(IdObject.class).proxyInstance()
        context.checking(new Expectations() {
            {
                oneOf(eventFactory).newIdObjectChanged(IdObjectChanged.ChangeType.CREATED, idObject)
                will(returnValue(createIdObject))
            }
        })
        publisher.publishCreate(idObject)
        assert createIdObject.is(lastPublished)
    }

    @Test
    public void testUpdateIdObject() {
        IdObject idObject = (IdObject) new MockFor(IdObject.class).proxyInstance()
        context.checking(new Expectations() {
            {
                oneOf(eventFactory).newIdObjectChanged(IdObjectChanged.ChangeType.UPDATED, idObject)
                will(returnValue(updateIdObject))
            }
        })
        publisher.publishUpdate(idObject)
        assert updateIdObject.is(lastPublished)
    }

    @Test
    public void testDeleteIdObject() {
        IdObject idObject = (IdObject) new MockFor(IdObject.class).proxyInstance()
        context.checking(new Expectations() {
            {
                oneOf(eventFactory).newIdObjectChanged(IdObjectChanged.ChangeType.DELETED, idObject)
                will(returnValue(deleteIdObject))
            }
        })
        publisher.publishDelete(idObject)
        assert deleteIdObject.is(lastPublished)
    }

    @Test
    public void testCreateOwnedObject() {
        AppUserOwnedObject owned = (AppUserOwnedObject) new MockFor(AppUserOwnedObject.class).proxyInstance()
        context.checking(new Expectations() {
            {
                oneOf(eventFactory).newAppUserOwnedObjectChanged(IdObjectChanged.ChangeType.CREATED, owned)
                will(returnValue(createAUOC))
            }
        })
        publisher.publishCreate(owned)
        assert createAUOC.is(lastPublished)
    }

    @Test
    public void testUpdateOwnedObject() {
        AppUserOwnedObject owned = (AppUserOwnedObject) new MockFor(AppUserOwnedObject.class).proxyInstance()
        context.checking(new Expectations() {
            {
                oneOf(eventFactory).newAppUserOwnedObjectChanged(IdObjectChanged.ChangeType.UPDATED, owned)
                will(returnValue(updateAUOC))
            }
        })
        publisher.publishUpdate(owned)
        assert updateAUOC.is(lastPublished)
    }

    @Test
    public void testDeleteOwnedObject() {
        AppUserOwnedObject owned = (IdObject) new MockFor(AppUserOwnedObject.class).proxyInstance()
        context.checking(new Expectations() {
            {
                oneOf(eventFactory).newAppUserOwnedObjectChanged(IdObjectChanged.ChangeType.DELETED, owned)
                will(returnValue(deleteAOUC))
            }
        })
        publisher.publishDelete(owned)
        assert deleteAOUC.is(lastPublished)
    }
}
