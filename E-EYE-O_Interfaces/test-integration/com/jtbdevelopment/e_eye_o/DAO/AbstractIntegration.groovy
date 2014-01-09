package com.jtbdevelopment.e_eye_o.DAO

import com.jtbdevelopment.e_eye_o.TestingPhotoHelper
import com.jtbdevelopment.e_eye_o.entities.*
import com.jtbdevelopment.e_eye_o.entities.builders.ObservationBuilder
import com.jtbdevelopment.e_eye_o.serialization.JSONIdObjectSerializer
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
    @Autowired
    JSONIdObjectSerializer serializer

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
                                            Observable observable,
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

    //  Var args of lists and sets making up total results that should come back
    //  List where order is expected, set where order is unimportant
    //  However each collection is still order dependent
    protected void deepCompare(List actuals, Collection... expecteds) {
        int expectedSize = 0;
        expecteds.each { expectedSize += it.size() }
        //  Shallow check compares id orders and sizes but not fields
        assert expectedSize == actuals.size()

        //  Break up actuals into lists and sets
        ArrayList actualList = breakupActuals(actuals, expecteds)

        int index = -1;
        expecteds.each {
            expected ->
                ++index
                def actual = actualList.get(index)
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

    protected ArrayList breakupActuals(delObjFixed, Collection... expecteds) {
        def actuals = [];
        int start = 0;
        expecteds.each {
            actuals.add(delObjFixed.subList(start, start + it.size()))
            start += it.size()
        }
        actuals
    }

    protected Map compareObjects(e, a) {
        e.properties.each {
            String key, Object value ->
                if (e in DeletedObject && key == "modificationTimestamp") {
                    return
                }
                assert e."$key" == a."$key", "Failure on $key object " + serializer.write(e) + " vs. " + serializer.write(a)
        }
    }

}
