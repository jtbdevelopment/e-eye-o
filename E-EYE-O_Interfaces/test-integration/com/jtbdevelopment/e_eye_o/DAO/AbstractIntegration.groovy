package com.jtbdevelopment.e_eye_o.DAO

import com.jtbdevelopment.e_eye_o.TestingPhotoHelper
import com.jtbdevelopment.e_eye_o.entities.*
import com.jtbdevelopment.e_eye_o.entities.builders.ObservationBuilder
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

/**
 * Date: 12/29/13
 * Time: 3:55 PM
 */
abstract class AbstractIntegration extends AbstractTestNGSpringContextTests {
    @Autowired
    protected ReadWriteDAO rwDAO;
    @Autowired
    protected IdObjectFactory factory;

    protected ClassList createClassList(AppUser user, String baseName, boolean archived = false) {
        rwDAO.create(
                factory.newClassListBuilder(user).
                        withDescription(baseName).
                        withArchived(archived).
                        build()
        )
    }

    protected Semester createSemester(AppUser user, String baseName, LocalDate start, LocalDate end, boolean archived = false) {
        rwDAO.create(
                factory.newSemesterBuilder(user).
                        withDescription(baseName).
                        withStart(start).
                        withEnd(end).
                        withArchived(archived).
                        build()
        )
    }

    protected Observation createObservation(AppUser user,
                                            ObservationCategory category,
                                            com.jtbdevelopment.e_eye_o.entities.Observable observable,
                                            String baseName,
                                            boolean archived = false,
                                            LocalDateTime time = LocalDateTime.now()) {
        ObservationBuilder builder = (ObservationBuilder) factory.newObservationBuilder(user).
                withObservationSubject(observable).
                withObservationTimestamp(time).
                withComment(baseName).
                withArchived(archived);

        if (category) builder.addCategory(category)

        rwDAO.create(builder.build())
    }

    protected ObservationCategory createCategory(AppUser user, String baseName, String baseShort, boolean archived = false) {
        rwDAO.create(
                factory.newObservationCategoryBuilder(user).
                        withDescription(baseName).
                        withShortName(baseShort).
                        withArchived(archived).
                        build()
        )
    }

    protected Photo createPhoto(AppUser user, AppUserOwnedObject photoFor, String baseName, boolean archived = false) {
        rwDAO.create(
                factory.newPhotoBuilder(user).
                        withPhotoFor(photoFor).
                        withDescription(baseName).
                        withImageData(TestingPhotoHelper.simpleImageBytes).
                        withMimeType(TestingPhotoHelper.PNG).
                        withArchived(archived).
                        build())
    }

    protected Student createStudent(AppUser user, String firstName, boolean archived = false, Set<ClassList> classes = [] as Set) {
        rwDAO.create(
                factory.newStudentBuilder(user).
                        withFirstName(firstName).
                        withClassLists(classes).
                        withArchived(archived).
                        build()
        )
    }

    protected AppUserSettings createAppUserSettings(AppUser user, boolean archived = false) {
        rwDAO.create(
                factory.newAppUserSettingsBuilder(user).
                        withSetting("a", "settings").
                        withArchived(archived).
                        build()
        )
    }

    protected TwoPhaseActivity createActivity(AppUser user, boolean archived = false) {
        rwDAO.create(
                factory.newTwoPhaseActivityBuilder(user).
                        withActivityType(TwoPhaseActivity.Activity.EMAIL_CHANGE).
                        withExpirationTime(DateTime.now()).
                        withArchived(archived).
                        build()
        )
    }

    protected AppUser createUser(String baseName) {
        String email = baseName + "@test.com"
        rwDAO.create(factory.newAppUserBuilder().withFirstName(baseName).withEmailAddress(email).withPassword("X").build())
        rwDAO.getUser(email)
    }

}
