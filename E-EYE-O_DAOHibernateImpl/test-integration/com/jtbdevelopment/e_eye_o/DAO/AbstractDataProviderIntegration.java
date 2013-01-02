package com.jtbdevelopment.e_eye_o.DAO;

import com.jtbdevelopment.e_eye_o.DAO.helpers.ObservationCategoryHelper;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.hibernate.entities.HDBObservation;
import com.jtbdevelopment.e_eye_o.hibernate.entities.HDBPhoto;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static org.testng.Assert.*;

/**
 * Date: 11/21/12
 * Time: 1:45 AM
 * <p/>
 * Suite of tests that can be run against any data source provider to test hibernate.
 */
@Transactional(propagation = Propagation.NOT_SUPPORTED)
//  Otherwise exception is not raised until transaction committed outside this call
@Test(groups = {"integration"})
public abstract class AbstractDataProviderIntegration extends AbstractTransactionalTestNGSpringContextTests implements ApplicationContextAware {
    private static Logger logger = LoggerFactory.getLogger(AbstractDataProviderIntegration.class);

    @Autowired
    private ReadWriteDAO readWriteDAO;
    @Autowired
    private ObservationCategoryHelper observationCategoryHelper;
    @Autowired
    private IdObjectFactory objectFactory;

    private static AppUser testUser1;
    private static AppUser testUser2;
    private static Map<String, ObservationCategory> testOCsForU1;
    private static ClassList testClassListForU1;
    private static Student testStudentForU1;
    private static Observation testObservationForU1;

    @BeforeClass
    public synchronized void initialize() {
        if (readWriteDAO == null) {
            return;
        }
        if (testUser1 != null) {
            return;
        }
        testUser1 = createUser("Testy", "Tester", "test@test.com");
        observationCategoryHelper.createDefaultCategoriesForUser(testUser1);
        testOCsForU1 = observationCategoryHelper.getObservationCategoriesAsMap(testUser1);
        testUser2 = createUser("Another", "Tester", "another@test.com");
        logger.info("Created Test Tester with ID " + testUser1.getId());
        logger.info("Created Test Tester2 with ID " + testUser2.getId());
        testClassListForU1 = objectFactory.newClassList(testUser1).setDescription("Test Class List");
        testClassListForU1 = readWriteDAO.create(testClassListForU1);
        testStudentForU1 = objectFactory.newStudent(testUser1).addClassList(testClassListForU1).setFirstName("Test").setLastName("Student");
        testStudentForU1 = readWriteDAO.create(testStudentForU1);
        testObservationForU1 = objectFactory.newObservation(testUser1).setComment("Test Observation").setObservationSubject(testStudentForU1).addCategory(testOCsForU1.get("IDEA")).addCategory(testOCsForU1.get("PHYS"));
        testObservationForU1 = readWriteDAO.create(testObservationForU1);
    }

    @Test
    public void testBeanValidationIsActive() {
        boolean exception = false;
        try {
            AppUser user = createUser("", null, "INVALID_EMAIL");
        } catch (ConstraintViolationException e) {
            assertEquals(3, e.getConstraintViolations().size());
            logger.info(e.getMessage());
            exception = true;
        }
        assertTrue(exception, "Should have had an exception!");
        exception = false;
        try {
            Student student = objectFactory.newStudent(testUser2).addClassList(testClassListForU1).setFirstName("X").setLastName("Y");
            readWriteDAO.create(student);
        } catch (ConstraintViolationException e) {
            assertEquals(2, e.getConstraintViolations().size());
            logger.info(e.getMessage());
            exception = true;
        }
        assertTrue(exception, "Should have had an exception!");
    }

    @Test
    public void testDuplicateLoginAppUserFails() {
        try {
            createUser("Testy", "Tester", "test@test.com");
        } catch (Exception e) {
            //  Expected
            return;
        }
        fail("Should have had an exception.");
    }

    @Test
    public void testCreateDefaultCategories() {
        Set<ObservationCategory> initialCategories = observationCategoryHelper.createDefaultCategoriesForUser(testUser2);
        Set<ObservationCategory> reloaded = readWriteDAO.getEntitiesForUser(ObservationCategory.class, testUser2);
        assertTrue(reloaded.containsAll(initialCategories));
    }

    @Test
    public void testAddCategory() {
        ObservationCategory newCategory = createOC(testUser1, "TESTNEW", "Test New Category");
        Set<ObservationCategory> categories = readWriteDAO.getEntitiesForUser(ObservationCategory.class, testUser1);
        assertTrue(categories.contains(newCategory));
    }

    @Test
    public void testAddDuplicateCategoryCodeFail() {
        ObservationCategory newTest1, newTest2 = null, newTest3;
        newTest1 = createOC(testUser1, "TESTDUPE", "desc 1");

        boolean exception = false;
        try {
            newTest2 = createOC(testUser1, "TESTDUPE", "desc 2");
        } catch (Exception e) {
            //  Expected
            exception = true;
        }
        assertTrue(exception);
        newTest3 = createOC(testUser2, "TESTDUPE", "desc 1");

        Set<ObservationCategory> categories = readWriteDAO.getEntitiesForUser(ObservationCategory.class, testUser1);
        assertTrue(categories.contains(newTest1));
        assertFalse(categories.contains(newTest2));
        categories = readWriteDAO.getEntitiesForUser(ObservationCategory.class, testUser2);
        assertTrue(categories.contains(newTest3));
    }

    //  TODO - actual photo
    @Test
    public void testCreatePhotoForStudent() {
        Photo photo = objectFactory.newPhoto(testUser1);
        photo.setDescription("Create Test").setTimestamp(new LocalDateTime()).setPhotoFor(testStudentForU1).setArchived(false);
        photo = readWriteDAO.create(photo);
        Set<Photo> photos = readWriteDAO.getActiveEntitiesForUser(Photo.class, testUser1);
        assertTrue(photos.contains(photo));
        for (Photo setPhoto : photos) {
            if (setPhoto.equals(photo)) {
                assertEquals(testStudentForU1, setPhoto.getPhotoFor());
            }
        }
    }

    //  TODO - actual photo
    @Test
    public void testCreatePhotoForClassList() {
        Photo photo = objectFactory.newPhoto(testUser1);
        photo.setDescription("Create Test").setTimestamp(new LocalDateTime()).setPhotoFor(testClassListForU1).setArchived(false);
        photo = readWriteDAO.create(photo);
        Set<Photo> photos = readWriteDAO.getActiveEntitiesForUser(Photo.class, testUser1);
        assertTrue(photos.contains(photo));
        for (Photo setPhoto : photos) {
            if (setPhoto.equals(photo)) {
                assertEquals(testClassListForU1, setPhoto.getPhotoFor());
            }
        }
    }

    //  TODO - actual photo
    @Test
    public void testCreatePhotoForObservation() {
        Photo photo = objectFactory.newPhoto(testUser1);
        photo.setDescription("Create Test").setTimestamp(new LocalDateTime()).setPhotoFor(testObservationForU1).setArchived(false);
        photo = readWriteDAO.create(photo);
        Set<Photo> photos = readWriteDAO.getActiveEntitiesForUser(Photo.class, testUser1);
        assertTrue(photos.contains(photo));
        for (Photo setPhoto : photos) {
            if (setPhoto.equals(photo)) {
                assertEquals(testObservationForU1, setPhoto.getPhotoFor());
            }
        }
    }

    @Test
    public void testUpdateArchivePhoto() {
        Photo photo = objectFactory.newPhoto(testUser1);
        photo.setDescription("UpdateTest").setTimestamp(new LocalDateTime()).setPhotoFor(testStudentForU1).setArchived(false);
        photo = readWriteDAO.create(photo);
        Set<Photo> activePhotos = readWriteDAO.getActiveEntitiesForUser(Photo.class, testUser1);
        Set<Photo> archivePhotos = readWriteDAO.getArchivedEntitiesForUser(Photo.class, testUser1);
        assertTrue(activePhotos.contains(photo));
        assertFalse(archivePhotos.contains(photo));

        photo.setDescription("Archived").setArchived(true);
        readWriteDAO.update(photo);
        activePhotos = readWriteDAO.getActiveEntitiesForUser(Photo.class, testUser1);
        archivePhotos = readWriteDAO.getArchivedEntitiesForUser(Photo.class, testUser1);
        assertFalse(activePhotos.contains(photo));
        assertTrue(archivePhotos.contains(photo));
        photo = readWriteDAO.get(HDBPhoto.class, photo.getId());
        assertEquals("Archived", photo.getDescription());
    }

    @Test
    public void testCreateObservationForStudent() {
        final ObservationCategory social = testOCsForU1.get("SOCIAL");
        final ObservationCategory kauw = testOCsForU1.get("KAUW");
        final String comment = "Test Observation";
        Observation o = createObservation(testUser1, Arrays.asList(social, kauw), comment, testStudentForU1);
        assertEquals(comment, o.getComment());
        assertEquals(2, o.getCategories().size());
        assertTrue(o.getCategories().contains(social));
        assertTrue(o.getCategories().contains(kauw));
        assertFalse(o.getNeedsFollowUp());
        assertNull(o.getFollowUpObservation());
        assertNull(o.getFollowUpReminder());
        assertEquals(testStudentForU1, o.getObservationSubject());
    }

    @Test
    public void testModifyObservation() {
        final ObservationCategory social = testOCsForU1.get("SOCIAL");
        final ObservationCategory kauw = testOCsForU1.get("KAUW");
        final ObservationCategory lang = testOCsForU1.get("LANG");
        final String comment = "Test Observation";
        Observation o = createObservation(testUser1, Arrays.asList(social, kauw), comment, testStudentForU1);
        o.removeCategory(social);
        o.addCategory(lang);
        o.setObservationSubject(testClassListForU1);
        readWriteDAO.update(o);
        o = readWriteDAO.get(HDBObservation.class, o.getId());
        assertEquals(2, o.getCategories().size());
        assertTrue(o.getCategories().contains(lang));
        assertTrue(o.getCategories().contains(kauw));
        assertEquals(testClassListForU1, o.getObservationSubject());
    }

    @Test
    public void testLinksObservations() {
        final ObservationCategory social = testOCsForU1.get("SOCIAL");
        final ObservationCategory kauw = testOCsForU1.get("KAUW");
        final String comment1 = "Test Observation 1";
        final String comment2 = "Test Observation 2";
        Observation o1 = createObservation(testUser1, Arrays.asList(social, kauw), comment1, testStudentForU1);
        Observation o2 = createObservation(testUser1, Arrays.asList(kauw), comment2, testStudentForU1);
        o1.setFollowUpObservation(o2);
        o2.setNeedsFollowUp(true);
        final LocalDate reminderDate = new LocalDate(2012, 11, 12);
        o2.setFollowUpReminder(reminderDate);
        readWriteDAO.update(Arrays.asList(o1, o2));
        o1 = readWriteDAO.get(HDBObservation.class, o1.getId());
        o2 = readWriteDAO.get(HDBObservation.class, o2.getId());
        assertEquals(o1.getFollowUpObservation(), o2);
        assertNull(o2.getFollowUpObservation());
        assertTrue(o2.getNeedsFollowUp());
        assertFalse(o1.getNeedsFollowUp());
        assertEquals(o2.getFollowUpReminder(), reminderDate);
    }

    private AppUser createUser(final String first, final String last, final String email) {
        AppUser appUser = objectFactory.newAppUser().setFirstName(first).setLastName(last).setEmailAddress(email);
        return readWriteDAO.create(appUser);
    }

    private Observation createObservation(final AppUser appUser, final Collection<ObservationCategory> ocs, final String comment, final AppUserOwnedObject observationFor) {
        Observation o = objectFactory.newObservation(appUser)
                .setNeedsFollowUp(false)
                .setObservationTimestamp(new LocalDateTime())
                .addCategories(ocs)
                .setComment(comment)
                .setObservationSubject(observationFor);
        return readWriteDAO.create(o);
    }

    private ObservationCategory createOC(final AppUser appuser, final String shortCode, final String description) {
        ObservationCategory oc = objectFactory.newObservationCategory(appuser).setShortName(shortCode).setDescription(description);
        return readWriteDAO.create(oc);
    }

}
