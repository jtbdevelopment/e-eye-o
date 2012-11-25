package com.jtbdevelopment.e_eye_o.DAO;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.ClassList;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import com.jtbdevelopment.e_eye_o.superclasses.ArchivableAppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.superclasses.AppUserOwnedObject;

import java.util.Map;
import java.util.Set;

/**
 * Provides access to physical storage
 * Date: 11/18/12
 * Time: 12:18 AM
 */
public interface ReadOnlyDAO {

    public <T> T get(Class<T> type, String id);

    public Map<String, ObservationCategory> getObservationCategoriesAsMap(final AppUser appUser);

    public <T extends AppUserOwnedObject> Set<T> getEntitiesForUser(Class<T> entityType, final AppUser appUser);

    public <T extends ArchivableAppUserOwnedObject> Set<T> getActiveEntitiesForUser(Class<T> entityType, final AppUser appUser);

    public <T extends ArchivableAppUserOwnedObject> Set<T> getArchivedEntitiesForUser(Class<T> entityType, final AppUser appUser);

}

