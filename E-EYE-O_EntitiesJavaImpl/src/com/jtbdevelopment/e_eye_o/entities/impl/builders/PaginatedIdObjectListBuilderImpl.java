package com.jtbdevelopment.e_eye_o.entities.impl.builders;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.PaginatedIdObjectList;
import com.jtbdevelopment.e_eye_o.entities.builders.PaginatedIdObjectListBuilder;
import com.jtbdevelopment.e_eye_o.entities.impl.PaginatedIdObjectListImpl;

import java.util.Collection;

/**
 * Date: 12/21/13
 * Time: 6:41 PM
 */
public class PaginatedIdObjectListBuilderImpl implements PaginatedIdObjectListBuilder {
    private PaginatedIdObjectListImpl impl = new PaginatedIdObjectListImpl();

    @Override
    public PaginatedIdObjectListBuilder withMoreAvailable(final boolean moreAvailable) {
        impl.setMoreAvailable(moreAvailable);
        return this;
    }

    @Override
    public PaginatedIdObjectListBuilder withCurrentPage(final int currentPage) {
        impl.setCurrentPage(currentPage);
        return this;
    }

    @Override
    public PaginatedIdObjectListBuilder withPageSize(final int pageSize) {
        impl.setPageSize(pageSize);
        return this;
    }

    @Override
    public PaginatedIdObjectListBuilder withEntities(final Collection<? extends IdObject> entities) {
        impl.setEntities(entities);
        return this;
    }

    @Override
    public PaginatedIdObjectList build() {
        return impl;
    }
}
