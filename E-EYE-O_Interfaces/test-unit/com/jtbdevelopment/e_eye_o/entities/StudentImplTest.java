package com.jtbdevelopment.e_eye_o.entities;

import org.testng.annotations.Test;

/**
 * Date: 12/8/12
 * Time: 6:52 PM
 */
public class StudentImplTest extends AbstractIdObjectTest {

    @Test
    public void testSetGetFirstName() throws Exception {
        stringSetGetsWithBlanksAndNullsAsException(StudentImpl.class, "firstName");
    }

    @Test
    public void testSetLastName() throws Exception {
        stringSetGetsWithNullsSavedAsBlanks(StudentImpl.class, "lastName");
    }

    @Test
    public void testSetObservations() throws Exception {

    }

    @Test
    public void testAddObservation() throws Exception {

    }

    @Test
    public void testRemoveObservation() throws Exception {

    }

    @Test
    public void testSetStudentPhoto() throws Exception {

    }
}
