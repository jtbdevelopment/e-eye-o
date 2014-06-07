package com.jtbdevelopment.e_eye_o.hibernate.entities;

import com.jtbdevelopment.e_eye_o.entities.Semester;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.joda.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Date: 11/11/13
 * Time: 5:55 PM
 */
@Entity(name = "Semester")
@Audited
@Proxy(lazy = false)
public class HibernateSemester extends HibernateAppUserOwnedObject<Semester> implements Semester {
    @SuppressWarnings("unused")
    public HibernateSemester() {
    }

    @SuppressWarnings("unused")
    public HibernateSemester(final Semester appUserOwnedObject) {
        super(appUserOwnedObject);
    }

    @Override
    @Column(nullable = false)
    public String getDescription() {
        return wrapped.getDescription();
    }

    @Override
    public void setDescription(final String description) {
        wrapped.setDescription(description);
    }

    @Override
    @Column(nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    public LocalDate getStart() {
        return wrapped.getStart();
    }

    @Override
    public void setStart(final LocalDate start) {
        wrapped.setStart(start);
    }

    @Override
    @Column(nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    public LocalDate getEnd() {
        return wrapped.getEnd();
    }

    @Override
    public void setEnd(final LocalDate end) {
        wrapped.setEnd(end);
    }
}
