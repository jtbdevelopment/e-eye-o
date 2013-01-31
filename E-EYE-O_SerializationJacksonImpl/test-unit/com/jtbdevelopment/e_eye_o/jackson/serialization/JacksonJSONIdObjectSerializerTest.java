package com.jtbdevelopment.e_eye_o.jackson.serialization;

import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.impl.IdObjectImplFactory;
import com.jtbdevelopment.e_eye_o.entities.impl.helpers.IdObjectInterfaceResolverImpl;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.AssertJUnit.assertEquals;

/**
 * Date: 1/26/13
 * Time: 10:59 PM
 */
public class JacksonJSONIdObjectSerializerTest {
    private final JacksonJSONIdObjectSerializer serializer =
            new JacksonJSONIdObjectSerializer(new JacksonIdObjectModule(new JacksonIdObjectSerializer(new IdObjectInterfaceResolverImpl())));
    private final IdObjectImplFactory factory = new IdObjectImplFactory();
    private final AppUser appUser1 = factory.newAppUser().setEmailAddress("test@test.com").setFirstName("Testy")
            .setLastName("Tester").setLastLogin(new DateTime(2012, 12, 12, 12, 12, 13)).setId("AU1");
    private final AppUser appUser2 = factory.newAppUser().setEmailAddress("test2@test.com").setFirstName("Testier").setLastName("Tester");
    private final ClassList classList1For1 = factory.newClassList().setDescription("CL1-1").setAppUser(appUser1).setId("CL1-1");
    private final ClassList classList2For1 = factory.newClassList().setDescription("CL1-2").setAppUser(appUser1).setId("CL1-2");
    private final ClassList classList1For2 = factory.newClassList().setDescription("CL2-1").setAppUser(appUser1).setId("CL2-1");
    private final ObservationCategory oc1For1 = factory.newObservationCategory().setDescription("Description").setShortName("OC-1").setId("OC1-1");
    private final ObservationCategory oc2For1 = factory.newObservationCategory().setDescription("Description").setShortName("OC-2").setId("OC1-2");
    private final Student student1For1 = factory.newStudent().setLastName("Last").setFirstName("First")
            .addClassList(classList1For1).setArchived(true).setAppUser(appUser1).setId("S1-1");
    private final Student student2For1 = factory.newStudent().setLastName("Last").setFirstName("First")
            .addClassList(classList1For1).addClassList(classList2For1).setAppUser(appUser1).setId("S2-1");
    private final Observation o1ForS1 = factory.newObservation().addCategory(oc1For1).setObservationSubject(student1For1)
            .setComment("Comment").setObservationTimestamp(new LocalDateTime(2013, 1, 18, 15, 12)).setAppUser(appUser1).setId("O1-1");
    private final Observation o2ForS1 = factory.newObservation().addCategory(oc1For1).setObservationSubject(student1For1)
            .setFollowUpObservation(o1ForS1).setComment("Comment").setObservationTimestamp(new LocalDateTime(2012, 1, 18, 15, 12))
            .setAppUser(appUser1).setId("O2-1");
    private final Photo photo1for1 = factory.newPhoto().setPhotoFor(student1For1).setDescription("Photo1").setAppUser(appUser1).setId("P1-1");
    private final Photo photo2for1 = factory.newPhoto().setPhotoFor(o2ForS1).setDescription("Photo2").setAppUser(appUser1).setId("P2-1");
    private final Photo photo3for1 = factory.newPhoto().setPhotoFor(classList1For1).setDescription("Photo3").setAppUser(appUser1).setId("P3-1");

    private final Map<IdObject, String> jsonValues = new HashMap<IdObject, String>() {{
        put(appUser1, "");
        put(appUser2, "");
        put(classList1For1, "");
        put(classList1For2, "");
        put(oc1For1, "");
        put(oc2For1, "");
        put(student1For1, "");
        put(student2For1, "");
        put(o1ForS1, "");
        put(o2ForS1, "");
        put(photo1for1, "");
        put(photo2for1, "");
        put(photo3for1, "");
    }};

    @BeforeMethod
    public void setUp() throws Exception {
    }

    @Test
    public void testWriteSingleEntities() throws Exception {
        for(Map.Entry<IdObject, String> entry : jsonValues.entrySet()) {
            final String jsonOutput = serializer.write(entry.getKey());
//            final IdObject object = serializer.read(jsonOutput, entry.getKey().getClass());
            assertEquals(entry.getValue(), jsonOutput);
        }
    }

    @Test
    public void testWriteCollectionEntity() throws Exception {

    }

    @Test
    public void testReadSingle() throws Exception {

    }

    @Test
    public void testReadCollection() throws Exception {

    }
}
