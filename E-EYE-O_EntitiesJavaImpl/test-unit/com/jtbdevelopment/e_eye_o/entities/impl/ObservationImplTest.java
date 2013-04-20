package com.jtbdevelopment.e_eye_o.entities.impl;

import com.google.common.base.Strings;
import com.jtbdevelopment.e_eye_o.entities.Observation;
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
