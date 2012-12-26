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
@Transactional
@Test(groups = {"integration"})
public abstract class AbstractDataProviderIntegration extends AbstractTransactionalTestNGSpringContextTests implements ApplicationContextAware {
    private static Logger logger = LoggerFactory.getLogger(AbstractDataProviderIntegration.class);

    @Autowired
    private ReadWriteDAO readWriteDAO;
    @Autowired
    private ObservationCategoryHelper observationCategoryHelper;

    private static AppUser testAppUser1;
    private static Map<String, ObservationCategory> testAppUser1OCs;
    private static AppUser testAppUser2;

    @BeforeClass
    public synchronized void initialize() {
        if (readWriteDAO == null) {
            return;
        }
        if (testAppUser1 != null) {
            return;
        }

        testAppUser1 = createUser("Testy", "Tester", "test@test.com");
        observationCategoryHelper.createDefaultCategoriesForUser(testAppUser1);
        testAppUser1OCs = observationCategoryHelper.getObservationCategoriesAsMap(testAppUser1);
        testAppUser2 = createUser("Another", "Tester", "another@test.com");
        logger.info("Created Test Tester with ID " + testAppUser1.getId());
        logger.info("Created Test Tester2 with ID " + testAppUser2.getId());
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    //  Otherwise exception is not raised until transaction committed outside this call
    public void duplicateLoginAppUserFails() {
        try {
            createUser("Testy", "Tester", "test@test.com");
        } catch (Exception e) {
            //  Expected
            return;
        }
        fail("Should have exceptioned");
    }

    @Test
    public void testCreateDefaultCategories() {
        Set<ObservationCategory> initialCategories = observationCategoryHelper.createDefaultCategoriesForUser(testAppUser2);
        Set<ObservationCategory> reloaded = readWriteDAO.getEntitiesForUser(ObservationCategory.class, testAppUser2);
        assertTrue(reloaded.containsAll(initialCategories));
    }

    @Test
    public void testAddCategory() {
        ObservationCategory newCategory = createOC(testAppUser1, "TESTNEW", "Test New Category");
        Set<ObservationCategory> categories = readWriteDAO.getEntitiesForUser(ObservationCategory.class, testAppUser1);
        assertTrue(categories.contains(newCategory));
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    //  Otherwise exception is not raised until transaction committed outside this call
    public void addDuplicateCategoryCodeFail() {
        ObservationCategory newTest1, newTest2 = null, newTest3;
        newTest1 = createOC(testAppUser1, "TESTDUPE", "desc 1");

        boolean exception = false;
        try {
            newTest2 = createOC(testAppUser1, "TESTDUPE", "desc 2");
        } catch (Exception e) {
            //  Expected
            exception = true;
        }
        assertTrue(exception);
        newTest3 = createOC(testAppUser2, "TESTDUPE", "desc 1");

        Set<ObservationCategory> categories = readWriteDAO.getEntitiesForUser(ObservationCategory.class, testAppUser1);
        assertTrue(categories.contains(newTest1));
        assertFalse(categories.contains(newTest2));
        categories = readWriteDAO.getEntitiesForUser(ObservationCategory.class, testAppUser2);
        assertTrue(categories.contains(newTest3));
    }

    //  TODO - actual photo
    @Test
    public void testCreatePhoto() {
        Photo photo = new PhotoImpl(testAppUser1);
        photo.setDescription("Create Test").setTimestamp(new LocalDateTime()).setArchived(false);
        photo = readWriteDAO.create(photo);
        Set<Photo> photos = readWriteDAO.getActiveEntitiesForUser(Photo.class, testAppUser1);
        assertTrue(photos.contains(photo));
    }

    @Test
    public void testUpdateArchivePhoto() {
        Photo photo = new PhotoImpl(testAppUser1);
        photo.setDescription("UpdateTest").setTimestamp(new LocalDateTime()).setArchived(false);
        photo = readWriteDAO.create(photo);
        Set<Photo> activePhotos = readWriteDAO.getActiveEntitiesForUser(Photo.class, testAppUser1);
        Set<Photo> archivePhotos = readWriteDAO.getArchivedEntitiesForUser(Photo.class, testAppUser1);
        assertTrue(activePhotos.contains(photo));
        assertFalse(archivePhotos.contains(photo));

        photo.setDescription("Archived").setArchived(true);
        readWriteDAO.update(photo);
        activePhotos = readWriteDAO.getActiveEntitiesForUser(Photo.class, testAppUser1);
        archivePhotos = readWriteDAO.getArchivedEntitiesForUser(Photo.class, testAppUser1);
        assertFalse(activePhotos.contains(photo));
        assertTrue(archivePhotos.contains(photo));
        photo = readWriteDAO.get(HDBPhoto.class, photo.getId());
        assertEquals("Archived", photo.getDescription());
    }

    @Test
    public void testCreateObservation() {
        Photo p = new HDBPhoto(new PhotoImpl()).setAppUser(testAppUser1).setDescription("Test Obs Create");
        final ObservationCategory social = testAppUser1OCs.get("SOCIAL");
        final ObservationCategory kauw = testAppUser1OCs.get("KAUW");
        final String comment = "Test Observation";
        Observation o = createObservation(testAppUser1, Arrays.asList(social, kauw), comment, Arrays.asList(p));
        assertEquals(comment, o.getComment());
        assertEquals(2, o.getCategories().size());
        assertTrue(o.getCategories().contains(social));
        assertTrue(o.getCategories().contains(kauw));
        assertFalse(o.getNeedsFollowUp());
        assertNull(o.getFollowUpObservation());
        assertNull(o.getFollowUpReminder());
        assertEquals(1, o.getPhotos().size());
        assertTrue(o.getPhotos().contains(p));
    }

    @Test
    public void testModifyObservation() {
        Photo p = new HDBPhoto(new PhotoImpl()).setAppUser(testAppUser1).setDescription("Test Obs Update");
        final ObservationCategory social = testAppUser1OCs.get("SOCIAL");
        final ObservationCategory kauw = testAppUser1OCs.get("KAUW");
        final ObservationCategory lang = testAppUser1OCs.get("LANG");
        final String comment = "Test Observation";
        Observation o = createObservation(testAppUser1, Arrays.asList(social, kauw), comment, Arrays.asList(p));
        o.removeCategory(social);
        o.addCategory(lang);
        readWriteDAO.update(o);
        assertEquals(2, o.getCategories().size());
        assertTrue(o.getCategories().contains(lang));
        assertTrue(o.getCategories().contains(kauw));
    }

    @Test
    public void testLinksObservations() {
        final ObservationCategory social = testAppUser1OCs.get("SOCIAL");
        final ObservationCategory kauw = testAppUser1OCs.get("KAUW");
        final String comment1 = "Test Observation 1";
        final String comment2 = "Test Observation 2";
        Observation o1 = createObservation(testAppUser1, Arrays.asList(social, kauw), comment1, null);
        Observation o2 = createObservation(testAppUser1, Arrays.asList(kauw), comment2, null);
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
        AppUser appUser = new AppUserImpl();
        appUser.setFirstName(first).setLastName(last).setEmailAddress(email);
        return readWriteDAO.create(appUser);
    }

    private Observation createObservation(final AppUser appUser, final Collection<ObservationCategory> ocs, final String comment, Collection<Photo> photos) {
        Observation o = new ObservationImpl(appUser);
        o.setNeedsFollowUp(false);
        o.setObservationTimestamp(new LocalDateTime());
        o.addCategories(ocs);
        o.setComment(comment);
        if (photos != null) {
            o.addPhotos(photos);
        }
        return readWriteDAO.create(o);
    }

    private ObservationCategory createOC(final AppUser appuser, final String shortCode, final String description) {
        ObservationCategory oc = new ObservationCategoryImpl(appuser).setShortName(shortCode).setDescription(description);
        return readWriteDAO.create(oc);
    }

}
