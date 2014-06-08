package com.jtbdevelopment.e_eye_o.entities.wrapper;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.reflection.IdObjectReflectionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidParameterException;
import java.util.*;

/**
 * In the end was unable to get this to compile successfully as a groovy class, weird casting issues and parameterized types exceptions
 * <p/>
 * groovy code at bottom
 */
@Service
public class IdObjectWrapperFactoryImpl implements IdObjectWrapperFactory {
    private final Table<WrapperKind, Class<? extends IdObject>, Class<? extends IdObject>> entityToWrapperTable = HashBasedTable.create();
    private final Table<WrapperKind, Class<? extends IdObject>, Class<? extends IdObject>> wrapperToEntityTable = HashBasedTable.create();
    private final Map<WrapperKind, Class<? extends IdObjectWrapper>> baseClassMap = new HashMap<>();
    private final IdObjectReflectionHelper idObjectReflectionHelper;

    @Autowired
    public IdObjectWrapperFactoryImpl(final IdObjectReflectionHelper idObjectReflectionHelper) {
        this.idObjectReflectionHelper = idObjectReflectionHelper;
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
        if (wrapperType.isInterface()) {
            throw new IllegalArgumentException("wrapperType should be implementation not " + entityType.getSimpleName());
        }
        Class<? extends IdObjectWrapper> baseClass = baseClassMap.get(wrapperKind);
        if (baseClass == null) {
            throw new IllegalStateException("base class for type " + wrapperKind + " is not set");
        }
        if (!baseClass.isAssignableFrom(wrapperType)) {
            throw new IllegalArgumentException("wrapperType class of " + wrapperType.getSimpleName() + " must subclass " + baseClass.getSimpleName());
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
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IdObject> Class<T> getWrapperForEntity(final WrapperKind wrapperKind, final Class<T> entityType) {
        return (Class<T>) entityToWrapperTable.get(wrapperKind, entityType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IdObject> Class<T> getEntityForWrapper(final WrapperKind wrapperKind, final Class<T> wrapperType) {
        return (Class<T>) wrapperToEntityTable.get(wrapperKind, wrapperType);
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
        if (entityToWrapperTable.contains(wrapperKind, idObjectType)) {
            return (Class<T>) entityToWrapperTable.get(wrapperKind, idObjectType);
        }
        throw new InvalidParameterException("Not able to find wrapperType mapping for " + idObjectType.getSimpleName());
    }
}


/*
@Component
@CompileStatic
class IdObjectWrapperFactoryGImpl implements IdObjectWrapperFactory {
    private final Map<IdObjectWrapperFactory.WrapperKind, Class<? extends IdObjectWrapper>> wrapperKindBaseClassMap = [:]
    private final Table<IdObjectWrapperFactory.WrapperKind, Class<? extends IdObject>, Class<? extends IdObject>> entityToWrapperTable = HashBasedTable.<IdObjectWrapperFactory.WrapperKind, Class<? extends IdObject>, Class<? extends IdObject>> create();
    private final Table<IdObjectWrapperFactory.WrapperKind, Class<? extends IdObject>, Class<? extends IdObject>> wrapperToEntityTable = HashBasedTable.create();

    private final IdObjectReflectionHelper idObjectReflectionHelper

    @Autowired
    public IdObjectWrapperFactoryGImpl(final IdObjectReflectionHelper idObjectReflectionHelper) {
        this.idObjectReflectionHelper = idObjectReflectionHelper
    }

    @Override
    def <T extends IdObjectWrapper> void addBaseClass(final IdObjectWrapperFactory.WrapperKind wrapperKind, final Class<T> baseClass) {
        wrapperKindBaseClassMap.put(wrapperKind, baseClass)
    }

    @Override
    def <T extends IdObject, W extends T> void addMapping(final IdObjectWrapperFactory.WrapperKind wrapperKind, final Class<T> entityType, final Class<W> wrapperType) {
        if (!entityType.isInterface()) {
            throw new IllegalArgumentException("entityType should be interface not " + entityType.getSimpleName());
        }
        if (wrapperType.isInterface()) {
            throw new IllegalArgumentException("wrapperType should be implementation not " + entityType.getSimpleName());
        }
        Class<? extends IdObjectWrapper> baseClass = wrapperKindBaseClassMap.get(wrapperKind);
        if (baseClass == null) {
            throw new IllegalStateException("base class for type " + wrapperKind + " is not set")
        }
        if (!baseClass.isAssignableFrom(wrapperType)) {
            throw new IllegalArgumentException("wrapperType class of " + wrapperType.getSimpleName() + " must subclass " + baseClass.getSimpleName());
        }
        def idObjectInterfaceForWrapper = idObjectReflectionHelper.getIdObjectInterfaceForClass((Class<? extends IdObject>) wrapperType);
        if (entityType != idObjectInterfaceForWrapper) {
            throw new IllegalArgumentException(
                    "entityType and wrapperType should implement same IdObject interface entityType = "
                            + entityType.getSimpleName()
                            + ", wrapperType = "
                            + idObjectInterfaceForWrapper.getSimpleName());
        }

        //  Finally
        entityToWrapperTable.put(wrapperKind, entityType, wrapperType);
        wrapperToEntityTable.put(wrapperKind, wrapperType, entityType);
    }

    @Override
    def <T extends IdObject> Class<T> getWrapperForEntity(final IdObjectWrapperFactory.WrapperKind wrapperKind, final Class<T> entityType) {
        return (Class<T>) entityToWrapperTable.get(wrapperKind, entityType)
    }

    @Override
    def <T extends IdObject> Class<T> getEntityForWrapper(final IdObjectWrapperFactory.WrapperKind wrapperKind, final Class<T> wrapperType) {
        return (Class<T>) wrapperToEntityTable.get(wrapperKind, wrapperType);
    }

    @Override
    def <W extends IdObject> W wrap(final IdObjectWrapperFactory.WrapperKind wrapperKind, final W entity) {
        if (entity == null) {
            return entity;
        }

        if (!needsWrapping(wrapperKind, entity)) {
            return entity;
        }

        return (W) newWrapperFor(wrapperKind, getEntityToWrap((IdObject) entity));
    }

    @Override
    def <W extends IdObject, C extends Collection<W>> C wrap(final IdObjectWrapperFactory.WrapperKind wrapperKind, final C entities) {
        if (entities == null) {
            return entities;
        }

        C newCollection = newCollectionFor(entities);
        //  TODO - more efficient to get constructor once but probably not an issue for now
        List<IdObject> list = ((Collection<IdObject>)entities).collect { wrap(wrapperKind, (IdObject) it) }
        //newCollection.addAll((List<W>)list)
        return newCollection;
    }

    //  Can't just clone the collection - hibernate gives you it's own internal types for example
    @SuppressWarnings("unchecked")
    private static <T extends IdObject, C extends Collection<T>> C newCollectionFor(final C entities) {
        switch (entities) {
            case List:
            case { "Values" == entities.getClass().getSimpleName() }:
                return (C) ([]);
            case Set:
                return (C) ([] as Set)
        }
        throw new RuntimeException("Don't know how to construct collection of " + entities.getClass().getSimpleName());
    }

    @SuppressWarnings("unchecked")
    private static <T extends IdObject> T getEntityToWrap(final T entity) {
        if (entity instanceof IdObjectWrapper) {
            IdObjectWrapper asWrapper = (IdObjectWrapper) entity;
            final IdObject wrapped = asWrapper.getWrapped();
            if (wrapped == null) {
                throw new IllegalArgumentException("Cannot re-wrap a wrapperType of " + entity.getClass().getSimpleName() + " with null for wrapped object");
            }
            return (T) wrapped;
        } else {
            return entity;
        }
    }

    //  TODO - protected for unit testing - code or test smell
    protected boolean needsWrapping(final IdObjectWrapperFactory.WrapperKind wrapperKind, final Object entity) {
        return !(wrapperKindBaseClassMap.get(wrapperKind).isAssignableFrom(entity.getClass()));
    }

    private <T extends IdObject> T newWrapperFor(final IdObjectWrapperFactory.WrapperKind wrapperKind, final T entityToWrap) {
        Class<T> idObjectInterface = (Class<T>) idObjectReflectionHelper.getIdObjectInterfaceForClass(entityToWrap.class);
        Class<T> implWrapperClass = getWrapperForInterface(wrapperKind, idObjectInterface);
        try {
            final Constructor<T> declaredConstructor = implWrapperClass.getDeclaredConstructor(idObjectInterface);
            declaredConstructor.setAccessible(true);
            return declaredConstructor.newInstance(entityToWrap);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Could not instantiate new wrapperType for " + implWrapperClass.getClass().getSimpleName() + " with exception", e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends IdObject> Class<T> getWrapperForInterface(final IdObjectWrapperFactory.WrapperKind wrapperKind, final Class<T> idObjectType) {
        if (entityToWrapperTable.contains(wrapperKind, idObjectType)) {
            return (Class<T>) entityToWrapperTable.get(wrapperKind, idObjectType);
        }
        throw new InvalidParameterException("Not able to find wrapperType mapping for " + idObjectType.getSimpleName());
    }
}

  */