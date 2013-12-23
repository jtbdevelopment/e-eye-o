package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory

/**
 * Date: 12/3/13
 * Time: 7:01 AM
 */
class IdObjectFactoryImplTest extends AbstractIdObjectFactoryTest {
    public static String APPEND = "Impl"

    @Override
    IdObjectFactory createFactory() {
        return new IdObjectFactoryImpl()
    }

    String getExpectedBuilderClassName(String name) {
        name + "Builder" + APPEND
    }

    String getExpectedImplClassName(String name) {
        name + APPEND
    }

}
