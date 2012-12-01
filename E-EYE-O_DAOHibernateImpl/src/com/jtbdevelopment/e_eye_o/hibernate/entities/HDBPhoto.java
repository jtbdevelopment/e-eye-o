package com.jtbdevelopment.e_eye_o.hibernate.entities;

import com.jtbdevelopment.e_eye_o.entities.Photo;
import com.jtbdevelopment.e_eye_o.entities.PhotoImpl;
import org.joda.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Date: 11/18/12
 * Time: 12:56 AM
 */
@Entity(name = "Photo")
public class HDBPhoto extends HDBArchivableAppUserOwnedObject<Photo> implements Photo {
    @SuppressWarnings("unused")
    protected HDBPhoto() {
        super(new PhotoImpl());
        //  For hibernate
    }

    public HDBPhoto(final Photo photo) {
        super(photo);
    }

    @Override
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
