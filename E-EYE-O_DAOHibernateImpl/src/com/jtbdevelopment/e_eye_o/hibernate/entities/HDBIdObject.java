package com.jtbdevelopment.e_eye_o.hibernate.entities;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.wrapper.IdObjectWrapper;
import com.jtbdevelopment.e_eye_o.hibernate.entities.wrapper.HDBIdObjectWrapperFactory;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * Date: 11/18/12
 * Time: 12:45 AM
 */
@MappedSuperclass
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
        if (this == o) return true;
        if (o == null || !(o instanceof IdObject)) return false;

        IdObject idObject = (IdObject) o;

        if (getId() != null ? !getId().equals(idObject.getId()) : idObject.getId() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
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
    protected HDBIdObjectWrapperFactory getWrapper() {
        return HDBIdObjectWrapperFactory.getInstance();
    }

}
