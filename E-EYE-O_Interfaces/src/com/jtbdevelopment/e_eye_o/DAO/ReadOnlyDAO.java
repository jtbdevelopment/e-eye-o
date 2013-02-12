package com.jtbdevelopment.e_eye_o.DAO;

import com.jtbdevelopment.e_eye_o.entities.*;

import java.util.List;
import java.util.Set;

/**
 * Provides access to physical storage
 * Date: 11/18/12
 * Time: 12:18 AM
 */
public interface ReadOnlyDAO {

    public Set<AppUser> getUsers();

    public <T extends IdObject> T get(final Class<T> entityType, final String id);

    public <T extends AppUserOwnedObject> Set<T> getEntitiesForUser(final Class<T> entityType, final AppUser appUser);

    public <T extends AppUserOwnedObject> Set<T> getActiveEntitiesForUser(Class<T> entityType, final AppUser appUser);

    public <T extends AppUserOwnedObject> Set<T> getArchivedEntitiesForUser(Class<T> entityType, final AppUser appUser);

    public List<Photo> getAllPhotosForEntity(AppUserOwnedObject ownedObject);

    public List<Observation> getAllObservationsForEntity(AppUserOwnedObject ownedObject);

    public List<Observation> getAllObservationsForObservationCategory(ObservationCategory observationCategory);

    public List<Observation> getAllObservationsForFollowup(Observation followup);

    public List<Student> getAllStudentsForClassList(ClassList classList);
}

