package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.IdObject
import com.jtbdevelopment.e_eye_o.entities.PaginatedIdObjectList
import groovy.transform.CompileStatic

/**
 * Date: 12/21/13
 * Time: 6:44 PM
 */
@CompileStatic
class PaginatedIdObjectListGImpl implements PaginatedIdObjectList {
    boolean moreAvailable
    int pageSize
    int currentPage

    List<? extends IdObject> entities

    @Override
    void setEntities(final Collection<? extends IdObject> entities) {
        this.entities = new LinkedList<>(entities)
    }

    @Override
    List<? extends IdObject> getEntities() {
        entities.asImmutable();
    }

    @Override
    public boolean equals(final def o) {
        if (this.is(o)) return true;
        if (o == null || !(o instanceof PaginatedIdObjectList)) return false;

        final PaginatedIdObjectList that = (PaginatedIdObjectList) o;

        if (currentPage != that.getCurrentPage()) return false;
        if (moreAvailable != that.isMoreAvailable()) return false;
        if (pageSize != that.getPageSize()) return false;
        return this.entities == that.entities
    }

    @Override
    public int hashCode() {
        int result = (moreAvailable ? 1 : 0);
        result = 31 * result + currentPage;
        result = 31 * result + pageSize;
        result = 31 * result + entities.hashCode();
        return result;
    }
}

