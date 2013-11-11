package com.jtbdevelopment.e_eye_o.DAO;

import com.jtbdevelopment.e_eye_o.entities.*;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.List;
import java.util.Set;

/**
 * Provides access to physical storage
 * Date: 11/18/12
 * Time: 12:18 AM
 */
public interface ReadOnlyDAO {

    public Set<AppUser> getUsers();

    public AppUser getUser(final String emailAddress);

    public <T extends IdObject> T get(final Class<T> entityType, final String id);


    /**
     * Get results of all, active only or archived only objects
     *
     * @param entityType  - entity type, use any of the interface classes
     * @param appUser     - user for objects
     * @param firstResult - if using pagination, starting result
     * @param maxResult   - set to 0 to retrieve all, or any positive number for pagination
     * @param <T>         - type of entity which will be returned
     * @return set of T
     */
    public <T extends AppUserOwnedObject> Set<T> getEntitiesForUser(final Class<T> entityType, final AppUser appUser, final int firstResult, final int maxResult);

    public <T extends AppUserOwnedObject> Set<T> getActiveEntitiesForUser(final Class<T> entityType, final AppUser appUser, final int firstResult, final int maxResult);

    public <T extends AppUserOwnedObject> Set<T> getArchivedEntitiesForUser(final Class<T> entityType, final AppUser appUser, final int firstResult, final int maxResult);

    /**
     * Get count of all, active only or archived only objects
     *
     * @param entityType - entity type, use any of the interface classes
     * @param appUser    - user for objects
     * @param <T>        - type of entity which will be returned
     * @return count of T
     */
    public <T extends AppUserOwnedObject> int getEntitiesForUserCount(final Class<T> entityType, final AppUser appUser);

    public <T extends AppUserOwnedObject> int getActiveEntitiesForUserCount(final Class<T> entityType, final AppUser appUser);

    public <T extends AppUserOwnedObject> int getArchivedEntitiesForUserCount(final Class<T> entityType, final AppUser appUser);

    public List<Photo> getAllPhotosForEntity(final AppUserOwnedObject ownedObject, final int firstResult, final int maxResults);

    public List<Photo> getActivePhotosForEntity(final AppUserOwnedObject ownedObject, final int firstResult, final int maxResults);

    public List<Photo> getArchivedPhotosForEntity(final AppUserOwnedObject ownedObject, final int firstResult, final int maxResults);

    public int getAllPhotosForEntityCount(final AppUserOwnedObject ownedObject);

    public int getActivePhotosForEntityCount(final AppUserOwnedObject ownedObject);

    public int getArchivedPhotosForEntityCount(final AppUserOwnedObject ownedObject);

    public Set<Observation> getAllObservationsForSemester(final Semester semester, final int firstResult, final int maxResults);

    public Set<Observation> getActiveObservationsForSemester(final Semester semester, final int firstResult, final int maxResults);

    public Set<Observation> getArchivedObservationsForSemester(final Semester semester, final int firstResult, final int maxResults);

    /**
     * Generally expected to return an ordered set by modification timestamp ascending and id of all versions of changes made
     * Note this function is also expected to include DeletedObjects which are generally filtered out of other queries
     * <p/>
     * Returns items where:
     * modificationTimestamp > since OR
     * modificationTimestamp = since && modified id > id
     *
     * @param appUser - the user
     * @param since   - timestamp to look since
     * @param sinceId - last id to use if timestamp same
     * @param <T>     - see entityType
     * @return an ordered set of modified entities, ordered by modification timestamp ascending
     */
    public <T extends AppUserOwnedObject> List<String> getModificationsSince(final AppUser appUser, final DateTime since, final String sinceId, int maxResults);

    public LocalDateTime getLastObservationTimestampForEntity(final Observable observable);

    public List<Observation> getAllObservationsForEntity(final Observable observable);

    public List<Observation> getAllObservationsForObservationCategory(final ObservationCategory observationCategory);

    public List<Observation> getAllObservationsForEntityAndCategory(final Observable observable, final ObservationCategory observationCategory, final LocalDate from, final LocalDate to);

    public List<Student> getAllStudentsForClassList(final ClassList classList);
}

