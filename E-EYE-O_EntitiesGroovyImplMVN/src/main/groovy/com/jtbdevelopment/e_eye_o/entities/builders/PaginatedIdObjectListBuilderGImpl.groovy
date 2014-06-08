package com.jtbdevelopment.e_eye_o.entities.builders

import com.jtbdevelopment.e_eye_o.entities.IdObject
import com.jtbdevelopment.e_eye_o.entities.PaginatedIdObjectList
import groovy.transform.CompileStatic

/**
 * Date: 12/21/13
 * Time: 6:47 PM
 */
@CompileStatic
class PaginatedIdObjectListBuilderGImpl implements PaginatedIdObjectListBuilder {
    PaginatedIdObjectList entity

    @Override
    PaginatedIdObjectListBuilder withMoreAvailable(final boolean moreAvailable) {
        entity.moreAvailable = moreAvailable
        return this
    }

    @Override
    PaginatedIdObjectListBuilder withCurrentPage(final int currentPage) {
        entity.currentPage = currentPage
        return this
    }

    @Override
    PaginatedIdObjectListBuilder withPageSize(final int pageSize) {
        entity.pageSize = pageSize
        return this
    }

    @Override
    PaginatedIdObjectListBuilder withEntities(final Collection<? extends IdObject> entities) {
        entity.setEntities(entities)
        return this
    }

    @Override
    PaginatedIdObjectList build() {
        return entity
    }
}
