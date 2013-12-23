package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.impl.AppUserImpl

/**
 * Date: 12/1/13
 * Time: 8:58 PM
 */
class AppUserBuilderImplTest extends AbstractAppUserBuilderTest {
    @Override
    def createEntity() {
        return new AppUserImpl()
    }

    @Override
    def createBuilder(final entity) {
        return new AppUserBuilderImpl((AppUser) entity)
    }
}
