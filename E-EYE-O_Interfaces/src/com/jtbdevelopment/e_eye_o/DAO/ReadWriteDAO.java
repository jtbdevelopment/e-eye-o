package com.jtbdevelopment.e_eye_o.DAO;

import com.jtbdevelopment.e_eye_o.entities.*;

import java.util.Map;

/**
 * Date: 11/19/12
 * Time: 5:32 PM
 * <p/>
 * All create/update functions may return new instances of the save object
 * Always use new object provided
 */
public interface ReadWriteDAO extends ReadOnlyDAO {

    <T extends IdObject> T create(final T entity);

    /**
     * An import part of the contract is that update will ignore changes to fields which the user is not entitled
     * to change.
     *
     * @param updatingAppUser - user performing change
     * @param entity          - entity under change
     * @param <T>             - entityType
     * @return updated entity
     */
    <T extends IdObject> T update(final AppUser updatingAppUser, final T entity);

    <T extends AppUserOwnedObject> T changeArchiveStatus(final T entity);

    TwoPhaseActivity activateUser(final TwoPhaseActivity relatedActivity);

    TwoPhaseActivity resetUserPassword(final TwoPhaseActivity relatedActivity, final String newPassword);

    AppUser updateAppUserLogout(final AppUser appUser);

    AppUserSettings updateSettings(final AppUser appUser, Map<String, Object> settings);

    /**
     * Similar to delete, will return all app user owned objects that were deleted as part of this.
     *
     * @param user user to delete
     */
    void deleteUser(final AppUser user);

    /**
     * Will return the set of items which were deleted along with the requested item
     * <p/>
     * For example, deleting a student will delete all observations on that student.
     *
     * @param entity - entity to delete
     * @param <T>    - their type
     */
    <T extends AppUserOwnedObject> void delete(final T entity);

}
