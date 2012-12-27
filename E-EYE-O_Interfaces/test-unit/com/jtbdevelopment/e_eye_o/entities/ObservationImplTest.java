package com.jtbdevelopment.e_eye_o.entities;

import org.joda.time.LocalDateTime;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Date: 12/8/12
 * Time: 8:08 PM
 */
public class ObservationImplTest extends AbstractAppUserOwnedObjectTest {
    @Test
    public void testConstructors() {
        checkDefaultAndAppUserConstructorTests(ClassListImpl.class);
    }

    @Test
    public void testNewObservationDefaultTimestamp() throws Exception {
        LocalDateTime before = new LocalDateTime();
        Thread.sleep(1);
        ObservationImpl observation = new ObservationImpl();
        Thread.sleep(1);
        LocalDateTime after = new LocalDateTime();
        assertTrue(before.compareTo(observation.getObservationTimestamp()) < 0);
        assertTrue(after.compareTo(observation.getObservationTimestamp()) > 0);
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testGetPhotosNonModifiable() {
        checkGetSetIsUnmodifiable(new ObservationImpl().getPhotos());
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testGetCategoriesNonModifiable() {
        checkGetSetIsUnmodifiable(new ObservationImpl().getCategories());
    }

    @Test
    public void testSetObservationDate() throws Exception {
        LocalDateTime timestamp = new LocalDateTime().minusDays(1);
        assertEquals(timestamp, new ObservationImpl().setObservationTimestamp(timestamp).getObservationTimestamp());
    }

    @Test
    public void testSetSignificant() throws Exception {
        checkBooleanDefaultAndSetGet(ObservationImpl.class, "significant", false);
    }

    @Test
    public void testSetPhotos() throws Exception {
        checkSetCollection(ObservationImpl.class, PhotoImpl.class, "photos");
    }

    @Test
    public void testSetPhotosValidates() {
        checkSetCollectionValidates(ObservationImpl.class, PhotoImpl.class, "photos");
    }

    @Test
    public void testAddPhoto() throws Exception {

    }

    @Test
    public void testAddPhotosValidates() {
        checkAddCollectionValidates(ObservationImpl.class, PhotoImpl.class, "photos");
    }

    @Test
    public void testAddPhotos() throws Exception {
        checkAddCollection(ObservationImpl.class, PhotoImpl.class, "photos");
    }

    @Test
    public void testRemovePhoto() throws Exception {

    }

    @Test
    public void testSetNeedsFollowUp() throws Exception {
        checkBooleanDefaultAndSetGet(ObservationImpl.class, "needsFollowUp", false);
    }

    @Test
    public void testSetFollowUpReminder() throws Exception {

    }

    @Test
    public void testSetFollowUpObservation() throws Exception {

    }

    @Test
    public void testSetCategories() throws Exception {
        checkSetCollection(ObservationImpl.class, ObservationCategoryImpl.class, "categories");
    }

    @Test
    public void testSetCategoriesValidates() {
        checkSetCollectionValidates(ObservationImpl.class, ObservationCategoryImpl.class, "categories");
    }

    @Test
    public void testAddCategory() throws Exception {

    }

    @Test
    public void testAddCategoriesValidates() throws Exception {
        checkAddCollectionValidates(ObservationImpl.class, ObservationCategoryImpl.class, "categories");
    }

    @Test
    public void testAddCategories() throws Exception {
        checkAddCollection(ObservationImpl.class, ObservationCategoryImpl.class, "categories");
    }

    @Test
    public void testRemoveCategory() throws Exception {

    }

    @Test
    public void testSetGetComment() throws Exception {
//        checkStringSetGetsWithNullsSavedAsBlanks(ObservationImpl.class, "comment");
    }
}
