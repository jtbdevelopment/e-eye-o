package com.jtbdevelopment.e_eye_o.entities.builders;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.PaginatedIdObjectList;

import java.util.Collection;

/**
 * Date: 12/21/13
 * Time: 3:51 PM
 */
public interface PaginatedIdObjectListBuilder {
    PaginatedIdObjectListBuilder withMoreAvailable(final boolean moreAvailable);

    PaginatedIdObjectListBuilder withCurrentPage(final int currentPage);

    PaginatedIdObjectListBuilder withPageSize(final int pageSize);

    PaginatedIdObjectListBuilder withEntities(final Collection<? extends IdObject> entities);

    PaginatedIdObjectList build();
}
