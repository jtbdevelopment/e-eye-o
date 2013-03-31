package com.jtbdevelopment.e_eye_o.entities.impl;

import com.google.common.base.Strings;
import com.jtbdevelopment.e_eye_o.entities.Observation;
import com.jtbdevelopment.e_eye_o.entities.validation.ConsistentAppUserValidator;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

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
    public void testDefaultObservationSubjectAndValidation() {
        ObservationImpl o = new ObservationImpl(USER1);
        assertNull(o.getObservationSubject());
        validateExpectingError(o, Observation.OBSERVATION_OBSERVATION_SUBJECT_CANNOT_BE_NULL_ERROR);
    }

    @Test
    public void testSettingObservationSubject() {
        ObservationImpl o = new ObservationImpl(USER1);
        o.setObservationSubject(new StudentImpl(USER1));
        validateNotExpectingError(o, Observation.OBSERVATION_OBSERVATION_SUBJECT_CANNOT_BE_NULL_ERROR);
        o.setObservationSubject(new ClassListImpl(USER1));
        validateNotExpectingError(o, Observation.OBSERVATION_OBSERVATION_SUBJECT_CANNOT_BE_NULL_ERROR);
    }

    @Test
    public void testNewObservationDefaultTimestamp() throws Exception {
        LocalDateTime before = new LocalDateTime();
        Thread.sleep(1);
        ObservationImpl observation = new ObservationImpl(USER1);
        Thread.sleep(1);
        LocalDateTime after = new LocalDateTime();
        assertTrue(before.compareTo(observation.getObservationTimestamp()) < 0);
        assertTrue(after.compareTo(observation.getObservationTimestamp()) > 0);
    }

    @Test
    public void testGetCategoriesNonModifiable() {
        checkCollectionIsUnmodifiable(new ObservationImpl(USER1).getCategories());
    }

    @Test
    public void testSetObservationDate() throws Exception {
        LocalDateTime timestamp = new LocalDateTime().minusDays(1);
        final ObservationImpl observation = new ObservationImpl(USER1);
        observation.setObservationTimestamp(timestamp);
        assertEquals(timestamp, observation.getObservationTimestamp());
    }

    @Test
    public void testSetSignificant() throws Exception {
        checkBooleanDefaultAndSetGet("significant", false);
    }

    @Test
    public void testSetNeedsFollowUp() throws Exception {
        checkBooleanDefaultAndSetGet("followUpNeeded", false);
    }

    @Test
    public void testSetFollowUpReminder() throws Exception {
        ObservationImpl o = new ObservationImpl(USER1);
        assertNull(o.getFollowUpReminder());
        LocalDate reminder = new LocalDate(2012, 4, 1);
        o.setFollowUpReminder(reminder);
        assertEquals(reminder, o.getFollowUpReminder());
    }

    @Test
    public void testSetFollowUpObservation() throws Exception {
        ObservationImpl o1 = new ObservationImpl(USER1);
        o1.setId("1");
        ObservationImpl o2 = new ObservationImpl(USER1);
        o2.setId("2");
        assertNull(o1.getFollowUpForObservation());
        o1.setFollowUpForObservation(o2);
        assertEquals(o2, o1.getFollowUpForObservation());
        validateNotExpectingErrors(o1, new String[]{ConsistentAppUserValidator.getGeneralErrorMessage(o1), ConsistentAppUserValidator.getSpecificErrorMessage(o1, o2)});
    }

    @Test
    public void testFollowUpCannotPointToItself() throws Exception {
        ObservationImpl o = new ObservationImpl(USER1);
        o.setId("SELF");
        assertNull(o.getFollowUpForObservation());
        o.setFollowUpForObservation(o);
        assertEquals(o, o.getFollowUpForObservation());
        validateExpectingErrors(o, new String[]{Observation.OBSERVATION_FOLLOW_UP_OBSERVATION_SELF_REFERENCE_ERROR});
    }

    @Test
    public void testFollowUpAppUserConsistent() throws Exception {
        ObservationImpl o1 = new ObservationImpl(USER1);
        o1.setId("1");
        ObservationImpl o2 = new ObservationImpl(USER2);
        o2.setId("2");
        o1.setFollowUpForObservation(o2);
        assertEquals(o2, o1.getFollowUpForObservation());
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
