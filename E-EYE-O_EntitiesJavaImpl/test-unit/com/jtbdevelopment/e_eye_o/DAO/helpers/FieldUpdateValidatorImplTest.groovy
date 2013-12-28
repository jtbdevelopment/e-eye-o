package com.jtbdevelopment.e_eye_o.DAO.helpers
/**
 * Date: 12/13/13
 * Time: 11:29 PM
 */
class FieldUpdateValidatorImplTest extends AbstractFieldUpdateValidatorTest {
    @Override
    FieldUpdateValidator createHelper() {
        FieldUpdateValidatorImpl impl = new FieldUpdateValidatorImpl()
        impl.reflectionHelper = helper
        return impl
    }
}
