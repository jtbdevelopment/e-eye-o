package com.jtbdevelopment.e_eye_o.entities;

/**
 * Date: 11/25/12
 * Time: 3:18 PM
 */
public interface ObservationCategory extends AppUserOwnedObject {
    String getShortName();

    ObservationCategory setShortName(String shortName);

    String getDescription();

    ObservationCategory setDescription(String description);
}
