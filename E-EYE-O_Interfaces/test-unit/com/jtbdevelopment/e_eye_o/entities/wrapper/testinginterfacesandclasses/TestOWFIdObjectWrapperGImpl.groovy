package com.jtbdevelopment.e_eye_o.entities.wrapper.testinginterfacesandclasses

import com.jtbdevelopment.e_eye_o.entities.IdObject
import com.jtbdevelopment.e_eye_o.entities.wrapper.IdObjectWrapper
import org.joda.time.DateTime

/**
 * Date: 12/8/13
 * Time: 3:59 PM
 */
public class TestOWFIdObjectWrapperGImpl<T extends IdObject> implements IdObjectWrapper<T>, IdObject {
    T wrapped;

    public TestOWFIdObjectWrapperGImpl(final T entityToWrap) {
        wrapped = entityToWrap;
    }

    @Override
    public String getId() {
        return wrapped.getId();
    }

    @Override
    public void setId(final String id) {
        wrapped.setId(id);
    }

    @Override
    public DateTime getModificationTimestamp() {
        return wrapped.getModificationTimestamp();
    }

    @Override
    public void setModificationTimestamp(final DateTime modificationTimestamp) {
        wrapped.setModificationTimestamp(modificationTimestamp);
    }

    @Override
    public String getSummaryDescription() {
        return wrapped.getSummaryDescription();
    }
}
