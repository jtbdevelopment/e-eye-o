package com.jtbdevelopment.e_eye_o.entities.impl.reflection

import com.jtbdevelopment.e_eye_o.entities.reflection.IdObjectReflectionHelper

class IdObjectReflectionHelperGImplTest extends AbstractIdObjectReflectionHelperTest {

    @Override
    IdObjectReflectionHelper createReflectionHelper() {
        return new IdObjectReflectionHelperGImpl()
    }

}
