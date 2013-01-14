package com.jtbdevelopment.e_eye_o.DAO;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.IdObject;

import java.util.Collection;

/**
 * Date: 11/19/12
 * Time: 5:32 PM
 * <p/>
 * All create/update functions may return new instances of the save object
 * Always use new object provided
 */
public interface ReadWriteDAO extends ReadOnlyDAO {
    public <T extends IdObject> T create(final T entity);

    public <T extends IdObject> Collection<T> create(final Collection<T> entities);

    public <T extends IdObject> T update(final T entity);

    public <T extends IdObject> Collection<T> update(final Collection<T> entities);

    public void deleteUser(final AppUser user);

    public void deleteUsers(final Collection<AppUser> users);

    public <T extends AppUserOwnedObject> void delete(final T entity);

    public <T extends AppUserOwnedObject> void delete(final Collection<T> entities);
}
