package com.jtbdevelopment.e_eye_o.entities;

/**
 * Date: 11/25/12
 * Time: 3:09 PM
 */
public interface IdObject {
    String getId();

    <T extends IdObject> T setId(final String id);
}
