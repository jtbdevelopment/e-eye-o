package com.jtbdevelopment.e_eye_o.entities;

import org.testng.annotations.Test;

import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Date: 12/9/12
 * Time: 9:47 AM
 */
public class ClassListImplTest extends AbstractIdObjectTest {

    @Test
    public void testConstructors() {
        checkDefaultAndAppUserConstructorTests(ClassListImpl.class);
    }

    @Test
    public void testSetDescription() throws Exception {
        checkStringSetGetsWithBlanksAndNullsAsException(ClassListImpl.class, "description");
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testGetStudentsNonModifiable() {
        checkGetSetIsUnmodifiable(new ClassListImpl().getStudents());
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testGetPhotosNonModifiable() {
        checkGetSetIsUnmodifiable(new ClassListImpl().getPhotos());
    }

    @Test
    public void testSetStudents() throws Exception {
        checkSetCollection(ClassListImpl.class, StudentImpl.class, "students");
    }

    @Test
    public void testSetStudentsValidates() {
        checkSetCollectionValidates(ClassListImpl.class, StudentImpl.class, "students");
    }

    @Test
    public void testAddStudent() throws Exception {

    }

    @Test
    public void testAddStudentsValidates() throws Exception {
        checkAddCollectionValidates(ClassListImpl.class, StudentImpl.class, "students");
    }

    @Test
    public void testAddStudents() throws Exception {
        checkAddCollection(ClassListImpl.class, StudentImpl.class, "students");
    }

    @Test
    public void testRemoveStudent() throws Exception {
        Set<StudentImpl> students = getSetOfFor(StudentImpl.class, USER1, 5);
        ClassList classList = new ClassListImpl(USER1).setStudents(students);
        StudentImpl student = students.iterator().next();
        classList.removeStudent(student);
        Set<Student> classStudents =classList.getStudents();
        assertEquals(students.size() -1, classStudents.size());
        assertFalse(classStudents.contains(student));
        students.remove(student);
        assertTrue(classStudents.containsAll(students));
    }

    @Test
    public void testSetPhotos() throws Exception {
        checkSetCollection(ClassListImpl.class, PhotoImpl.class, "photos");
    }

    @Test
    public void testSetPhotosValidates() {
        checkSetCollectionValidates(ClassListImpl.class, PhotoImpl.class, "photos");
    }

    @Test
    public void testAddPhoto() throws Exception {

    }

    @Test
    public void testAddPhotosValidates() throws Exception {
        checkAddCollectionValidates(ClassListImpl.class, PhotoImpl.class, "photos");
    }

    @Test
    public void testAddPhotos() throws Exception {

    }

    @Test
    public void testRemovePhoto() throws Exception {

    }
}
