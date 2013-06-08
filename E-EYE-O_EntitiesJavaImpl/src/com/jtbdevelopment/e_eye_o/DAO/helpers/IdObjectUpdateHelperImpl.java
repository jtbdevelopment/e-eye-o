package com.jtbdevelopment.e_eye_o.DAO.helpers;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectFieldSettings;
import com.jtbdevelopment.e_eye_o.entities.reflection.IdObjectReflectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Date: 6/8/13
 * Time: 4:27 PM
 */
@Component
@SuppressWarnings("unused")
public class IdObjectUpdateHelperImpl implements IdObjectUpdateHelper {
    private static Logger logger = LoggerFactory.getLogger(IdObjectUpdateHelperImpl.class);

    @Autowired
    private IdObjectReflectionHelper reflectionHelper;


    @Override
    public <T extends IdObject> void validateUpdates(final AppUser updatingUser, final T currentEntity, final T updatedEntity) {
        Class<T> idObjectInterface = reflectionHelper.getIdObjectInterfaceForClass((Class<T>) updatedEntity.getClass());
        Map<String, Method> getters = reflectionHelper.getAllGetMethods(idObjectInterface);
        Map<String, Method> setters = reflectionHelper.getAllSetMethods(idObjectInterface);
        for (Map.Entry<String, Method> getter : getters.entrySet()) {
            String fieldName = getter.getKey();
            switch (getter.getValue().getAnnotation(IdObjectFieldSettings.class).editableBy()) {
                case NONE:
                case CONTROLLED:
                    removeFieldUpdate(fieldName, currentEntity, updatedEntity, getter.getValue(), setters.get(fieldName));
                    break;
                case ADMIN:
                    if (!updatingUser.isAdmin()) {
                        removeFieldUpdate(fieldName, currentEntity, updatedEntity, getter.getValue(), setters.get(fieldName));
                    }
                    break;
                case USER:
                    break;
            }
        }
    }

    private <T extends IdObject> void removeFieldUpdate(final String fieldName, final T currentEntity, final T updatedEntity, final Method get, final Method set) {
        try {
            set.invoke(updatedEntity, get.invoke(currentEntity));
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.warn("Unable to override " + fieldName, e);
            throw new RuntimeException("Unable to override " + fieldName, e);
        }
    }
}
