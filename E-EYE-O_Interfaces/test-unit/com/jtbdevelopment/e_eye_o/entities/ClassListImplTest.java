package com.jtbdevelopment.e_eye_o.entities;

import org.testng.annotations.Test;

/**
 * Date: 12/9/12
 * Time: 9:47 AM
 */
public class ClassListImplTest extends AbstractAppUserOwnedObjectTest<ClassListImpl> {

    public ClassListImplTest() {
        super(ClassListImpl.class);
    }

    @Test
    public void testConstructorsForNewObjects() {
        checkDefaultAndAppUserConstructorTests();
    }

    @Test
    public void testSetDescription() throws Exception {
        checkStringSetGetsAndValidateNullsAndBlanksAsError("description", ClassList.CLASS_LIST_DESCRIPTION_CANNOT_BE_BLANK_OR_NULL_ERROR);
    }

    @Test
    public void testDescriptionSize() throws Exception {
        checkStringSizeValidation("description", TOO_LONG_FOR_DESCRIPTION, ClassList.CLASS_LIST_DESCRIPTION_SIZE_ERROR);
    }

    @Test
    public void testGetStudentsNonModifiable() {
        checkCollectionIsUnmodifiable(new ClassListImpl().getStudents());
    }

    @Test
    public void testGetPhotosNonModifiable() {
        checkCollectionIsUnmodifiable(new ClassListImpl().getPhotos());
    }

    @Test
    public void testSetStudents() throws Exception {
        checkSetCollection(StudentImpl.class, "students", ClassList.CLASS_LIST_STUDENTS_CANNOT_CONTAIN_NULL_ERROR);
    }

    @Test
    public void testStudentsValidates() {
        checkCollectionValidates(StudentImpl.class, "students", ClassList.CLASS_LIST_STUDENTS_CANNOT_CONTAIN_NULL_ERROR);
    }

    @Test
    public void testAddStudent() throws Exception {
        checkAddSingleEntityToCollection(StudentImpl.class, "student", "students", ClassList.CLASS_LIST_STUDENTS_CANNOT_CONTAIN_NULL_ERROR);
    }

    @Test
    public void testAddStudents() throws Exception {
        checkAddManyEntitiesToCollection(StudentImpl.class, "students", ClassList.CLASS_LIST_STUDENTS_CANNOT_CONTAIN_NULL_ERROR);
    }

    @Test
    public void testRemoveStudent() throws Exception {
        checkRemoveSingleEntityToCollection(StudentImpl.class, "student", "students", ClassList.CLASS_LIST_STUDENTS_CANNOT_CONTAIN_NULL_ERROR);
    }

    @Test
    public void testSetPhotos() throws Exception {
        checkSetCollection(PhotoImpl.class, "photos", ClassList.CLASS_LIST_PHOTOS_CANNOT_CONTAIN_NULL_ERROR);
    }

    @Test
    public void testPhotosValidates() {
        checkCollectionValidates(PhotoImpl.class, "photos", ClassList.CLASS_LIST_PHOTOS_CANNOT_CONTAIN_NULL_ERROR);
    }

    @Test
    public void testAddPhoto() throws Exception {
        checkAddSingleEntityToCollection(PhotoImpl.class, "photo", "photos", ClassList.CLASS_LIST_PHOTOS_CANNOT_CONTAIN_NULL_ERROR);
    }

    @Test
    public void testAddPhotos() throws Exception {
        checkAddManyEntitiesToCollection(PhotoImpl.class, "photos", ClassList.CLASS_LIST_PHOTOS_CANNOT_CONTAIN_NULL_ERROR);
    }

    @Test
    public void testRemovePhoto() throws Exception {
        checkRemoveSingleEntityToCollection(PhotoImpl.class, "photo", "photos", ClassList.CLASS_LIST_PHOTOS_CANNOT_CONTAIN_NULL_ERROR);
    }
}
