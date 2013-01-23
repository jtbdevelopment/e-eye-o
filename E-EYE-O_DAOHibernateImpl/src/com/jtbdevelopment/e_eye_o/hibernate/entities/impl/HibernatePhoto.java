package com.jtbdevelopment.e_eye_o.hibernate.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.Photo;
import org.joda.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Date: 11/18/12
 * Time: 12:56 AM
 */
@Entity(name = "Photo")
public class HibernatePhoto extends HibernateArchivableAppUserOwnedObject<Photo> implements Photo {
    @SuppressWarnings("unused")    // Hibernate
    protected HibernatePhoto() {
    }

    @SuppressWarnings("unused")  //  HibernateIdObjectWrapperFactory via reflection
    protected HibernatePhoto(final Photo photo) {
        super(photo);
    }

    @Override
    @ManyToOne(targetEntity = HibernateAppUserOwnedObject.class, optional = false)
    public AppUserOwnedObject getPhotoFor() {
        return wrapped.getPhotoFor();
    }

    @Override
    public Photo setPhotoFor(final AppUserOwnedObject photoFor) {
        wrapped.setPhotoFor(wrap(photoFor));
        return this;
    }

    @Override
    @Column(nullable = false, length = Photo.MAX_DESCRIPTION_SIZE)
    public String getDescription() {
        return wrapped.getDescription();
    }

    @Override
    public Photo setDescription(final String description) {
        wrapped.setDescription(description);
        return this;
    }

    @Override
    @Column(nullable = false)
    public LocalDateTime getTimestamp() {
        return wrapped.getTimestamp();
    }

    @Override
    public Photo setTimestamp(final LocalDateTime timestamp) {
        wrapped.setTimestamp(timestamp);
        return this;
    }
}