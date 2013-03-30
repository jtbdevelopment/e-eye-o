package com.jtbdevelopment.e_eye_o.hibernate.DAO;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * Date: 2/13/13
 * Time: 12:34 AM
 */
public class ModificationTimestampGenerator extends EmptyInterceptor {

    private static final String MODIFICATION_TIMESTAMP = "modificationTimestamp";

    @Override
    public boolean onFlushDirty(final Object entity, final Serializable id, final Object[] currentState, final Object[] previousState, final String[] propertyNames, final Type[] types) {
        return updateModificationTimestamp(entity, propertyNames, currentState) || super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
    }

    @Override
    public boolean onSave(final Object entity, final Serializable id, final Object[] state, final String[] propertyNames, final Type[] types) {
        return updateModificationTimestamp(entity, propertyNames, state) || super.onSave(entity, id, state, propertyNames, types);
    }

    private boolean updateModificationTimestamp(final Object entity, final String[] propertyNames, final Object[] state) {
        if (entity instanceof IdObject) {
            for (int i = 0; i < propertyNames.length; ++i) {
                if (MODIFICATION_TIMESTAMP.equals(propertyNames[i])) {
                    state[i] = new DateTime();
                    return true;
                }
            }
        }
        return false;
    }

}
