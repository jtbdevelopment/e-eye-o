package com.jtbdevelopment.e_eye_o.DAO.helpers

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.IdObject
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectFieldSettings
import com.jtbdevelopment.e_eye_o.reflection.IdObjectReflectionHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Date: 12/13/13
 * Time: 11:17 PM
 */
@Component
class FieldUpdateValidatorGImpl implements FieldUpdateValidator {
    @Autowired
    IdObjectReflectionHelper reflectionHelper

    @Override
    def <T extends IdObject> void removeInvalidFieldUpdates(
            final AppUser updatingUser, final T currentEntity, final T updatedEntity) {
        Map<String, IdObjectFieldSettings> settings = reflectionHelper.getAllFieldPreferences(currentEntity.class);
        settings.each { String key, IdObjectFieldSettings value ->
            switch (value.editableBy()) {
                case IdObjectFieldSettings.EditableBy.NONE:
                case IdObjectFieldSettings.EditableBy.CONTROLLED:
                case { value.editableBy() == IdObjectFieldSettings.EditableBy.ADMIN && !updatingUser.admin }:
                    updatedEntity."$key" = currentEntity."$key"
                    break;
            }
        }
    }
}
