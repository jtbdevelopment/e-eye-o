package com.jtbdevelopment.e_eye_o.entities;

import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectFieldPreferences;
import com.jtbdevelopment.e_eye_o.entities.validation.ConsistentAppUserCheck;

import javax.validation.constraints.NotNull;

/**
 * Date: 11/25/12
 * Time: 3:10 PM
 * <p/>
 * Base for any object that is associated for a user
 * <p/>
 * Archived objects will generally not be synced with smart devices
 * but will still be visible on web pages
 */
@ConsistentAppUserCheck(message = AppUserOwnedObject.ALL_OWNED_OBJECTS_MUST_BE_FOR_SAME_USER_ERROR)
public interface AppUserOwnedObject extends IdObject {

    public final static String APP_USER_CANNOT_BE_NULL_ERROR = "AppUserOwnerObject.appUser" + CANNOT_BE_NULL_ERROR;
    public final static String ALL_OWNED_OBJECTS_MUST_BE_FOR_SAME_USER_ERROR = "All AppUserOwnedObjects must match.";

    @NotNull(message = APP_USER_CANNOT_BE_NULL_ERROR)
    @IdObjectFieldPreferences(viewable = false, editableBy = IdObjectFieldPreferences.EditableBy.NONE)
    AppUser getAppUser();

    void setAppUser(final AppUser appUser);

    @IdObjectFieldPreferences(label = "Active", editableBy = IdObjectFieldPreferences.EditableBy.NONE, fieldType = IdObjectFieldPreferences.DisplayFieldType.REVERSE_CHECKBOX, alignment = IdObjectFieldPreferences.DisplayAlignment.CENTER)
    boolean isArchived();

    void setArchived(boolean archived);
}
