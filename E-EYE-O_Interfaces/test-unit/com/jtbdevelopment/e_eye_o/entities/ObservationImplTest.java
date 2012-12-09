package com.jtbdevelopment.e_eye_o.entities;

import org.testng.annotations.Test;

/**
 * Date: 12/8/12
 * Time: 8:08 PM
 */
public class ObservationImplTest extends AbstractIdObjectTest {
    @Test
    public void testSetObservationDate() throws Exception {

    }

    @Test
    public void testSetSignificant() throws Exception {
        booleanSetGetAndDefaultCheck(ObservationImpl.class, "significant", false);
    }

    @Test
    public void testSetPhotos() throws Exception {

    }

    @Test
    public void testAddPhoto() throws Exception {

    }

    @Test
    public void testAddPhotos() throws Exception {

    }

    @Test
    public void testRemovePhoto() throws Exception {

    }

    @Test
    public void testSetNeedsFollowUp() throws Exception {
        booleanSetGetAndDefaultCheck(ObservationImpl.class, "needsFollowUp", false);
    }

    @Test
    public void testSetFollowUpReminder() throws Exception {

    }

    @Test
    public void testSetFollowUpObservation() throws Exception {

    }

    @Test
    public void testSetCategories() throws Exception {

    }

    @Test
    public void testAddCategory() throws Exception {

    }

    @Test
    public void testAddCategories() throws Exception {

    }

    @Test
    public void testRemoveCategory() throws Exception {

    }

    @Test
    public void testSetGetComment() throws Exception {
        stringSetGetsWithNullsSavedAsBlanks(ObservationImpl.class, "comment");
    }
}
