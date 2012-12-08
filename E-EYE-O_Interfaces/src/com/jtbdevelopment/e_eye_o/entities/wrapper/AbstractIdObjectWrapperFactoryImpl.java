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
    private final Map<Class<? extends IdObject>, Class> entityWrapperMap = new HashMap<>();
    private final Class<? extends IdObjectWrapper> baseClass;

    protected AbstractIdObjectWrapperFactoryImpl(final Class<? extends IdObjectWrapper> baseClass) {
        this.baseClass = baseClass;
    }

    protected boolean needsWrapping(final Object entity) {
        return !(baseClass.isAssignableFrom(entity.getClass()));
    }

    protected void addMapping(final Class<? extends IdObject> entity, final Class wrapper) {
        entityWrapperMap.put(entity, wrapper);
    }

    @Override
    public <T extends IdObject> T wrap(final T entity) {
        if (entity == null) {
            return entity;
        }

        if (!needsWrapping(entity)) {
            return entity;
        }
        return constructNewWrapper(getEntityToWrap(entity));
    }

    @Override
    public <T extends IdObject, C extends Collection<T>> C wrap(final C entities) {
        C newCollection = constructNewCollection(entities);
        //  TODO - might be more efficient to get constructor once but probably not an issue for now
        for (T entity : entities) {
            newCollection.add(wrap(entity));
        }
        return newCollection;
    }

    //  Can't just clone the collection - hibernate gives you it's own
    private <T extends IdObject, C extends Collection<T>> C constructNewCollection(final C entities) {
        if (entities instanceof Map) {
            return (C) new HashMap();
        }
        if (entities instanceof List) {
            return (C) new ArrayList<T>(entities.size());
        }
        if (entities instanceof Set) {
            return (C) new HashSet<T>();
        }
        logger.warn("Don't know how to construct collection of " + entities.getClass().getSimpleName());
        throw new RuntimeException("Couldn't instantiate new collection");
    }

    private <T extends IdObject> T getEntityToWrap(final T entity) {
        return (entity instanceof IdObjectWrapper) ? ((IdObjectWrapper<T>) entity).getWrapped() : entity;
    }

    private <T extends IdObject> T constructNewWrapper(final T entityToWrap) {
        final Constructor<T> constructor = getConstructor(entityToWrap);
        try {
            return constructor.newInstance(entityToWrap);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            logger.warn("Could not instantiate new wrapper for " + constructor.getClass().getSimpleName() + " with exception", e);
            throw new RuntimeException("Couldn't instantiate new HDBImpl");
        }
    }

    private <T extends IdObject> Constructor<T> getConstructor(final T entity) {
        Class<? extends IdObjectWrapper> impl = getImplForClass(entity.getClass());
        //  TODO - bad assumption
        return (Constructor<T>) impl.getConstructors()[0];
    }

    private Class<? extends IdObjectWrapper> getImplForClass(final Class<? extends IdObject> entity) {
        for (Class i : entity.getInterfaces()) {
            if (IdObject.class.isAssignableFrom(i)) {
                return entityWrapperMap.get(i);
            }
        }
        throw new InvalidParameterException("Not able to find HDB mapping for " + entity.getSimpleName());
    }
}