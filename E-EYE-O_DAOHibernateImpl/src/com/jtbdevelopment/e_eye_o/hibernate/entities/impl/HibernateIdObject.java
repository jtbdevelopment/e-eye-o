package com.jtbdevelopment.e_eye_o.hibernate.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.entities.wrapper.IdObjectWrapper;
import com.jtbdevelopment.e_eye_o.entities.wrapper.IdObjectWrapperFactory;
import com.jtbdevelopment.e_eye_o.hibernate.entities.helpers.HibernateFactoryContext;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;

/**
 * Date: 11/18/12
 * Time: 12:45 AM
 */
@Entity(name = "IdObject")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class HibernateIdObject<T extends IdObject> implements IdObjectWrapper<T>, IdObject {
    private T wrapped;

    protected HibernateIdObject() {
    }

    @SuppressWarnings("unused")  //  HibernateIdObjectWrapperFactory via reflection
    public HibernateIdObject(final T wrapped) {
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
        if (wrapped != null) return wrapped;
        wrapped = (T) getImplFactory().newIdObject(getWrapperFactory().getEntityForWrapper(getClass()));
        return wrapped;
    }

    @Override
    public boolean equals(Object o) {
        return getWrapped().equals(o);
    }

    @Override
    public int hashCode() {
        return getWrapped().hashCode();
    }

    @Override
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    public String getId() {
        if (wrapped == null) {
            return null;
        }  //  Hibernate initialization seems to call this before everything is ready.
        return getWrapped().getId();
    }

    @SuppressWarnings("unchecked")
    public T setId(final String id) {
        getWrapped().setId(id);
        return (T) this;
    }

    private static IdObjectWrapperFactory getWrapperFactory() {
        return HibernateFactoryContext.getDaoIdObjectWrapperFactory();
    }

    protected static <OO extends IdObject> OO wrap(OO entity) {
        return getWrapperFactory().wrap(entity);
    }

    protected static <OO extends IdObject, C extends Collection<OO>> C wrap(final C entities) {
        return getWrapperFactory().wrap(entities);
    }

    private static IdObjectFactory getImplFactory() {
        return HibernateFactoryContext.getImplFactory();
    }
}
