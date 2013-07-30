package com.jtbdevelopment.e_eye_o.hibernate.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.entities.wrapper.DAOIdObjectWrapperFactory;
import com.jtbdevelopment.e_eye_o.entities.wrapper.IdObjectWrapper;
import org.hibernate.annotations.GenericGenerator;
import org.joda.time.DateTime;

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
 * being created where factories not yet available.  These aren't used but you have to cater for
 * all states of initialization.
 * <p/>
 * Using the static factories is distasteful, but allows for independent testing and injection
 * for objects not being instantiated via Spring
 */
@Entity(name = "IdObject")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class HibernateIdObject<T extends IdObject> implements IdObjectWrapper<T>, IdObject {
    private static IdObjectFactory implFactory;
    private static DAOIdObjectWrapperFactory daoFactory;

    public static void setImplFactory(final IdObjectFactory implFactory) {
        HibernateIdObject.implFactory = implFactory;
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
        return (o instanceof IdObject) && wrapped.equals(o);
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

    public void setId(final String id) {
        wrapped.setId(id);
    }

    //
    //  Unfortunately, despite adding sub-second support in MySQL in 5.6.4
    //  prepared statements via hibernate are still truncated in the driver
    //  as of version 5.1.24
    //
    //  To manage maximum flexibility, storing modificationTimestamp as instant for now
    //
    @Override
    @Transient
    public DateTime getModificationTimestamp() {
        return wrapped.getModificationTimestamp();
    }

    @Override
    public void setModificationTimestamp(final DateTime modificationTimestamp) {
        wrapped.setModificationTimestamp(modificationTimestamp);
    }

    @Column(name = "modificationTimestamp", nullable = false)
    @SuppressWarnings("unused")
    private long getModificationTimestampInstant() {
        return wrapped.getModificationTimestamp().getMillis();
    }

    @SuppressWarnings("unused")
    private void setModificationTimestampInstant(final long instant) {
        wrapped.setModificationTimestamp(new DateTime(instant));
    }

    @Override
    @Transient
    public String getSummaryDescription() {
        return wrapped.getSummaryDescription();
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
