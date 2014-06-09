package com.jtbdevelopment.e_eye_o.DAO

import com.jtbdevelopment.e_eye_o.DAO.helpers.DeletionHelper
import com.jtbdevelopment.e_eye_o.entities.*
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import org.springframework.beans.factory.annotation.Autowired
import org.testng.annotations.Test

/**
 * Date: 12/29/13
 * Time: 4:04 PM
 *
 * Test uses modified since function but does not accurately test it for ordering
 */
abstract class AbstractDeletionIntegration extends AbstractIntegration {
    @Autowired
    DeletionHelper deletionHelper

    @Test(groups = ["integration"])
    public void testDeactivateUser() {
        String baseName = "Deactivate"
        AppUser user = createUser(baseName)
        assert user.active
        deletionHelper.deactivateUser(user)
        assert !(rwDAO.get(AppUser, user.id).active)
    }

    @Test(groups = ["integration"], expectedExceptions = [IllegalArgumentException])
    public void testDeletingDeletedObjectExceptions() {
        String baseName = "DeleteDelObj"
        AppUser user = createUser(baseName)
        DeletedObject deletedObject = factory.newDeletedObject(user)
        deletionHelper.delete(deletedObject)
    }

    @Test(groups = ["integration"])
    public void testDeletingObjectNotReturnedReturns() {
        String baseName = "DeleteDeleted"
        AppUser user = createUser(baseName)
        Student student = createStudent(user, baseName)
        rwDAO.trustedDelete(student)
        DateTime since = DateTime.now()
        Thread.sleep(10)
        deletionHelper.delete(student)
        assert [] == rwDAO.getModificationsSince(user, since, "", 0)
    }

    @Test(groups = ["integration"])
    public void testPhotoDeletion() {
        String baseName = "DeletePhoto"
        AppUser user = createUser(baseName)
        Student student = createStudent(user, baseName)
        Photo photo = createPhoto(user, student, baseName)
        assert [photo] as Set == rwDAO.getAllPhotosForEntity(student, 0, 0)
        assert [photo, student] as Set == rwDAO.getEntitiesForUser(AppUserOwnedObject, user, 0, 0)
        deleteAndAssert(photo, user, [], [photo.id], [student] as Set)
    }

    @Test(groups = ["integration"])
    public void testTwoPhaseActivityDeletion() {
        String baseName = "DeleteActivity"
        AppUser user = createUser(baseName)
        TwoPhaseActivity activity = createActivity(user)
        assert [activity] as Set == rwDAO.getEntitiesForUser(AppUserOwnedObject, user, 0, 0)
        deleteAndAssert(activity, user, [], [activity.id], [] as Set)
    }

    @Test(groups = ["integration"])
    public void testAppUserSetttingsDeletion() {
        String baseName = "DeleteSettings"
        AppUser user = createUser(baseName)
        AppUserSettings settings = createAppUserSettings(user)
        assert [settings] as Set == rwDAO.getEntitiesForUser(AppUserOwnedObject, user, 0, 0)
        //  Settings not expected to generate historical feed
        deleteAndAssert(settings, user, [], [], [] as Set)
    }

    @Test(groups = ["integration"])
    public void testSemesterDeletion() {
        String baseName = "DeleteSemester"
        AppUser user = createUser(baseName)
        Student student = createStudent(user, baseName)
        Semester semester = createSemester(user, baseName, LocalDate.now().minusDays(365), LocalDate.now())
        Observation observation = createObservation(user, null, student, baseName, false, semester.start.plusDays(1).toLocalDateTime(LocalTime.now()))
        assert [student, semester, observation] as Set == rwDAO.getEntitiesForUser(AppUserOwnedObject, user, 0, 0)
        assert [observation] as Set == rwDAO.getAllObservationsForSemester(semester)
        deleteAndAssert(semester, user, [], [semester.id], [student, observation] as Set)
    }

    @Test(groups = ["integration"])
    public void testObservationDeletion() {
        String baseName = "DeleteObs"
        AppUser user = createUser(baseName)
        Student student = createStudent(user, baseName)
        Observation observation1 = createObservation(user, null, student, baseName)
        Observation observation2 = createObservation(user, null, student, baseName, false, LocalDateTime.now().minusDays(5))
        Photo photo1 = createPhoto(user, observation1, baseName)
        Photo photo2 = createPhoto(user, observation2, baseName)
        assert [student, observation1, observation2, photo1, photo2] as Set == rwDAO.getEntitiesForUser(AppUserOwnedObject, user, 0, 0)
        assert [observation1, observation2] as Set == rwDAO.getAllObservationsForEntity(student)
        deleteAndAssert(observation1, user, [student], [observation1.id, photo1.id], [student, observation2, photo2] as Set)
    }

    @Test(groups = ["integration"])
    public void testStudentDeletion() {
        String baseName = "DeleteStudent"
        AppUser user = createUser(baseName)
        Student student = createStudent(user, baseName)
        Student studentKeep = createStudent(user, baseName)
        Observation observation1 = createObservation(user, null, student, baseName)
        Observation observation2 = createObservation(user, null, student, baseName, false, LocalDateTime.now().minusDays(5))
        Observation observationKeep = createObservation(user, null, studentKeep, baseName, false, LocalDateTime.now().minusDays(5))
        Photo photo1 = createPhoto(user, observation1, baseName)
        Photo photo2 = createPhoto(user, observation2, baseName)
        Photo photoKeep = createPhoto(user, observationKeep, baseName)
        assert [student, studentKeep, observation1, observation2, photo1, photo2, photoKeep, observationKeep] as Set == rwDAO.getEntitiesForUser(AppUserOwnedObject, user, 0, 0)
        assert [observation1, observation2] as Set == rwDAO.getAllObservationsForEntity(student)
        assert [observationKeep] as Set == rwDAO.getAllObservationsForEntity(studentKeep)
        deleteAndAssert(student, user, [], [observation1.id, photo1.id, observation2.id, photo2.id, student.id], [studentKeep, observationKeep, photoKeep] as Set)
    }

    @Test(groups = ["integration"])
    public void testClassListDeletion() {
        String baseName = "DeleteClass"
        AppUser user = createUser(baseName)
        ClassList classDelete = createClassList(user, baseName + "DEL")
        ClassList classKeep = createClassList(user, baseName + "KEEP")
        Student student1 = createStudent(user, baseName, false, [classDelete, classKeep] as Set)
        Student student2 = createStudent(user, baseName, false, [classDelete] as Set)
        Observation s1o1 = createObservation(user, null, student1, baseName)
        Observation s1o2 = createObservation(user, null, student1, baseName, false, LocalDateTime.now().minusDays(5))
        Observation s2o1 = createObservation(user, null, student2, baseName, false, LocalDateTime.now().minusDays(5))
        Photo s1o1p1 = createPhoto(user, s1o1, baseName)
        Photo s1o2p1 = createPhoto(user, s1o2, baseName)
        Photo s2o1p1 = createPhoto(user, s2o1, baseName)
        Photo clDp1 = createPhoto(user, classDelete, baseName)
        Photo clKp1 = createPhoto(user, classKeep, baseName)
        Observation cDo1 = createObservation(user, null, classDelete, baseName)
        Observation cKo2 = createObservation(user, null, classKeep, baseName)
        assert [student1, student2, s1o1, s1o2, s1o1p1, s1o2p1, s2o1p1, s2o1, clKp1, clKp1, cDo1, cKo2, clDp1, classDelete, classKeep] as Set == rwDAO.getEntitiesForUser(AppUserOwnedObject, user, 0, 0)
        deleteAndAssert(classDelete, user, [student1, student2], [classDelete.id, cDo1.id, clDp1.id], [student1, student2, s1o1, s1o2, s1o1p1, s1o2p1, s2o1p1, s2o1, clKp1, clKp1, cKo2, classKeep] as Set)
    }

    @Test(groups = ["integration"])
    public void testObservationCategoryDeletion() {
        String baseName = "DeleteOC"
        AppUser user = createUser(baseName)
        Student student1 = createStudent(user, baseName)
        Student student2 = createStudent(user, baseName)
        ObservationCategory category1 = createCategory(user, baseName, "OC1")
        ObservationCategory category2 = createCategory(user, baseName, "OC2")
        Observation s1o1 = createObservation(user, category1, student1, baseName)
        Observation s1o2 = createObservation(user, category2, student1, baseName, false, LocalDateTime.now().minusDays(5))
        Observation s2o1 = createObservation(user, category1, student2, baseName, false, LocalDateTime.now().minusDays(5))
        Photo s1o1p1 = createPhoto(user, s1o1, baseName)
        Photo s1o2p1 = createPhoto(user, s1o2, baseName)
        Photo s2o1p1 = createPhoto(user, s2o1, baseName)
        assert [student1, student2, s1o1, s1o2, s1o1p1, s1o2p1, s2o1p1, s2o1, category2, category1] as Set == rwDAO.getEntitiesForUser(AppUserOwnedObject, user, 0, 0)
        assert [s1o1, s2o1] as Set == rwDAO.getAllObservationsForObservationCategory(user, category1)
        assert [s1o2] as Set == rwDAO.getAllObservationsForObservationCategory(user, category2)
        deleteAndAssert(category1, user, [s1o1, s2o1], [category1.id], [student1, student2, s1o1, s1o2, s1o1p1, s1o2p1, s2o1p1, s2o1, category2] as Set)
    }

    @Test(groups = ["integration"])
    public void testDeletingDeletedUser() {
        String baseName = "DeleteDelUser"
        AppUser user = createUser(baseName)
        deletionHelper.deleteUser(user)

        assert null == rwDAO.get(AppUser, user.id)
        deletionHelper.deleteUser(user)
    }

    @Test(groups = ["integration"])
    public void testDeleteUser() {
        String baseName = "DeleteUser"
        AppUser user = createUser(baseName)
        ClassList cl1 = createClassList(user, baseName + "1")
        ClassList cl2 = createClassList(user, baseName + "2")
        Semester semester = createSemester(user, baseName, LocalDate.now(), LocalDate.now().plusDays(5))
        AppUserSettings settings = createAppUserSettings(user)
        TwoPhaseActivity activity = createActivity(user)
        Student student1 = createStudent(user, baseName, false, [cl1, cl2] as Set)
        Student student2 = createStudent(user, baseName, false, [cl1] as Set)
        Observation s1o1 = createObservation(user, null, student1, baseName)
        Observation s1o2 = createObservation(user, null, student1, baseName, false, LocalDateTime.now().minusDays(5))
        Observation s2o1 = createObservation(user, null, student2, baseName, false, LocalDateTime.now().minusDays(5))
        Photo s1o1p1 = createPhoto(user, s1o1, baseName)
        Photo s1o2p1 = createPhoto(user, s1o2, baseName)
        Photo s2o1p1 = createPhoto(user, s2o1, baseName)
        Photo cl1p1 = createPhoto(user, cl1, baseName)
        Photo cl2p1 = createPhoto(user, cl2, baseName)
        Observation cl1o1 = createObservation(user, null, cl1, baseName)
        Observation cl2o1 = createObservation(user, null, cl2, baseName)
        Photo cl1o1p1 = createPhoto(user, cl1o1, baseName)
        assert [activity, settings, cl1o1p1, semester, student1, student2, s1o1, s1o2, s1o1p1, s1o2p1, s2o1p1, s2o1, cl2p1, cl2p1, cl1o1, cl2o1, cl1p1, cl1, cl2] as Set == rwDAO.getEntitiesForUser(AppUserOwnedObject, user, 0, 0)
        deletionHelper.deleteUser(user)
        assert [] as Set == rwDAO.getEntitiesForUser(AppUserOwnedObject, user, 0, 0)
    }

    private void deleteAndAssert(AppUserOwnedObject toDelete, AppUser user, List<IdObject> requeryAndExpect, List expectedModifiedSince, Set expectedRemainingObjects) {
        DateTime since = DateTime.now()
        Thread.sleep(1000) //  for DBs not promising sub-second accuracy
        deletionHelper.delete(toDelete)
        expectedModifiedSince +=
                requeryAndExpect.collect { rwDAO.get(it.getClass(), it.id) }

        //  DAO may choose to generate interim updates on objects are deleted but which will also be deleted as part of this same action
        //  either way OK - so ignore them if they come in
        //  Primary case is deleting Observable - will delete Observations which may trigger updates on Observable to change last observation time
        List<Object> modifiedSince = getModified(user, since)
        List<Object> filtered = modifiedSince.findAll {
            it != null && toDelete != it
        }
        assert expectedModifiedSince as Set == filtered as Set
        assert expectedRemainingObjects == rwDAO.getEntitiesForUser(AppUserOwnedObject, user, 0, 0)
    }

    private List<Object> getModified(AppUser user, DateTime since) {
        rwDAO.getModificationsSince(user, since, "", 0).collect {
            if (it in DeletedObject) {
                it.deletedId
            } else {
                it
            }
        }
    }
}
