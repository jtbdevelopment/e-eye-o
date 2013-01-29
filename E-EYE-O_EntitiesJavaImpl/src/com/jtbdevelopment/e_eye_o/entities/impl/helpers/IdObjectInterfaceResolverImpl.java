package com.jtbdevelopment.e_eye_o.entities.impl.helpers;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.helpers.IdObjectInterfaceResolver;
import org.springframework.stereotype.Service;

/**
 * Date: 1/28/13
 * Time: 10:39 PM
 */
@Service
@SuppressWarnings("unused")
public class IdObjectInterfaceResolverImpl implements IdObjectInterfaceResolver {
    @SuppressWarnings("unchecked")
    public <T extends IdObject> Class<T> getIdObjectInterfaceForClass(final Class<T> entityType) {
        Class<T> found = null;
        for (Class i : entityType.getInterfaces()) {
            if (IdObject.class.isAssignableFrom(i)) {
                found = (Class<T>) i;
                break;
            }
        }
        return found;
    }
}
