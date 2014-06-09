package com.jtbdevelopment.e_eye_o.events;

import com.jtbdevelopment.e_eye_o.entities.IdObject;

/**
 * Date: 1/11/14
 * Time: 8:59 PM
 */
public interface IdObjectChangedPublisher {
    public <T extends IdObject> void publishCreate(T wrapped);

    public <T extends IdObject> void publishUpdate(T wrapped);

    public <T extends IdObject> void publishDelete(final T wrapped);
}
