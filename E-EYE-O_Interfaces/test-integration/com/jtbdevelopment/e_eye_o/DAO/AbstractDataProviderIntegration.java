package com.jtbdevelopment.e_eye_o.DAO;

import com.jtbdevelopment.e_eye_o.DAO.helpers.ObservationCategoryHelper;
import com.jtbdevelopment.e_eye_o.entities.*;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolationException;
import java.util.*;

import static org.testng.Assert.*;

/**
 * Date: 11/21/12
 * Time: 1:45 AM
 * <p/>
 * Suite of tests that can be run against any data source provider.
 */
@Test(groups = {"integration"})
public abstract class AbstractDataProviderIntegration extends AbstractTestNGSpringContextTests {
    private static Logger logger = LoggerFactory.getLogger(AbstractDataProviderIntegration.class);

    @Autowired
    private ReadWriteDAO rwDAO;
    @Autowired
    private ObservationCategoryHelper observationCategoryHelper;
    @Autowired
    private IdObjectFactory factory;

    private static AppUser testUser1;
    private static AppUser testUser2;
    private static Map<String, ObservationCategory> testOCsForU1;
    private static ClassList testClassList1ForU1;
    private static ClassList testClassList2ForU1;
    private static ClassList testClassList3ForU1;
    private static Student testStudentForU1;
    private static Observation testObservationForU1;

    @BeforeMethod
    public synchronized void initialize() {
        if (rwDAO == null) {
            return;
        }
        if (testUser1 != null) {
            return;
        }
        testUser1 = rwDAO.create(factory.newAppUser().setFirstName("Testy").setLastName("Tester").setEmailAddress("test@test.com"));
        observationCategoryHelper.createDefaultCategoriesForUser(testUser1);
        testOCsForU1 = observationCategoryHelper.getObservationCategoriesAsMap(testUser1);
        testClassList1ForU1 = rwDAO.create(factory.newClassList(testUser1).setDescription("Test Class List1"));
        testClassList2ForU1 = rwDAO.create(factory.newClassList(testUser1).setDescription("Test Class List2"));
        testClassList3ForU1 = rwDAO.create(factory.newClassList(testUser1).setDescription("Test Class List3"));
        Student s = factory.newStudent(testUser1).addClassList(testClassList1ForU1);
        s.setFirstName("Test");
        s.setLastName("Student");
        testStudentForU1 = rwDAO.create(s);
        testObservationForU1 = rwDAO.create(factory.newObservation(testUser1).setComment("Test Observation").setObservationSubject(testStudentForU1).addCategory(testOCsForU1.get("IDEA")).addCategory(testOCsForU1.get("PHYS")));

        testUser2 = rwDAO.create(factory.newAppUser().setFirstName("Another").setLastName("Tester").setEmailAddress("another@test.com"));

        logger.info("Created Test Tester with ID " + testUser1.getId());
        logger.info("Created Test Tester2 with ID " + testUser2.getId());
    }

    @Test
    public void testBeanValidationIsActive() {
        //  Not testing them all, just a few
        boolean exception = false;
        try {
            rwDAO.create(factory.newAppUser().setFirstName("").setLastName(null).setEmailAddress("INVALID_EMAIL"));
        } catch (ConstraintViolationException e) {
            assertEquals(3, e.getConstraintViolations().size());
            logger.info(e.getMessage());
            exception = true;
        }
        assertTrue(exception, "Should have had an exception!");
        exception = false;
        try {
            final Student student = factory.newStudent(testUser2).addClassList(testClassList1ForU1);
            student.setFirstName("X");
            student.setLastName("Y");
            rwDAO.create(student);
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
            rwDAO.create(factory.newAppUser().setFirstName("Testy").setLastName("Tester").setEmailAddress("test@test.com"));
        } catch (Exception e) {
            //  Expected
            return;
        }
        fail("Should have had an exception.");
    }

    @Test
    public void testModificationTimestampSetOnNewObjectCreate() throws InterruptedException {
        Student student = factory.newStudent(testUser1);
        student.setFirstName("X");
        student.setLastName("Y");
        final DateTime initialTimestamp = student.getModificationTimestamp();
        assertNotNull(initialTimestamp);
        Thread.sleep(1);
        student = rwDAO.create(student);
        assertTrue(student.getModificationTimestamp().isAfter(initialTimestamp));
    }

    @Test
    public void testModificationTimestampSetOnUppdate() throws InterruptedException {
        Student student = factory.newStudent(testUser1);
        student.setFirstName("X");
        student.setLastName("Y");
        student = rwDAO.create(student);
        final DateTime initialTimestamp = student.getModificationTimestamp();
        student.setFirstName("XX");
        rwDAO.update(student);
        assertTrue(student.getModificationTimestamp().isAfter(initialTimestamp));
    }

    @Test
    public void testCreateDefaultCategories() {
        Set<ObservationCategory> initialCategories = observationCategoryHelper.createDefaultCategoriesForUser(testUser2);
        Set<ObservationCategory> reloaded = rwDAO.getEntitiesForUser(ObservationCategory.class, testUser2);
        assertTrue(reloaded.containsAll(initialCategories));
    }

    @Test
    public void testAddCategory() {
        ObservationCategory newCategory = rwDAO.create(factory.newObservationCategory(testUser1).setShortName("TESTNEW").setDescription("Test New Category"));
        Set<ObservationCategory> categories = rwDAO.getEntitiesForUser(ObservationCategory.class, testUser1);
        assertTrue(categories.contains(newCategory));
    }

    @Test
    public void testAddDuplicateCategoryCodeFail() {
        ObservationCategory newTest1, newTest2 = null, newTest3;
        newTest1 = rwDAO.create(factory.newObservationCategory(testUser1).setShortName("TESTDUPE").setDescription("desc 1"));

        boolean exception = false;
        try {
            newTest2 = rwDAO.create(factory.newObservationCategory(testUser1).setShortName("TESTDUPE").setDescription("desc 2"));
        } catch (Exception e) {
            //  Expected
            exception = true;
        }
        assertTrue(exception);
        newTest3 = rwDAO.create(factory.newObservationCategory(testUser2).setShortName("TESTDUPE").setDescription("desc 1"));

        Set<ObservationCategory> categories = rwDAO.getEntitiesForUser(ObservationCategory.class, testUser1);
        assertTrue(categories.contains(newTest1));
        assertFalse(categories.contains(newTest2));
        categories = rwDAO.getEntitiesForUser(ObservationCategory.class, testUser2);
        assertTrue(categories.contains(newTest3));
    }

    //  TODO - actual photo
    @Test
    public void testCreatePhotoForStudent() {
        Photo photo = rwDAO.create(factory.newPhoto(testUser1).setDescription("Create Test").setTimestamp(new LocalDateTime()).setPhotoFor(testStudentForU1));
        Set<Photo> photos = rwDAO.getActiveEntitiesForUser(Photo.class, testUser1);
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
        Photo photo = rwDAO.create(factory.newPhoto(testUser1).setDescription("Create Test").setTimestamp(new LocalDateTime()).setPhotoFor(testClassList1ForU1));
        Set<Photo> photos = rwDAO.getActiveEntitiesForUser(Photo.class, testUser1);
        assertTrue(photos.contains(photo));
        for (Photo setPhoto : photos) {
            if (setPhoto.equals(photo)) {
                assertEquals(testClassList1ForU1, setPhoto.getPhotoFor());
            }
        }
    }

    //  TODO - actual photo
    @Test
    public void testCreatePhotoForObservation() {
        Photo photo = rwDAO.create(factory.newPhoto(testUser1).setDescription("Create Test").setTimestamp(new LocalDateTime()).setPhotoFor(testObservationForU1));
        Set<Photo> photos = rwDAO.getActiveEntitiesForUser(Photo.class, testUser1);
        assertTrue(photos.contains(photo));
        for (Photo setPhoto : photos) {
            if (setPhoto.equals(photo)) {
                assertEquals(testObservationForU1, setPhoto.getPhotoFor());
            }
        }
    }

    @Test
    public void testUpdateArchivePhoto() {
        Photo photo = rwDAO.create(factory.newPhoto(testUser1).setDescription("UpdateTest").setTimestamp(new LocalDateTime()).setPhotoFor(testStudentForU1));
        Set<Photo> activePhotos = rwDAO.getActiveEntitiesForUser(Photo.class, testUser1);
        Set<Photo> archivePhotos = rwDAO.getArchivedEntitiesForUser(Photo.class, testUser1);
        assertTrue(activePhotos.contains(photo));
        assertFalse(archivePhotos.contains(photo));

        photo.setDescription("Archived").setArchived(true);
        DateTime originalTS = photo.getModificationTimestamp();
        photo = rwDAO.update(photo);
        assertTrue(originalTS.isBefore(photo.getModificationTimestamp()));
        activePhotos = rwDAO.getActiveEntitiesForUser(Photo.class, testUser1);
        archivePhotos = rwDAO.getArchivedEntitiesForUser(Photo.class, testUser1);
        assertFalse(activePhotos.contains(photo));
        assertTrue(archivePhotos.contains(photo));
        photo = rwDAO.get(Photo.class, photo.getId());
        assertEquals("Archived", photo.getDescription());
    }

    @Test
    public void testCreateObservationForStudent() {
        final ObservationCategory social = testOCsForU1.get("SOCIAL");
        final ObservationCategory kauw = testOCsForU1.get("KAUW");
        final String comment = "Test Observation";
        Observation o = rwDAO.create(factory.newObservation(testUser1).setFollowUpNeeded(false).setObservationTimestamp(new LocalDateTime()).addCategories(Arrays.asList(social, kauw)).setComment(comment).setObservationSubject(testStudentForU1));
        assertEquals(comment, o.getComment());
        assertEquals(2, o.getCategories().size());
        assertTrue(o.getCategories().contains(social));
        assertTrue(o.getCategories().contains(kauw));
        assertFalse(o.isFollowUpNeeded());
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
        Observation o = rwDAO.create(factory.newObservation(testUser1).setFollowUpNeeded(false).setObservationTimestamp(new LocalDateTime()).addCategories(Arrays.asList(social, kauw)).setComment(comment).setObservationSubject(testStudentForU1));
        o.removeCategory(social);
        o.addCategory(lang);
        o.setObservationSubject(testClassList1ForU1);
        DateTime originalTS = o.getModificationTimestamp();
        o = rwDAO.update(o);
        o = rwDAO.get(Observation.class, o.getId());
        assertTrue(originalTS.isBefore(o.getModificationTimestamp()));
        assertEquals(2, o.getCategories().size());
        assertTrue(o.getCategories().contains(lang));
        assertTrue(o.getCategories().contains(kauw));
        assertEquals(testClassList1ForU1, o.getObservationSubject());
    }

    @Test
    public void testLinksObservations() {
        final ObservationCategory social = testOCsForU1.get("SOCIAL");
        final ObservationCategory kauw = testOCsForU1.get("KAUW");
        final String comment1 = "Test Observation 1";
        final String comment2 = "Test Observation 2";
        Observation o1 = rwDAO.create(factory.newObservation(testUser1).setFollowUpNeeded(false).setObservationTimestamp(new LocalDateTime()).addCategories(Arrays.asList(social, kauw)).setComment(comment1).setObservationSubject(testStudentForU1));
        Observation o2 = rwDAO.create(factory.newObservation(testUser1).setFollowUpNeeded(false).setObservationTimestamp(new LocalDateTime()).addCategories(Arrays.asList(kauw)).setComment(comment2).setObservationSubject(testStudentForU1));
        o1.setFollowUpObservation(o2);
        o2.setFollowUpNeeded(true);
        final LocalDate reminderDate = new LocalDate(2012, 11, 12);
        o2.setFollowUpReminder(reminderDate);
        o2.addCategories(testOCsForU1.values());
        rwDAO.update(Arrays.asList(o1, o2));
        o1 = rwDAO.get(Observation.class, o1.getId());
        o2 = rwDAO.get(Observation.class, o2.getId());
        assertEquals(o1.getFollowUpObservation(), o2);
        assertNull(o2.getFollowUpObservation());
        assertTrue(o2.isFollowUpNeeded());
        assertFalse(o1.isFollowUpNeeded());
        assertEquals(o2.getFollowUpReminder(), reminderDate);
    }

    @Test
    public void testDeleteLinkedObservations() {
        final ObservationCategory social = testOCsForU1.get("SOCIAL");
        final ObservationCategory kauw = testOCsForU1.get("KAUW");
        final String comment1 = "Test Observation 1";
        final String comment2 = "Test Observation 2";
        Observation o1 = rwDAO.create(factory.newObservation(testUser1).setFollowUpNeeded(false).setObservationTimestamp(new LocalDateTime()).addCategories(Arrays.asList(social, kauw)).setComment(comment1).setObservationSubject(testStudentForU1));
        Observation o2 = rwDAO.create(factory.newObservation(testUser1).setFollowUpNeeded(false).setObservationTimestamp(new LocalDateTime()).addCategories(Arrays.asList(kauw)).setComment(comment2).setObservationSubject(testStudentForU1));
        o1.setFollowUpObservation(o2);
        rwDAO.update(Arrays.asList(o1, o2));

        rwDAO.delete(o2);
        o1 = rwDAO.get(Observation.class, o1.getId());
        assertNull(o1.getFollowUpObservation());
    }

    @Test
    public void testGetModifiedSince() throws InterruptedException {
        AppUser updateUser = rwDAO.create(factory.newAppUser().setEmailAddress("updateUser@delete.test").setFirstName("delete").setLastName("delete"));
        DateTime firstTS = new DateTime();
        Thread.sleep(1);
        ObservationCategory oc = rwDAO.create(factory.newObservationCategory(updateUser).setShortName("X").setDescription("X"));
        ClassList cl = rwDAO.create(factory.newClassList(updateUser).setDescription("CL"));
        final Student student = factory.newStudent(updateUser);
        student.setFirstName("A");
        student.setLastName("B");
        Student s = rwDAO.create(student);
        Photo p = rwDAO.create(factory.newPhoto(updateUser).setDescription("D").setPhotoFor(s));
        Observation o = rwDAO.create(factory.newObservation(updateUser).setComment("T").setObservationSubject(cl));

        Set<AppUserOwnedObject> firstSet = rwDAO.getEntitiesModifiedSince(AppUserOwnedObject.class, updateUser, firstTS);
        final List<AppUserOwnedObject> initialList = Arrays.asList(oc, cl, s, p, o);
        assertEquals(initialList.size(), firstSet.size());
        assertTrue(firstSet.containsAll(initialList));

        DateTime secondTS = new DateTime();
        Thread.sleep(1);
        p.setDescription("P2");
        s.addClassList(cl);
        p = rwDAO.update(p);
        s = rwDAO.update(s);
        final List<AppUserOwnedObject> secondList = Arrays.asList(s, p);
        final Set<AppUserOwnedObject> secondSet = rwDAO.getEntitiesModifiedSince(AppUserOwnedObject.class, updateUser, secondTS);
        assertEquals(secondList.size(), secondSet.size());
        assertTrue(secondSet.containsAll(secondList));

    }

    @Test
    public void testDeletingObjectsCreatesDeletedObjects() throws InterruptedException {
        AppUser deleteUserThings = rwDAO.create(factory.newAppUser().setEmailAddress("deleteUserThings@delete.test").setFirstName("delete").setLastName("delete"));
        ObservationCategory oc = rwDAO.create(factory.newObservationCategory(deleteUserThings).setShortName("X").setDescription("X"));
        ClassList cl = rwDAO.create(factory.newClassList(deleteUserThings).setDescription("CL"));
        final Student student = factory.newStudent(deleteUserThings);
        student.setFirstName("A");
        student.setLastName("B");
        Student s = rwDAO.create(student);
        Photo p = rwDAO.create(factory.newPhoto(deleteUserThings).setDescription("D").setPhotoFor(s));
        Observation o = rwDAO.create(factory.newObservation(deleteUserThings).setComment("T").setObservationSubject(cl));

        DateTime baseTime = new DateTime();
        List<String> ids = new ArrayList<>(Arrays.asList(oc.getId(), cl.getId(), s.getId(), p.getId(), o.getId()));
        Thread.sleep(1);

        rwDAO.delete(Arrays.asList(oc, cl, s, p, o));
        assertTrue(rwDAO.getEntitiesForUser(AppUserOwnedObject.class, deleteUserThings).isEmpty());
        final Set<DeletedObject> deletedObjects = rwDAO.getEntitiesForUser(DeletedObject.class, deleteUserThings);
        assertFalse(deletedObjects.isEmpty());
        for (DeletedObject deletedObject : deletedObjects) {
            assertTrue(baseTime.isBefore(deletedObject.getModificationTimestamp()));
            assertTrue(ids.contains(deletedObject.getDeletedId()));
            ids.remove(deletedObject.getDeletedId());
        }
        assertTrue(ids.isEmpty());

    }

    @Test
    public void testDeletingAUser() {
        AppUser deleteUser1 = rwDAO.create(factory.newAppUser().setEmailAddress("delete1@delete.test").setFirstName("delete").setLastName("delete"));
        AppUser deleteUser2 = rwDAO.create(factory.newAppUser().setEmailAddress("delete2@delete.test").setFirstName("delete").setLastName("delete"));
        observationCategoryHelper.createDefaultCategoriesForUser(deleteUser1);
        Set<ObservationCategory> deleteCategories = rwDAO.getEntitiesForUser(ObservationCategory.class, deleteUser1);
        ClassList cl = rwDAO.create(factory.newClassList(deleteUser1).setDescription("delete"));
        Student student = factory.newStudent(deleteUser1);
        student.setFirstName("deleteS1");
        student.setLastName("deleteS1");
        student.addClassList(cl);
        Student s1 = rwDAO.create(student);
        student= factory.newStudent(deleteUser1);
        student.setFirstName("deleteS2");
        student.setLastName("deleteS2");
        student.addClassList(cl);
        Student s2 = rwDAO.create(student);
        Photo p = rwDAO.create(factory.newPhoto(deleteUser1).setPhotoFor(cl).setDescription("deletePhoto"));
        Observation o = rwDAO.create(factory.newObservation(deleteUser1).setObservationSubject(cl).setObservationTimestamp(new LocalDateTime()).addCategories(deleteCategories).setComment("delete"));
        Map<String, Class<? extends IdObject>> idMap = new HashMap<>();
        idMap.put(deleteUser1.getId(), AppUser.class);
        idMap.put(deleteUser2.getId(), AppUser.class);
        idMap.put(cl.getId(), ClassList.class);
        idMap.put(s1.getId(), Student.class);
        idMap.put(s2.getId(), Student.class);
        idMap.put(p.getId(), Photo.class);
        idMap.put(o.getId(), Observation.class);
        for (ObservationCategory oc : deleteCategories) {
            idMap.put(oc.getId(), ObservationCategory.class);
        }

        rwDAO.deleteUsers(Arrays.<AppUser>asList(deleteUser1, deleteUser2));

        for (Map.Entry<String, Class<? extends IdObject>> entry : idMap.entrySet()) {
            assertNull(rwDAO.get(entry.getValue(), entry.getKey()));
        }
    }

    @Test
    public void testDeletingUserTwiceOK() {
        AppUser appUser = rwDAO.create(factory.newAppUser().setFirstName("Double").setLastName("Delete").setEmailAddress("delete@double.com"));
        String id = appUser.getId();
        rwDAO.deleteUser(appUser);
        rwDAO.deleteUser(appUser);
        assertNull(rwDAO.get(AppUser.class, id));
    }

    @Test
    public void testAddingClassListsToStudent() {
        testStudentForU1.removeClassList(testClassList1ForU1);
        testStudentForU1 = rwDAO.update(testStudentForU1);
        assertTrue(testStudentForU1.getClassLists().isEmpty());
        testStudentForU1.addClassList(testClassList1ForU1);
        testStudentForU1.addClassLists(Arrays.asList(testClassList2ForU1, testClassList3ForU1));
        testStudentForU1 = rwDAO.update(testStudentForU1);
        assertTrue(testStudentForU1.getClassLists().containsAll(Arrays.asList(testClassList1ForU1, testClassList2ForU1, testClassList3ForU1)));
    }

    @Test
    public void testGetAppUsers() {
        Set<AppUser> users = rwDAO.getUsers();
        assertTrue(users.size() >= 2);
        assertTrue(users.contains(testUser1));
        assertTrue(users.contains(testUser2));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testExplicitlyCreatingADeletedObjectListExceptions() {
        rwDAO.create(Arrays.asList(factory.newDeletedObject(testUser2).setDeletedId("TEST")));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testExplicitlyCreatingADeletedObjectExceptions() {
        rwDAO.create(factory.newDeletedObject(testUser2).setDeletedId("TEST"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testExplicitlyUpdatingADeletedObjectExceptions() {
        ClassList cl = rwDAO.create(factory.newClassList(testUser2).setDescription("CLTODELETE1"));
        rwDAO.delete(cl);
        Set<DeletedObject> deleted = rwDAO.getEntitiesForUser(DeletedObject.class, testUser2);
        assertFalse(deleted.isEmpty());
        DeletedObject dobj = deleted.iterator().next();
        dobj.setModificationTimestamp(dobj.getModificationTimestamp().plusMillis(1));
        rwDAO.update(dobj);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testExplicitlyUpdatingAsListADeletedObjectExceptions() {
        ClassList cl = rwDAO.create(factory.newClassList(testUser2).setDescription("CLTODELETE1"));
        rwDAO.delete(cl);
        Set<DeletedObject> deleted = rwDAO.getEntitiesForUser(DeletedObject.class, testUser2);
        assertFalse(deleted.isEmpty());
        DeletedObject dobj = deleted.iterator().next();
        dobj.setModificationTimestamp(dobj.getModificationTimestamp().plusMillis(1));
        rwDAO.update(Arrays.asList(dobj));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testExplicitlyDeletingADeletedObjectExceptions() {
        ClassList cl = rwDAO.create(factory.newClassList(testUser2).setDescription("CLTODELETE1"));
        rwDAO.delete(cl);
        Set<DeletedObject> deleted = rwDAO.getEntitiesForUser(DeletedObject.class, testUser2);
        assertFalse(deleted.isEmpty());
        DeletedObject dobj = deleted.iterator().next();
        dobj.setModificationTimestamp(dobj.getModificationTimestamp().plusMillis(1));
        rwDAO.delete(dobj);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testExplicitlyDeletingAsListADeletedObjectExceptions() {
        ClassList cl = rwDAO.create(factory.newClassList(testUser2).setDescription("CLTODELETE1"));
        rwDAO.delete(cl);
        Set<DeletedObject> deleted = rwDAO.getEntitiesForUser(DeletedObject.class, testUser2);
        assertFalse(deleted.isEmpty());
        DeletedObject dobj = deleted.iterator().next();
        dobj.setModificationTimestamp(dobj.getModificationTimestamp().plusMillis(1));
        rwDAO.delete(Arrays.asList(dobj));
    }
}
