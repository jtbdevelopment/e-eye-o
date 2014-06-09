package com.jtbdevelopment.e_eye_o.entities.builders

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObjectGImpl

/**
 * Date: 12/1/13
 * Time: 7:43 PM
 */
class AppUserOwnedObjectBuilderGImplTest extends AbstractAppUserOwnedObjectBuilderTest {
    static class AppUserOwnedImpl extends AppUserOwnedObjectGImpl {
    }

    @Override
    def createEntity() {
        return new AppUserOwnedImpl()
    }

    @Override
    def createBuilder(final entity) {
        return new AppUserOwnedObjectBuilderGImpl<>(entity: entity)
    }
}
