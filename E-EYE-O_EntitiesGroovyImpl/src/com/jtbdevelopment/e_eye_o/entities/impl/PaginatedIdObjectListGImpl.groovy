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

    List<? extends IdObject> entities = []

    @Override
    void setEntities(final Collection<? extends IdObject> entities) {
        this.entities = new LinkedList<>(entities)
    }

    @Override
    List<? extends IdObject> getEntities() {
        entities.asImmutable();
    }
}

