package com.jtbdevelopment.e_eye_o.hibernate.DAO.helpers

import com.jtbdevelopment.e_eye_o.DAO.helpers.AbstractDeletionHelperTest
import com.jtbdevelopment.e_eye_o.DAO.helpers.DeletionHelper

/**
 * Date: 12/27/13
 * Time: 4:48 PM
 */
class HibernateDeletionHelperImplTest extends AbstractDeletionHelperTest {

    @Override
    DeletionHelper createDeletionHelper() {
        return new HibernateDeletionHelperImpl()
    }
}
