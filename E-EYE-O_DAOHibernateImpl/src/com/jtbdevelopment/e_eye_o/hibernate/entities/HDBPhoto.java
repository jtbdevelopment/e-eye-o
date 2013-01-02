package com.jtbdevelopment.e_eye_o.hibernate.entities;

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
public class HDBPhoto extends HDBArchivableAppUserOwnedObject<Photo> implements Photo {
    @SuppressWarnings("unused")
    protected HDBPhoto() {
        super();
        //  For hibernate
    }

    public HDBPhoto(final Photo photo) {
        super(photo);
    }

    @Override
    @ManyToOne(targetEntity = HDBAppUserOwnedObject.class, optional = false)
    public AppUserOwnedObject getPhotoFor() {
        return getWrapped().getPhotoFor();
    }

    @Override
    public Photo setPhotoFor(final AppUserOwnedObject photoFor) {
        return getWrapped().setPhotoFor(wrap(photoFor));
    }

    @Override
    @Column(nullable = false, length = Photo.MAX_DESCRIPTION_SIZE)
    public String getDescription() {
        return getWrapped().getDescription();
    }

    @Override
    public Photo setDescription(final String description) {
        getWrapped().setDescription(description);
        return this;
    }

    @Override
    @Column(nullable = false)
    public LocalDateTime getTimestamp() {
        return getWrapped().getTimestamp();
    }

    @Override
    public Photo setTimestamp(final LocalDateTime timestamp) {
        getWrapped().setTimestamp(timestamp);
        return this;
    }
}
