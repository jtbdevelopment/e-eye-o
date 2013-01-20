package com.jtbdevelopment.e_eye_o.hibernate.entities.impl;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsSame;
import org.junit.internal.matchers.CombinableMatcher;

/**
 * Date: 1/20/13
 * Time: 5:23 PM
 */
public class IsEqualButNotTheSame<T> extends BaseMatcher<T> {
    private final CombinableMatcher<T> matcher;

    public IsEqualButNotTheSame(final T arg) {
        matcher = new CombinableMatcher<>(new IsEqual<>(arg)).and(new IsNot<>(new IsSame<>(arg)));
    }

    @Override
    public boolean matches(final Object o) {
        return matcher.matches(o);
    }

    @Override
    public void describeTo(final Description description) {
        matcher.describeTo(description);
    }
}
