package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.IdObject

/**
 * Date: 11/18/13
 * Time: 9:06 PM
 */
class IdObjectImplTest extends AbstractIdObjectTest {
    public static class IdObjectGTest extends IdObjectImpl {

    }

    @Override
    def <T extends IdObject> T createObjectUnderTest() {
        return new IdObjectGTest()
    }
}
