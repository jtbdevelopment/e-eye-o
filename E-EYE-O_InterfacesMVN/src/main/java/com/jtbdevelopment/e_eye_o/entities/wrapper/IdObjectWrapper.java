package com.jtbdevelopment.e_eye_o.entities.wrapper;

import com.jtbdevelopment.e_eye_o.entities.IdObject;

/**
 * Date: 11/30/12
 * Time: 8:49 PM
 */
public interface IdObjectWrapper<T extends IdObject> {
    public T getWrapped();
}
