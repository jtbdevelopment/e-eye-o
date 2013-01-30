package com.jtbdevelopment.e_eye_o.hibernate.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.Photo;
import com.jtbdevelopment.e_eye_o.entities.Student;
import org.jmock.Expectations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;

/**
 * Date: 1/13/13
 * Time: 8:02 PM
 */
public class HibernatePhotoTest extends HibernateAbstractIdObjectTest {
    private Photo implPhoto;
    private Student implStudent;
    private HibernatePhoto hibernatePhoto;

    @BeforeMethod
    public void setUp() {
        super.setUp();
        implPhoto = context.mock(Photo.class, "default");
        hibernatePhoto = new HibernatePhoto(implPhoto);
        implStudent = context.mock(Student.class);
        context.checking(new Expectations() {{
            allowing(implFactory).newIdObject(Photo.class);
            will(returnValue(implPhoto));
        }});
    }

    @Test
    public void testDefaultConstructor() {
        assertSame(implPhoto, new HibernatePhoto().getWrapped());
    }

    @Test
    public void testWrappedConstructor() {
        final Photo local = context.mock(Photo.class, "local");
        assertSame(local, new HibernatePhoto(local).getWrapped());
    }

    @Test
    public void testWrappedConstructorFromWrapped() {
        final Photo local = context.mock(Photo.class, "local");
        assertSame(local, new HibernatePhoto(new HibernatePhoto(local)).getWrapped());
    }


    @Test
    public void testGetPhotoFor() throws Exception {
        context.checking(new Expectations() {{
            one(implPhoto).getPhotoFor();
            will(returnValue(implStudent));
        }});
        assertSame(implStudent, hibernatePhoto.getPhotoFor());
    }

    @Test
    public void testSetPhotoFor() throws Exception {
        context.checking(new Expectations() {{
            one(implPhoto).setPhotoFor(with(any(HibernateStudent.class)));
            will(returnValue(implPhoto));
        }});
        assertSame(hibernatePhoto, hibernatePhoto.setPhotoFor(implStudent));
    }

    @Test
    public void testGetDescription() throws Exception {
        context.checking(new Expectations() {{
            one(implPhoto).getDescription();
            will(returnValue(STRING_VALUE));
        }});
        assertEquals(STRING_VALUE, hibernatePhoto.getDescription());
    }

    @Test
    public void testSetDescription() throws Exception {
        context.checking(new Expectations() {{
            one(implPhoto).setDescription(STRING_VALUE);
            will(returnValue(implPhoto));
        }});
        assertSame(hibernatePhoto, hibernatePhoto.setDescription(STRING_VALUE));
    }

    @Test
    public void testGetTimestamp() throws Exception {
        context.checking(new Expectations() {{
            one(implPhoto).getTimestamp();
            will(returnValue(LOCALDATETIME_VALUE));
        }});
        assertEquals(LOCALDATETIME_VALUE, hibernatePhoto.getTimestamp());
    }

    @Test
    public void testSetTimestamp() throws Exception {
        context.checking(new Expectations() {{
            one(implPhoto).setTimestamp(LOCALDATETIME_VALUE);
            will(returnValue(implPhoto));
        }});
        assertSame(hibernatePhoto, hibernatePhoto.setTimestamp(LOCALDATETIME_VALUE));
    }
}
