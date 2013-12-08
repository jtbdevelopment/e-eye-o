package com.jtbdevelopment.e_eye_o.entities.wrapper;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.reflection.IdObjectReflectionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidParameterException;
import java.util.*;

@Service
public class IdObjectWrapperFactoryImpl implements IdObjectWrapperFactory {
    private final Table<WrapperKind, Class<? extends IdObject>, Class<? extends IdObject>> entityToWrapperTable = HashBasedTable.create();
    private final Table<WrapperKind, Class<? extends IdObject>, Class<? extends IdObject>> wrapperToEntityTable = HashBasedTable.create();
    //    private final Map<WrapperKind, Map<Class<? extends IdObject>, Class<? extends IdObject>>> entityToWrapperMaps = new HashMap<>();
//    private final Map<WrapperKind, Map<Class<? extends IdObject>, Class<? extends IdObject>>> wrapperToEntityMaps = new HashMap<>();
    private final Map<WrapperKind, Class<? extends IdObjectWrapper>> baseClassMap = new HashMap<>();
    private final IdObjectReflectionHelper idObjectReflectionHelper;

    @Autowired
    public IdObjectWrapperFactoryImpl(final IdObjectReflectionHelper idObjectReflectionHelper) {
        this.idObjectReflectionHelper = idObjectReflectionHelper;
        for (WrapperKind wrapperKind : WrapperKind.values()) {
//            entityToWrapperMaps.put(wrapperKind, new HashMap<Class<? extends IdObject>, Class<? extends IdObject>>());
//            wrapperToEntityMaps.put(wrapperKind, new HashMap<Class<? extends IdObject>, Class<? extends IdObject>>());
        }
    }

    @Override
    public <T extends IdObjectWrapper> void addBaseClass(final WrapperKind wrapperKind, Class<T> baseClass) {
        baseClassMap.put(wrapperKind, baseClass);
    }

    @Override
    public <T extends IdObject, W extends T> void addMapping(final WrapperKind wrapperKind, final Class<T> entityType, final Class<W> wrapperType) {
        if (!entityType.isInterface()) {
            throw new IllegalArgumentException("entityType should be interface not " + entityType.getSimpleName());
        }
        Class<? extends IdObjectWrapper> baseClass = baseClassMap.get(wrapperKind);
        if (!baseClass.isAssignableFrom(wrapperType)) {
            throw new IllegalArgumentException("wrapperType class of " + wrapperType.getSimpleName() + " must be implement " + baseClass.getSimpleName());
        }
        final Class<W> idObjectInterfaceForWrapper = idObjectReflectionHelper.getIdObjectInterfaceForClass(wrapperType);
        if (!entityType.equals(idObjectInterfaceForWrapper)) {
            throw new IllegalArgumentException(
                    "entityType and wrapperType should implement same IdObject interface entityType = "
                            + entityType.getSimpleName()
                            + ", wrapperType = "
                            + idObjectInterfaceForWrapper.getSimpleName());
        }
        entityToWrapperTable.put(wrapperKind, entityType, wrapperType);
        wrapperToEntityTable.put(wrapperKind, wrapperType, entityType);
//        entityToWrapperMaps.get(wrapperKind).put(entityType, wrapperType);
//        wrapperToEntityMaps.get(wrapperKind).put(wrapperType, entityType);
    }

    @Override
    public <T extends IdObject> T wrap(final WrapperKind wrapperKind, final T entity) {
        if (entity == null) {
            return entity;
        }

        if (!needsWrapping(wrapperKind, entity)) {
            return entity;
        }

        return newWrapperFor(wrapperKind, getEntityToWrap(entity));
    }

    @Override
    public <T extends IdObject, C extends Collection<T>> C wrap(final WrapperKind wrapperKind, final C entities) {
        if (entities == null) {
            return entities;
        }

        C newCollection = newCollectionFor(entities);
        //  TODO - more efficient to get constructor once but probably not an issue for now
        for (T entity : entities) {
            newCollection.add(wrap(wrapperKind, entity));
        }
        return newCollection;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IdObject> Class<T> getWrapperForEntity(final WrapperKind wrapperKind, final Class<T> entityType) {
        return (Class<T>) entityToWrapperTable.get(wrapperKind, entityType);
//        return (Class<T>) entityToWrapperMaps.get(wrapperKind).get(entityType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IdObject> Class<T> getEntityForWrapper(final WrapperKind wrapperKind, final Class<T> wrapperType) {
        return (Class<T>) wrapperToEntityTable.get(wrapperKind, wrapperType);
//        return (Class<T>) wrapperToEntityMaps.get(wrapperKind).get(wrapperType);
    }

    //  Can't just clone the collection - hibernate gives you it's own internal types for example
    @SuppressWarnings("unchecked")
    private <T extends IdObject, C extends Collection<T>> C newCollectionFor(final C entities) {
        if (entities instanceof List || entities.getClass().getSimpleName().equals("Values")) {
            return (C) new ArrayList<T>(entities.size());
        }
        if (entities instanceof Set) {
            return (C) new HashSet<T>(entities.size());
        }
        throw new RuntimeException("Don't know how to construct collection of " + entities.getClass().getSimpleName());
    }

    @SuppressWarnings("unchecked")
    private <T extends IdObject> T getEntityToWrap(final T entity) {
        if (entity instanceof IdObjectWrapper) {
            IdObjectWrapper asWrapper = (IdObjectWrapper) entity;
            final IdObject wrapped = asWrapper.getWrapped();
            if (wrapped == null) {
                throw new IllegalArgumentException("Cannot re-wrap a wrapperType of " + entity.getClass().getSimpleName() + " with null for wrapped object");
            }
            Class wrappedInterface = idObjectReflectionHelper.getIdObjectInterfaceForClass(wrapped.getClass());
            Class wrapperInterface = idObjectReflectionHelper.getIdObjectInterfaceForClass(entity.getClass());
            if (wrappedInterface.equals(wrapperInterface)) {
                return (T) wrapped;
            } else {
                throw new IllegalArgumentException("Mismatched wrapperType to wrapped.  Wrapped = " + wrapped.getClass().getSimpleName() + ", wrapperType = " + asWrapper.getClass().getSimpleName());
            }
        } else {
            return entity;
        }
    }

    //  TODO - protected for unit testing - code or test smell
    protected boolean needsWrapping(final WrapperKind wrapperKind, final Object entity) {
        return !(baseClassMap.get(wrapperKind).isAssignableFrom(entity.getClass()));
    }

    private <T extends IdObject> T newWrapperFor(final WrapperKind wrapperKind, final T entityToWrap) {
        final Constructor<T> constructor = getConstructor(wrapperKind, entityToWrap);
        try {
            return constructor.newInstance(entityToWrap);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Could not instantiate new wrapperType for " + constructor.getClass().getSimpleName() + " with exception", e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends IdObject> Constructor<T> getConstructor(final WrapperKind wrapperKind, final T entity) {
        Class<T> idObjectInterface = (Class<T>) idObjectReflectionHelper.getIdObjectInterfaceForClass(entity.getClass());
        Class<T> impl = getWrapperForInterface(wrapperKind, idObjectInterface);
        try {
            final Constructor<T> declaredConstructor = impl.getDeclaredConstructor(idObjectInterface);
            declaredConstructor.setAccessible(true);
            return declaredConstructor;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Not able to find constructor accepting " + idObjectInterface.getSimpleName(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends IdObject> Class<T> getWrapperForInterface(final WrapperKind wrapperKind, final Class<T> idObjectType) {
//        Map<Class<? extends IdObject>, Class<? extends IdObject>> wrapperMap = entityToWrapperTable.get(wrapperKind);
        if (entityToWrapperTable.contains(wrapperKind, idObjectType)) {
            return (Class<T>) entityToWrapperTable.get(wrapperKind, idObjectType);
        }
        throw new InvalidParameterException("Not able to find wrapperType mapping for " + idObjectType.getSimpleName());
    }


}