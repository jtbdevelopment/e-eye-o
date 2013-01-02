package com.jtbdevelopment.e_eye_o.hibernate.entities;

import com.jtbdevelopment.e_eye_o.entities.ClassList;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Date: 11/17/12
 * Time: 2:05 PM
 */
@Entity(name = "ClassList")
public class HDBClassList extends HDBArchivableAppUserOwnedObject<ClassList> implements ClassList {
    @SuppressWarnings("unused")
    protected HDBClassList() {
        super();
    }

    public HDBClassList(final ClassList classList) {
        super(classList);
    }

    @Override
    @Column(nullable = false, length = ClassList.MAX_DESCRIPTION_SIZE)
    public String getDescription() {
        return getWrapped().getDescription();
    }

    @Override
    public ClassList setDescription(final String description) {
        getWrapped().setDescription(description);
        return this;
    }
}
