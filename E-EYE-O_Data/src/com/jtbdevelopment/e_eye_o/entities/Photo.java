package com.jtbdevelopment.e_eye_o.entities;

import com.jtbdevelopment.e_eye_o.superclasses.ArchivableAppUserOwnedObject;
import org.joda.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Date: 11/18/12
 * Time: 12:56 AM
 */
@Entity
public class Photo extends ArchivableAppUserOwnedObject {
    private String description = "";
    private LocalDateTime timestamp;
    //  TODO - actual photo

    @SuppressWarnings("unused")
    private Photo() {
        //  For hibernate
    }

    public Photo(final AppUser appUser) {
        super(appUser);
    }

    public String getDescription() {
        return description == null ? "" : description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(nullable = false)
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
