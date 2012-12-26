package com.jtbdevelopment.e_eye_o.entities;

import com.jtbdevelopment.e_eye_o.entities.validation.ConsistentAppUserCheck;

import javax.validation.constraints.NotNull;

/**
 * Date: 11/25/12
 * Time: 3:10 PM
 */
@ConsistentAppUserCheck(message = AppUserOwnedObject.ALL_OWNED_OBJECTS_MUST_BE_FOR_SAME_USER_ERROR)
public interface AppUserOwnedObject extends IdObject {

    public final static String APP_USER_CANNOT_BE_NULL_ERROR = "AppUserOwnerObject.appUser" + CANNOT_BE_NULL_ERROR;
    public final static String ALL_OWNED_OBJECTS_MUST_BE_FOR_SAME_USER_ERROR = "All AppUserOwnedObjects must match.";

    @NotNull(message = APP_USER_CANNOT_BE_NULL_ERROR)
    AppUser getAppUser();

    <T extends AppUserOwnedObject> T setAppUser(final AppUser appUser);
}
