package com.jtbdevelopment.e_eye_o.entities.impl.builders;

import com.jtbdevelopment.e_eye_o.entities.DeletedObject;
import com.jtbdevelopment.e_eye_o.entities.builders.DeletedObjectBuilder;

/**
 * Date: 3/9/13
 * Time: 11:51 AM
 */
public class DeletedObjectBuilderImpl extends AppUserOwnedObjectBuilderImpl<DeletedObject> implements DeletedObjectBuilder {
    public DeletedObjectBuilderImpl(final DeletedObject entity) {
        super(entity);
    }

    @Override
    public DeletedObjectBuilder withDeletedId(final String deletedId) {
        entity.setDeletedId(deletedId);
        return this;
    }
}
