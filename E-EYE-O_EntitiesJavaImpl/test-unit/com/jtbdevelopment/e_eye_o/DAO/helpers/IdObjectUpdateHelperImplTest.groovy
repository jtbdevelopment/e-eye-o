package com.jtbdevelopment.e_eye_o.DAO.helpers
/**
 * Date: 12/13/13
 * Time: 11:29 PM
 */
class IdObjectUpdateHelperImplTest extends AbstractIdObjectUpdateHelperTest {
    @Override
    IdObjectFieldUpdateValidator createHelper() {
        IdObjectFieldUpdateValidatorImpl impl = new IdObjectFieldUpdateValidatorImpl()
        impl.reflectionHelper = helper
        return impl
    }
}
