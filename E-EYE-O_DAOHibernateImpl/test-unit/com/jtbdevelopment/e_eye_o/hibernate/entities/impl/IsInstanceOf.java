package com.jtbdevelopment.e_eye_o.hibernate.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * Date: 1/29/13
 * Time: 10:30 PM
 */
public class IsInstanceOf extends BaseMatcher<Class<? extends IdObject>> {
    private final Class<? extends IdObject> matchClass;

    public IsInstanceOf(final Class<? extends IdObject> matchClass) {
        this.matchClass = matchClass;
    }

    @Override
    public boolean matches(final Object o) {
        return matchClass.isAssignableFrom((Class) o);
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("Expecting assignable value to match " + matchClass.getSimpleName());
    }
}
