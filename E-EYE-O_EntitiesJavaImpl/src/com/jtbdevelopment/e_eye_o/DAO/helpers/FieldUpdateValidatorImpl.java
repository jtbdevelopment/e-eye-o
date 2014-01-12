package com.jtbdevelopment.e_eye_o.DAO.helpers;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectFieldSettings;
import com.jtbdevelopment.e_eye_o.reflection.IdObjectReflectionHelper;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Date: 6/8/13
 * Time: 4:27 PM
 */
@Component
@SuppressWarnings("unused")
public class FieldUpdateValidatorImpl implements FieldUpdateValidator {
    private static Logger logger = LoggerFactory.getLogger(FieldUpdateValidatorImpl.class);

    @Autowired
    protected IdObjectReflectionHelper reflectionHelper;


    @Override
    public <T extends IdObject> void removeInvalidFieldUpdates(final AppUser updatingUser, final T currentEntity, final T updatedEntity) {
        Map<String, IdObjectFieldSettings> settings = reflectionHelper.getAllFieldPreferences(updatedEntity.getClass());
        for (Map.Entry<String, IdObjectFieldSettings> setting : settings.entrySet()) {
            String fieldName = setting.getKey();
            switch (setting.getValue().editableBy()) {
                case NONE:
                case CONTROLLED:
                    removeFieldUpdate(fieldName, currentEntity, updatedEntity);
                    break;
                case ADMIN:
                    if (!updatingUser.isAdmin()) {
                        removeFieldUpdate(fieldName, currentEntity, updatedEntity);
                    }
                    break;
                case USER:
                    break;
            }
        }
    }

    private <T extends IdObject> void removeFieldUpdate(final String fieldName, final T currentEntity, final T updatedEntity) {
        try {
            PropertyUtils.setProperty(updatedEntity, fieldName, PropertyUtils.getProperty(currentEntity, fieldName));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.warn("Unable to override " + fieldName, e);
            throw new RuntimeException("Unable to override " + fieldName, e);
        }
    }
}
