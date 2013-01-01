package com.jtbdevelopment.e_eye_o.hibernate.entities;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.wrapper.IdObjectWrapper;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;

/**
 * Date: 11/18/12
 * Time: 12:45 AM
 */
@Entity(name = "IdObject")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class HDBIdObject<T extends IdObject> implements IdObjectWrapper<T>, IdObject {
    protected T wrapped;

    public HDBIdObject(final T wrapped) {
        if (wrapped instanceof IdObjectWrapper) {
            this.wrapped = ((IdObjectWrapper<T>) wrapped).getWrapped();
        } else {
            this.wrapped = wrapped;
        }
    }

    @Override
    @Transient
    public T getWrapped() {
        return wrapped;
    }

    @Override
    public boolean equals(Object o) {
        return wrapped.equals(o);
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
        return wrapped.getId();
    }

    @SuppressWarnings("unused")
    public T setId(final String id) {
        wrapped.setId(id);
        return (T) this;
    }

    @Transient
    private HDBIdObjectWrapperFactory getWrapper() {
        return HDBIdObjectWrapperFactory.getInstance();
    }

    @Transient
    protected <OO extends IdObject> OO wrap(OO entity) {
        return getWrapper().wrap(entity);
    }

    @Transient
    protected <OO extends IdObject, C extends Collection<OO>> C wrap(final C entities) {
        return getWrapper().wrap(entities);
    }

}
