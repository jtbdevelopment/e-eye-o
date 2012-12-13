package com.jtbdevelopment.e_eye_o.entities;

/**
 * Date: 11/25/12
 * Time: 3:10 PM
 */
public interface AppUserOwnedObject extends IdObject {
    AppUser getAppUser();

    <T extends AppUserOwnedObject> T setAppUser(final AppUser appUser);
}
