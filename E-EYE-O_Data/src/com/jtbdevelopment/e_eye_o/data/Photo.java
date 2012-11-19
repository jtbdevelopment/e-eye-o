package com.jtbdevelopment.e_eye_o.data;

import org.joda.time.LocalDateTime;

import javax.persistence.Entity;

/**
 * Date: 11/18/12
 * Time: 12:56 AM
 */
@Entity
public class Photo extends IdObject {
    private String description = "";
    private LocalDateTime timestamp;
    //  TODO - actual photo
}
