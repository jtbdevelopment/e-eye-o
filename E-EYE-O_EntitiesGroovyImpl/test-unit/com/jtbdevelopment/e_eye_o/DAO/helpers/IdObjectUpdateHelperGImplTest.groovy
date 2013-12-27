package com.jtbdevelopment.e_eye_o.DAO.helpers
/**
 * Date: 12/13/13
 * Time: 11:29 PM
 */
class IdObjectUpdateHelperGImplTest extends AbstractIdObjectUpdateHelperTest {
    @Override
    IdObjectFieldUpdateValidator createHelper() {
        return new IdObjectFieldUpdateValidatorGImpl(reflectionHelper: helper)
    }
}
