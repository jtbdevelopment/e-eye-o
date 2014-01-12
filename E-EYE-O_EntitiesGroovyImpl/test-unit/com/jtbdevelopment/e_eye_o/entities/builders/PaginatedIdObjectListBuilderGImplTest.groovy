package com.jtbdevelopment.e_eye_o.entities.builders

import com.jtbdevelopment.e_eye_o.entities.PaginatedIdObjectListGImpl

/**
 * Date: 12/22/13
 * Time: 11:09 AM
 */
class PaginatedIdObjectListBuilderGImplTest extends AbstractPaginatedIdObjectListBuilderTest {
    @Override
    def createEntity() {
        return new PaginatedIdObjectListGImpl()
    }

    @Override
    def createBuilder(final entity) {
        return new PaginatedIdObjectListBuilderGImpl(entity: entity)
    }
}
