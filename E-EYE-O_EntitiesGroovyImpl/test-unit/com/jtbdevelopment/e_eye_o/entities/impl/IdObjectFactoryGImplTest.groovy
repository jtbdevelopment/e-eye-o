package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactoryGImpl

/**
 * Date: 12/3/13
 * Time: 7:01 AM
 */
class IdObjectFactoryGImplTest extends AbstractIdObjectFactoryTest {
    public static String APPEND = "GImpl"

    @Override
    IdObjectFactory createFactory() {
        return new IdObjectFactoryGImpl()
    }

    String getExpectedBuilderClassName(String name) {
        name + "Builder" + APPEND
    }

    String getExpectedImplClassName(String name) {
        name + APPEND
    }

}
