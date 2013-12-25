package com.jtbdevelopment.e_eye_o.hibernate.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.Photo;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.joda.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

/**
 * Date: 11/18/12
 * Time: 12:56 AM
 */
@Entity(name = "Photo")
@Audited
@Proxy(lazy = false)
public class HibernatePhoto extends HibernateAppUserOwnedObject<Photo> implements Photo {
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
    public void setPhotoFor(final AppUserOwnedObject photoFor) {
        wrapped.setPhotoFor(wrap(photoFor));
    }

    @Override
    @Column(nullable = false, length = Photo.MAX_DESCRIPTION_SIZE)
    public String getDescription() {
        return wrapped.getDescription();
    }

    @Override
    public void setDescription(final String description) {
        wrapped.setDescription(description);
    }

    @Override
    @Column(nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    public LocalDateTime getTimestamp() {
        return wrapped.getTimestamp().withMillisOfSecond(0);
    }

    @Override
    public void setTimestamp(final LocalDateTime timestamp) {
        wrapped.setTimestamp(timestamp.withMillisOfSecond(0));
    }

    @Override
    @Column
    public String getMimeType() {
        return wrapped.getMimeType();
    }

    @Override
    public void setMimeType(final String mimeType) {
        wrapped.setMimeType(mimeType);
    }

    @Override
    @Lob
    public byte[] getImageData() {
        return wrapped.getImageData();
    }

    @Override
    public void setImageData(final byte[] imageBytes) {
        wrapped.setImageData(imageBytes);
    }

    @Override
    @Lob
    public byte[] getThumbnailImageData() {
        return wrapped.getThumbnailImageData();
    }

    @Override
    public void setThumbnailImageData(final byte[] thumbnailImageData) {
        wrapped.setThumbnailImageData(thumbnailImageData);
    }
}
