package com.jtbdevelopment.e_eye_o.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.PaginatedIdObjectList;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 12/21/13
 * Time: 6:34 PM
 */
public class PaginatedIdObjectListImpl implements PaginatedIdObjectList {
    private boolean moreAvailable;
    private int currentPage = 0;
    private int pageSize = 0;
    private List<? extends IdObject> entities = new LinkedList<>();

    @Override
    public boolean isMoreAvailable() {
        return moreAvailable;
    }

    @Override
    public void setMoreAvailable(final boolean moreAvailable) {
        this.moreAvailable = moreAvailable;
    }

    @Override
    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    public void setCurrentPage(final int currentPage) {
        this.currentPage = currentPage;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public void setPageSize(final int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public Collection<? extends IdObject> getEntities() {
        return new LinkedList<>(entities);
    }

    @Override
    public void setEntities(final Collection<? extends IdObject> entities) {
        this.entities = new LinkedList<>(entities);
    }
}
