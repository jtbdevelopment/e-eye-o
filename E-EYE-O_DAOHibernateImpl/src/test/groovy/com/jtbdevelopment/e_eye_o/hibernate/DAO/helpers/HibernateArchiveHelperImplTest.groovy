package com.jtbdevelopment.e_eye_o.hibernate.DAO.helpers

import com.jtbdevelopment.e_eye_o.DAO.helpers.AbstractArchiveHelperTest
import com.jtbdevelopment.e_eye_o.DAO.helpers.ArchiveHelper

/**
 * Date: 12/28/13
 * Time: 6:36 PM
 */
class HibernateArchiveHelperImplTest extends AbstractArchiveHelperTest {
    @Override
    ArchiveHelper createArchiveHelper() {
        return new HibernateArchiveHelperImpl()
    }
}
