package com.jtbdevelopment.e_eye_o.jasper.reports.designer;

import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.impl.IdObjectImplFactory;
import org.joda.time.LocalDateTime;

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
        IdObjectImplFactory factory = new IdObjectImplFactory();

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
}
