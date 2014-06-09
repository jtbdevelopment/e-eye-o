package com.jtbdevelopment.e_eye_o.events;

import com.google.common.eventbus.EventBus;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Date: 1/11/14
 * Time: 9:01 PM
 */
@Component
@SuppressWarnings("unused")
public class IdObjectChangedPublisherImpl implements IdObjectChangedPublisher {
    @Autowired
    protected EventBus eventBus;

    @Autowired
    protected EventFactory eventFactory;

    public <T extends IdObject> void publishCreate(T wrapped) {
        if (eventBus != null) {
            if (wrapped instanceof AppUserOwnedObject) {
                eventBus.post(eventFactory.newAppUserOwnedObjectChanged(IdObjectChanged.ChangeType.CREATED, (AppUserOwnedObject) wrapped));
            } else {
                eventBus.post(eventFactory.newIdObjectChanged(IdObjectChanged.ChangeType.CREATED, wrapped));
            }
        }
    }

    public <T extends IdObject> void publishUpdate(T wrapped) {
        if (eventBus != null) {
            if (wrapped instanceof AppUserOwnedObject) {
                eventBus.post(eventFactory.newAppUserOwnedObjectChanged(IdObjectChanged.ChangeType.UPDATED, (AppUserOwnedObject) wrapped));
            } else {
                eventBus.post(eventFactory.newIdObjectChanged(IdObjectChanged.ChangeType.UPDATED, wrapped));
            }
        }
    }

    public <T extends IdObject> void publishDelete(final T wrapped) {
        if (eventBus != null) {
            if (wrapped instanceof AppUserOwnedObject) {
                eventBus.post(eventFactory.newAppUserOwnedObjectChanged(IdObjectChanged.ChangeType.DELETED, (AppUserOwnedObject) wrapped));
            } else {
                eventBus.post(eventFactory.newIdObjectChanged(IdObjectChanged.ChangeType.DELETED, wrapped));
            }
        }
    }
}
