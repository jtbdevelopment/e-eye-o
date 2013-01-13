package com.jtbdevelopment.e_eye_o.hibernate.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.entities.wrapper.DAOIdObjectWrapperFactory;
import com.jtbdevelopment.e_eye_o.entities.wrapper.IdObjectWrapper;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;

/**
 * Date: 11/18/12
 * Time: 12:45 AM
 * <p/>
 * We have two mechanisms of being generated.
 * 1.  Via Hibernate Instantiating an Object.  It uses default constructor which means wrapper
 * needs to be built then and hence somewhat ugly statics
 * 2.  Via IdObjectFactory which always should pass in valid item to be wrapped
 * <p/>
 * In addition, unfortunately, Hibernate also constructs sample objects as part of initialization
 * which means we have to cater for not only using the default constructor, but some test objects
 * being created where factories not yet available.
 *
 * Using the static factories is distasteful, but allows for independent testing and injection
 *
 */
@Entity(name = "IdObject")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class HibernateIdObject<T extends IdObject> implements IdObjectWrapper<T>, IdObject {
    private static IdObjectFactory implFactory;
    private static DAOIdObjectWrapperFactory daoFactory;
    public static IdObjectFactory getImplFactory() {
        return implFactory;
    }

    public static void setImplFactory(final IdObjectFactory implFactory) {
        HibernateIdObject.implFactory = implFactory;
    }

    public static DAOIdObjectWrapperFactory getDaoFactory() {
        return daoFactory;
    }

    public static void setDaoFactory(final DAOIdObjectWrapperFactory daoFactory) {
        HibernateIdObject.daoFactory = daoFactory;
    }

    protected T wrapped;

    @SuppressWarnings("unchecked")
    protected HibernateIdObject() {
        if (implFactory != null && daoFactory != null) {
            wrapped = (T) implFactory.newIdObject(daoFactory.getEntityForWrapper(getClass()));
        }
    }

    @SuppressWarnings({"unused", "unchecked"})  //  HibernateIdObjectWrapperFactory via reflection
    protected HibernateIdObject(final T wrapped) {
        if (wrapped == null) {
            throw new IllegalArgumentException("Cannot wrap null object");
        }
        //  TODO - should we be going through and rewrapping its subobjects?
        if (wrapped instanceof IdObjectWrapper) {
            this.wrapped = ((IdObjectWrapper<T>) wrapped).getWrapped();
        } else {
            this.wrapped = wrapped;
        }
    }

    @Override
    @Transient
    @SuppressWarnings("unchecked")
    public T getWrapped() {
        return wrapped;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof IdObject)  && wrapped.equals(o);
    }

    @Override
    public int hashCode() {
        return wrapped.hashCode();
    }

    @Override
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    public String getId() {
        //  Hibernate initialization seems to call this before everything is ready.
        if (wrapped == null) {
            return null;
        }
        return wrapped.getId();
    }

    @SuppressWarnings("unchecked")
    public T setId(final String id) {
        wrapped.setId(id);
        return (T) this;
    }

    protected static <OO extends IdObject> OO wrap(OO entity) {
        //  TODO - should we be going through and rewrapping its subobjects?
        return daoFactory.wrap(entity);
    }

    protected static <OO extends IdObject, C extends Collection<OO>> C wrap(final C entities) {
        //  TODO - should we be going through and rewrapping its subobjects?
        return daoFactory.wrap(entities);
    }
}
