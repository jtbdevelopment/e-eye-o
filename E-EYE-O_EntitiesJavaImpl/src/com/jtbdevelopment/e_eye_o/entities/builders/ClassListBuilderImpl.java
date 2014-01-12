package com.jtbdevelopment.e_eye_o.entities.builders;

import com.jtbdevelopment.e_eye_o.entities.ClassList;

/**
 * Date: 3/9/13
 * Time: 12:06 PM
 */
public class ClassListBuilderImpl extends ObservableBulderImpl<ClassList> implements ClassListBuilder {
    public ClassListBuilderImpl(final ClassList entity) {
        super(entity);
    }

    @Override
    public ClassListBuilder withDescription(final String description) {
        entity.setDescription(description);
        return this;
    }
}
