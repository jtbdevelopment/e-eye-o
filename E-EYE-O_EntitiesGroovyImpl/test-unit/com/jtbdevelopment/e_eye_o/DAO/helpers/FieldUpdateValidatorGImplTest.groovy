package com.jtbdevelopment.e_eye_o.DAO.helpers
/**
 * Date: 12/13/13
 * Time: 11:29 PM
 */
class FieldUpdateValidatorGImplTest extends AbstractFieldUpdateValidatorTest {
    @Override
    FieldUpdateValidator createHelper() {
        return new FieldUpdateValidatorGImpl(reflectionHelper: helper)
    }
}
