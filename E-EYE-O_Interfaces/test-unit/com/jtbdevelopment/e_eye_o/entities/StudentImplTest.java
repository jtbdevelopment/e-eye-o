package com.jtbdevelopment.e_eye_o.entities;

import org.testng.annotations.Test;

import java.security.InvalidParameterException;

import static org.testng.Assert.assertEquals;

/**
 * Date: 12/8/12
 * Time: 6:52 PM
 */
public class StudentImplTest extends AbstractIdObjectTest {

    @Test
    public void testConstructors() {
        checkDefaultAndAppUserConstructorTests(StudentImpl.class);
    }

    @Test
    public void testSetGetFirstName() throws Exception {
        checkStringSetGetsWithBlanksAndNullsAsException(StudentImpl.class, "firstName");
    }

    @Test
    public void testSetLastName() throws Exception {
        checkStringSetGetsWithNullsSavedAsBlanks(StudentImpl.class, "lastName");
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testGetObservationsNonModifiable() {
        checkGetSetIsUnmodifiable(new StudentImpl().getObservations());
    }

    @Test
    public void testSetObservations() throws Exception {
        checkSetCollection(StudentImpl.class, ObservationImpl.class, "observations");
    }

    @Test
    public void testSetObservationsValidates() throws Exception {
        checkSetCollectionValidates(StudentImpl.class, ObservationImpl.class, "observations");
    }

    @Test
    public void testAddObservation() throws Exception {
    }

    @Test
    public void testAddObservationsValidates() throws Exception {
        checkAddCollectionValidates(StudentImpl.class, ObservationImpl.class, "observations");
    }

    @Test
    public void testAddObservations() throws Exception {
        checkAddCollection(StudentImpl.class, ObservationImpl.class, "observations");
    }

    @Test
    public void testRemoveObservation() throws Exception {

    }

    @Test
    public void testDefaultPhoto() {
        assertEquals(null, new StudentImpl().getStudentPhoto());
    }

    @Test
    public void testSetStudentPhoto() throws Exception {
        Photo photo = new PhotoImpl(USER1);
        StudentImpl student = new StudentImpl(USER1);
        assertEquals(photo, student.setStudentPhoto(photo).getStudentPhoto());
        assertEquals(null, student.setStudentPhoto(null).getStudentPhoto());
    }

    @Test(expectedExceptions = InvalidParameterException.class)
    public void testSetStudentPhotoValidates() {
        Photo photo = new PhotoImpl(USER1);
        StudentImpl student = new StudentImpl(USER2);
        student.setStudentPhoto(photo);
    }
}
