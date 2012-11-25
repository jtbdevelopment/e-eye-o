package com.jtbdevelopment.e_eye_o.HibernateDAO;

import com.jtbdevelopment.e_eye_o.DAO.WriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.Observation;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import com.jtbdevelopment.e_eye_o.entities.Photo;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.annotation.Timed;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.Assert.*;

import java.util.*;

/**
 * Date: 11/21/12
 * Time: 1:45 AM
 *
 * Suite of tests that can be run against any data source provider to test hibernate.
 */
@Transactional
public abstract class AbstractDataProviderTest implements ApplicationContextAware {
    private static Logger logger = LoggerFactory.getLogger(AbstractDataProviderTest.class);

    @Autowired
    private WriteDAO writeDAO;
    private static AppUser testAppUser1;
    private static AppUser testAppUser2;

    //  Can't use junit beforeclass because we don't have app context or injected bean yet then
    //  We do use this to setup and then junit afterclass
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        initialize();
    }

    public synchronized void initialize() {
        if (writeDAO == null) {
            return;
        }
        if(testAppUser1 != null) {
            return;
        }

        testAppUser1 = createUser("TT", "Testy", "Tester", "test@test.com");
        List<ObservationCategory> initialCategories = ObservationCategory.createDefaultCategoriesForUser(testAppUser1);
        writeDAO.create(initialCategories);
        testAppUser2 = createUser("AT", "Another", "Tester", "another@test.com");
        logger.info("Created Test Tester with ID " + testAppUser1.getId());
        logger.info("Created Test Tester2 with ID " + testAppUser2.getId());
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    //  Otherwise exception is not raised until transaction committed outside this call
    public void duplicateLoginAppUserFails() {
        try {
            AppUser dupe = createUser("TT", "Testy", "Tester", "test@test.com");
        } catch (Exception e) {
            //  Expected
            return;
        }
        fail("Should have exceptioned");
    }

    @Test
    public void testCreateDefaultCategories() {
        List<ObservationCategory> initialCategories = ObservationCategory.createDefaultCategoriesForUser(testAppUser2);
        writeDAO.create(initialCategories);
        Set<ObservationCategory> reloaded = writeDAO.getEntitiesForUser(ObservationCategory.class, testAppUser2);
        assertTrue(reloaded.containsAll(initialCategories));
    }

    @Test
    public void testAddCategory() {
        ObservationCategory newCategory = createOC(testAppUser1, "TESTNEW", "Test New Category");
        Set<ObservationCategory> categories = writeDAO.getEntitiesForUser(ObservationCategory.class, testAppUser1);
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

        Set<ObservationCategory> categories = writeDAO.getEntitiesForUser(ObservationCategory.class, testAppUser1);
        assertTrue(categories.contains(newTest1));
        assertFalse(categories.contains(newTest2));
        categories = writeDAO.getEntitiesForUser(ObservationCategory.class, testAppUser2);
        assertTrue(categories.contains(newTest3));
    }

    //  TODO - actual photo
    @Test
    public void testCreatePhoto() {
        Photo photo = new Photo(testAppUser1);
        photo.setDescription("Create Test").setTimestamp(new LocalDateTime()).setArchived(false);
        writeDAO.create(photo);
        Set<Photo> photos = writeDAO.getActiveEntitiesForUser(Photo.class, testAppUser1);
        assertTrue(photos.contains(photo));
    }

    @Test
    public void testUpdateArchivePhoto() {
        Photo photo = new Photo(testAppUser1);
        photo.setDescription("UpdateTest").setTimestamp(new LocalDateTime()).setArchived(false);
        writeDAO.create(photo);
        Set<Photo> activePhotos = writeDAO.getActiveEntitiesForUser(Photo.class, testAppUser1);
        Set<Photo> archivePhotos= writeDAO.getArchivedEntitiesForUser(Photo.class, testAppUser1);
        assertTrue(activePhotos.contains(photo));
        assertFalse(archivePhotos.contains(photo));

        photo.setDescription("Archived").setArchived(true);
        writeDAO.update(photo);
        activePhotos = writeDAO.getActiveEntitiesForUser(Photo.class, testAppUser1);
        archivePhotos= writeDAO.getArchivedEntitiesForUser(Photo.class, testAppUser1);
        assertFalse(activePhotos.contains(photo));
        assertTrue(archivePhotos.contains(photo));
        photo = writeDAO.get(Photo.class, photo.getId());
        assertEquals("Archived", photo.getDescription());
    }

    @Test
    public void testCreateObservation() {
        Map<String, ObservationCategory> ocs = writeDAO.getObservationCategoriesAsMap(testAppUser1);
        Photo p = new Photo(testAppUser1).setDescription("Test Obs Photo");
        final ObservationCategory social = ocs.get("SOCIAL");
        final ObservationCategory kauw = ocs.get("KAUW");
        final String comment = "Test Observation";
        Observation o = createObservation(testAppUser1, Arrays.asList(social, kauw), comment, Arrays.asList(p));
        writeDAO.update(o);
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
    @Timed(millis = 0)
    public void testLinksObservations() {
        Map<String, ObservationCategory> ocs = writeDAO.getObservationCategoriesAsMap(testAppUser1);
        final ObservationCategory social = ocs.get("SOCIAL");
        final ObservationCategory kauw = ocs.get("KAUW");
        final String comment1 = "Test Observation 1";
        final String comment2 = "Test Observation 2";
        Observation o1 = createObservation(testAppUser1, Arrays.asList(social, kauw), comment1, null);
        Observation o2 = createObservation(testAppUser1, Arrays.asList(kauw), comment2, null);
        o1.setFollowUpObservation(o2);
        o2.setNeedsFollowUp(true);
        final LocalDate reminderDate = new LocalDate(2012, 11, 12);
        o2.setFollowUpReminder(reminderDate);
        writeDAO.update(Arrays.asList(o1, o2));
        o1 = writeDAO.get(Observation.class, o1.getId());
        o2 = writeDAO.get(Observation.class, o2.getId());
        assertEquals(o1.getFollowUpObservation(), o2);
        assertNull(o2.getFollowUpObservation());
        assertTrue(o2.getNeedsFollowUp());
        assertFalse(o1.getNeedsFollowUp());
        assertEquals(o2.getFollowUpReminder(), reminderDate);
    }

    private AppUser createUser(final String login, final String first, final String last, final String email) {
        AppUser appUser = new AppUser();
        appUser.setLogin(login).setFirstName(first).setLastName(last).setEmailAddress(email);
        writeDAO.create(appUser);
        return appUser;
    }

    private Observation createObservation(final AppUser appUser, final Collection<ObservationCategory> ocs, final String comment, Collection<Photo> photos) {
        Observation o = new Observation(appUser);
        o.setNeedsFollowUp(false);
        o.setObservationDate(new LocalDateTime());
        o.addCategories(ocs);
        o.setComment(comment);
        if( photos != null) {
            o.addPhotos(photos);
        }
        writeDAO.create(o);
        return o;
    }

    private ObservationCategory createOC(final AppUser appuser, final String shortCode, final String description) {
        ObservationCategory oc = new ObservationCategory(appuser, shortCode, description);
        writeDAO.create(oc);
        return oc;
    }

}
