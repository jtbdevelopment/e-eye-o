package com.jtbdevelopment.e_eye_o.entities.validation;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.impl.AppUserImpl;
import com.jtbdevelopment.e_eye_o.entities.impl.AppUserOwnedObjectImpl;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.testng.AssertJUnit.*;

/**
 * Date: 12/30/12
 * Time: 3:18 PM
 */
public class ConsistentAppUserValidatorTest {
    private Mockery context;
    private ConstraintValidatorContext validatorContext;
    private ConstraintValidatorContext.ConstraintViolationBuilder builder;
    final private ConsistentAppUserValidator check = new ConsistentAppUserValidator();
    final private static AppUserImpl USER1 = new AppUserImpl().setId("1");
    final private static AppUserImpl USER2 = new AppUserImpl().setId("2");
    private static int idCounter = 1;

    public static class LocalEntity extends AppUserOwnedObjectImpl {
        public LocalEntity() {
            super();
            setId("" + idCounter++);
        }

        public LocalEntity item1;
        public LocalEntity item2;
        public Set<LocalEntity> collection1 = new HashSet<>();
        public List<LocalEntity> collection2 = new ArrayList<>();

        public static LocalEntity createValidLE(boolean setLEOwner) {
            LocalEntity le = new LocalEntity();
            if (setLEOwner) {
                le.setAppUser(USER1);
            }
            le.item1 = new LocalEntity().setAppUser(USER1);
            le.item2 = new LocalEntity().setAppUser(USER1);
            le.collection1.add(le.item1);
            le.collection1.add(le.item2);
            le.collection2.add(le.item2);
            le.collection2.add(le.item1);
            return le;
        }

        public LocalEntity getItem1() {
            return item1;
        }

        public void setItem1(final LocalEntity item1) {
            this.item1 = item1;
        }

        public LocalEntity getItem2() {
            return item2;
        }

        public void setItem2(final LocalEntity item2) {
            this.item2 = item2;
        }

        public Set<LocalEntity> getCollection1() {
            return collection1;
        }

        public void setCollection1(final Set<LocalEntity> collection1) {
            this.collection1 = collection1;
        }

        public List<LocalEntity> getCollection2() {
            return collection2;
        }

        public void setCollection2(final List<LocalEntity> collection2) {
            this.collection2 = collection2;
        }
    }

    @BeforeMethod
    public void beforeTest() {
        context = new JUnit4Mockery();
        validatorContext = context.mock(ConstraintValidatorContext.class);
        builder = context.mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
    }

    @Test
    public void testInitializeEmpty() {
        check.initialize(null);
    }

    @Test
    public void testGeneralErrorMessageGenerator() {
        LocalEntity le = new LocalEntity();
        assertEquals("LocalEntity[" + le.getId() + "] has a mismatched owner somewhere.", ConsistentAppUserValidator.getGeneralErrorMessage(le));
    }

    @Test
    public void testExceptionErrorMessageGenerator() {
        final String message = "MESSAGE";
        RuntimeException re = new RuntimeException(message);
        LocalEntity le = LocalEntity.createValidLE(true);
        assertEquals("Error examining ownership of LocalEntity[" + le.getId() + "] with exception " + message, ConsistentAppUserValidator.getExceptionErrorMessage(le, re));
    }

    @Test
    public void testSpecificErrorMessageGeneratorOwnedObject() {
        LocalEntity le = new LocalEntity().setAppUser(USER1);
        LocalEntity oo = new LocalEntity().setAppUser(USER2);
        assertEquals("LocalEntity["
                + le.getId()
                + "] has a LocalEntity["
                + oo.getId()
                + "] with a different owner ["
                + USER1.getId()
                + " vs. "
                + USER2.getId()
                + "]",
                ConsistentAppUserValidator.getSpecificErrorMessage(le, oo));

    }

    @Test
    public void testSpecificErrorMessageGeneratorUnownedObject() {
        LocalEntity le = new LocalEntity().setAppUser(USER1);
        LocalEntity oo = new LocalEntity();
        assertEquals("LocalEntity["
                + le.getId()
                + "] has a LocalEntity["
                + oo.getId()
                + "] with a different owner ["
                + USER1.getId()
                + " vs. null]",
                ConsistentAppUserValidator.getSpecificErrorMessage(le, oo));

    }

    @Test
    public void testAnUnownedObjectPassesValidationRegardlessOfSubObjects() {
        LocalEntity le = LocalEntity.createValidLE(false);
        le.item2 = new LocalEntity().setAppUser(USER2);

        assertTrue(check.isValid(le, null));
    }

    @Test
    public void testACompletelyValidObject() {
        final LocalEntity le = LocalEntity.createValidLE(true);
        context.checking(new Expectations() {{
            one(validatorContext).disableDefaultConstraintViolation();
        }});

        assertTrue(check.isValid(le, validatorContext));
    }

    @Test
    public void testASimpleAppUserMismatch() {
        final LocalEntity le = LocalEntity.createValidLE(true);
        le.item2 = new LocalEntity().setAppUser(USER2);

        context.checking(new Expectations() {{
            one(validatorContext).disableDefaultConstraintViolation();
            one(validatorContext).buildConstraintViolationWithTemplate(with(equal(ConsistentAppUserValidator.getGeneralErrorMessage(le))));
            will(returnValue(builder));
            one(validatorContext).buildConstraintViolationWithTemplate(with(equal(ConsistentAppUserValidator.getSpecificErrorMessage(le, le.item2))));
            will(returnValue(builder));
            allowing(builder).addConstraintViolation();
        }});
        assertFalse(check.isValid(le, validatorContext));
    }

    @Test
    public void testASimpleMultipleAppUserMismatch() {
        final LocalEntity le = LocalEntity.createValidLE(true);
        le.item2 = new LocalEntity().setAppUser(USER2);
        le.item1 = new LocalEntity().setAppUser(USER2);

        context.checking(new Expectations() {{
            one(validatorContext).disableDefaultConstraintViolation();
            one(validatorContext).buildConstraintViolationWithTemplate(with(equal(ConsistentAppUserValidator.getGeneralErrorMessage(le))));
            will(returnValue(builder));
            one(validatorContext).buildConstraintViolationWithTemplate(with(equal(ConsistentAppUserValidator.getSpecificErrorMessage(le, le.item1))));
            will(returnValue(builder));
            one(validatorContext).buildConstraintViolationWithTemplate(with(equal(ConsistentAppUserValidator.getSpecificErrorMessage(le, le.item2))));
            will(returnValue(builder));
            allowing(builder).addConstraintViolation();
        }});
        assertFalse(check.isValid(le, validatorContext));
    }

    @Test
    public void testASetContainingMismatch() {
        final LocalEntity le = LocalEntity.createValidLE(true);
        final LocalEntity le3 = new LocalEntity().setAppUser(USER2);
        le.collection1.add(le3);

        context.checking(new Expectations() {{
            one(validatorContext).disableDefaultConstraintViolation();
            one(validatorContext).buildConstraintViolationWithTemplate(with(equal(ConsistentAppUserValidator.getGeneralErrorMessage(le))));
            will(returnValue(builder));
            one(validatorContext).buildConstraintViolationWithTemplate(with(equal(ConsistentAppUserValidator.getSpecificErrorMessage(le, le3))));
            will(returnValue(builder));
            allowing(builder).addConstraintViolation();
        }});
        assertFalse(check.isValid(le, validatorContext));
    }

    @Test
    public void testAnExceptionHandling() {
        final LocalEntity le = LocalEntity.createValidLE(true);
        final AppUser USER3 = context.mock(AppUser.class);
        final LocalEntity le3 = new LocalEntity().setAppUser(USER3);
        le.collection1.add(le3);
        final RuntimeException re = new RuntimeException();
        context.checking(new Expectations() {{
            one(USER3).getId();
            will(throwException(re));
            one(validatorContext).disableDefaultConstraintViolation();
            one(validatorContext).buildConstraintViolationWithTemplate(with(equal(ConsistentAppUserValidator.getGeneralErrorMessage(le))));
            will(returnValue(builder));
            one(validatorContext).buildConstraintViolationWithTemplate(with(equal(ConsistentAppUserValidator.getExceptionErrorMessage(le, re))));
            will(returnValue(builder));
            allowing(builder).addConstraintViolation();
        }});
        assertFalse(check.isValid(le, validatorContext));
    }
}

