package com.jtbdevelopment.e_eye_o.DAO

import com.jtbdevelopment.e_eye_o.DAO.helpers.ArchiveHelper
import com.jtbdevelopment.e_eye_o.DAO.helpers.DeletionHelper
import com.jtbdevelopment.e_eye_o.entities.*
import com.jtbdevelopment.e_eye_o.serialization.JSONIdObjectSerializer
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
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
        createAppUserSettings(user) //  Not in history

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
    public void testBasicStudentSimpleHistory() {
        String baseName = "SHistStu"
        AppUser user = createUser(baseName)
        ClassList class1 = createClassList(user, baseName + "1")
        ClassList class2 = createClassList(user, baseName + "2")
        DateTime since = DateTime.now()
        Thread.sleep(1000)
        Student update
        Student v1 = createStudent(user, baseName)
        update = rwDAO.get(Student, v1.id)
        update.lastName = "new desc"
        update.addClassList(class1)
        Student v2 = rwDAO.update(user, update)
        archiveHelper.flipArchiveStatus(v2)
        Student v3 = rwDAO.get(Student, v2.id)
        archiveHelper.flipArchiveStatus(v2)
        Student v4 = rwDAO.get(Student, v2.id)
        update = rwDAO.get(Student, v1.id)
        update.setClassLists([class2] as Set)
        Student v5 = rwDAO.update(user, update)
        update.setClassLists([class2, class1] as Set)
        Student v6 = rwDAO.update(user, update)
        deletionHelper.delete(v6)

        deepCompare(user, since, [v1, v2, v3, v4, v5, v6, fakeDeleted(user, v1.id)])
    }

    @Test(groups = ["integration"])
    public void testBasicObservationSimpleHistory() {
        String baseName = "SHistObs"
        AppUser user = createUser(baseName)
        Student student = createStudent(user, baseName)
        ObservationCategory cat1 = createCategory(user, baseName, "SH1")
        ObservationCategory cat2 = createCategory(user, baseName, "SH2")
        //  future obs keeps out updates on student
        createObservation(user, null, student, baseName, false, LocalDateTime.now().plusYears(1))
        DateTime since = DateTime.now()
        Thread.sleep(1000)
        Observation update
        Observation v1 = createObservation(user, cat1, student, baseName)
        update = rwDAO.get(Observation, v1.id)
        update.comment = "new comments"
        update.observationTimestamp = LocalDateTime.now().minusMonths(3).minusDays(1)
        update.addCategory(cat2)
        Observation v2 = rwDAO.update(user, update)
        archiveHelper.flipArchiveStatus(v2)
        Observation v3 = rwDAO.get(Observation, v2.id)
        archiveHelper.flipArchiveStatus(v2)
        Observation v4 = rwDAO.get(Observation, v2.id)
        update = rwDAO.get(Observation, v1.id)
        update.setCategories([] as Set)
        update.setSignificant(!update.significant)
        update.setObservationTimestamp(update.observationTimestamp.minusDays(10))
        Observation v5 = rwDAO.update(user, update)
        update.setCategories([cat2] as Set)
        Observation v6 = rwDAO.update(user, update)
        deletionHelper.delete(v6)

        deepCompare(user, since, [v1, v2, v3, v4, v5, v6, fakeDeleted(user, v1.id)])
    }

    @Test(groups = ["integration"])
    public void testBasicPhotoSimpleHistory() {
        String baseName = "SHistPhoto"
        AppUser user = createUser(baseName)
        ClassList classList = createClassList(user, baseName)
        DateTime since = DateTime.now()
        Thread.sleep(1000)
        Photo update
        Photo v1 = createPhoto(user, classList, baseName)
        update = rwDAO.get(Photo, v1.id)
        update.description = "new desc"
        update.timestamp = LocalDateTime.now().minusMonths(3).minusDays(1)
        Photo v2 = rwDAO.update(user, update)
        archiveHelper.flipArchiveStatus(v2)
        Photo v3 = rwDAO.get(Photo, v2.id)
        archiveHelper.flipArchiveStatus(v2)
        Photo v4 = rwDAO.get(Photo, v2.id)
        deletionHelper.delete(v4)

        deepCompare(user, since, [v1, v2, v3, v4, fakeDeleted(user, v1.id)])
    }

    @Test(groups = ["integration"])
    public void testUpdatesFromObservations() {
        String baseName = "BasicObs"
        AppUser user = createUser(baseName)
        DateTime since = DateTime.now()
        Thread.sleep(1000)
        ClassList cl1 = createClassList(user, baseName)
        Student st1 = createStudent(user, baseName)
        ObservationCategory cat1 = createCategory(user, baseName, "OC1")
        Photo p1 = createPhoto(user, cl1, baseName)
        Semester sem1 = createSemester(user, baseName, LocalDate.now().minusYears(1), LocalDate.now())
        Observation o11 = createObservation(user, cat1, st1, baseName)
        Student st2 = rwDAO.get(Student, st1.id)
        Observation o21 = createObservation(user, cat1, st2, baseName, false, LocalDateTime.now().minusYears(1))
        Observation o31 = createObservation(user, cat1, st2, baseName, false, LocalDateTime.now().minusYears(2))
        Observation o22 = rwDAO.get(Observation, o21.id)
        o22.observationTimestamp = LocalDateTime.now().plusYears(1)
        o22 = rwDAO.update(user, o22)
        Student st3 = rwDAO.get(Student, st1.id)
        archiveHelper.flipArchiveStatus(o22)
        Observation o23 = rwDAO.get(Observation, o22.id)
        deletionHelper.delete(o23)
        Student st4 = rwDAO.get(Student, st1.id)

        deepCompare(user,
                since,
                [cl1, st1, cat1, p1, sem1],
                [o11, st2] as Set,
                [o21, o31],
                [st3, o22] as Set,
                [o23],
                [fakeDeleted(user, o23.id), st4] as Set
        )
    }

    //  Var args of lists and sets making up total results that should come back
    //  List where order is expected, set where order is unimportant
    //  However each collection is still order dependent
    private void deepCompare(AppUser user, DateTime since, Collection... expecteds) {
        List queried = rwDAO.getModificationsSince(user, since, "", 0)

        //  DeletedObjects may or may not be real depending on DAO - strip ID out
        List delObjFixed = queried.collect {
            if (it in DeletedObject) {
                factory.newDeletedObjectBuilder(user).withDeletedId(it.deletedId).withArchived(it.archived).withModificationTimestamp(it.modificationTimestamp).withId("").build()
            } else {
                it
            }
        }.toList()

        int expectedSize = 0;
        expecteds.each { expectedSize += it.size() }
        //  Shallow check compares id orders and sizes but not fields
        assert expectedSize == delObjFixed.size()

        //  Break up actuals into lists and sets
        ArrayList actuals = breakupActuals(delObjFixed, expecteds)

        int index = -1;
        expecteds.each {
            expected ->
                ++index
                def actual = actuals.get(index)
                if (expected in Set) {
                    expected.each {
                        e ->
                            def a = actual.find { it == e }
                            compareObjects(e, a)
                    }
                } else {
                    int subIndex = -1
                    expected.each {
                        e ->
                            ++subIndex
                            def a = ((List) actual).get(subIndex)
                            compareObjects(e, a)
                    }
                }
        }
    }

    ArrayList breakupActuals(delObjFixed, Collection... expecteds) {
        def actuals = [];
        int start = 0;
        expecteds.each {
            actuals.add(delObjFixed.subList(start, start + it.size()))
            start += it.size()
        }
        actuals
    }

    Map compareObjects(e, a) {
        e.properties.each {
            String key, Object value ->
                if (e in DeletedObject && key == "modificationTimestamp") {
                    return
                }
                assert e."$key" == a."$key", "Failure on $key object " + serializer.write(e) + " vs. " + serializer.write(a)
        }
    }

    private DeletedObject fakeDeleted(final AppUser user, final String delId) {
        factory.newDeletedObjectBuilder(user).withDeletedId(delId).withId("").build()
    }

}
