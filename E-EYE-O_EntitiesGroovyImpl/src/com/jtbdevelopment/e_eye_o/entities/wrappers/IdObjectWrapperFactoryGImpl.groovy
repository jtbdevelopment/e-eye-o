package com.jtbdevelopment.e_eye_o.entities.wrappers
/**
 * Date: 12/8/13
 * Time: 12:52 PM
 *
 * Couldn't get this to work as a groovy service unfortunately
 */
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