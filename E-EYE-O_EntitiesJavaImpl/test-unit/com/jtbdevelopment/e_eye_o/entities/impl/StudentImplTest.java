package com.jtbdevelopment.e_eye_o.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.ClassList;
import com.jtbdevelopment.e_eye_o.entities.Photo;
import com.jtbdevelopment.e_eye_o.entities.Student;
import com.jtbdevelopment.e_eye_o.entities.validation.ConsistentAppUserValidator;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

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
    public void testDefaultPhoto() {
        assertEquals(null, new StudentImpl().getStudentPhoto());
    }

    @Test
    public void checkClassListsAreUnmodifiable() {
        StudentImpl s = new StudentImpl();
        checkCollectionIsUnmodifiable(s.getClassLists());
        checkCollectionIsUnmodifiable(s.getActiveClassLists());
        checkCollectionIsUnmodifiable(s.getArchivedClassLists());
    }

    @Test
    public void testSetClassLists() throws Exception {
        checkSetCollection(ClassListImpl.class, "classLists", Student.STUDENT_CLASS_LISTS_CANNOT_CONTAIN_NULL);
    }

    @Test
    public void testClassListsValidates() {
        checkCollectionValidates(ClassListImpl.class, "classLists", Student.STUDENT_CLASS_LISTS_CANNOT_CONTAIN_NULL);
    }

    @Test
    public void testClassListSizeValidation() {
        validateExpectingError(new StudentImpl(), Student.STUDENT_CLASS_LIST_SIZE_ERROR);
    }

    @Test
    public void testAddClassList() throws Exception {
        checkAddSingleEntityToCollection(ClassListImpl.class, "classList", "classLists", Student.STUDENT_CLASS_LISTS_CANNOT_CONTAIN_NULL);
    }

    @Test
    public void testAddClassLists() throws Exception {
        checkAddManyEntitiesToCollection(ClassListImpl.class, "classLists", Student.STUDENT_CLASS_LISTS_CANNOT_CONTAIN_NULL);
    }

    @Test
    public void testRemoveClassLists() throws Exception {
        checkRemoveSingleEntityToCollection(ClassListImpl.class, "classList", "classLists", Student.STUDENT_CLASS_LISTS_CANNOT_CONTAIN_NULL);
    }

    @Test
    public void testGetActiveAndArchivedClassLists() {
        Set<ClassList> archived = new HashSet<ClassList>() {
            {
                add((ClassList) new ClassListImpl().setArchived(true));
                add((ClassList) new ClassListImpl().setArchived(true));
                add((ClassList) new ClassListImpl().setArchived(true));
            }
        };
        Set<ClassList> active = new HashSet<ClassList>() {
            {
                add((ClassList) new ClassListImpl().setArchived(false));
            }
        };

        Student student = new StudentImpl();
        student.addClassLists(archived);
        student.addClassLists(active);

        Set<ClassList> activeGet = student.getActiveClassLists();
        Set<ClassList> archivedGet = student.getArchivedClassLists();
        assertTrue(active.containsAll(activeGet));
        assertTrue(activeGet.containsAll(active));
        assertTrue(archived.containsAll(archivedGet));
        assertTrue(archivedGet.containsAll(archived));
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