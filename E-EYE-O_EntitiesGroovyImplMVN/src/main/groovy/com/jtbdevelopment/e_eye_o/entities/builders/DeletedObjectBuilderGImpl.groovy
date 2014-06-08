package com.jtbdevelopment.e_eye_o.entities.builders

import com.jtbdevelopment.e_eye_o.entities.DeletedObject

/**
 * Date: 12/1/13
 * Time: 3:39 PM
 */
class DeletedObjectBuilderGImpl extends AppUserOwnedObjectBuilderGImpl<DeletedObject> implements DeletedObjectBuilder {
    @Override
    DeletedObjectBuilder withDeletedId(final String deletedId) {
        entity.deletedId = deletedId
        return this
    }
}
