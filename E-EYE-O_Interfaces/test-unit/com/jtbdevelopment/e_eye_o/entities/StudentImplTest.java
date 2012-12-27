package com.jtbdevelopment.e_eye_o.entities;

import org.testng.annotations.Test;

import java.security.InvalidParameterException;

import static org.testng.Assert.assertEquals;

/**
 * Date: 12/8/12
 * Time: 6:52 PM
 */
public class StudentImplTest extends AbstractAppUserOwnedObjectTest {

    @Test
    public void testConstructors() {
        checkDefaultAndAppUserConstructorTests(StudentImpl.class);
    }

    @Test
    public void testSetGetFirstName() throws Exception {
        checkStringSetGetsAndValidateNullsAndBlanksAsError(StudentImpl.class, "firstName", Student.STUDENT_FIRST_NAME_CANNOT_BE_NULL_OR_BLANK_ERROR);
    }

    @Test
    public void testFirstNameSize() throws Exception {
        checkStringSizeValidation(StudentImpl.class, "firstName", TOO_LONG_FOR_NAME, Student.STUDENT_FIRST_NAME_SIZE_ERROR);
    }

    @Test
    public void testSetLastName() throws Exception {
        checkStringSetGetsAndValidateNullsAsError(StudentImpl.class, "lastName", Student.STUDENT_LAST_NAME_CANNOT_BE_NULL_ERROR);
    }

    @Test
    public void testLastNameSize() throws Exception {
        checkStringSizeValidation(StudentImpl.class, "lastName", TOO_LONG_FOR_NAME, Student.STUDENT_LAST_NAME_SIZE_ERROR);
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
