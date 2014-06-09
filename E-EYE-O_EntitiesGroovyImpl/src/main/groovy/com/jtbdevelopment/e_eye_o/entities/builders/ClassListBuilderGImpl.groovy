package com.jtbdevelopment.e_eye_o.entities.builders

import com.jtbdevelopment.e_eye_o.entities.ClassList

/**
 * Date: 12/1/13
 * Time: 3:37 PM
 */
class ClassListBuilderGImpl extends ObservableBuilderGImpl<ClassList> implements ClassListBuilder {
    @Override
    ClassListBuilder withDescription(final String description) {
        entity.description = description
        return this
    }
}
