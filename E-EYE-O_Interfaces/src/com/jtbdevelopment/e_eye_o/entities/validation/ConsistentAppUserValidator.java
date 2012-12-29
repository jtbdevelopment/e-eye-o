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
        boolean isValid = true;
        constraintValidatorContext.disableDefaultConstraintViolation();
        for (PropertyDescriptor property : properties) {
            try {
                Class propertyType = property.getPropertyType();
                if (AppUserOwnedObject.class.isAssignableFrom(propertyType)) {
                    final AppUserOwnedObject ownedObject = (AppUserOwnedObject) property.getReadMethod().invoke(appUserOwnedObject);
                    if (ownedObject != null && !ownedObject.getAppUser().equals(appUserOwnedObject.getAppUser())) {
                        isValid = false;
                        constraintValidatorContext.buildConstraintViolationWithTemplate(getSpecificErrorMessage(appUserOwnedObject, ownedObject)).addConstraintViolation();
                    }
                } else if (Collection.class.isAssignableFrom(propertyType)) {
                    Collection<?> collection = (Collection<?>) property.getReadMethod().invoke(appUserOwnedObject);
                    for (Object item : collection) {
                        if (item instanceof AppUserOwnedObject) {
                            final AppUserOwnedObject ownedObject = (AppUserOwnedObject) item;
                            if (!ownedObject.getAppUser().equals(appUserOwnedObject.getAppUser())) {
                                isValid = false;
                                constraintValidatorContext.buildConstraintViolationWithTemplate(getSpecificErrorMessage(appUserOwnedObject, ownedObject)).addConstraintViolation();
                            }
                        }
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return false;
            }
        }
        if (!isValid) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(getGeneralErrorMessage(appUserOwnedObject)).addConstraintViolation();
        }
        return isValid;
    }

    public static String getGeneralErrorMessage(final AppUserOwnedObject appUserOwnedObject) {
        return appUserOwnedObject.getClass().getSimpleName()
                + "["
                + appUserOwnedObject.getId()
                + "] has a mismatched owner somewhere.";
    }

    public static String getSpecificErrorMessage(final AppUserOwnedObject appUserOwnedObject, final AppUserOwnedObject ownedObject) {
        return appUserOwnedObject.getClass().getSimpleName()
                + "["
                + appUserOwnedObject.getId()
                + "] has a "
                + ownedObject.getClass().getSimpleName()
                + "["
                + ownedObject.getId()
                + "] with a different owner ["
                + appUserOwnedObject.getAppUser().getId()
                + " vs. "
                + ownedObject.getAppUser().getId()
                + "]";
    }
}
