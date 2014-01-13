package com.jtbdevelopment.e_eye_o.hibernate.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.ClassList;
import org.jmock.Expectations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertSame;

/**
 * Date: 1/13/13
 * Time: 7:13 PM
 */
public class HibernateClassListTest extends HibernateAbstractIdObjectTest {
    private ClassList implClassList;
    private HibernateClassList hibernateClassList;

    @BeforeMethod
    public void setUp() {
        super.setUp();
        implClassList = context.mock(ClassList.class, "default");
        hibernateClassList = new HibernateClassList(implClassList);
        context.checking(new Expectations() {{
            allowing(implFactory).newIdObject(ClassList.class);
            will(returnValue(implClassList));
        }});
    }

    @Test
    public void testDefaultConstructor() {
        assertSame(implClassList, new HibernateClassList().getWrapped());
    }

    @Test
    public void testWrappedConstructor() {
        final ClassList local = context.mock(ClassList.class, "local");
        assertSame(local, new HibernateClassList(local).getWrapped());
    }

    @Test
    public void testWrappedConstructorFromWrapped() {
        final ClassList local = context.mock(ClassList.class, "local");
        assertSame(local, new HibernateClassList(new HibernateClassList(local)).getWrapped());
    }

    @Test
    public void testGetDescription() throws Exception {
        final String desc = "desc";
        context.checking(new Expectations() {{
            oneOf(implClassList).getDescription();
            will(returnValue(desc));
        }});

        assertSame(desc, hibernateClassList.getDescription());
    }

    @Test
    public void testSetDescription() throws Exception {
        final String desc = "desc";
        context.checking(new Expectations() {{
            oneOf(implClassList).setDescription(desc);
        }});

        hibernateClassList.setDescription(desc);
    }
}
