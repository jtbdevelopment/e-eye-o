package com.jtbdevelopment.e_eye_o.DAO.helpers

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO
import com.jtbdevelopment.e_eye_o.entities.*
import org.jmock.Expectations
import org.jmock.Mockery
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * Date: 12/15/13
 * Time: 5:52 PM
 */
abstract class AbstractDeletionHelperTest {
    public Mockery context
    protected ReadWriteDAO readWriteDAO
    protected DeletionHelper deletionHelper
    protected AppUser userID
    protected AppUser userDAO
    protected AppUser userDAOUP

    abstract DeletionHelper createDeletionHelper();

    @BeforeMethod
    public void setUp() {
        deletionHelper = createDeletionHelper()
        context = new Mockery()
        readWriteDAO = context.mock(ReadWriteDAO.class)
        userID = context.mock(AppUser.class, "UID")
        userDAO = context.mock(AppUser.class, "UDAO")
        userDAOUP = context.mock(AppUser.class, "UDAOUP")
        deletionHelper.readWriteDAO = readWriteDAO
    }

    @Test
    public void testDeactivateUser() {
        context.checking(new Expectations() {
            {
                one(userID).getId()
                will(returnValue("7"))
                one(readWriteDAO).get(AppUser.class, "7")
                will(returnValue(userDAO))
                one(userDAO).setActive(false)
                one(readWriteDAO).trustedUpdate(userDAO)
                will(returnValue(userDAOUP))
            }
        })
        deletionHelper.deactivateUser(userID)
    }

    @Test(expectedExceptions = [IllegalArgumentException])
    public void testDeletingDeletedObjectExceptions() {
        DeletedObject deletedObject = context.mock(DeletedObject.class)
        deletionHelper.delete(deletedObject)
    }

    @Test
    public void testDeletingObjectNotReturnedReturns() {
        Student student = context.mock(Student.class)
        context.checking(new Expectations() {
            {
                one(student).getId()
                will(returnValue("X"))
                one(readWriteDAO).get(student.getClass(), "X");
                will(returnValue(null))
            }
        })
        deletionHelper.delete(student)
    }

    @Test
    public void testPhotoDeletion() {
        Photo photoID = context.mock(Photo.class, "ID")
        Photo photoDAO = context.mock(Photo, "PDAO")
        setBaseDeleteExpectations(photoID, photoDAO, "PID", [:])
        deletionHelper.delete(photoID)
    }

    @Test
    public void testTwoPhaseActivityDeletion() {
        TwoPhaseActivity activityID = context.mock(TwoPhaseActivity.class, "ID")
        TwoPhaseActivity activityDAO = context.mock(TwoPhaseActivity, "DAO")
        setBaseDeleteExpectations(activityID, activityDAO, "AID", [:])
        deletionHelper.delete(activityID)
    }

    @Test
    public void testAppUserSetttingsDeletion() {
        AppUserSettings settingsID = context.mock(AppUserSettings.class, "ID")
        AppUserSettings settingsDAO = context.mock(AppUserSettings, "DAO")
        setBaseDeleteExpectations(settingsID, settingsDAO, "AID", [:])
        deletionHelper.delete(settingsID)
    }

    @Test
    public void testSemesterDeletion() {
        Semester semesterID = context.mock(Semester.class, "SID")
        Semester semesterDAO = context.mock(Semester, "SDAO")
        setBaseDeleteExpectations(semesterID, semesterDAO, "SID", [:])
        deletionHelper.delete(semesterID)
    }

    @Test
    public void testObservationDeletion() {
        Observation observationID = context.mock(Observation.class, "OID")
        Observation observationDAO = context.mock(Observation, "ODAO")
        setBaseDeleteExpectations(observationID, observationDAO, "OID", [
                "O1P1": context.mock(Photo, "O1P1"),
                "O1P2": context.mock(Photo, "O1P2")
        ])
        deletionHelper.delete(observationID)
    }

    @Test
    public void testStudentDeletion() {
        Student studentID = context.mock(Student, "SID")
        Student studentDAO = context.mock(Student, "SDAO")
        Observation observation1 = context.mock(Observation, "O1")
        Observation observation2 = context.mock(Observation, "O2")
        Observation observation3 = context.mock(Observation, "O3")
        setBaseDeleteExpectations(studentID, studentDAO, "SID", [:])
        setObservableExpectations(
                studentDAO,
                [
                        "O1": observation1,
                        "O2": observation2,
                        "O3": observation3
                ],
                [
                        (observation1): [
                                "O1P1": context.mock(Photo, "O1P1")
                        ],
                        (observation2): [
                                "O2P1": context.mock(Photo, "O2P1"),
                                "O2P2": context.mock(Photo, "O2P2")
                        ],
                        (observation3): [:]
                ])
        deletionHelper.delete(studentID)
    }

    @Test
    public void testClassListDeletion() {
        ClassList classID = context.mock(ClassList, "CLID")
        ClassList classDAO = context.mock(ClassList, "CLDAO")
        Observation observation1 = context.mock(Observation, "O1")
        Observation observation2 = context.mock(Observation, "O2")
        Observation observation3 = context.mock(Observation, "O3")
        setBaseDeleteExpectations(
                classID,
                classDAO,
                "CLID",
                [
                        "CLP1": context.mock(Photo, "CLP1"),
                        "CLP2": context.mock(Photo, "CLP2")
                ]
        )
        setClassListExpectations(
                classDAO,
                [context.mock(Student, "S1"), context.mock(Student, "S2"), context.mock(Student, "S3"), context.mock(Student, "S4")]
        )
        setObservableExpectations(
                classDAO,
                [
                        "O1": observation1,
                        "O2": observation2,
                        "O3": observation3
                ],
                [
                        (observation1): [
                                "O1P1": context.mock(Photo, "O1P1")
                        ],
                        (observation2): [
                                "O2P1": context.mock(Photo, "O2P1"),
                                "O2P2": context.mock(Photo, "O2P2")
                        ],
                        (observation3): [:]
                ])
        deletionHelper.delete(classID)
    }

    @Test
    public void testObservationCategoryDeletion() {
        ObservationCategory categoryID = context.mock(ObservationCategory, "OCID")
        ObservationCategory categoryDAO = context.mock(ObservationCategory, "OCDAO")
        setBaseDeleteExpectations(categoryID, categoryDAO, "OCID", [:])
        setObservationCategoryExpectations(categoryDAO, [context.mock(Observation, "O1"), context.mock(Observation, "O2")])
        deletionHelper.delete(categoryID)
    }

    @Test
    public void testDeletingDeletedUser() {
        context.checking(new Expectations() {
            {
                one(userID).getId()
                will(returnValue("U"))
                one(readWriteDAO).get(AppUser.class, "U")
                will(returnValue(null))
            }
        })
        deletionHelper.deleteUser(userID)
    }

    @Test
    public void testDeleteUser() {
        Map<String, Student> students = ["S1": context.mock(Student, "S1"), "S2": context.mock(Student, "S2")]
        Map<String, ClassList> classes = ["C1": context.mock(ClassList, "C1")]
        Map<String, ObservationCategory> ocs = ["OC1": context.mock(ObservationCategory, "OC1"), "OC2": context.mock(ObservationCategory.class, "OC2")]
        Map<String, Semester> semesters = ["SE1": context.mock(Semester.class, "SE1"), "SE2": context.mock(Semester.class, "SE2")]
        Map<String, TwoPhaseActivity> activities = ["A1": context.mock(TwoPhaseActivity.class, "A1"), "A2": context.mock(TwoPhaseActivity, "A2")]
        Map<String, AppUserSettings> settings = ["AUS": context.mock(AppUserSettings)]

        context.checking(new Expectations() {
            {
                one(userID).getId()
                will(returnValue("U"))
                one(readWriteDAO).get(AppUser.class, "U")
                will(returnValue(userDAO))
                one(readWriteDAO).trustedDelete(userDAO)

                int counter


                one(readWriteDAO).getEntitiesForUser(Student, userDAO, 0, 0)
                will(returnValue(students.values().toSet()))
                counter = 0
                students.each {
                    counter++
                    setBaseDeleteExpectations(it.value, it.value, it.key, [:])
                    String oid = "SO" + counter
                    Observation observation = context.mock(Observation, oid)
                    String pid = "SOP" + counter
                    setObservableExpectations(it.value, [(oid): observation], [(observation): [(pid): context.mock(Photo, pid)]])
                }

                one(readWriteDAO).getEntitiesForUser(ClassList, userDAO, 0, 0)
                will(returnValue(classes.values().toSet()))
                counter = 0
                classes.each {
                    counter++
                    setBaseDeleteExpectations(it.value, it.value, it.key, [:])
                    String oid = "CLO" + counter
                    Observation observation = context.mock(Observation, oid)
                    String pid = "CLOP" + counter
                    setObservableExpectations(it.value, [(oid): observation], [(observation): [(pid): context.mock(Photo, pid)]])
                    setClassListExpectations(it.value, [])
                }

                one(readWriteDAO).getEntitiesForUser(ObservationCategory, userDAO, 0, 0)
                will(returnValue(ocs.values().toSet()))
                ocs.each {
                    setBaseDeleteExpectations(it.value, it.value, it.key, [:])
                    setObservationCategoryExpectations(it.value, [])
                }

                one(readWriteDAO).getEntitiesForUser(AppUserSettings, userDAO, 0, 0)
                will(returnValue(settings.values().toSet()))
                settings.each {
                    setBaseDeleteExpectations(it.value, it.value, it.key, [:])
                }

                one(readWriteDAO).getEntitiesForUser(TwoPhaseActivity, userDAO, 0, 0)
                will(returnValue(activities.values().toSet()))
                activities.each {
                    setBaseDeleteExpectations(it.value, it.value, it.key, [:])
                }

                one(readWriteDAO).getEntitiesForUser(Semester, userDAO, 0, 0)
                will(returnValue(semesters.values().toSet()))
                semesters.each {
                    setBaseDeleteExpectations(it.value, it.value, it.key, [:])
                }
            }
        })
        deletionHelper.deleteUser(userID)
    }

    private void setObservationCategoryExpectations(ObservationCategory category, List<Observation> obs) {
        context.checking(new Expectations() {
            {
                one(readWriteDAO).getAllObservationsForObservationCategory(category)
                will(returnValue(obs))
                obs.each {
                    one(it).removeCategory(category)
                    one(readWriteDAO).trustedUpdate(it)
                }
            }
        })
    }

    private void setClassListExpectations(ClassList classList, List<Student> students) {
        context.checking(new Expectations() {
            {
                one(readWriteDAO).getAllStudentsForClassList(classList)
                will(returnValue(students))
                students.each {
                    one(it).removeClassList(classList)
                    one(readWriteDAO).trustedUpdate(it)
                }
            }
        })
    }

    private void setObservableExpectations(Observable daoObject, Map<String, Observation> obs, Map<Observation, Map<String, Photo>> obsPhotos) {
        context.checking(new Expectations() {
            {
                one(readWriteDAO).getAllObservationsForEntity(daoObject)
                will(returnValue(obs.values().toList()))
                obs.each { setBaseDeleteExpectations(it.value, it.value, it.key, obsPhotos.get(it.value)) }
            }
        })

    }

    private void setBaseDeleteExpectations(AppUserOwnedObject idObject, AppUserOwnedObject idObjectDAO, String id, Map<String, Photo> photosFor) {
        context.checking(new Expectations() {
            {
                one(idObject).getId()
                will(returnValue(id))
                one(readWriteDAO).get(idObject.getClass(), id)
                will(returnValue(idObjectDAO))
                one(readWriteDAO).getAllPhotosForEntity(idObjectDAO, 0, 0)
                will(returnValue(photosFor.values().toList()))
                photosFor.each { setBaseDeleteExpectations(it.value, it.value, it.key, [:]) }
                one(readWriteDAO).trustedDelete(idObjectDAO)
            }
        })
    }
}
