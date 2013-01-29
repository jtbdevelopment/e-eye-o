package com.jtbdevelopment.e_eye_o.entities.helpers;

import com.jtbdevelopment.e_eye_o.entities.IdObject;

/**
 * Date: 1/28/13
 * Time: 10:36 PM
 *
 * Given a concrete implementation of an IdObject returns the interface it implements
 */
public interface IdObjectInterfaceResolver {
    <T extends IdObject> Class<T> getIdObjectInterfaceForClass(final Class<T> entityType);
}
