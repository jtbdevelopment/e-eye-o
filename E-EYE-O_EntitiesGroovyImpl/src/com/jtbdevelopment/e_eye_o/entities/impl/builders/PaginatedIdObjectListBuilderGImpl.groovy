package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.IdObject
import com.jtbdevelopment.e_eye_o.entities.PaginatedIdObjectList
import com.jtbdevelopment.e_eye_o.entities.builders.PaginatedIdObjectListBuilder
import com.jtbdevelopment.e_eye_o.entities.impl.PaginatedIdObjectListGImpl
import groovy.transform.CompileStatic

/**
 * Date: 12/21/13
 * Time: 6:47 PM
 */
@CompileStatic
class PaginatedIdObjectListBuilderGImpl implements PaginatedIdObjectListBuilder {
    private PaginatedIdObjectListGImpl impl = new PaginatedIdObjectListGImpl()

    @Override
    PaginatedIdObjectListBuilder withMoreAvailable(final boolean moreAvailable) {
        impl.moreAvailable = moreAvailable
        return this
    }

    @Override
    PaginatedIdObjectListBuilder withCurrentPage(final int currentPage) {
        impl.currentPage = currentPage
        return this
    }

    @Override
    PaginatedIdObjectListBuilder withPageSize(final int pageSize) {
        impl.pageSize = pageSize
        return this
    }

    @Override
    PaginatedIdObjectListBuilder withEntities(final Collection<? extends IdObject> entities) {
        impl.setEntities(entities)
        return this
    }

    @Override
    PaginatedIdObjectList build() {
        return impl
    }
}
