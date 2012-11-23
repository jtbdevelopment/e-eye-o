package com.jtbdevelopment.e_eye_o.DAO;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.superclasses.IdObject;
import com.jtbdevelopment.e_eye_o.superclasses.AppUserOwnedObject;

/**
 * Date: 11/19/12
 * Time: 5:32 PM
 */
public interface WriteDAO extends ReadOnlyDAO {
    public <T extends IdObject> T create(final T entity);

    public <T extends IdObject> T update(final T entity);

    public Iterable<? extends IdObject> create(final Iterable<? extends IdObject> entities);

    public Iterable<? extends IdObject> update(final Iterable<? extends IdObject> entities);

    public void deleteUser(final AppUser user);

    public void deleteUsers(final Iterable<AppUser> users);

    public <T extends AppUserOwnedObject> void delete(final T entity);

    public void delete(final Iterable<? extends AppUserOwnedObject> entities);
}
