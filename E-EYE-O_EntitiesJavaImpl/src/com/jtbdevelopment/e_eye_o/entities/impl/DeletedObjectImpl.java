package com.jtbdevelopment.e_eye_o.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.DeletedObject;
import org.springframework.util.StringUtils;

/**
 * Date: 2/16/13
 * Time: 4:26 PM
 */
public class DeletedObjectImpl extends AppUserOwnedObjectImpl implements DeletedObject {
    private String deleteId = "";

    DeletedObjectImpl(final AppUser appUser) {
        super(appUser);
    }

    @Override
    public String getDeletedId() {
        return deleteId;
    }

    @Override
    public DeletedObject setDeletedId(final String deletedId) {
        if (StringUtils.hasLength(this.deleteId) && !this.deleteId.equals(deletedId)) {
            throw new IllegalStateException("Cannot change deletedId once set, create a new object");
        }
        this.deleteId = deletedId;
        return this;
    }
}
