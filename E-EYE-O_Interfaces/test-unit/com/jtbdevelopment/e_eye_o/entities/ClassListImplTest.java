package com.jtbdevelopment.e_eye_o.entities;

import org.testng.annotations.Test;

/**
 * Date: 12/9/12
 * Time: 9:47 AM
 */
public class ClassListImplTest extends AbstractIdObjectTest {
    @Test
    public void testSetDescription() throws Exception {
        stringSetGetsWithBlanksAndNullsAsException(ClassListImpl.class, "description");
    }

    @Test
    public void testSetStudents() throws Exception {

    }

    @Test
    public void testAddStudent() throws Exception {

    }

    @Test
    public void testRemoveStudent() throws Exception {

    }

    @Test
    public void testSetPhotos() throws Exception {

    }

    @Test
    public void testAddPhoto() throws Exception {

    }

    @Test
    public void testRemovePhoto() throws Exception {

    }
}
