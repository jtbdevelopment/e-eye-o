package com.jtbdevelopment.e_eye_o.DAO;

import java.util.HashSet;
import java.util.Set;

/**
 * Date: 3/21/13
 * Time: 9:01 PM
 */
public class ChainedUpdateSetImpl<T> implements ReadWriteDAO.ChainedUpdateSet<T> {
    private final Set<T> deletedItems = new HashSet<>();
    private final Set<T> modifiedItems = new HashSet<>();

    public ChainedUpdateSetImpl(final Set<T> modifiedItems, final Set<T> deletedItems) {
        if (deletedItems != null)
            this.deletedItems.addAll(deletedItems);
        if (modifiedItems != null)
            this.modifiedItems.addAll(modifiedItems);
    }

    public ChainedUpdateSetImpl(final Iterable<ReadWriteDAO.ChainedUpdateSet<T>> sets) {
        for (ReadWriteDAO.ChainedUpdateSet<T> set : sets) {
            deletedItems.addAll(set.getDeletedItems());
            modifiedItems.addAll(set.getModifiedItems());
        }
    }

    public Set<T> getDeletedItems() {
        return deletedItems;
    }

    public Set<T> getModifiedItems() {
        return modifiedItems;
    }
}
