package com.jtbdevelopment.e_eye_o.entities;

import com.google.common.base.Strings;
import com.jtbdevelopment.e_eye_o.entities.validation.ConsistentAppUserValidator;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * Date: 12/8/12
 * Time: 8:08 PM
 */
public class ObservationImplTest extends AbstractAppUserOwnedObjectTest<ObservationImpl> {
    private final static String TOO_LONG_FOR_COMMENT;

    static {
        TOO_LONG_FOR_COMMENT = Strings.padStart("", Observation.MAX_COMMENT_SIZE + 1, 'X');
    }

    public ObservationImplTest() {
        super(ObservationImpl.class);
    }

    @Test
    public void testConstructorsForNewObjects() {
        checkDefaultAndAppUserConstructorTests();
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

    @Test
    public void testGetPhotosNonModifiable() {
        checkCollectionIsUnmodifiable(new ObservationImpl().getPhotos());
    }

    @Test
    public void testGetCategoriesNonModifiable() {
        checkCollectionIsUnmodifiable(new ObservationImpl().getCategories());
    }

    @Test
    public void testSetObservationDate() throws Exception {
        LocalDateTime timestamp = new LocalDateTime().minusDays(1);
        assertEquals(timestamp, new ObservationImpl().setObservationTimestamp(timestamp).getObservationTimestamp());
    }

    @Test
    public void testSetSignificant() throws Exception {
        checkBooleanDefaultAndSetGet("significant", false);
    }

    @Test
    public void testSetPhotos() throws Exception {
        checkSetCollection(PhotoImpl.class, "photos", Observation.OBSERVATION_PHOTOS_CANNOT_CONTAIN_NULL_ERROR);
    }

    @Test
    public void testPhotosValidates() {
        checkCollectionValidates(PhotoImpl.class, "photos", Observation.OBSERVATION_PHOTOS_CANNOT_CONTAIN_NULL_ERROR);
    }

    @Test
    public void testAddPhoto() throws Exception {
        checkAddSingleEntityToCollection(PhotoImpl.class, "photo", "photos", Observation.OBSERVATION_PHOTOS_CANNOT_CONTAIN_NULL_ERROR);
    }

    @Test
    public void testAddPhotos() throws Exception {
        checkAddManyEntitiesToCollection(PhotoImpl.class, "photos", Observation.OBSERVATION_PHOTOS_CANNOT_CONTAIN_NULL_ERROR);
    }

    @Test
    public void testRemovePhoto() throws Exception {
        checkRemoveSingleEntityToCollection(PhotoImpl.class, "photo", "photos", Observation.OBSERVATION_PHOTOS_CANNOT_CONTAIN_NULL_ERROR);
    }

    @Test
    public void testSetNeedsFollowUp() throws Exception {
        checkBooleanDefaultAndSetGet("needsFollowUp", false);
    }

    @Test
    public void testSetFollowUpReminder() throws Exception {
        ObservationImpl o = new ObservationImpl();
        assertNull(o.getFollowUpReminder());
        LocalDate reminder = new LocalDate(2012, 4, 1);
        assertEquals(reminder, o.setFollowUpReminder(reminder).getFollowUpReminder());
    }

    @Test
    public void testSetFollowUpObservation() throws Exception {
        ObservationImpl o1 = new ObservationImpl(USER1).setId("1");
        ObservationImpl o2 = new ObservationImpl(USER1).setId("2");
        assertNull(o1.getFollowUpObservation());
        assertEquals(o2, o1.setFollowUpObservation(o2).getFollowUpObservation());
        validateNotExpectingErrors(o1, new String[]{ConsistentAppUserValidator.getGeneralErrorMessage(o1), ConsistentAppUserValidator.getSpecificErrorMessage(o1, o2)});
    }

    @Test
    public void testFollowUpCannotPointToItself() throws Exception {
        ObservationImpl o = new ObservationImpl(USER1).setId("SELF");
        assertNull(o.getFollowUpObservation());
        assertEquals(o, o.setFollowUpObservation(o).getFollowUpObservation());
        validateExpectingErrors(o, new String[]{Observation.OBSERVATION_FOLLOW_UP_OBSERVATION_SELF_REFERENCE_ERROR});
    }

    @Test
    public void testFollowUpAppUserConsistent() throws Exception {
        ObservationImpl o1 = new ObservationImpl(USER1).setId("1");
        ObservationImpl o2 = new ObservationImpl(USER2).setId("2");
        assertEquals(o2, o1.setFollowUpObservation(o2).getFollowUpObservation());
        validateExpectingErrors(o1, new String[]{ConsistentAppUserValidator.getGeneralErrorMessage(o1), ConsistentAppUserValidator.getSpecificErrorMessage(o1, o2)});
    }

    @Test
    public void testSetCategories() throws Exception {
        checkSetCollection(ObservationCategoryImpl.class, "categories", Observation.OBSERVATION_CATEGORIES_CANNOT_CONTAIN_NULL_ERROR);
    }

    @Test
    public void testCategoriesValidates() {
        checkCollectionValidates(ObservationCategoryImpl.class, "categories", Observation.OBSERVATION_CATEGORIES_CANNOT_CONTAIN_NULL_ERROR);
    }

    @Test
    public void testAddCategory() throws Exception {
        checkAddSingleEntityToCollection(ObservationCategoryImpl.class, "category", "categories", Observation.OBSERVATION_CATEGORIES_CANNOT_CONTAIN_NULL_ERROR);
    }

    @Test
    public void testAddCategories() throws Exception {
        checkAddManyEntitiesToCollection(ObservationCategoryImpl.class, "categories", Observation.OBSERVATION_CATEGORIES_CANNOT_CONTAIN_NULL_ERROR);
    }

    @Test
    public void testRemoveCategory() throws Exception {
        checkRemoveSingleEntityToCollection(ObservationCategoryImpl.class, "category", "categories", Observation.OBSERVATION_CATEGORIES_CANNOT_CONTAIN_NULL_ERROR);
    }

    @Test
    public void testSetGetComment() throws Exception {
        checkStringSetGetsAndValidateNullsAndBlanksAsError("comment", Observation.OBSERVATION_COMMENT_CANNOT_BE_BLANK_OR_NULL_ERROR);
    }

    @Test
    public void testCommentSize() throws Exception {
        checkStringSizeValidation("comment", TOO_LONG_FOR_COMMENT, Observation.OBSERVATION_COMMENT_SIZE_ERROR);
    }
}
