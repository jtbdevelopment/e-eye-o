package com.jtbdevelopment.e_eye_o.jasper.reports.designer;

import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.Observable;
import com.jtbdevelopment.e_eye_o.entities.impl.IdObjectFactoryImpl;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.*;

/**
 * Date: 8/26/13
 * Time: 6:45 AM
 */
public class BaseData {
    public static final AppUser appUser1;
    public static final ClassList classList1For1;
    public static final ClassList classList2For1;
    public static final ObservationCategory oc1For1;
    public static final ObservationCategory oc2For1;
    public static final Student student1For1;
    public static final Student student2For1;
    public static final Student student3For1;
    public static final Observation observation1For1For1;
    public static final Observation observation2For1For1;
    public static final Observation observation3For1For1;
    public static final Observation observation1For2For1;
    public static final Observation observation2For2For1;
    public static final Observation observation1For3For1;
    public static final Observation observation2For3For1;
    //public static final Photo photo1ForO1For1For1;

    static {
        IdObjectFactoryImpl factory = new IdObjectFactoryImpl();

        appUser1 = factory.newAppUserBuilder().withId("AU1").build();
        classList1For1 = factory.newClassListBuilder(appUser1).withDescription("CL1-1").withId("CL1-1").build();
        classList2For1 = factory.newClassListBuilder(appUser1).withDescription("CL2-1").withId("CL2-1").build();
        oc1For1 = factory.newObservationCategoryBuilder(appUser1).withShortName("OC1-1").withDescription("Category 1-1").withId("OC1-1").build();
        oc2For1 = factory.newObservationCategoryBuilder(appUser1).withShortName("OC2-1").withDescription("Category 2-1").withId("OC2-1").build();
        student1For1 = factory.newStudentBuilder(appUser1).addClassList(classList1For1).withFirstName("Student").withLastName("One").withId("S1-1").build();
        student2For1 = factory.newStudentBuilder(appUser1).addClassList(classList2For1).withFirstName("Student").withLastName("Two").withId("S2-1").build();
        student3For1 = factory.newStudentBuilder(appUser1).addClassList(classList1For1).addClassList(classList2For1).withFirstName("Student").withLastName("Three").withId("S3-1").build();
        observation1For1For1 = factory.newObservationBuilder(appUser1)
                .withObservationSubject(student1For1)
                .withObservationTimestamp(new LocalDateTime())
                .addCategory(oc1For1)
                .withComment("The first comment on Student 1")
                .withId("O1-1-1").build();
        observation2For1For1 = factory.newObservationBuilder(appUser1)
                .withObservationSubject(student1For1)
                .withObservationTimestamp(new LocalDateTime())
                .addCategory(oc2For1)
                .withComment("The second comment on Student 1")
                .withId("O2-1-1").build();
        observation3For1For1 = factory.newObservationBuilder(appUser1)
                .withObservationSubject(student1For1)
                .withObservationTimestamp(new LocalDateTime())
                .addCategory(oc1For1)
                .addCategory(oc2For1)
                .withComment("A longer and longer multi-line\n entry on Student 1")
                .withId("O3-1-1").build();
        observation1For2For1 = factory.newObservationBuilder(appUser1)
                .withObservationSubject(student2For1)
                .withObservationTimestamp(new LocalDateTime())
                .addCategory(oc1For1)
                .withComment("O1-2-1")
                .withId("O1-2-1").build();
        observation2For2For1 = factory.newObservationBuilder(appUser1)
                .withObservationSubject(student2For1)
                .withObservationTimestamp(new LocalDateTime())
                .addCategory(oc2For1)
                .withComment("O2-2-1")
                .withId("O2-2-1").build();
        observation1For3For1 = factory.newObservationBuilder(appUser1)
                .withObservationSubject(student3For1)
                .withObservationTimestamp(new LocalDateTime())
                .addCategory(oc1For1)
                .withComment("O1-3-1")
                .withId("O1-3-1").build();
        observation2For3For1 = factory.newObservationBuilder(appUser1)
                .withObservationSubject(student3For1)
                .withObservationTimestamp(new LocalDateTime())
                .withComment("O2-3-1")
                .withId("O2-3-1").build();
/*        photo1ForO1For1For1 = factory.newPhotoBuilder(appUser1)
                .withPhotoFor(observation1For1For1)
                .withDescription("P1")
                .withImageData(TestingPhotoHelper.simpleImageBytes)
                .withMimeType(TestingPhotoHelper.PNG)
                .withTimestamp(new LocalDateTime()).withId("P1-1").build();
*/
    }

    public static class FakeDAO implements ReadOnlyDAO {

        @Override
        public Set<AppUser> getUsers() {
            return null;
        }

        @Override
        public AppUser getUser(String emailAddress) {
            return null;
        }

        @Override
        public <T extends IdObject> T get(Class<T> entityType, String id) {
            return null;
        }

        @Override
        public <T extends AppUserOwnedObject> Set<T> getEntitiesForUser(Class<T> entityType, AppUser appUser, int firstResult, int maxResults) {
            return null;
        }

        @Override
        public <T extends AppUserOwnedObject> Set<T> getActiveEntitiesForUser(Class<T> entityType, AppUser appUser, int firstResult, int maxResults) {
            if (ObservationCategory.class.isAssignableFrom(entityType)) {
                return (Set<T>) new HashSet<>(Arrays.<ObservationCategory>asList(BaseData.oc1For1, BaseData.oc2For1));
            }
            if (Student.class.isAssignableFrom(entityType)) {
                return (Set<T>) new HashSet<>(Arrays.asList(BaseData.student1For1, BaseData.student2For1, BaseData.student3For1));
            }
            return null;
        }

        @Override
        public <T extends AppUserOwnedObject> Set<T> getArchivedEntitiesForUser(Class<T> entityType, AppUser appUser, int firstResult, int maxResults) {
            return null;
        }

        @Override
        public <T extends AppUserOwnedObject> int getEntitiesForUserCount(Class<T> entityType, AppUser appUser) {
            return 0;
        }

        @Override
        public <T extends AppUserOwnedObject> int getActiveEntitiesForUserCount(Class<T> entityType, AppUser appUser) {
            return 0;
        }

        @Override
        public <T extends AppUserOwnedObject> int getArchivedEntitiesForUserCount(Class<T> entityType, AppUser appUser) {
            return 0;
        }

        @Override
        public List<? extends AppUserOwnedObject> getModificationsSince(AppUser appUser, DateTime since, String id, int maxResults) {
            return null;
        }

        @Override
        public List<Photo> getAllPhotosForEntity(AppUserOwnedObject ownedObject, int firstResult, int maxResults) {
            return null;
        }

        @Override
        public List<Photo> getActivePhotosForEntity(AppUserOwnedObject ownedObject, int firstResult, int maxResults) {
            return null;
        }

        @Override
        public List<Photo> getArchivedPhotosForEntity(AppUserOwnedObject ownedObject, int firstResult, int maxResults) {
            return null;
        }

        @Override
        public int getAllPhotosForEntityCount(AppUserOwnedObject ownedObject) {
            return 0;
        }

        @Override
        public int getActivePhotosForEntityCount(AppUserOwnedObject ownedObject) {
            return 0;
        }

        @Override
        public int getArchivedPhotosForEntityCount(AppUserOwnedObject ownedObject) {
            return 0;
        }

        @Override
        public Set<Observation> getAllObservationsForSemester(Semester semester, int firstResult, int maxResults) {
            return null;
        }

        @Override
        public Set<Observation> getActiveObservationsForSemester(Semester semester, int firstResult, int maxResults) {
            return null;
        }

        @Override
        public Set<Observation> getArchivedObservationsForSemester(Semester semester, int firstResult, int maxResults) {
            return null;
        }

        @Override
        public List<Observation> getAllObservationsForEntity(Observable observable) {
            return null;
        }

        @Override
        public List<Observation> getAllObservationsForObservationCategory(ObservationCategory observationCategory) {
            return null;
        }

        @Override
        public List<Observation> getAllObservationsForEntityAndCategory(final Observable observable, final ObservationCategory observationCategory, final LocalDate from, final LocalDate to) {
            if (observable.equals(BaseData.student1For1)) {
                if (BaseData.oc1For1.equals(observationCategory)) {
                    return Arrays.asList(BaseData.observation1For1For1, BaseData.observation3For1For1);
                }
                if (BaseData.oc2For1.equals(observationCategory)) {
                    return Arrays.asList(BaseData.observation2For1For1, BaseData.observation3For1For1);
                }
            }
            if (observable.equals(BaseData.student2For1)) {
                if (BaseData.oc1For1.equals(observationCategory)) {
                    return Arrays.asList(BaseData.observation1For2For1);
                }
                if (BaseData.oc2For1.equals(observationCategory)) {
                    return Arrays.asList(BaseData.observation2For2For1);
                }
            }
            if (observable.equals(BaseData.student3For1)) {
                if (BaseData.oc1For1.equals(observationCategory)) {
                    return Arrays.asList(BaseData.observation1For3For1);
                }
                if (observationCategory == null) {
                    return Arrays.asList(BaseData.observation2For3For1);
                }
            }
            return Collections.emptyList();
        }

        @Override
        public List<Student> getAllStudentsForClassList(ClassList classList) {
            return null;
        }
    }
}
