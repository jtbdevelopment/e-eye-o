package com.jtbdevelopment.e_eye_o.entities.validation;

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import org.springframework.beans.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

/**
 * Date: 12/13/12
 * Time: 10:13 PM
 */
public class ConsistentAppUserValidator implements ConstraintValidator<ConsistentAppUserCheck, AppUserOwnedObject> {
    @Override
    public void initialize(final ConsistentAppUserCheck consistentAppUserCheck) {

    }

    @Override
    public boolean isValid(final AppUserOwnedObject appUserOwnedObject, final ConstraintValidatorContext constraintValidatorContext) {
        PropertyDescriptor[] properties = BeanUtils.getPropertyDescriptors(appUserOwnedObject.getClass());
        for (PropertyDescriptor property : properties) {
            try {
                Class propertyType = property.getPropertyType();
                if (AppUserOwnedObject.class.isAssignableFrom(propertyType)) {
                    if (!((AppUserOwnedObject) property.getReadMethod().invoke(appUserOwnedObject)).getAppUser().equals(appUserOwnedObject.getAppUser()))
                        return false;
                } else if (Collection.class.isAssignableFrom(propertyType)) {
                    Collection<?> collection = (Collection<?>) property.getReadMethod().invoke(appUserOwnedObject);
                    for (Object item : collection) {
                        if (item instanceof AppUserOwnedObject) {
                            if (!((AppUserOwnedObject) item).getAppUser().equals(appUserOwnedObject.getAppUser())) {
                                return false;
                            }
                        }
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
