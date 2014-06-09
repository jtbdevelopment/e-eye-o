package com.jtbdevelopment.e_eye_o.hibernate.DAO.helpers;

import com.jtbdevelopment.e_eye_o.DAO.helpers.AbstractArchiveHelperImpl;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Date: 12/26/13
 * Time: 7:25 PM
 */
@Component
public class HibernateArchiveHelperImpl extends AbstractArchiveHelperImpl {

    @Override
    @Transactional(readOnly = false)
    public <T extends AppUserOwnedObject> void flipArchiveStatus(final T entity) {
        super.flipArchiveStatus(entity);
    }
}
