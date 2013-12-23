package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.IdObject

/**
 * Date: 11/18/13
 * Time: 9:06 PM
 */
class IdObjectGImplTest extends AbstractIdObjectTest {
    public static class IdObjectGTest extends IdObjectGImpl {

    }

    @Override
    def <T extends IdObject> T createObjectUnderTest() {
        return new IdObjectGTest()
    }
}
