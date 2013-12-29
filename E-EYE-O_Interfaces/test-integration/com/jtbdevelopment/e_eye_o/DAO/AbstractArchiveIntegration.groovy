package com.jtbdevelopment.e_eye_o.DAO

import com.jtbdevelopment.e_eye_o.DAO.helpers.ArchiveHelper
import com.jtbdevelopment.e_eye_o.entities.*
import org.joda.time.LocalDate
import org.joda.time.LocalTime
import org.springframework.beans.factory.annotation.Autowired
import org.testng.annotations.Test

/**
 * Date: 12/28/13
 * Time: 8:51 PM
 */
abstract class AbstractArchiveIntegration extends AbstractIntegration {
    @Autowired
    private ArchiveHelper archiveHelper

    @Test(groups = ["integration"])
    public void testArchiveDeletedItem() {
        String baseName = "ArchiveDel" + true
        String baseShort = "ARCDEL1"
        AppUser user = createUser(baseName)
        ObservationCategory category = createCategory(user, baseName, baseShort, false)
        rwDAO.trustedDelete(category)
        assert null == rwDAO.get(ObservationCategory, category.id)
        archiveHelper.flipArchiveStatus(category)
    }

    @Test(groups = ["integration"])
    public void testPhotoArchive() {
        testPhotoFlip(false)
    }

    @Test(groups = ["integration"])
    public void testPhotoUnarchive() {
        testPhotoFlip(true)
    }

    @Test(groups = ["integration"])
    public void testTwoPhaseActivityArchive() {
        testTwoPhaseActivityFlip(false)
    }

    @Test(groups = ["integration"])
    public void testTwoPhaseActivityUnarchive() {
        testTwoPhaseActivityFlip(true)
    }

    @Test(groups = ["integration"])
    public void testAppUserSetttingsArchive() {
        testSettingsFlip(false)
    }

    @Test(groups = ["integration"])
    public void testAppUserSetttingsUnarchive() {
        testSettingsFlip(true)
    }

    @Test(groups = ["integration"])
    public void testObservationCategoryArchive() {
        testCategoryFlip(false)
    }

    @Test(groups = ["integration"])
    public void testObservationCategoryUnarchive() {
        testCategoryFlip(true)
    }

    @Test(groups = ["integration"])
    public void testSemesterArchive() {
        testSemesterFlip(false)
    }

    @Test(groups = ["integration"])
    public void testSemesterUnarchive() {
        testSemesterFlip(true)
    }

    @Test(groups = ["integration"])
    public void testStudentArchive() {
        testStudentFlip(false)
    }

    @Test(groups = ["integration"])
    public void testStudentUnarchive() {
        testStudentFlip(true)
    }

    @Test(groups = ["integration"])
    public void testClassListArchive() {
        testClassListFlip(false)
    }

    @Test(groups = ["integration"])
    public void testClassListUnarchive() {
        testClassListFlip(true)
    }

    @Test(groups = ["integration"])
    public void testObservationArchive() {
        testObservationFlip(false)
    }

    @Test(groups = ["integration"])
    public void testObservationUnarchive() {
        testObservationFlip(true)
    }

    private void testClassListFlip(boolean initial) {
        String baseName = "ArchiveClass" + initial
        String baseShort = "ARCCL1"
        AppUser user = createUser(baseName)
        ObservationCategory category = createCategory(user, baseName, baseShort, initial)

        ClassList clFlip = createClassList(user, baseName, initial)
        ClassList cl2 = createClassList(user, baseName, initial)
        ClassList cl3 = createClassList(user, baseName, !initial)

        Student student1 = createStudent(user, baseName + "1", initial, [clFlip] as Set)
        Student student2 = createStudent(user, baseName + "2", !initial, [clFlip] as Set)
        Student student3 = createStudent(user, baseName + "3", initial, [clFlip, cl2] as Set)
        Student student4 = createStudent(user, baseName + "4", !initial, [clFlip, cl2] as Set)
        Student student5 = createStudent(user, baseName + "5", initial, [clFlip, cl3] as Set)
        Student student6 = createStudent(user, baseName + "6", !initial, [clFlip, cl3] as Set)

        Observation clFlipObsMatch = createObservation(user, category, clFlip, baseName, initial)
        Photo clMatchPhotoMatch = createPhoto(user, clFlipObsMatch, baseName, initial)
        Photo clMatchPhotoNoMatch = createPhoto(user, clFlipObsMatch, baseName, !initial)
        Observation clFlipObsNoMatch = createObservation(user, category, clFlip, baseName, !initial)
        Photo clNoMatchPhotoMatch = createPhoto(user, clFlipObsNoMatch, baseName, initial)
        Photo clNoMatchPhotoNoMatch = createPhoto(user, clFlipObsNoMatch, baseName, !initial)

        Photo clPhotoMatch = createPhoto(user, clFlip, baseName, initial)
        Photo clPhotoNoMatch = createPhoto(user, clFlip, baseName, !initial)
        Photo cl2PhotoMatch = createPhoto(user, cl2, baseName, initial)
        Photo cl3PhotoNoMatch = createPhoto(user, cl3, baseName, !initial)

        Observation s1Match = createObservation(user, category, student1, baseName, initial)
        Photo s1MatchPhotoMatch = createPhoto(user, s1Match, baseName, initial)
        Photo s1MatchPhotoNoMatch = createPhoto(user, s1Match, baseName, !initial)
        Observation s1NoMatch = createObservation(user, category, student1, baseName, !initial)
        Photo s1NoMatchPhotoMatch = createPhoto(user, s1NoMatch, baseName, initial)
        Photo s1NoMatchPhotoNoMatch = createPhoto(user, s1NoMatch, baseName, !initial)

        Observation s2Match = createObservation(user, category, student2, baseName, initial)
        Observation s2NoMatch = createObservation(user, category, student2, baseName, !initial)
        Observation s3Match = createObservation(user, category, student3, baseName, initial)
        Observation s3NoMatch = createObservation(user, category, student3, baseName, !initial)
        Observation s4Match = createObservation(user, category, student4, baseName, initial)
        Observation s4NoMatch = createObservation(user, category, student4, baseName, !initial)
        Observation s5Match = createObservation(user, category, student5, baseName, initial)
        Observation s5NoMatch = createObservation(user, category, student5, baseName, !initial)
        Observation s6Match = createObservation(user, category, student6, baseName, initial)
        Observation s6NoMatch = createObservation(user, category, student6, baseName, !initial)

        archiveHelper.flipArchiveStatus(clFlip)

        assert !initial == rwDAO.get(ClassList, clFlip.id).archived
        assert !initial == rwDAO.get(Student, student1.id).archived
        assert !initial == rwDAO.get(Student, student5.id).archived
        if (initial) {
            assert !initial == rwDAO.get(Student, student3.id).archived
            assert !initial == rwDAO.get(Observation, s3Match.id).archived
        } else {
            assert initial == rwDAO.get(Student, student3.id).archived
            assert initial == rwDAO.get(Observation, s3Match.id).archived
        }
        assert !initial == rwDAO.get(Observation, clFlipObsMatch.id).archived
        assert !initial == rwDAO.get(Observation, s1Match.id).archived
        assert !initial == rwDAO.get(Observation, s5Match.id).archived
        assert !initial == rwDAO.get(Photo, clMatchPhotoMatch.id).archived
        assert !initial == rwDAO.get(Photo, clPhotoMatch.id).archived
        assert !initial == rwDAO.get(Photo, s1MatchPhotoMatch.id).archived

        //  None of these should have changed
        assert category.archived == rwDAO.get(ObservationCategory, category.id).archived
        assert cl2.archived == rwDAO.get(ClassList, cl2.id).archived
        assert cl3.archived == rwDAO.get(ClassList, cl3.id).archived
        assert student2.archived == rwDAO.get(Student, student2.id).archived
        assert student4.archived == rwDAO.get(Student, student4.id).archived
        assert student6.archived == rwDAO.get(Student, student6.id).archived
        assert clFlipObsNoMatch.archived == rwDAO.get(Observation, clFlipObsNoMatch.id).archived
        assert s1NoMatch.archived == rwDAO.get(Observation, s1NoMatch.id).archived
        assert s2NoMatch.archived == rwDAO.get(Observation, s2NoMatch.id).archived
        assert s3NoMatch.archived == rwDAO.get(Observation, s3NoMatch.id).archived
        assert s4NoMatch.archived == rwDAO.get(Observation, s4NoMatch.id).archived
        assert s5NoMatch.archived == rwDAO.get(Observation, s5NoMatch.id).archived
        assert s6NoMatch.archived == rwDAO.get(Observation, s6NoMatch.id).archived
        assert s2Match.archived == rwDAO.get(Observation, s2Match.id).archived
        assert s4Match.archived == rwDAO.get(Observation, s4Match.id).archived
        assert s6Match.archived == rwDAO.get(Observation, s6Match.id).archived
        assert cl2PhotoMatch.archived == rwDAO.get(Photo, cl2PhotoMatch.id).archived
        assert cl3PhotoNoMatch.archived == rwDAO.get(Photo, cl3PhotoNoMatch.id).archived
        assert clMatchPhotoNoMatch.archived == rwDAO.get(Photo, clMatchPhotoNoMatch.id).archived
        assert clNoMatchPhotoMatch.archived == rwDAO.get(Photo, clNoMatchPhotoMatch.id).archived
        assert clNoMatchPhotoNoMatch.archived == rwDAO.get(Photo, clNoMatchPhotoNoMatch.id).archived
        assert clPhotoNoMatch.archived == rwDAO.get(Photo, clPhotoNoMatch.id).archived
        assert s1MatchPhotoNoMatch.archived == rwDAO.get(Photo, s1MatchPhotoNoMatch.id).archived
        assert s1NoMatchPhotoMatch.archived == rwDAO.get(Photo, s1NoMatchPhotoMatch.id).archived
        assert s1NoMatchPhotoNoMatch.archived == rwDAO.get(Photo, s1NoMatchPhotoNoMatch.id).archived
    }

    private void testStudentFlip(boolean initial) {
        String baseName = "ArchiveStudent" + initial
        String baseShort = "ARCSTU1"
        AppUser user = createUser(baseName)
        Student student = createStudent(user, baseName, initial)
        ObservationCategory category = createCategory(user, baseName, baseShort, initial)
        Observation observationMatch = createObservation(user, category, student, baseName, initial)
        Photo photoMatchMatch = createPhoto(user, observationMatch, baseName, initial)
        Photo photoMatchNoMatch = createPhoto(user, observationMatch, baseName, !initial)
        Observation observationNoMatch = createObservation(user, category, student, baseName, !initial)
        Photo photoNoMatchMatch = createPhoto(user, observationNoMatch, baseName, initial)
        Photo photoNoMatchNoMatch = createPhoto(user, observationNoMatch, baseName, !initial)
        archiveHelper.flipArchiveStatus(student)

        assert !initial == rwDAO.get(Student, student.id).archived
        assert !initial == rwDAO.get(Observation, observationMatch.id).archived
        assert !initial == rwDAO.get(Photo, photoMatchMatch.id).archived
        assert !initial == rwDAO.get(Photo, photoMatchNoMatch.id).archived

        //  None of these should have changed
        assert category.archived == rwDAO.get(ObservationCategory, category.id).archived
        assert observationNoMatch.archived == rwDAO.get(Observation, observationNoMatch.id).archived
        assert photoNoMatchMatch.archived == rwDAO.get(Photo, photoNoMatchMatch.id).archived
        assert photoNoMatchNoMatch.archived == rwDAO.get(Photo, photoNoMatchNoMatch.id).archived
    }

    private void testObservationFlip(boolean initial) {
        String baseName = "ArchiveObs" + initial
        String baseShort = "ARCOB1"
        AppUser user = createUser(baseName)
        ObservationCategory category = createCategory(user, baseName, baseShort, initial)
        Student student = createStudent(user, baseName, initial)
        Observation observation = createObservation(user, category, student, baseName, initial)
        Photo photoMatch = createPhoto(user, observation, baseName, initial)
        Photo photoNoMatch = createPhoto(user, observation, baseName, !initial)
        archiveHelper.flipArchiveStatus(observation)

        assert !initial == rwDAO.get(Observation, observation.id).archived
        assert !initial == rwDAO.get(Photo, photoMatch.id).archived
        assert !initial == rwDAO.get(Photo, photoNoMatch.id).archived

        //  None of these should have changed
        assert student.archived == rwDAO.get(Student, student.id).archived
        assert category.archived == rwDAO.get(ObservationCategory, category.id).archived
    }

    private void testPhotoFlip(boolean initial) {
        String baseName = "ArchivePhoto" + initial
        AppUser user = createUser(baseName)
        Student student = createStudent(user, baseName, initial)
        Photo photo = createPhoto(user, student, baseName, initial)
        archiveHelper.flipArchiveStatus(photo)
        photo = rwDAO.get(Photo, photo.id)
        assert !initial == photo.archived
    }

    private void testTwoPhaseActivityFlip(boolean initial) {
        String baseName = "ArchiveActivity" + initial
        AppUser user = createUser(baseName)
        TwoPhaseActivity activity = createActivity(user, initial)
        archiveHelper.flipArchiveStatus(activity)
        activity = rwDAO.get(TwoPhaseActivity, activity.id)
        assert !initial == activity.archived
    }

    private void testSettingsFlip(boolean initial) {
        String baseName = "ArchiveSettings" + initial
        AppUser user = createUser(baseName)
        AppUserSettings settings = createAppUserSettings(user, initial)
        archiveHelper.flipArchiveStatus(settings)
        settings = rwDAO.get(AppUserSettings, settings.id)
        assert !initial == settings.archived
    }

    private void testCategoryFlip(boolean initial) {
        String baseName = "ArchiveCategory" + initial
        String baseShort = "ARCCAT1"
        AppUser user = createUser(baseName)
        ObservationCategory category = createCategory(user, baseName, baseShort, initial)
        Student student = createStudent(user, baseName, initial)
        Observation observationMatch = createObservation(user, category, student, baseName, initial)
        Photo photoMatchMatch = createPhoto(user, observationMatch, baseName, initial)
        Photo photoMatchNoMatch = createPhoto(user, observationMatch, baseName, !initial)
        Observation observationNoMatch = createObservation(user, category, student, baseName, !initial)
        Photo photoNoMatchMatch = createPhoto(user, observationNoMatch, baseName, initial)
        Photo photoNoMatchNoMatch = createPhoto(user, observationNoMatch, baseName, !initial)
        archiveHelper.flipArchiveStatus(category)

        assert !initial == rwDAO.get(ObservationCategory, category.id).archived
        //  None of these should have changed
        assert student.archived == rwDAO.get(Student, student.id).archived
        assert observationMatch.archived == rwDAO.get(Observation, observationMatch.id).archived
        assert observationNoMatch.archived == rwDAO.get(Observation, observationNoMatch.id).archived
        assert photoMatchMatch.archived == rwDAO.get(Photo, photoMatchMatch.id).archived
        assert photoMatchNoMatch.archived == rwDAO.get(Photo, photoMatchNoMatch.id).archived
        assert photoNoMatchMatch.archived == rwDAO.get(Photo, photoNoMatchMatch.id).archived
        assert photoNoMatchNoMatch.archived == rwDAO.get(Photo, photoNoMatchNoMatch.id).archived
    }

    private void testSemesterFlip(boolean initial) {
        String baseName = "ArchiveSemester" + initial
        String baseShort = "ARCSSEM"
        AppUser user = createUser(baseName)
        LocalDate start = LocalDate.now().minusYears(2)
        LocalDate end = LocalDate.now().minusYears(2).plusDays(60)
        Semester semester = createSemester(user, baseName, start, end, initial)

        ObservationCategory category = createCategory(user, baseName, baseShort, initial)
        Student student1 = createStudent(user, baseName + "1", initial)
        Student student2 = createStudent(user, baseName + "2", !initial)

        Observation onBeforeMatch = createObservation(user, category, student1, baseName, initial, semester.start.toLocalDateTime(LocalTime.now()).minusDays(1))
        Observation onAfterMatch = createObservation(user, category, student2, baseName, initial, semester.end.toLocalDateTime(LocalTime.now()).plusDays(1))

        Observation onStartMatch = createObservation(user, category, student1, baseName, initial, semester.start.toLocalDateTime(LocalTime.now()))
        Observation onMidMatch = createObservation(user, category, student2, baseName, initial, semester.start.toLocalDateTime(LocalTime.now()).plusDays(5))
        Observation onEndMatch = createObservation(user, category, student1, baseName, initial, semester.start.toLocalDateTime(LocalTime.now()))

        Observation onStartNoMatch = createObservation(user, category, student2, baseName, !initial, semester.start.toLocalDateTime(LocalTime.now()))
        Observation onMidNoMatch = createObservation(user, category, student1, baseName, !initial, semester.start.toLocalDateTime(LocalTime.now()).plusDays(5))
        Observation onEndNoMatch = createObservation(user, category, student2, baseName, !initial, semester.start.toLocalDateTime(LocalTime.now()))

        Photo photoBeforeMatch = createPhoto(user, onBeforeMatch, baseName, initial)
        Photo photoAfterMatch = createPhoto(user, onAfterMatch, baseName, initial)

        Photo photoStartMatch = createPhoto(user, onStartMatch, baseName, initial)
        Photo photoEndNoMatch = createPhoto(user, onEndMatch, baseName, !initial)

        Photo photoStartNoMatchMatch = createPhoto(user, onStartNoMatch, baseName, initial)
        Photo photoEndNoMatchNoMatch = createPhoto(user, onMidNoMatch, baseName, !initial)

        archiveHelper.flipArchiveStatus(semester)

        assert !initial == rwDAO.get(Semester, semester.id).archived
        assert !initial == rwDAO.get(Observation, onStartMatch.id).archived
        assert !initial == rwDAO.get(Observation, onMidMatch.id).archived
        assert !initial == rwDAO.get(Observation, onEndMatch.id).archived
        assert !initial == rwDAO.get(Photo, photoStartMatch.id).archived

        //  None of these should have changed
        assert category.archived == rwDAO.get(ObservationCategory, category.id).archived
        assert student1.archived == rwDAO.get(Student, student1.id).archived
        assert student2.archived == rwDAO.get(Student, student2.id).archived
        assert onAfterMatch.archived == rwDAO.get(Observation, onAfterMatch.id).archived
        assert onBeforeMatch.archived == rwDAO.get(Observation, onBeforeMatch.id).archived
        assert onStartNoMatch.archived == rwDAO.get(Observation, onStartNoMatch.id).archived
        assert onMidNoMatch.archived == rwDAO.get(Observation, onMidNoMatch.id).archived
        assert onEndNoMatch.archived == rwDAO.get(Observation, onEndNoMatch.id).archived
        assert photoBeforeMatch.archived == rwDAO.get(Photo, photoBeforeMatch.id).archived
        assert photoAfterMatch.archived == rwDAO.get(Photo, photoAfterMatch.id).archived
        assert photoEndNoMatch.archived == rwDAO.get(Photo, photoEndNoMatch.id).archived
        assert photoStartNoMatchMatch.archived == rwDAO.get(Photo, photoStartNoMatchMatch.id).archived
        assert photoEndNoMatchNoMatch.archived == rwDAO.get(Photo, photoEndNoMatchNoMatch.id).archived
    }

}
