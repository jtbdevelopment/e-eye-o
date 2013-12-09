package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.ClassList
import com.jtbdevelopment.e_eye_o.entities.IdObject
import com.jtbdevelopment.e_eye_o.entities.Student
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.testng.Assert.assertEquals

/**
 * Date: 11/26/13
 * Time: 11:13 PM
 */
class StudentGImplTest extends AppUserOwnedObjectGImplTest {
    @BeforeMethod
    def setUp() {
        objectUnderTest = new StudentGImpl()
    }

    @Test
    public void testFirstName() throws Exception {
        testStringFieldExpectingErrorForNullOrBlank("firstName", "", Student.STUDENT_FIRST_NAME_CANNOT_BE_NULL_OR_BLANK_ERROR)
        testStringFieldSize("firstName", IdObject.MAX_NAME_SIZE, Student.STUDENT_FIRST_NAME_SIZE_ERROR)
    }

    @Test
    public void testLastName() throws Exception {
        testStringFieldExpectingErrorForNullOnly("lastName", "", Student.STUDENT_LAST_NAME_CANNOT_BE_NULL_ERROR)
        testStringFieldSize("lastName", IdObject.MAX_NAME_SIZE, Student.STUDENT_LAST_NAME_SIZE_ERROR)
    }

    @Test
    public void testClassLists() throws Exception {
        testSetField("classLists", "ClassList", new ClassListGImpl(), new ClassListGImpl(), Student.STUDENT_CLASS_LISTS_CANNOT_CONTAIN_NULL)
    }

    private ClassList createClassList(final AppUser user, boolean archived) {
        ClassList cl = new ClassListGImpl(appUser: user);
        cl.setArchived(archived);
        return cl;
    }

    @Test
    public void testGetActiveAndArchivedClassLists() {
        AppUser user = new AppUserGImpl()

        Set<ClassList> archived = [
                createClassList(user, true),
                createClassList(user, true),
                createClassList(user, true)
        ] as Set;
        Set<ClassList> active = [
                createClassList(user, false)
        ] as Set;

        Student student = new StudentGImpl(appUser: user);
        student.addClassLists(archived);
        student.addClassLists(active);

        assert archived == student.getArchivedClassLists()
        assert active == student.getActiveClassLists()

        try {
            student.activeClassLists.add(new ClassListGImpl())
            fail("should have exceptioned")
        } catch (Exception e) {
            //
        }
        try {
            student.archivedClassLists.add(new ClassListGImpl())
            fail("should have exceptioned")
        } catch (Exception e) {
            //
        }
    }

    @Test
    public void testSummaryDescription() {
        StudentGImpl student = new StudentGImpl();
        student.firstName = " Alfred ";
        student.lastName = " B ";
        assertEquals("Alfred B", student.getSummaryDescription());
    }

    @Test
    public void testSummaryDescriptionNoLast() {
        StudentGImpl student = new StudentGImpl();
        student.firstName = " Alfred ";
        student.lastName = " ";
        assertEquals("Alfred ", student.getSummaryDescription());
    }
}
