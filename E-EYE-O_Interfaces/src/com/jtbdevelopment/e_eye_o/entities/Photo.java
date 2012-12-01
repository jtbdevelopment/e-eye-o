package com.jtbdevelopment.e_eye_o.entities;

import org.joda.time.LocalDateTime;

/**
 * Date: 11/25/12
 * Time: 3:18 PM
 */
public interface Photo extends ArchivableAppUserOwnedObject {
    String getDescription();

    Photo setDescription(String description);

    LocalDateTime getTimestamp();

    Photo setTimestamp(LocalDateTime timestamp);
}
