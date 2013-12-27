package com.jtbdevelopment.e_eye_o.DAO;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.IdObject;

import java.util.Collection;
import java.util.List;

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

    /**
     * Similar to update, however this version does not vet the update fields for inappropriate changes
     * primarily to be used by helpers
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T extends IdObject> T trustedUpdate(final T entity);

    <T extends IdObject> List<T> trustedUpdates(final Collection<T> entity);

    /**
     * Similar to trustedUpdate(s) to be used by helpers
     * <p/>
     * Will delete entity without checking or logic
     *
     * @param entity
     * @param <T>
     */
    <T extends IdObject> void trustedDelete(final T entity);

    AppUser updateAppUserLogout(final AppUser appUser);
}
