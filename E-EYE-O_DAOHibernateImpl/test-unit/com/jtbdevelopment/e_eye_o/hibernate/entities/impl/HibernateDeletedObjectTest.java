package com.jtbdevelopment.e_eye_o.hibernate.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.DeletedObject;
import org.jmock.Expectations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;

/**
 * Date: 2/17/13
 * Time: 7:45 PM
 */
public class HibernateDeletedObjectTest extends HibernateAbstractIdObjectTest {
    private DeletedObject implDeletedObject;
    private HibernateDeletedObject hibernateDeletedObject;

    @BeforeMethod
    public void setUp() {
        super.setUp();
        implDeletedObject = context.mock(DeletedObject.class, "default");
        hibernateDeletedObject = new HibernateDeletedObject(implDeletedObject);
        context.checking(new Expectations() {{
            allowing(implFactory).newIdObject(DeletedObject.class);
            will(returnValue(implDeletedObject));
        }});
    }

    @Test
    public void testDefaultConstructor() {
        assertSame(implDeletedObject, new HibernateDeletedObject().getWrapped());
    }

    @Test
    public void testWrappedConstructor() {
        final DeletedObject local = context.mock(DeletedObject.class, "local");
        assertSame(local, new HibernateDeletedObject(local).getWrapped());
    }

    @Test
    public void testWrappedConstructorFromWrapped() {
        final DeletedObject local = context.mock(DeletedObject.class, "local");
        assertSame(local, new HibernateDeletedObject(new HibernateDeletedObject(local)).getWrapped());
    }

    @Test
    public void testGetDeletedId() throws Exception {
        context.checking(new Expectations() {{
            one(implDeletedObject).getDeletedId();
            will(returnValue(STRING_VALUE));
        }});
        assertEquals(STRING_VALUE, hibernateDeletedObject.getDeletedId());
    }

    @Test
    public void testSetDeletedId() throws Exception {
        context.checking(new Expectations() {{
            one(implDeletedObject).setDeletedId(STRING_VALUE);
        }});
        hibernateDeletedObject.setDeletedId(STRING_VALUE);

    }
}
