package com.jtbdevelopment.e_eye_o.DAO.helpers

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO
import com.jtbdevelopment.e_eye_o.entities.*
import org.jmock.Expectations
import org.jmock.Mockery
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * Date: 12/27/13
 * Time: 4:52 PM
 */
abstract class AbstractArchiveHelperTest {
    public Mockery context
    protected ReadWriteDAO readWriteDAO
    protected ArchiveHelper archiveHelper
    protected AppUser userID
    protected AppUser userDAO

    abstract ArchiveHelper createArchiveHelper();

    @BeforeMethod
    public void setUp() {
        archiveHelper = createArchiveHelper()
        context = new Mockery()
        readWriteDAO = context.mock(ReadWriteDAO.class)
        userID = context.mock(AppUser.class, "UID")
        userDAO = context.mock(AppUser.class, "UDAO")
        archiveHelper.readWriteDAO = readWriteDAO
    }

    @Test
    public void testArchivingObjectNotReturnedReturns() {
        Student student = context.mock(Student.class)
        context.checking(new Expectations() {
            {
                oneOf(student).getId()
                will(returnValue("X"))
                oneOf(readWriteDAO).get(student.getClass(), "X");
                will(returnValue(null))
            }
        })
        archiveHelper.flipArchiveStatus(student)
    }

    @Test
    public void testPhotoArchive() {
        testPhotoFlip(false)
    }

    @Test
    public void testPhotoUnarchive() {
        testPhotoFlip(true)
    }

    private void testPhotoFlip(boolean initial) {
        Photo photoID = context.mock(Photo.class, "ID")
        Photo photoDAO = context.mock(Photo, "PDAO")
        setBaseArchiveExpectations(initial, photoID, photoDAO, "PID", [:], [:])
        archiveHelper.flipArchiveStatus(photoID)
    }

    @Test
    public void testTwoPhaseActivityArchive() {
        testTwoPhaseActivityFlip(false)
    }

    @Test
    public void testTwoPhaseActivityUnarchive() {
        testTwoPhaseActivityFlip(true)
    }

    private void testTwoPhaseActivityFlip(boolean initial) {
        TwoPhaseActivity activityID = context.mock(TwoPhaseActivity.class, "ID")
        TwoPhaseActivity activityDAO = context.mock(TwoPhaseActivity, "DAO")
        setBaseArchiveExpectations(initial, activityID, activityDAO, "AID", [:], [:])
        archiveHelper.flipArchiveStatus(activityID)
    }

    @Test
    public void testAppUserSetttingsArchive() {
        testSettingsFlip(false)
    }

    @Test
    public void testAppUserSetttingsUnarchive() {
        testSettingsFlip(true)
    }

    private void testSettingsFlip(boolean initial) {
        AppUserSettings settingsID = context.mock(AppUserSettings.class, "ID")
        AppUserSettings settingsDAO = context.mock(AppUserSettings, "DAO")
        setBaseArchiveExpectations(initial, settingsID, settingsDAO, "AID", [:], [:])
        archiveHelper.flipArchiveStatus(settingsID)
    }

    @Test
    public void testObservationCategoryArchive() {
        testCategoryFlip(false)
    }

    @Test
    public void testObservationCategoryUnarchive() {
        testCategoryFlip(true)
    }

    private void testCategoryFlip(boolean initial) {
        ObservationCategory categoryID = context.mock(ObservationCategory, "OCID")
        ObservationCategory categoryDAO = context.mock(ObservationCategory, "OCDAO")
        setBaseArchiveExpectations(initial, categoryID, categoryDAO, "OCID", [:], [:])
        archiveHelper.flipArchiveStatus(categoryID)
    }

    @Test
    public void testSemesterArchive() {
        testSemesterFlip(false)
    }

    @Test
    public void testSemesterUnarchive() {
        testSemesterFlip(true)
    }

    private void testSemesterFlip(boolean initial) {
        Semester semesterID = context.mock(Semester.class, "SID")
        Semester semesterDAO = context.mock(Semester, "SDAO")
        setBaseArchiveExpectations(initial, semesterID, semesterDAO, "SID", [:], [:])
        Map<String, Observation> toFlip = ["O1": context.mock(Observation, "O1"), "O2": context.mock(Observation, "O2"), "O5": context.mock(Observation, "O5")]
        Map<String, Observation> noFlip = ["O3": context.mock(Observation, "O3"), "O4": context.mock(Observation, "O4")]

        context.checking(new Expectations() {
            {
                oneOf(readWriteDAO).getAllObservationsForSemester(semesterDAO)
                will(returnValue((toFlip.values().toList() + noFlip.values().toList()).toSet()))
                toFlip.each {
                    oneOf(it.value).isArchived()
                    will(returnValue(initial))
                    Photo pFlip = context.mock(Photo, it.key + "P1")
                    Photo pNoFlip = context.mock(Photo, it.key + "P2")
                    setBaseArchiveExpectations(initial, it.value, it.value, it.key, [(it.key + "P1"): pFlip], [(it.key + "P2"): pNoFlip])
                }
                noFlip.each {
                    oneOf(it.value).isArchived()
                    will(returnValue(!initial))
                }
            }
        })
        archiveHelper.flipArchiveStatus(semesterID)
    }

    @Test
    public void testStudentArchive() {
        testStudentFlip(false)
    }

    @Test
    public void testStudentUnarchive() {
        testStudentFlip(true)
    }

    private void testStudentFlip(boolean initial) {
        Student studentID = context.mock(Student.class, "SID")
        Student studentDAO = context.mock(Student, "SDAO")
        setBaseArchiveExpectations(initial, studentID, studentDAO, "SID", [:], [:])
        setObservableExpectations(initial, "SID", studentDAO)
        archiveHelper.flipArchiveStatus(studentID)
    }

    @Test
    public void testClassListArchive() {
        testClassListFlip(false)
    }

    @Test
    public void testClassListUnarchive() {
        testClassListFlip(true)
    }

    private void testClassListFlip(boolean initial) {
        ClassList classListID = context.mock(ClassList, "CLID")
        ClassList classListDAO = context.mock(ClassList, "CLDAO")
        setBaseArchiveExpectations(initial, classListID, classListDAO, "CLID", [:], [:])
        setObservableExpectations(initial, "CLID", classListDAO)
        Map<String, Student> flip = [
                "S1": context.mock(Student, "S1"),
                "S2": context.mock(Student, "S2")
        ]
        Map<String, Student> noFlipStatus = [
                "S3": context.mock(Student, "S3"),
                "S4": context.mock(Student, "S4")
        ]
        Map<String, Student> noFlipSize = [
                "S5": context.mock(Student, "S5")
        ]
        context.checking(new Expectations() {
            {
                oneOf(readWriteDAO).getAllStudentsForClassList(classListDAO)
                will(returnValue(flip.values().toSet() + noFlipSize.values().toSet() + noFlipStatus.values().toSet()))
                flip.each {
                    oneOf(it.value).isArchived()
                    will(returnValue(initial))
                    oneOf(it.value).getActiveClassLists()
                    will(returnValue([classListDAO] as Set))
                    setBaseArchiveExpectations(initial, it.value, it.value, it.key, [:], [:])
                    setObservableExpectations(initial, it.key, it.value)
                }
                noFlipStatus.each {
                    oneOf(it.value).isArchived()
                    will(returnValue(!initial))
                }
                noFlipSize.each {
                    oneOf(it.value).isArchived()
                    will(returnValue(initial))
                    if (!initial) {
                        oneOf(it.value).getActiveClassLists()
                        will(returnValue([] as Set))
                    } else {
                        setBaseArchiveExpectations(initial, it.value, it.value, it.key, [:], [:])
                        setObservableExpectations(initial, it.key, it.value)
                    }
                }
            }
        })
        archiveHelper.flipArchiveStatus(classListID)
    }

    private void setObservableExpectations(boolean initial, String id, Observable observable) {
        Map<String, Observation> toFlip = [
                (id + "O1"): context.mock(Observation, id + "O1"),
                (id + "O2"): context.mock(Observation, id + "O2"),
                (id + "O5"): context.mock(Observation, id + "O5")
        ]
        Map<String, Observation> noFlip = [
                (id + "O3"): context.mock(Observation, id + "O3"),
                (id + "O4"): context.mock(Observation, id + "O4")
        ]
        context.checking(new Expectations() {
            {
                oneOf(readWriteDAO).getAllObservationsForEntity(observable)
                will(returnValue(toFlip.values().toSet() + noFlip.values().toSet()))
                toFlip.each {
                    oneOf(it.value).isArchived()
                    will(returnValue(initial))
                    Photo pFlip = context.mock(Photo, it.key + "P1")
                    Photo pNoFlip = context.mock(Photo, it.key + "P2")
                    setBaseArchiveExpectations(initial, it.value, it.value, it.key, [(it.key + "P1"): pFlip], [(it.key + "P2"): pNoFlip])
                }
                noFlip.each {
                    oneOf(it.value).isArchived()
                    will(returnValue(!initial))
                }
            }
        })
    }

    @Test
    public void testObservationArchive() {
        testObservationFlip(false)
    }

    @Test
    public void testObservationUnarchive() {
        testObservationFlip(true)
    }

    private void testObservationFlip(boolean b) {
        Observation observationID = context.mock(Observation.class, "OID")
        Observation observationDAO = context.mock(Observation, "ODAO")
        setBaseArchiveExpectations(
                b,
                observationID,
                observationDAO,
                "OID",
                [
                        "O1P1": context.mock(Photo, "O1P1"),
                        "O1P2": context.mock(Photo, "O1P2")
                ],
                [
                        "O1P3": context.mock(Photo, "O1P3"),
                        "O1P4": context.mock(Photo, "O1P4")
                ]
        )
        archiveHelper.flipArchiveStatus(observationID)
    }

    private void setBaseArchiveExpectations(
            boolean initialValue,
            AppUserOwnedObject idObject,
            AppUserOwnedObject idObjectDAO,
            String id,
            Map<String, Photo> photosToFlip,
            Map<String, Photo> photosToLeave) {
        context.checking(new Expectations() {
            {
                oneOf(idObjectDAO).isArchived()
                will(returnValue(initialValue))
                oneOf(idObjectDAO).setArchived(!initialValue)
                oneOf(idObject).getId()
                will(returnValue(id))
                oneOf(readWriteDAO).get(idObject.getClass(), id)
                will(returnValue(idObjectDAO))
                oneOf(readWriteDAO).getAllPhotosForEntity(idObjectDAO, 0, 0)
                will(returnValue(photosToFlip.values().toSet() + photosToLeave.values().toSet()))
                photosToFlip.each {
                    oneOf(it.value).isArchived()
                    will(returnValue(initialValue))
                    setBaseArchiveExpectations(initialValue, it.value, it.value, it.key, [:], [:])
                }
                photosToLeave.each {
                    oneOf(it.value).isArchived()
                    will(returnValue(!initialValue))
                }
                oneOf(readWriteDAO).trustedUpdate(idObjectDAO)
                will(returnValue(idObjectDAO))
            }
        })
    }
}
