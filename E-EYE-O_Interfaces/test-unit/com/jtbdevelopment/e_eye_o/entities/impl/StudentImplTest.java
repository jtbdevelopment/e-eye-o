package com.jtbdevelopment.e_eye_o.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.Photo;
import com.jtbdevelopment.e_eye_o.entities.Student;
import com.jtbdevelopment.e_eye_o.entities.validation.ConsistentAppUserValidator;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Date: 12/8/12
 * Time: 6:52 PM
 */
public class StudentImplTest extends AbstractAppUserOwnedObjectTest<StudentImpl> {

    public StudentImplTest() {
        super(StudentImpl.class);
    }


    @Test
    public void testConstructorsForNewObjects() {
        checkDefaultAndAppUserConstructorTests();
    }

    @Test
    public void testSetGetFirstName() throws Exception {
        checkStringSetGetsAndValidateNullsAndBlanksAsError("firstName", Student.STUDENT_FIRST_NAME_CANNOT_BE_NULL_OR_BLANK_ERROR);
    }

    @Test
    public void testFirstNameSize() throws Exception {
        checkStringSizeValidation("firstName", TOO_LONG_FOR_NAME, Student.STUDENT_FIRST_NAME_SIZE_ERROR);
    }

    @Test
    public void testSetLastName() throws Exception {
        checkStringSetGetsAndValidateNullsAsError("lastName", Student.STUDENT_LAST_NAME_CANNOT_BE_NULL_ERROR);
    }

    @Test
    public void testLastNameSize() throws Exception {
        checkStringSizeValidation("lastName", TOO_LONG_FOR_NAME, Student.STUDENT_LAST_NAME_SIZE_ERROR);
    }

    @Test
    public void testGetObservationsNonModifiable() {
        checkCollectionIsUnmodifiable(new StudentImpl().getObservations());
    }

    @Test
    public void testSetObservations() throws Exception {
        checkSetCollection(ObservationImpl.class, "observations", Student.STUDENT_OBSERVATIONS_CANNOT_CONTAIN_NULL_ERROR);
    }

    @Test
    public void testObservationsValidates() throws Exception {
        checkCollectionValidates(ObservationImpl.class, "observations", Student.STUDENT_OBSERVATIONS_CANNOT_CONTAIN_NULL_ERROR);
    }

    @Test
    public void testAddObservation() throws Exception {
        checkAddSingleEntityToCollection(ObservationImpl.class, "observation", "observations", Student.STUDENT_OBSERVATIONS_CANNOT_CONTAIN_NULL_ERROR);
    }

    @Test
    public void testAddObservations() throws Exception {
        checkAddManyEntitiesToCollection(ObservationImpl.class, "observations", Student.STUDENT_OBSERVATIONS_CANNOT_CONTAIN_NULL_ERROR);
    }

    @Test
    public void testRemoveObservation() throws Exception {
        checkRemoveSingleEntityToCollection(ObservationImpl.class, "observation", "observations", Student.STUDENT_OBSERVATIONS_CANNOT_CONTAIN_NULL_ERROR);
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

    @Test
    public void testSetStudentPhotoValidates() {
        Photo photo = new PhotoImpl(USER2);
        StudentImpl student = new StudentImpl(USER1);
        student.setStudentPhoto(photo);
        validateExpectingErrors(student, new String[]{ConsistentAppUserValidator.getSpecificErrorMessage(student, photo), ConsistentAppUserValidator.getGeneralErrorMessage(student)});
    }
}
