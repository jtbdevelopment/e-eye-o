package com.jtbdevelopment.e_eye_o.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.ClassList;
import com.jtbdevelopment.e_eye_o.entities.Student;
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
    public void checkClassListsAreUnmodifiable() {
        StudentImpl s = new StudentImpl(USER1);
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

    private ClassList createClassList(final AppUser user, boolean archived) {
        ClassList cl = new ClassListImpl(user);
        cl.setArchived(archived);
        return cl;
    }

    @Test
    public void testGetActiveAndArchivedClassLists() {
        Set<ClassList> archived = new HashSet<ClassList>() {
            {
                add(createClassList(USER1, true));
                add(createClassList(USER1, true));
                add(createClassList(USER1, true));
            }
        };
        Set<ClassList> active = new HashSet<ClassList>() {
            {
                add(createClassList(USER1, false));
            }
        };

        Student student = new StudentImpl(USER1);
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
    public void testSummaryDescription() {
        StudentImpl student = new StudentImpl(USER1);
        final String first = "Alfred";
        final String last = "B";
        student.setFirstName(" " + first + " ");
        student.setLastName("   " + last + " ");
        assertEquals(first + " " + last, student.getSummaryDescription());
    }

    @Test
    public void testSummaryDescriptionNoLast() {
        StudentImpl student = new StudentImpl(USER1);
        final String first = "Alfred";
        final String last = "";
        student.setFirstName(" " + first + " ");
        student.setLastName("   " + last + " ");
        assertEquals(first, student.getSummaryDescription());
    }
}
