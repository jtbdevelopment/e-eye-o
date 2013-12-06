package com.jtbdevelopment.e_eye_o.entities.wrapper;

import com.jtbdevelopment.e_eye_o.entities.IdObject;

import java.util.Collection;

/**
 * Date: 11/30/12
 * Time: 9:34 PM
 */
public interface IdObjectWrapperFactory {
    enum WrapperKind {
        DAO
    }

    <T extends IdObjectWrapper> void addBaseClass(final WrapperKind wrapperKind, Class<T> baseClass);

    <T extends IdObject, W extends T> void addMapping(final WrapperKind wrapperKind, final Class<T> entityType, final Class<W> wrapperType);

    <W extends IdObject> W wrap(final WrapperKind wrapperKind, final W entity);

    <W extends IdObject, C extends Collection<W>> C wrap(final WrapperKind wrapperKind, final C entities);

    <T extends IdObject> Class<T> getWrapperForEntity(final WrapperKind wrapperKind, final Class<T> entityType);

    <T extends IdObject> Class<T> getEntityForWrapper(final WrapperKind wrapperKind, final Class<T> wrapperType);

}
