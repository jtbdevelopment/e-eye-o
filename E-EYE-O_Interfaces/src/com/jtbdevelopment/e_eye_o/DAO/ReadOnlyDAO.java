package com.jtbdevelopment.e_eye_o.DAO;

import com.jtbdevelopment.e_eye_o.entities.*;
import org.joda.time.DateTime;
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

    public <T extends AppUserOwnedObject> Set<T> getEntitiesForUser(final Class<T> entityType, final AppUser appUser);

    public <T extends AppUserOwnedObject> Set<T> getEntitiesForUser(final Class<T> entityType, final AppUser appUser, final boolean activeFlag);

    public <T extends AppUserOwnedObject> Set<T> getActiveEntitiesForUser(final Class<T> entityType, final AppUser appUser);

    public <T extends AppUserOwnedObject> Set<T> getArchivedEntitiesForUser(final Class<T> entityType, final AppUser appUser);

    /**
     * Generally expected to return an ordered set by modification timestamp ascending
     * Note this function is also expected to include DeletedObjects which are generally filtered out of other queries
     *
     * @param entityType - use AppUserOwnedObject for all entities by appuser
     * @param appUser    - the user
     * @param since      - timestamp
     * @param <T>        - see entityType
     * @return an ordered set of modified entities, ordered by modification timestamp ascending
     */
    public <T extends AppUserOwnedObject> Set<T> getEntitiesModifiedSince(final Class<T> entityType, final AppUser appUser, final DateTime since);

    public List<Photo> getAllPhotosForEntity(final AppUserOwnedObject ownedObject);

    public LocalDateTime getLastObservationTimestampForEntity(final Observable observable);

    public List<Observation> getAllObservationsForEntity(final Observable observable);

    public List<Observation> getAllObservationsForObservationCategory(final ObservationCategory observationCategory);

    public List<Observation> getAllObservationFollowups(final Observation initialObservation);

    public List<Student> getAllStudentsForClassList(final ClassList classList);
}

