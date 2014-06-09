package com.jtbdevelopment.e_eye_o.entities.builders;

import com.jtbdevelopment.e_eye_o.entities.DeletedObject;

/**
 * Date: 3/9/13
 * Time: 11:19 AM
 */
public interface DeletedObjectBuilder extends AppUserOwnedObjectBuilder<DeletedObject> {
    DeletedObjectBuilder withDeletedId(final String deletedId);
}
