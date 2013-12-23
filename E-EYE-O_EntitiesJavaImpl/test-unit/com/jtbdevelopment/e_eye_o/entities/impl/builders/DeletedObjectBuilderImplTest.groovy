package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.DeletedObject
import com.jtbdevelopment.e_eye_o.entities.impl.DeletedObjectImpl

/**
 * Date: 12/1/13
 * Time: 8:27 PM
 */
class DeletedObjectBuilderImplTest extends AbstractDeletedObjectBuilderTest {
    @Override
    def createEntity() {
        return new DeletedObjectImpl()
    }

    @Override
    def createBuilder(final entity) {
        return new DeletedObjectBuilderImpl((DeletedObject) entity)
    }
}
