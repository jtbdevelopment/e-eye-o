package com.jtbdevelopment.e_eye_o.DAO.helpers
/**
 * Date: 12/13/13
 * Time: 11:29 PM
 */
class IdObjectUpdateHelperImplTest extends AbstractIdObjectUpdateHelperTest {
    @Override
    IdObjectUpdateHelper createHelper() {
        IdObjectUpdateHelperImpl impl = new IdObjectUpdateHelperImpl()
        impl.reflectionHelper = helper
        return impl
    }
}
