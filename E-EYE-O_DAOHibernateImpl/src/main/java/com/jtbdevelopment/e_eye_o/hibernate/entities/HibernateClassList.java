package com.jtbdevelopment.e_eye_o.hibernate.entities;

import com.jtbdevelopment.e_eye_o.entities.ClassList;
import org.hibernate.annotations.Proxy;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Date: 11/17/12
 * Time: 2:05 PM
 */
@Entity(name = "ClassList")
@Audited
@Proxy(lazy = false)
public class HibernateClassList extends HibernateObservable<ClassList> implements ClassList {
    @SuppressWarnings("unused")    // Hibernate
    protected HibernateClassList() {
    }

    @SuppressWarnings("unused")  //  HibernateIdObjectWrapperFactory via reflection
    protected HibernateClassList(final ClassList classList) {
        super(classList);
    }

    @Override
    @Column(nullable = false, length = ClassList.MAX_DESCRIPTION_SIZE)
    public String getDescription() {
        return wrapped.getDescription();
    }

    @Override
    public void setDescription(final String description) {
        wrapped.setDescription(description);
    }
}
