package com.jtbdevelopment.e_eye_o.entities.validation;

import com.sun.jmx.remote.internal.ArrayQueue;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.testng.annotations.Test;

import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Date: 12/29/12
 * Time: 4:27 PM
 */
public class NoNullsInCollectionValidatorTest {
    public static final String STRING_1 = "String1";
    public static final String STRING_2 = "String2";
    private final Mockery context = new JUnit4Mockery();
    private final ConstraintValidatorContext validatorContext = context.mock(ConstraintValidatorContext.class);
    final private NoNullsInCollectionValidator check = new NoNullsInCollectionValidator();

    @Test
    public void testReturnsValidForEmptyDifferentCollectionTypes() {
        assertTrue(check.isValid(new ArrayList<>(), validatorContext));
        assertTrue(check.isValid(new HashSet<>(), validatorContext));
        assertTrue(check.isValid(new ArrayQueue<>(5), validatorContext));
    }

    @Test
    public void testReturnsValidForDifferentCollectionsTypesWithValues() {
        assertTrue(check.isValid(Arrays.asList(STRING_1, STRING_2), validatorContext));
        ArrayQueue<String> q = new ArrayQueue<>(5);
        q.add(STRING_1);
        q.add(STRING_2);
        assertTrue(check.isValid(q, validatorContext));
        HashSet<String> s = new HashSet<>();
        s.add(STRING_1);
        s.add(STRING_2);
        assertTrue(check.isValid(s, validatorContext));
    }

    @Test
    public void testReturnsInvalidForDifferentCollectionsTypesWithNull() {
        assertFalse(check.isValid(Arrays.asList(STRING_1, null, STRING_2), validatorContext));
        ArrayQueue<String> q = new ArrayQueue<>(5);
        q.add(STRING_1);
        q.add(null);
        q.add(STRING_2);
        assertFalse(check.isValid(q, validatorContext));
        HashSet<String> s = new HashSet<>();
        s.add(STRING_1);
        s.add(null);
        s.add(STRING_2);
        assertFalse(check.isValid(s, validatorContext));
    }
}
