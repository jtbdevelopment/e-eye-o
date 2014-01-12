package com.jtbdevelopment.e_eye_o.entities

import groovy.transform.CompileStatic

/**
 * Date: 11/30/13
 * Time: 12:26 PM
 */
@CompileStatic
class DeletedObjectGImpl extends AppUserOwnedObjectGImpl implements DeletedObject {
    String deletedId = "";

    @Override
    public void setDeletedId(final String deletedId) {
        if (this.deletedId?.length() > 0 && this.deletedId != deletedId) {
            throw new IllegalStateException("Cannot change deletedId once set, create a new object");
        }
        this.deletedId = deletedId
    }

    @Override
    public String getSummaryDescription() {
        return deletedId;
    }
}
