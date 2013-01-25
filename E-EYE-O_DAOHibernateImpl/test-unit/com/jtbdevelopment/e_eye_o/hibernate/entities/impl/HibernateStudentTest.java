package com.jtbdevelopment.e_eye_o.hibernate.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.ClassList;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.entities.Student;
import com.jtbdevelopment.e_eye_o.hibernate.entities.wrapper.HibernateIdObjectWrapperFactory;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;

/**
 * Date: 1/20/13
 * Time: 3:19 PM
 */
public class HibernateStudentTest {
    private Mockery context;
    private IdObjectFactory implFactory;
    private Student implStudent;
    private HibernateStudent hibernateStudent;
    private final String STRING_VALUE = "S";

    @BeforeMethod
    public void setUp() {
        context = new Mockery();
        implFactory = context.mock(IdObjectFactory.class);
        implStudent = context.mock(Student.class);
        hibernateStudent = new HibernateStudent(implStudent);
        @SuppressWarnings("unused")
        HibernateIdObjectWrapperFactory daoFactory = new HibernateIdObjectWrapperFactory(implFactory);
        context.checking(new Expectations() {{
            allowing(implFactory).newIdObject(Student.class);
            will(returnValue(implStudent));
        }});
    }

    @Test
    public void testDefaultConstructor() {
        assertSame(implStudent, new HibernateStudent().getWrapped());
    }

    @Test
    public void testWrappedConstructor() {
        final Student local = context.mock(Student.class, "local");
        assertSame(local, new HibernateStudent(local).getWrapped());
    }

    @Test
    public void testWrappedConstructorFromWrapped() {
        final Student local = context.mock(Student.class, "local");
        assertSame(local, new HibernateStudent(new HibernateStudent(local)).getWrapped());
    }

    @Test
    public void testGetClassLists() throws Exception {
        final Set<ClassList> cl = new HashSet<>(Arrays.asList(context.mock(ClassList.class)));
        context.checking(new Expectations() {{
            one(implStudent).getClassLists();
            will(returnValue(cl));
        }});

        assertSame(cl, hibernateStudent.getClassLists());
    }

    @Test
    public void testGetActiveClassLists() throws Exception {
        final Set<ClassList> cl = new HashSet<>(Arrays.asList(context.mock(ClassList.class)));
        context.checking(new Expectations() {{
            one(implStudent).getActiveClassLists();
            will(returnValue(cl));
        }});

        assertSame(cl, hibernateStudent.getActiveClassLists());
    }

    @Test
    public void testGetArchivedClassLists() throws Exception {
        final Set<ClassList> cl = new HashSet<>(Arrays.asList(context.mock(ClassList.class)));
        context.checking(new Expectations() {{
            one(implStudent).getArchivedClassLists();
            will(returnValue(cl));
        }});

        assertSame(cl, hibernateStudent.getArchivedClassLists());
    }

    @Test
    public void testSetClassLists() throws Exception {
        final Set<ClassList> cl = new HashSet<>(Arrays.asList(context.mock(ClassList.class)));
        context.checking(new Expectations() {{
            oneOf(implStudent).setClassLists(with(new IsEqualButNotTheSame<>(cl)));
            will(returnValue(implStudent));
        }});

        assertSame(hibernateStudent, hibernateStudent.setClassLists(cl));
    }

    @Test
    public void testAddClassList() throws Exception {
        final ClassList cl = context.mock(ClassList.class);
        context.checking(new Expectations() {{
            one(implStudent).addClassList(with(any(HibernateClassList.class)));
            will(returnValue(implStudent));
        }});

        assertSame(hibernateStudent, hibernateStudent.addClassList(cl));
    }

    @Test
    public void testAddClassLists() throws Exception {
        final ClassList cl = context.mock(ClassList.class);
        final Set<ClassList> cls = new HashSet<>(Arrays.asList(cl));
        context.checking(new Expectations() {{
            oneOf(implStudent).addClassLists(with(new IsEqualButNotTheSame<>(cls)));
            will(returnValue(implStudent));
        }});

        assertSame(hibernateStudent, hibernateStudent.addClassLists(cls));
    }

    @Test
    public void testRemoveClassList() throws Exception {
        final ClassList cl = context.mock(ClassList.class);
        context.checking(new Expectations() {{
            one(implStudent).removeClassList(with(any(HibernateClassList.class)));
            will(returnValue(implStudent));
        }});

        assertSame(hibernateStudent, hibernateStudent.removeClassList(cl));
    }

    @Test
    public void testGetFirstName() throws Exception {
        context.checking(new Expectations() {{
            one(implStudent).getFirstName();
            will(returnValue(STRING_VALUE));
        }});

        assertEquals(STRING_VALUE, hibernateStudent.getFirstName());
    }

    @Test
    public void testSetFirstName() throws Exception {
        context.checking(new Expectations() {{
            one(implStudent).setFirstName(STRING_VALUE);
            will(returnValue(implStudent));
        }});

        assertSame(hibernateStudent, hibernateStudent.setFirstName(STRING_VALUE));
    }

    @Test
    public void testGetLastName() throws Exception {
        context.checking(new Expectations() {{
            one(implStudent).getLastName();
            will(returnValue(STRING_VALUE));
        }});

        assertEquals(STRING_VALUE, hibernateStudent.getLastName());
    }

    @Test
    public void testSetLastName() throws Exception {
        context.checking(new Expectations() {{
            one(implStudent).setLastName(STRING_VALUE);
            will(returnValue(implStudent));
        }});

        assertSame(hibernateStudent, hibernateStudent.setLastName(STRING_VALUE));
    }
}
