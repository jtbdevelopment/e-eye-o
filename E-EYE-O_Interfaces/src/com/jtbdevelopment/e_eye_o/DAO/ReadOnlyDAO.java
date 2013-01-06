package com.jtbdevelopment.e_eye_o.DAO;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.ArchivableAppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.IdObject;

import java.util.Set;

/**
 * Provides access to physical storage
 * Date: 11/18/12
 * Time: 12:18 AM
 */
public interface ReadOnlyDAO {

    public <T extends IdObject> T get(final Class<T> entityType, final String id);

    public <T extends AppUserOwnedObject> Set<T> getEntitiesForUser(final Class<T> entityType, final AppUser appUser);

    public <T extends ArchivableAppUserOwnedObject> Set<T> getActiveEntitiesForUser(Class<T> entityType, final AppUser appUser);

    public <T extends ArchivableAppUserOwnedObject> Set<T> getArchivedEntitiesForUser(Class<T> entityType, final AppUser appUser);

}

