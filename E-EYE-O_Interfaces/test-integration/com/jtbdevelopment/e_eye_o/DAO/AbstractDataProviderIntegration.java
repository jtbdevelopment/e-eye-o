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
//  TODO - add tests for observables and adding/inserting/deleting observations on them
@Test(groups = {"integration"})
public abstract class AbstractDataProviderIntegration extends AbstractTestNGSpringContextTests {
    private static final String PNG = "image/png";
    private static final byte[] someBytes = new byte[]{0, 1, 0, 1};
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
        testUser1 = rwDAO.create(factory.newAppUserBuilder().withFirstName("Testy").withLastName("Tester").withEmailAddress("test@test.com").withPassword("pass").build());
        observationCategoryHelper.createDefaultCategoriesForUser(testUser1);
        testOCsForU1 = observationCategoryHelper.getObservationCategoriesAsMap(testUser1);
        testClassList1ForU1 = rwDAO.create(factory.newClassListBuilder(testUser1).withDescription("Test Class List1").build());
        testClassList2ForU1 = rwDAO.create(factory.newClassListBuilder(testUser1).withDescription("Test Class List2").build());
        testClassList3ForU1 = rwDAO.create(factory.newClassListBuilder(testUser1).withDescription("Test Class List3").build());
        Student s = factory.newStudentBuilder(testUser1).addClassList(testClassList1ForU1).withFirstName("Test").withLastName("Student").build();
        testStudentForU1 = rwDAO.create(s);
        testObservationForU1 = rwDAO.create(factory.newObservationBuilder(testUser1).withComment("Test Observation").withObservationSubject(testStudentForU1).addCategory(testOCsForU1.get("CD")).addCategory(testOCsForU1.get("PD")).build());

        testUser2 = rwDAO.create(factory.newAppUserBuilder().withFirstName("Another").withLastName("Tester").withPassword("pass").withEmailAddress("another@test.com").build());

        logger.info("Created Test Tester with ID " + testUser1.getId());
        logger.info("Created Test Tester2 with ID " + testUser2.getId());
    }

    @Test
    public void testBeanValidationIsActive() {
        //  Not testing them all, just a few
        boolean exception = false;
        try {
            rwDAO.create(factory.newAppUserBuilder().withFirstName("").withLastName(null).withPassword("pass").withEmailAddress("INVALID_EMAIL").build());
        } catch (ConstraintViolationException e) {
            assertEquals(3, e.getConstraintViolations().size());
            logger.info(e.getMessage());
            exception = true;
        }
        assertTrue(exception, "Should have had an exception!");
        exception = false;
        try {
            final Student student = factory.newStudentBuilder(testUser2).addClassList(testClassList1ForU1).withFirstName("X").withLastName("Y").build();
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
            rwDAO.create(factory.newAppUserBuilder().withFirstName("Testy").withLastName("Tester").withPassword("pass").withEmailAddress("test@test.com").build());
        } catch (Exception e) {
            //  Expected
            return;
        }
        fail("Should have had an exception.");
    }

    @Test
    public void testModificationTimestampSetOnNewObjectCreate() throws InterruptedException {
        Student student = factory.newStudentBuilder(testUser1).withFirstName("X").withLastName("Y").build();
        final DateTime initialTimestamp = student.getModificationTimestamp();
        assertNotNull(initialTimestamp);
        Thread.sleep(1);
        student = rwDAO.create(student);
        assertTrue(student.getModificationTimestamp().isAfter(initialTimestamp));
    }

    @Test
    public void testModificationTimestampSetOnUppdate() throws InterruptedException {
        Student student = factory.newStudentBuilder(testUser1).withFirstName("X").withLastName("Y").build();
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
        ObservationCategory newCategory = rwDAO.create(factory.newObservationCategoryBuilder(testUser1).withShortName("TESTNEW").withDescription("Test New Category").build());
        Set<ObservationCategory> categories = rwDAO.getEntitiesForUser(ObservationCategory.class, testUser1);
        assertTrue(categories.contains(newCategory));
    }

    @Test
    public void testAddDuplicateCategoryCodeFail() {
        ObservationCategory newTest1, newTest2 = null, newTest3;
        newTest1 = rwDAO.create(factory.newObservationCategoryBuilder(testUser1).withShortName("TESTDUPE").withDescription("desc 1").build());

        boolean exception = false;
        try {
            newTest2 = rwDAO.create(factory.newObservationCategoryBuilder(testUser1).withShortName("TESTDUPE").withDescription("desc 2").build());
        } catch (Exception e) {
            //  Expected
            exception = true;
        }
        assertTrue(exception);
        newTest3 = rwDAO.create(factory.newObservationCategoryBuilder(testUser2).withShortName("TESTDUPE").withDescription("desc 1").build());

        Set<ObservationCategory> categories = rwDAO.getEntitiesForUser(ObservationCategory.class, testUser1);
        assertTrue(categories.contains(newTest1));
        assertFalse(categories.contains(newTest2));
        categories = rwDAO.getEntitiesForUser(ObservationCategory.class, testUser2);
        assertTrue(categories.contains(newTest3));
    }

    //  TODO - actual photo
    @Test
    public void testCreatePhotoForStudent() {
        Photo photo = rwDAO.create(factory.newPhotoBuilder(testUser1).withDescription("Create Test").withTimestamp(new LocalDateTime()).withPhotoFor(testStudentForU1).withMimeType(PNG).withImageData(someBytes).build());
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
        Photo photo = rwDAO.create(factory.newPhotoBuilder(testUser1).withDescription("Create Test").withTimestamp(new LocalDateTime()).withPhotoFor(testClassList1ForU1).withMimeType(PNG).withImageData(someBytes).build());
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
        Photo photo = rwDAO.create(factory.newPhotoBuilder(testUser1).withDescription("Create Test").withMimeType(PNG).withImageData(someBytes).withTimestamp(new LocalDateTime()).withPhotoFor(testObservationForU1).build());
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
        Photo photo = rwDAO.create(factory.newPhotoBuilder(testUser1).withDescription("UpdateTest").withTimestamp(new LocalDateTime()).withPhotoFor(testStudentForU1).withMimeType(PNG).withImageData(someBytes).build());
        Set<Photo> activePhotos = rwDAO.getActiveEntitiesForUser(Photo.class, testUser1);
        Set<Photo> archivePhotos = rwDAO.getArchivedEntitiesForUser(Photo.class, testUser1);
        assertTrue(activePhotos.contains(photo));
        assertFalse(archivePhotos.contains(photo));

        photo.setDescription("Archived");
        photo.setArchived(true);
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
        final ObservationCategory social = testOCsForU1.get("PSE");
        final ObservationCategory kauw = testOCsForU1.get("KUW");
        final String comment = "Test Observation";
        Observation o = rwDAO.create(factory.newObservationBuilder(testUser1).withFollowUpNeeded(false).withObservationTimestamp(new LocalDateTime()).addCategory(social).addCategory(kauw).withComment(comment).withObservationSubject(testStudentForU1).build());
        assertEquals(comment, o.getComment());
        assertEquals(2, o.getCategories().size());
        assertTrue(o.getCategories().contains(social));
        assertTrue(o.getCategories().contains(kauw));
        assertFalse(o.isFollowUpNeeded());
        assertNull(o.getFollowUpForObservation());
        assertNull(o.getFollowUpReminder());
        assertEquals(testStudentForU1, o.getObservationSubject());
    }

    @Test
    public void testModifyObservation() {
        final ObservationCategory social = testOCsForU1.get("PSE");
        final ObservationCategory kauw = testOCsForU1.get("KUW");
        final ObservationCategory lang = testOCsForU1.get("CLL");
        final String comment = "Test Observation";
        Observation o = rwDAO.create(factory.newObservationBuilder(testUser1).withFollowUpNeeded(false).withObservationTimestamp(new LocalDateTime()).addCategory(social).addCategory(kauw).withComment(comment).withObservationSubject(testStudentForU1).build());
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
        final ObservationCategory social = testOCsForU1.get("PSE");
        final ObservationCategory kauw = testOCsForU1.get("KUW");
        final String comment1 = "Test Observation 1";
        final String comment2 = "Test Observation 2";
        Observation o1 = rwDAO.create(factory.newObservationBuilder(testUser1).withFollowUpNeeded(false).withObservationTimestamp(new LocalDateTime()).addCategory(social).addCategory(kauw).withComment(comment1).withObservationSubject(testStudentForU1).build());
        Observation o2 = rwDAO.create(factory.newObservationBuilder(testUser1).withFollowUpNeeded(false).withObservationTimestamp(new LocalDateTime()).addCategory(social).addCategory(kauw).withComment(comment2).withObservationSubject(testStudentForU1).build());
        o2.setFollowUpNeeded(true);
        o2.setFollowUpForObservation(o1);
        final LocalDate reminderDate = new LocalDate(2012, 11, 12);
        o2.setFollowUpReminder(reminderDate);
        o2.addCategories(testOCsForU1.values());
        rwDAO.update(Arrays.asList(o1, o2));
        o1 = rwDAO.get(Observation.class, o1.getId());
        o2 = rwDAO.get(Observation.class, o2.getId());
        assertEquals(o2.getFollowUpForObservation(), o1);
        assertNull(o1.getFollowUpForObservation());
        assertTrue(o2.isFollowUpNeeded());
        assertFalse(o1.isFollowUpNeeded());
        assertEquals(o2.getFollowUpReminder(), reminderDate);
    }

    @Test
    public void testDeleteLinkedObservations() {
        final ObservationCategory social = testOCsForU1.get("CLL");
        final ObservationCategory kauw = testOCsForU1.get("KUW");
        final String comment1 = "Test Observation 1";
        final String comment2 = "Test Observation 2";
        Observation o1 = rwDAO.create(factory.newObservationBuilder(testUser1).withFollowUpNeeded(false).withObservationTimestamp(new LocalDateTime()).addCategory(social).addCategory(kauw).withComment(comment1).withObservationSubject(testStudentForU1).build());
        Observation o2 = rwDAO.create(factory.newObservationBuilder(testUser1).withFollowUpNeeded(false).withObservationTimestamp(new LocalDateTime()).addCategory(social).addCategory(kauw).withComment(comment2).withObservationSubject(testStudentForU1).build());
        o2.setFollowUpForObservation(o1);
        rwDAO.update(Arrays.asList(o1, o2));

        rwDAO.delete(o1);
        o2 = rwDAO.get(Observation.class, o2.getId());
        assertNull(o2.getFollowUpForObservation());
    }

    @Test
    public void testGetModifiedSince() throws InterruptedException {
        AppUser updateUser = rwDAO.create(factory.newAppUserBuilder().withPassword("pass").withEmailAddress("updateUser@delete.test").withFirstName("delete").withLastName("delete").build());
        DateTime firstTS = new DateTime();
        Thread.sleep(1);
        ObservationCategory oc = rwDAO.create(factory.newObservationCategoryBuilder(updateUser).withShortName("X").withDescription("X").build());
        ClassList cl = rwDAO.create(factory.newClassListBuilder(updateUser).withDescription("CL").build());
        Student s = rwDAO.create(factory.newStudentBuilder(updateUser).withFirstName("A").withLastName("B").build());
        Photo p = rwDAO.create(factory.newPhotoBuilder(updateUser).withDescription("D").withMimeType(PNG).withImageData(someBytes).withPhotoFor(s).build());
        Observation o = rwDAO.create(factory.newObservationBuilder(updateUser).withComment("T").withObservationSubject(cl).build());
        cl = rwDAO.get(ClassList.class, cl.getId());  //  Observation made it dirty need to re-read for compare to work

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
        AppUser deleteUserThings = rwDAO.create(factory.newAppUserBuilder().withPassword("pass").withEmailAddress("deleteUserThings@delete.test").withFirstName("delete").withLastName("delete").build());
        ObservationCategory oc = rwDAO.create(factory.newObservationCategoryBuilder(deleteUserThings).withShortName("X").withDescription("X").build());
        ClassList cl = rwDAO.create(factory.newClassListBuilder(deleteUserThings).withDescription("CL").build());
        Student s = rwDAO.create(factory.newStudentBuilder(deleteUserThings).withFirstName("A").withLastName("B").build());
        Photo p = rwDAO.create(factory.newPhotoBuilder(deleteUserThings).withDescription("D").withImageData(someBytes).withMimeType(PNG).withPhotoFor(s).build());
        Observation o = rwDAO.create(factory.newObservationBuilder(deleteUserThings).withComment("T").withObservationSubject(cl).build());

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
        AppUser deleteUser1 = rwDAO.create(factory.newAppUserBuilder().withPassword("pass").withEmailAddress("delete1@delete.test").withFirstName("delete").withLastName("delete").build());
        AppUser deleteUser2 = rwDAO.create(factory.newAppUserBuilder().withPassword("pass").withEmailAddress("delete2@delete.test").withFirstName("delete").withLastName("delete").build());
        observationCategoryHelper.createDefaultCategoriesForUser(deleteUser1);
        Set<ObservationCategory> deleteCategories = rwDAO.getEntitiesForUser(ObservationCategory.class, deleteUser1);
        ClassList cl = rwDAO.create(factory.newClassListBuilder(deleteUser1).withDescription("delete").build());
        Student student = factory.newStudentBuilder(deleteUser1).withFirstName("deleteS1").withLastName("deleteS1").addClassList(cl).build();
        Student s1 = rwDAO.create(student);
        student = factory.newStudent(deleteUser1);
        student.setFirstName("deleteS2");
        student.setLastName("deleteS2");
        student.addClassList(cl);
        Student s2 = rwDAO.create(student);
        Photo p = rwDAO.create(factory.newPhotoBuilder(deleteUser1).withPhotoFor(cl).withDescription("deletePhoto").withImageData(someBytes).withMimeType(PNG).build());
        Observation o = rwDAO.create(factory.newObservationBuilder(deleteUser1).withObservationSubject(cl).withObservationTimestamp(new LocalDateTime()).withCategories(deleteCategories).withComment("delete").build());
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
        AppUser appUser = rwDAO.create(factory.newAppUserBuilder().withPassword("pass").withFirstName("Double").withLastName("Delete").withEmailAddress("delete@double.com").build());
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
        rwDAO.create(Arrays.asList(factory.newDeletedObjectBuilder(testUser2).withDeletedId("TEST").build()));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testExplicitlyCreatingADeletedObjectExceptions() {
        rwDAO.create(factory.newDeletedObjectBuilder(testUser2).withDeletedId("TEST").build());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testExplicitlyUpdatingADeletedObjectExceptions() {
        ClassList cl = rwDAO.create(factory.newClassListBuilder(testUser2).withDescription("CLTODELETE1").build());
        rwDAO.delete(cl);
        Set<DeletedObject> deleted = rwDAO.getEntitiesForUser(DeletedObject.class, testUser2);
        assertFalse(deleted.isEmpty());
        DeletedObject dobj = deleted.iterator().next();
        dobj.setModificationTimestamp(dobj.getModificationTimestamp().plusMillis(1));
        rwDAO.update(dobj);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testExplicitlyUpdatingAsListADeletedObjectExceptions() {
        ClassList cl = rwDAO.create(factory.newClassListBuilder(testUser2).withDescription("CLTODELETE1").build());
        rwDAO.delete(cl);
        Set<DeletedObject> deleted = rwDAO.getEntitiesForUser(DeletedObject.class, testUser2);
        assertFalse(deleted.isEmpty());
        DeletedObject dobj = deleted.iterator().next();
        dobj.setModificationTimestamp(dobj.getModificationTimestamp().plusMillis(1));
        rwDAO.update(Arrays.asList(dobj));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testExplicitlyDeletingADeletedObjectExceptions() {
        ClassList cl = rwDAO.create(factory.newClassListBuilder(testUser2).withDescription("CLTODELETE1").build());
        rwDAO.delete(cl);
        Set<DeletedObject> deleted = rwDAO.getEntitiesForUser(DeletedObject.class, testUser2);
        assertFalse(deleted.isEmpty());
        DeletedObject dobj = deleted.iterator().next();
        dobj.setModificationTimestamp(dobj.getModificationTimestamp().plusMillis(1));
        rwDAO.delete(dobj);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testExplicitlyDeletingAsListADeletedObjectExceptions() {
        ClassList cl = rwDAO.create(factory.newClassListBuilder(testUser2).withDescription("CLTODELETE1").build());
        rwDAO.delete(cl);
        Set<DeletedObject> deleted = rwDAO.getEntitiesForUser(DeletedObject.class, testUser2);
        assertFalse(deleted.isEmpty());
        DeletedObject dobj = deleted.iterator().next();
        dobj.setModificationTimestamp(dobj.getModificationTimestamp().plusMillis(1));
        rwDAO.delete(Arrays.asList(dobj));
    }
}
