package com.jtbdevelopment.e_eye_o.events

import com.google.common.eventbus.EventBus
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject
import com.jtbdevelopment.e_eye_o.entities.IdObject
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Date: 1/11/14
 * Time: 9:09 PM
 */
@Component
@SuppressWarnings("unused")
@CompileStatic
class IdObjectChangedPublisherGImpl implements IdObjectChangedPublisher {
    @Autowired
    protected EventBus eventBus;

    @Autowired
    protected EventFactory eventFactory;

    public <T extends IdObject> void publishCreate(T entity) {
        if (eventBus) {
            if (entity instanceof AppUserOwnedObject) {
                eventBus.post(eventFactory.newAppUserOwnedObjectChanged(IdObjectChanged.ChangeType.CREATED, (AppUserOwnedObject) entity));
            } else {
                eventBus.post(eventFactory.newIdObjectChanged(IdObjectChanged.ChangeType.CREATED, (IdObject) entity));
            }
        }
    }

    public <T extends IdObject> void publishUpdate(T entity) {
        if (eventBus) {
            if (entity instanceof AppUserOwnedObject) {
                eventBus.post(eventFactory.newAppUserOwnedObjectChanged(IdObjectChanged.ChangeType.UPDATED, (AppUserOwnedObject) entity));
            } else {
                eventBus.post(eventFactory.newIdObjectChanged(IdObjectChanged.ChangeType.UPDATED, (IdObject) entity));
            }
        }
    }

    public <T extends IdObject> void publishDelete(final T entity) {
        if (eventBus) {
            if (entity instanceof AppUserOwnedObject) {
                eventBus.post(eventFactory.newAppUserOwnedObjectChanged(IdObjectChanged.ChangeType.DELETED, (AppUserOwnedObject) entity));
            } else {
                eventBus.post(eventFactory.newIdObjectChanged(IdObjectChanged.ChangeType.DELETED, (IdObject) entity));
            }
        }
    }
}
