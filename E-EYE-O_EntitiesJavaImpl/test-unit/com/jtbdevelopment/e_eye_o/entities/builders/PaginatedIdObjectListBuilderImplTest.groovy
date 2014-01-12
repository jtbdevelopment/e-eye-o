package com.jtbdevelopment.e_eye_o.entities.builders

import com.jtbdevelopment.e_eye_o.entities.PaginatedIdObjectList
import com.jtbdevelopment.e_eye_o.entities.PaginatedIdObjectListImpl

/**
 * Date: 12/22/13
 * Time: 11:09 AM
 */
class PaginatedIdObjectListBuilderImplTest extends AbstractPaginatedIdObjectListBuilderTest {
    @Override
    def createEntity() {
        return new PaginatedIdObjectListImpl()
    }

    @Override
    def createBuilder(final entity) {
        return new PaginatedIdObjectListBuilderImpl((PaginatedIdObjectList) entity)
    }
}
