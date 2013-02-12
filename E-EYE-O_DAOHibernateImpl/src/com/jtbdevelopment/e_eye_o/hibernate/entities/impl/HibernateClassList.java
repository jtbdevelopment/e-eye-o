package com.jtbdevelopment.e_eye_o.hibernate.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.ClassList;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Date: 11/17/12
 * Time: 2:05 PM
 */
@Entity(name = "ClassList")
public class HibernateClassList extends HibernateAppUserOwnedObject<ClassList> implements ClassList {
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
    public ClassList setDescription(final String description) {
        wrapped.setDescription(description);
        return this;
    }
}
