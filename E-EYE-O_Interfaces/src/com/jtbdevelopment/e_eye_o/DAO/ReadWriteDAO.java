package com.jtbdevelopment.e_eye_o.DAO;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.Observation;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Date: 11/19/12
 * Time: 5:32 PM
 * <p/>
 * All create/update functions may return new instances of the save object
 * Always use new object provided
 */
public interface ReadWriteDAO extends ReadOnlyDAO {
    public interface ChainedUpdateSet<T> {
        Set<T> getModifiedItems();

        Set<T> getDeletedItems();
    }

    <T extends IdObject> T create(final T entity);

    <T extends IdObject> Collection<T> create(final Collection<T> entities);

    <T extends IdObject> T update(final T entity);

    <T extends IdObject> Collection<T> update(final Collection<T> entities);

    <T extends AppUserOwnedObject> ChainedUpdateSet<AppUserOwnedObject> changeArchiveStatus(final T entity);

    <T extends AppUserOwnedObject> Map<T, ChainedUpdateSet<AppUserOwnedObject>> changeArchiveStatus(final Collection<T> entities);

    /**
     * Link two existing observations
     *
     * @param initialObservation  as described
     * @param followUpObservation as described
     * @return updated followup observation
     */
    Observation linkFollowUpObservation(final Observation initialObservation, final Observation followUpObservation);

    /**
     * Create new observation and link to existing observation
     *
     * @param initialObservation  as described
     * @param followUpObservation as described
     * @return updated followup observation
     */
    Observation createAndLinkFollowUpObservation(final Observation initialObservation, final Observation followUpObservation);

    /**
     * Similar to delete, will return all app user owned objects that were deleted as part of this.
     *
     * @param user user to delete
     * @return their items which were deleted
     */
    ChainedUpdateSet<AppUserOwnedObject> deleteUser(final AppUser user);

    /**
     * similar to deleteUser but with a mapped result of users to objects
     *
     * @param users users to delete
     * @return their deleted objects
     */
    Map<AppUser, ChainedUpdateSet<AppUserOwnedObject>> deleteUsers(final Collection<AppUser> users);

    /**
     * Will return the set of items which were deleted along with the requested item
     * <p/>
     * For example, deleting a student will delete all observations on that student.
     *
     * @param entity - entity to delete
     * @param <T>    - their type
     * @return - set of items deleted
     */
    <T extends AppUserOwnedObject> ChainedUpdateSet<AppUserOwnedObject> delete(final T entity);

    /**
     * Similar to delete, returns map of items deleted for each requested item
     *
     * @param entities - entities to delete
     * @param <T>      - their type
     * @return - mapped set of entity to deleted items
     */
    <T extends AppUserOwnedObject> Map<T, ChainedUpdateSet<AppUserOwnedObject>> delete(final Collection<T> entities);
}
