package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject
import com.jtbdevelopment.e_eye_o.entities.impl.AppUserOwnedObjectImpl

/**
 * Date: 12/1/13
 * Time: 7:43 PM
 */
class AppUserOwnedObjectBuilderImplTest extends AbstractAppUserOwnedObjectBuilderTest {
    static class AppUserOwnedImpl extends AppUserOwnedObjectImpl {
        public AppUserOwnedImpl() {
            super(null)
        }
    }

    @Override
    def createEntity() {
        return new AppUserOwnedImpl()
    }

    @Override
    def createBuilder(final entity) {
        return new AppUserOwnedObjectBuilderImpl<>((AppUserOwnedObject) entity)
    }
}
