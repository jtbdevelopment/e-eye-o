package com.jtbdevelopment.e_eye_o.entities.builders;

import com.jtbdevelopment.e_eye_o.entities.ClassList;

/**
 * Date: 3/9/13
 * Time: 11:16 AM
 */
public interface ClassListBuilder extends ObservableBuilder<ClassList> {
    ClassListBuilder withDescription(final String description);
}
