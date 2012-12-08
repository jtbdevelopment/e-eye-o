package com.jtbdevelopment.e_eye_o.entities.wrapper;

import com.jtbdevelopment.e_eye_o.entities.IdObject;

import java.util.Collection;

/**
 * Date: 11/30/12
 * Time: 9:34 PM
 */
public interface IdObjectWrapperFactory {
    <W extends IdObject> W wrap(final W entity);

    <W extends IdObject, C extends Collection<W>> C wrap(C entities);
}
