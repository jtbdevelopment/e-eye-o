package com.jtbdevelopment.e_eye_o.entities.wrappers.testinginterfacesandclasses

import com.jtbdevelopment.e_eye_o.entities.IdObject
import com.jtbdevelopment.e_eye_o.entities.wrapper.IdObjectWrapper
import org.joda.time.DateTime

/**
 * Date: 12/8/13
 * Time: 4:00 PM
 */
//  Oddly defined to allow some bad injections
public class TestOWFAlternateWrapper implements IdObjectWrapper<IdObject>, TestOWFInterface {
    protected IdObject wrapped;

    public TestOWFAlternateWrapper(final IdObject entityToWrap) {
        wrapped = entityToWrap;
    }

    @Override
    public IdObject getWrapped() {
        return wrapped;
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
