package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.ClassList
import com.jtbdevelopment.e_eye_o.entities.IdObject
import com.jtbdevelopment.e_eye_o.entities.Student
import org.testng.Assert
import org.testng.annotations.Test

/**
 * Date: 11/26/13
 * Time: 11:13 PM
 */
abstract class AbstractStudentTest extends AbstractAppUserOwnedObjectTest {
    abstract ClassList createClassList(final AppUser user, boolean archived)

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
        testSetField("classLists", "ClassList", createClassList(null, false), createClassList(null, false), Student.STUDENT_CLASS_LISTS_CANNOT_CONTAIN_NULL)
    }

    @Test
    public void testGetActiveAndArchivedClassLists() {
        AppUser user = createAppUser()

        Set<ClassList> archived = [
                createClassList(user, true),
                createClassList(user, true),
                createClassList(user, true)
        ] as Set;
        Set<ClassList> active = [
                createClassList(user, false)
        ] as Set;

        Student student = createObjectUnderTest()
        student.appUser = user
        student.addClassLists(archived);
        student.addClassLists(active);

        assert archived == student.getArchivedClassLists()
        assert active == student.getActiveClassLists()

        try {
            student.activeClassLists.add(createClassList(null, false))
            fail("should have exceptioned")
        } catch (Exception e) {
            //
        }
        try {
            student.archivedClassLists.add(createClassList(null, false))
            fail("should have exceptioned")
        } catch (Exception e) {
            //
        }
    }

    @Test
    public void testSummaryDescription() {
        Student student = createObjectUnderTest()
        student.firstName = " Alfred ";
        student.lastName = " B ";
        Assert.assertEquals("Alfred B", student.getSummaryDescription());
    }

    @Test
    public void testSummaryDescriptionNoLast() {
        Student student = createObjectUnderTest()
        student.firstName = " Alfred ";
        student.lastName = " ";
        Assert.assertEquals("Alfred", student.getSummaryDescription());
    }
}
