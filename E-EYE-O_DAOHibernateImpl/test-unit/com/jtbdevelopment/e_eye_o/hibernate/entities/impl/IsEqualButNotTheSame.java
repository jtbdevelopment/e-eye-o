package com.jtbdevelopment.e_eye_o.hibernate.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.wrapper.IdObjectWrapper;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.core.CombinableMatcher;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsSame;

import java.util.HashSet;
import java.util.Set;

/**
 * Date: 1/20/13
 * Time: 5:23 PM
 */
public class IsEqualButNotTheSame<I extends IdObject, T extends Set<I>> extends BaseMatcher<T> {
    private final CombinableMatcher<T> matcher;
    private final IsEqual<T> equalMatcher;
    private final IsSame<T> sameMatcher;

    public IsEqualButNotTheSame(final T arg) {
        matcher = new CombinableMatcher<>(new IsEqual<>(arg)).and(new IsNot<>(new IsSame<>(arg)));
        equalMatcher = new IsEqual<>(arg);
        sameMatcher = new IsSame<>(arg);
    }

    @Override
    public boolean matches(final Object o) {
        if(o instanceof Set) {
            Set oAsS = (Set) o;
            Set local = new HashSet<>();
            for(Object i : oAsS) {
                if(i instanceof IdObjectWrapper) {
                    local.add(((IdObjectWrapper) i).getWrapped());
                } else {
                    local.add(i);
                }
            }
            return equalMatcher.matches(local) && !sameMatcher.matches(o);
        }
        return false;
    }

    @Override
    public void describeTo(final Description description) {
        matcher.describeTo(description);
    }
}
