package com.jtbdevelopment.e_eye_o.hibernate.DAO.helpers;

import com.jtbdevelopment.e_eye_o.DAO.helpers.AbstractDeletionHelperImpl;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Date: 12/26/13
 * Time: 8:38 PM
 */
@Component
public class HibernateDeletionHelperImpl extends AbstractDeletionHelperImpl {
    @Override
    @Transactional(readOnly = false)
    public void deactivateUser(final AppUser user) {
        super.deactivateUser(user);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteUser(final AppUser user) {
        super.deleteUser(user);
    }

    @Override
    @Transactional(readOnly = false)
    public <T extends AppUserOwnedObject> void delete(final T entity) {
        super.delete(entity);
    }
}
