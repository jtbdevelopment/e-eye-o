package com.jtbdevelopment.e_eye_o.hibernate.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.entities.Observation;
import com.jtbdevelopment.e_eye_o.hibernate.entities.wrapper.HibernateIdObjectWrapperFactory;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertSame;

/**
 * Date: 1/20/13
 * Time: 5:29 PM
 */
public class HibernateObservationTest {
    private Mockery context;
    private IdObjectFactory implFactory;
    private Observation implObservation;
    private HibernateObservation hibernateObservation;
    @SuppressWarnings("unused")  //  used if run standalone to init factory
    private HibernateIdObjectWrapperFactory daoFactory;

    @BeforeMethod
    public void setUp() {
        context = new Mockery();
        implFactory = context.mock(IdObjectFactory.class);
        implObservation = context.mock(Observation.class, "default");
        hibernateObservation = new HibernateObservation(implObservation);
        daoFactory = new HibernateIdObjectWrapperFactory(implFactory);
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

    }

    @Test
    public void testSetObservationSubject() throws Exception {

    }

    @Test
    public void testGetObservationTimestamp() throws Exception {

    }

    @Test
    public void testSetObservationTimestamp() throws Exception {

    }

    @Test
    public void testIsSignificant() throws Exception {

    }

    @Test
    public void testSetSignificant() throws Exception {

    }

    @Test
    public void testGetNeedsFollowUp() throws Exception {

    }

    @Test
    public void testSetNeedsFollowUp() throws Exception {

    }

    @Test
    public void testGetFollowUpReminder() throws Exception {

    }

    @Test
    public void testSetFollowUpReminder() throws Exception {

    }

    @Test
    public void testGetFollowUpObservation() throws Exception {

    }

    @Test
    public void testSetFollowUpObservation() throws Exception {

    }

    @Test
    public void testGetCategories() throws Exception {

    }

    @Test
    public void testSetCategories() throws Exception {

    }

    @Test
    public void testAddCategory() throws Exception {

    }

    @Test
    public void testAddCategories() throws Exception {

    }

    @Test
    public void testRemoveCategory() throws Exception {

    }

    @Test
    public void testGetComment() throws Exception {

    }

    @Test
    public void testSetComment() throws Exception {

    }
}
