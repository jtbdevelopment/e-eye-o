package com.jtbdevelopment.e_eye_o.DAO

import com.jtbdevelopment.e_eye_o.DAO.helpers.ArchiveHelper
import com.jtbdevelopment.e_eye_o.DAO.helpers.DeletionHelper
import com.jtbdevelopment.e_eye_o.entities.*
import com.jtbdevelopment.e_eye_o.serialization.JSONIdObjectSerializer
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.springframework.beans.factory.annotation.Autowired
import org.testng.annotations.Test

/**
 * Date: 12/29/13
 * Time: 8:55 PM
 *
 * This is a more detailed test of the getModifiedSince feature
 */
abstract class AbstractHistoryIntegration extends AbstractIntegration {
    @Autowired
    DeletionHelper deletionHelper

    @Autowired
    ArchiveHelper archiveHelper

    @Autowired
    JSONIdObjectSerializer serializer

    @Test(groups = ["integration"])
    public void testBasicCreates() {
        String baseName = "BasicCreate"
        AppUser user = createUser(baseName)
        DateTime since = DateTime.now()
        Thread.sleep(1000)
        ClassList cl = createClassList(user, baseName)
        Student student = createStudent(user, baseName)
        ObservationCategory category = createCategory(user, baseName, "OC1")
        Photo photo = createPhoto(user, cl, baseName)
        Semester semester = createSemester(user, baseName, LocalDate.now().minusYears(1), LocalDate.now())
        AppUserSettings settings = createAppUserSettings(user) //  Not in history

        deepCompare(user, since, [cl, student, category, photo, semester])
    }

    @Test(groups = ["integration"])
    public void testBasicClassListSimpleHistory() {
        String baseName = "SHistCL"
        AppUser user = createUser(baseName)
        DateTime since = DateTime.now()
        Thread.sleep(1000)
        ClassList update
        ClassList v1 = createClassList(user, baseName)
        update = rwDAO.get(ClassList, v1.id)
        update.description = "new desc"
        ClassList v2 = rwDAO.update(user, update)
        archiveHelper.flipArchiveStatus(v2)
        ClassList v3 = rwDAO.get(ClassList, v2.id)
        archiveHelper.flipArchiveStatus(v2)
        ClassList v4 = rwDAO.get(ClassList, v2.id)
        deletionHelper.delete(v4)

        deepCompare(user, since, [v1, v2, v3, v4, fakeDeleted(user, v4.id)])
    }

    @Test(groups = ["integration"])
    public void testBasicSemesterSimpleHistory() {
        String baseName = "SHistSem"
        AppUser user = createUser(baseName)
        DateTime since = DateTime.now()
        Thread.sleep(1000)
        Semester update
        Semester v1 = createSemester(user, baseName, LocalDate.now().minusMonths(1), LocalDate.now())
        update = rwDAO.get(Semester, v1.id)
        update.description = "new desc"
        update.start = LocalDate.now().minusMonths(5)
        Semester v2 = rwDAO.update(user, update)
        archiveHelper.flipArchiveStatus(v2)
        Semester v3 = rwDAO.get(Semester, v2.id)
        archiveHelper.flipArchiveStatus(v2)
        Semester v4 = rwDAO.get(Semester, v2.id)
        update = rwDAO.get(Semester, v1.id)
        update.end = LocalDate.now().plusDays(5)
        Semester v5 = rwDAO.update(user, update)
        deletionHelper.delete(v5)

        deepCompare(user, since, [v1, v2, v3, v4, v5, fakeDeleted(user, v5.id)])
    }

    @Test(groups = ["integration"])
    public void testBasicCategorySimpleHistory() {
        String baseName = "SHistCat"
        AppUser user = createUser(baseName)
        DateTime since = DateTime.now()
        Thread.sleep(1000)
        ObservationCategory update
        ObservationCategory v1 = createCategory(user, baseName, "SCH")
        update = rwDAO.get(ObservationCategory, v1.id)
        update.description = "new desc"
        ObservationCategory v2 = rwDAO.update(user, update)
        archiveHelper.flipArchiveStatus(v2)
        ObservationCategory v3 = rwDAO.get(ObservationCategory, v2.id)
        archiveHelper.flipArchiveStatus(v2)
        ObservationCategory v4 = rwDAO.get(ObservationCategory, v2.id)
        update = rwDAO.get(ObservationCategory, v1.id)
        update.shortName = "SCHN"
        ObservationCategory v5 = rwDAO.update(user, update)
        deletionHelper.delete(v5)

        deepCompare(user, since, [v1, v2, v3, v4, v5, fakeDeleted(user, v5.id)])
    }

    @Test(groups = ["integration"])
    public void testBasicUpdatesFromObservations() {
        String baseName = "BasicObs"
        AppUser user = createUser(baseName)
        DateTime since = DateTime.now()
        Thread.sleep(1000)
        ClassList cl = createClassList(user, baseName)
        Student student = createStudent(user, baseName)
        Student studentV1 = rwDAO.get(Student, student.id)
        ObservationCategory category = createCategory(user, baseName, "OC1")
        Photo photo = createPhoto(user, cl, baseName)
        Semester semester = createSemester(user, baseName, LocalDate.now().minusYears(1), LocalDate.now())
        Observation observation = createObservation(user, category, student, baseName)
        Student studentV2 = rwDAO.get(Student, student.id)

        deepCompare(user, since, [cl, studentV1, category, photo, semester, observation, studentV2])
    }

    private void deepCompare(AppUser user, DateTime since, List expected) {
        List actual = rwDAO.getModificationsSince(user, since, "", 0)

        //  DeletedObjects may or may not be real depending on DAO - strip ID out
        actual = actual.collect {
            if (it in DeletedObject) {
                factory.newDeletedObjectBuilder(user).withDeletedId(it.deletedId).withArchived(it.archived).withModificationTimestamp(it.modificationTimestamp).withId("").build()
            } else {
                it
            }
        }.toList()
        //  Shallow check compares id orders and sizes but not fields
        for (int i = 0; i < expected.size(); ++i) {
            assert expected[i] == actual[i]
        }
        assert expected == actual


        int index = -1;
        expected.each {
            e ->
                ++index
                def a = actual.get(index)
                e.properties.each {
                    String key, Object value ->
                        if (e in DeletedObject && key == "modificationTimestamp") {
                            return
                        }
                        assert e."$key" == a."$key", "Failure on $key for $index object " + serializer.write(e) + " vs. " + serializer.write(a)
                }
        }
    }

    private DeletedObject fakeDeleted(final AppUser user, final String delId) {
        factory.newDeletedObjectBuilder(user).withDeletedId(delId).withId("").build()
    }

}
