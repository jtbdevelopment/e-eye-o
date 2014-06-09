package com.jtbdevelopment.e_eye_o.entities;

import java.util.Collection;

/**
 * Date: 12/21/13
 * Time: 3:45 PM
 * <p/>
 * A data class for serialization of paginated results
 * Is not expected to be stored or validated
 */
public interface PaginatedIdObjectList {
    boolean isMoreAvailable();

    void setMoreAvailable(final boolean moreAvailable);

    int getCurrentPage();

    void setCurrentPage(final int currentPage);

    int getPageSize();

    void setPageSize(final int pageSize);

    Collection<? extends IdObject> getEntities();

    void setEntities(final Collection<? extends IdObject> entities);
}
