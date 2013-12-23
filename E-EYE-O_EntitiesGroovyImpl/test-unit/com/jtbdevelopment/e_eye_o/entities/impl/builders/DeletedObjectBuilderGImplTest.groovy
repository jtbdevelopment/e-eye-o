package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.impl.DeletedObjectGImpl

/**
 * Date: 12/1/13
 * Time: 8:27 PM
 */
class DeletedObjectBuilderGImplTest extends AbstractDeletedObjectBuilderTest {
    @Override
    def createEntity() {
        return new DeletedObjectGImpl()
    }

    @Override
    def createBuilder(final entity) {
        return new DeletedObjectBuilderGImpl(entity: entity)
    }
}
