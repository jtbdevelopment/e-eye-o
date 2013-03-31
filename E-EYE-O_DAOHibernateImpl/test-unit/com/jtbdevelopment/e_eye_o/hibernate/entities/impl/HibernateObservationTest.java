package com.jtbdevelopment.e_eye_o.hibernate.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.ClassList;
import com.jtbdevelopment.e_eye_o.entities.Observation;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import com.jtbdevelopment.e_eye_o.entities.Student;
import org.jmock.Expectations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;

/**
 * Date: 1/20/13
 * Time: 5:29 PM
 */
public class HibernateObservationTest extends HibernateAbstractIdObjectTest {
    private Observation implObservation;
    private HibernateObservation hibernateObservation;

    @BeforeMethod
    public void setUp() {
        super.setUp();
        implObservation = context.mock(Observation.class, "default");
        hibernateObservation = new HibernateObservation(implObservation);
        context.checking(new Expectations() {{
            allowing(implFactory).newIdObject(Observation.class);
            will(returnValue(implObservation));
        }});
    }

    @Test
    public void testDefaultConstructor() {
        assertSame(implObservation, new HibernateObservation().getWrapped());
    }

    @Test
    public void testWrappedConstructor() {
        final Observation local = context.mock(Observation.class, "local");
        assertSame(local, new HibernateObservation(local).getWrapped());
    }

    @Test
    public void testWrappedConstructorFromWrapped() {
        final Observation local = context.mock(Observation.class, "local");
        assertSame(local, new HibernateObservation(new HibernateObservation(local)).getWrapped());
    }

    @Test
    public void testGetObservationSubject() throws Exception {
        final Student s = context.mock(Student.class);
        context.checking(new Expectations() {{
            one(implObservation).getObservationSubject();
            will(returnValue(s));
        }});
        assertSame(s, hibernateObservation.getObservationSubject());
    }

    @Test
    public void testSetObservationSubject() throws Exception {
        final ClassList cl = context.mock(ClassList.class);
        context.checking(new Expectations() {{
            one(implObservation).setObservationSubject(with(any(HibernateClassList.class)));
        }});
        hibernateObservation.setObservationSubject(cl);
    }

    @Test
    public void testGetObservationTimestamp() throws Exception {
        context.checking(new Expectations() {{
            one(implObservation).getObservationTimestamp();
            will(returnValue(LOCALDATETIME_VALUE));
        }});
        assertEquals(LOCALDATETIME_VALUE, hibernateObservation.getObservationTimestamp());
    }

    @Test
    public void testSetObservationTimestamp() throws Exception {
        context.checking(new Expectations() {{
            one(implObservation).setObservationTimestamp(LOCALDATETIME_VALUE);
        }});
        hibernateObservation.setObservationTimestamp(LOCALDATETIME_VALUE);
    }

    @Test
    public void testIsSignificant() throws Exception {
        context.checking(new Expectations() {{
            one(implObservation).isSignificant();
            will(returnValue(true));
        }});
        assertEquals(true, hibernateObservation.isSignificant());
    }

    @Test
    public void testSetSignificant() throws Exception {
        context.checking(new Expectations() {{
            one(implObservation).setSignificant(false);
        }});
        hibernateObservation.setSignificant(false);
    }

    @Test
    public void testGetNeedsFollowUp() throws Exception {
        context.checking(new Expectations() {{
            one(implObservation).isFollowUpNeeded();
            will(returnValue(false));
        }});
        assertEquals(false, hibernateObservation.isFollowUpNeeded());
    }

    @Test
    public void testSetNeedsFollowUp() throws Exception {
        context.checking(new Expectations() {{
            one(implObservation).setFollowUpNeeded(true);
        }});
        hibernateObservation.setFollowUpNeeded(true);
    }

    @Test
    public void testGetFollowUpReminder() throws Exception {
        context.checking(new Expectations() {{
            one(implObservation).getFollowUpReminder();
            will(returnValue(LOCALDATE_VALUE));
        }});
        assertEquals(LOCALDATE_VALUE, hibernateObservation.getFollowUpReminder());
    }

    @Test
    public void testSetFollowUpReminder() throws Exception {
        context.checking(new Expectations() {{
            one(implObservation).setFollowUpReminder(LOCALDATE_VALUE);
        }});
        hibernateObservation.setFollowUpReminder(LOCALDATE_VALUE);
    }

    @Test
    public void testGetFollowUpForObservation() throws Exception {
        final HibernateObservation fu = new HibernateObservation();
        context.checking(new Expectations() {{
            one(implObservation).getFollowUpForObservation();
            will(returnValue(fu));
        }});

        assertSame(fu, hibernateObservation.getFollowUpForObservation());
    }

    @Test
    public void testSetFollowUpForObservation() throws Exception {
        final Observation fu = context.mock(Observation.class, "local");
        context.checking(new Expectations() {{
            one(implObservation).setFollowUpForObservation(with(any(HibernateObservation.class)));
        }});

        hibernateObservation.setFollowUpForObservation(fu);
    }

    @Test
    public void testGetCategories() throws Exception {
        final Set<ObservationCategory> ocs = new HashSet<>(Arrays.asList(context.mock(ObservationCategory.class)));
        context.checking(new Expectations() {{
            one(implObservation).getCategories();
            will(returnValue(ocs));
        }});
        assertSame(ocs, hibernateObservation.getCategories());
    }

    @Test
    public void testSetCategories() throws Exception {
        final Set<ObservationCategory> ocs = new HashSet<>(Arrays.asList(context.mock(ObservationCategory.class)));
        context.checking(new Expectations() {{
            one(implObservation).setCategories(with(new IsEqualButNotTheSame<>(ocs)));
        }});
        hibernateObservation.setCategories(ocs);
    }

    @Test
    public void testAddCategory() throws Exception {
        final ObservationCategory ocs = context.mock(ObservationCategory.class);
        context.checking(new Expectations() {{
            one(implObservation).addCategory(with(any(HibernateObservationCategory.class)));
        }});
        hibernateObservation.addCategory(ocs);
    }

    @Test
    public void testAddCategories() throws Exception {
        final Set<ObservationCategory> ocs = new HashSet<>(Arrays.asList(context.mock(ObservationCategory.class)));
        context.checking(new Expectations() {{
            one(implObservation).addCategories(with(new IsEqualButNotTheSame<>(ocs)));
        }});
        hibernateObservation.addCategories(ocs);
    }

    @Test
    public void testRemoveCategory() throws Exception {
        final ObservationCategory ocs = context.mock(ObservationCategory.class);
        context.checking(new Expectations() {{
            one(implObservation).removeCategory(ocs);
        }});
        hibernateObservation.removeCategory(ocs);
    }

    @Test
    public void testGetComment() throws Exception {
        context.checking(new Expectations() {{
            one(implObservation).getComment();
            will(returnValue(STRING_VALUE));
        }});
        assertEquals(STRING_VALUE, hibernateObservation.getComment());
    }

    @Test
    public void testSetComment() throws Exception {
        context.checking(new Expectations() {{
            one(implObservation).setComment(STRING_VALUE);
        }});
        hibernateObservation.setComment(STRING_VALUE);
    }
}
