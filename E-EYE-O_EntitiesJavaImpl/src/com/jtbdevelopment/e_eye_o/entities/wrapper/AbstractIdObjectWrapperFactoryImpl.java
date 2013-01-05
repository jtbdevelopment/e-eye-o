package com.jtbdevelopment.e_eye_o.entities.wrapper;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidParameterException;
import java.util.*;

public abstract class AbstractIdObjectWrapperFactoryImpl implements IdObjectWrapperFactory {
    private final Logger logger = LoggerFactory.getLogger(AbstractIdObjectWrapperFactoryImpl.class);
    private final Map<Class<? extends IdObject>, Class<? extends IdObject>> entityToWrapperMap = new HashMap<>();
    private final Map<Class<? extends IdObject>, Class<? extends IdObject>> wrapperToEntityMap = new HashMap<>();
    private final Class<? extends IdObjectWrapper> baseClass;

    protected AbstractIdObjectWrapperFactoryImpl(final Class<? extends IdObjectWrapper> baseClass) {
        this.baseClass = baseClass;
    }

    protected boolean needsWrapping(final Object entity) {
        return !(baseClass.isAssignableFrom(entity.getClass()));
    }

    protected <T extends IdObject, W extends T> void addMapping(final Class<T> entity, final Class<W> wrapper) {
        if(!baseClass.isAssignableFrom(wrapper)) {
            throw new IllegalArgumentException("wrapper class of " + wrapper.getSimpleName() + " must be subclass of " + baseClass.getSimpleName());
        }
        final Class<W> idObjectInterfaceForWrapper = getIdObjectInterfaceForClass(wrapper);
        if (!entity.equals(idObjectInterfaceForWrapper)) {
            throw new IllegalArgumentException(
                    "entity and wrapper should implement same IdObject interface entity = "
                            + entity.getSimpleName()
                            + ", wrapper = "
                            + idObjectInterfaceForWrapper.getSimpleName());
        }
        entityToWrapperMap.put(entity, wrapper);
        wrapperToEntityMap.put(wrapper, entity);
    }

    @Override
    public <T extends IdObject> T wrap(final T entity) {
        if (entity == null) {
            return entity;
        }

        if (!needsWrapping(entity)) {
            return entity;
        }

        return newWrapperFor(getEntityToWrap(entity));
    }

    @Override
    public <T extends IdObject, C extends Collection<T>> C wrap(final C entities) {
        if(entities == null) {
            return entities;
        }

        C newCollection = newCollectionFor(entities);
        //  TODO - might be more efficient to get constructor once but probably not an issue for now
        for (T entity : entities) {
            newCollection.add(wrap(entity));
        }
        return newCollection;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IdObject> Class<T> getWrapperForEntity(final Class<T> entityType) {
        return (Class<T>) entityToWrapperMap.get(entityType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IdObject> Class<T> getEntityForWrapper(final Class<T> wrapperType) {
        return (Class<T>) wrapperToEntityMap.get(wrapperType);
    }

    //  Can't just clone the collection - hibernate gives you it's own internal types for example
    @SuppressWarnings("unchecked")
    private <T extends IdObject, C extends Collection<T>> C newCollectionFor(final C entities) {
        if (entities instanceof List) {
            return (C) new ArrayList<T>(entities.size());
        }
        if (entities instanceof Set) {
            return (C) new HashSet<T>(entities.size());
        }
        logger.warn("Don't know how to construct collection of " + entities.getClass().getSimpleName());
        throw new RuntimeException("Couldn't instantiate new collection");
    }

    @SuppressWarnings("unchecked")
    private <T extends IdObject> T getEntityToWrap(final T entity) {
        if (entity instanceof IdObjectWrapper) {
            IdObjectWrapper asWrapper = (IdObjectWrapper) entity;
            final IdObject wrapped = asWrapper.getWrapped();
            if (wrapped == null) {
                throw new IllegalArgumentException("Cannot re-wrap a wrapper of " + entity.getClass().getSimpleName() + " with null for wrapped object");
            }
            Class wrappedInterface = getIdObjectInterfaceForClass(wrapped.getClass());
            Class wrapperInterface = getIdObjectInterfaceForClass(entity.getClass());
            if (wrappedInterface.equals(wrapperInterface)) {
                return (T) wrapped;
            } else {
                throw new IllegalArgumentException("Mismatched wrapper to wrapped.  Wrapped = " + wrapped.getClass().getSimpleName() + ", wrapper = " + asWrapper.getClass().getSimpleName());
            }
        } else {
            return entity;
        }
    }

    private <T extends IdObject> T newWrapperFor(final T entityToWrap) {
        final Constructor<T> constructor = getConstructor(entityToWrap);
        try {
            return constructor.newInstance(entityToWrap);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            logger.warn("Could not instantiate new wrapper for " + constructor.getClass().getSimpleName() + " with exception", e);
            throw new RuntimeException("Couldn't instantiate new wrapper");
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends IdObject> Constructor<T> getConstructor(final T entity) {
        Class<T> idObjectInterface = (Class<T>) getIdObjectInterfaceForClass(entity.getClass());
        Class<T> impl = getWrapperForInterface(idObjectInterface);
        try {
            return impl.getConstructor(idObjectInterface);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Not able to find constructor accepting " + idObjectInterface.getSimpleName(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends IdObject> Class<T> getWrapperForInterface(final Class<T> idObjectType) {
        if (entityToWrapperMap.containsKey(idObjectType)) {
            return (Class<T>) entityToWrapperMap.get(idObjectType);
        }
        throw new InvalidParameterException("Not able to find wrapper mapping for " + idObjectType.getSimpleName());
    }

    @SuppressWarnings("unchecked")
    private <T extends IdObject> Class<T> getIdObjectInterfaceForClass(final Class<T> entityType) {
        Class<T> found = null;
        for (Class i : entityType.getInterfaces()) {
            if (IdObject.class.isAssignableFrom(i)) {
                found = (Class<T>) i;
                break;
            }
        }
        return found;
    }

}