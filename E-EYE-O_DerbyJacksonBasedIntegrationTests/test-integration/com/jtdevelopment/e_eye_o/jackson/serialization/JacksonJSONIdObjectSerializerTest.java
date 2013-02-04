package com.jtdevelopment.e_eye_o.jackson.serialization;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.jackson.serialization.JacksonJSONIdObjectSerializer;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.AssertJUnit.assertEquals;

/**
 * Date: 1/26/13
 * Time: 10:59 PM
 */
@ContextConfiguration("/test-integration-context.xml")
@Test(groups = {"integration"})
public class JacksonJSONIdObjectSerializerTest extends AbstractTransactionalTestNGSpringContextTests implements ApplicationContextAware {
    private final static String newline = System.getProperty("line.separator");

    @Autowired
    private JacksonJSONIdObjectSerializer serializer;

    @Autowired
    private ReadWriteDAO readWriteDAO;

    @Autowired
    private IdObjectFactory factory;

    private AppUser appUser1;
    private AppUser appUser2;
    private ClassList classList1For1;
    private ClassList classList2For1;
    private ClassList classList1For2;
    private ObservationCategory oc1For1;
    private ObservationCategory oc2For1;
    private Student student1For1;
    private Student student2For1;
    private Observation o1ForS1;
    private Observation o2ForS1;
    private Photo photo1for1;
    private Photo photo2for1;
    private Photo photo3for1;
    private Map<IdObject, String> jsonValues;

    @BeforeMethod
    public synchronized void initialize() {
        if (readWriteDAO == null) {
            return;
        }
        if (serializer == null) {
            return;
        }
        if (factory == null) {
            return;
        }
        if (appUser1 != null) {
            return;
        }

        appUser1 = factory.newAppUser().setEmailAddress("test@test.com").setFirstName("Testy")
                .setLastName("Tester").setLastLogin(new DateTime(2012, 12, 12, 12, 12, 13));
        appUser1 = readWriteDAO.create(appUser1);
        appUser2 = factory.newAppUser().setEmailAddress("test2@test.com").setFirstName("Testier").setLastName("Tester");
        appUser2 = readWriteDAO.create(appUser2);
        classList1For1 = factory.newClassList().setDescription("CL1-1").setAppUser(appUser1);
        classList1For1 = readWriteDAO.create(classList1For1);
        classList2For1 = factory.newClassList().setDescription("CL1-2").setAppUser(appUser1);
        classList2For1 = readWriteDAO.create(classList2For1);
        classList1For2 = factory.newClassList().setDescription("CL2-1").setAppUser(appUser1);
        classList1For2 = readWriteDAO.create(classList1For2);
        oc1For1 = factory.newObservationCategory().setDescription("Description").setShortName("OC-1").setAppUser(appUser1);
        oc1For1 = readWriteDAO.create(oc1For1);
        oc2For1 = factory.newObservationCategory().setDescription("Description").setShortName("OC-2").setAppUser(appUser1);
        oc2For1 = readWriteDAO.create(oc2For1);
        student1For1 = factory.newStudent().setLastName("Last").setFirstName("First")
                .addClassList(classList1For1).setArchived(true).setAppUser(appUser1);
        student1For1 = readWriteDAO.create(student1For1);
        student2For1 = factory.newStudent().setLastName("Last").setFirstName("First")
                .addClassList(classList1For1).addClassList(classList2For1).setAppUser(appUser1);
        student2For1 = readWriteDAO.create(student2For1);
        o1ForS1 = factory.newObservation().addCategory(oc1For1).setObservationSubject(student1For1)
                .setComment("Comment").setObservationTimestamp(new LocalDateTime(2013, 1, 18, 15, 12)).setAppUser(appUser1);
        o1ForS1 = readWriteDAO.create(o1ForS1);
        o2ForS1 = factory.newObservation().addCategory(oc1For1).setObservationSubject(student1For1)
                .setFollowUpObservation(o1ForS1).setComment("Comment").setObservationTimestamp(new LocalDateTime(2012, 1, 18, 15, 12))
                .setAppUser(appUser1);
        o2ForS1 = readWriteDAO.create(o2ForS1);
        photo1for1 = factory.newPhoto().setTimestamp(new LocalDateTime(2011, 11, 11, 11, 11, 11)).setPhotoFor(student1For1).setDescription("Photo1").setAppUser(appUser1);
        photo1for1 = readWriteDAO.create(photo1for1);
        photo2for1 = factory.newPhoto().setTimestamp(new LocalDateTime(2012, 12, 12, 12, 12, 12)).setPhotoFor(o2ForS1).setDescription("Photo2").setAppUser(appUser1);
        photo2for1 = readWriteDAO.create(photo2for1);
        photo3for1 = factory.newPhoto().setTimestamp(new LocalDateTime(2013, 1, 1, 15, 12, 45)).setPhotoFor(classList1For1).setDescription("Photo3").setAppUser(appUser1);
        photo3for1 = readWriteDAO.create(photo3for1);
        buildObjectToExpectedJSONMap();
    }

    @Test
    public void testWriteSingleEntities() throws Exception {
        for (Map.Entry<IdObject, String> entry : jsonValues.entrySet()) {
            final String jsonOutput = serializer.write(entry.getKey());
            assertEquals(entry.getValue(), jsonOutput);
        }
    }

    @Test
    public void testWriteCollectionEntity() throws Exception {

    }

    @Test
    public void testReadSingle() throws Exception {
        for (Map.Entry<IdObject, String> entry : jsonValues.entrySet()) {
            final IdObject object = serializer.read(entry.getValue());
            assertEquals(entry.getKey(), object);
        }
    }

    @Test
    public void testReadCollection() throws Exception {

    }

    private void buildObjectToExpectedJSONMap() {
        jsonValues = new HashMap<IdObject, String>() {{
            put(appUser1, "{" + newline +
                    "  \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.AppUser\"," + newline +
                    "  \"emailAddress\" : \"test@test.com\"," + newline +
                    "  \"firstName\" : \"Testy\"," + newline +
                    "  \"id\" : \"" + appUser1.getId() + "\"," + newline +
                    "  \"lastLogin\" : 1355332333000," + newline +
                    "  \"lastName\" : \"Tester\"" + newline +
                    "}");
            put(appUser2, "{" + newline +
                    "  \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.AppUser\"," + newline +
                    "  \"emailAddress\" : \"test2@test.com\"," + newline +
                    "  \"firstName\" : \"Testier\"," + newline +
                    "  \"id\" : \"" + appUser2.getId() + "\"," + newline +
                    "  \"lastLogin\" : 946702800000," + newline +
                    "  \"lastName\" : \"Tester\"" + newline +
                    "}");
            put(classList1For1, "{" + newline +
                    "  \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.ClassList\"," + newline +
                    "  \"appUser\" : {" + newline +
                    "    \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.AppUser\"," + newline +
                    "    \"id\" : \"" + appUser1.getId() + "\"" + newline +
                    "  }," + newline +
                    "  \"description\" : \"CL1-1\"," + newline +
                    "  \"id\" : \"" + classList1For1.getId() + "\"," + newline +
                    "  \"archived\" : false" + newline +
                    "}");
            put(classList2For1, "{" + newline +
                    "  \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.ClassList\"," + newline +
                    "  \"appUser\" : {" + newline +
                    "    \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.AppUser\"," + newline +
                    "    \"id\" : \"" + appUser1.getId() + "\"" + newline +
                    "  }," + newline +
                    "  \"description\" : \"CL1-2\"," + newline +
                    "  \"id\" : \"" + classList2For1.getId() + "\"," + newline +
                    "  \"archived\" : false" + newline +
                    "}");
            put(classList1For2, "{" + newline +
                    "  \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.ClassList\"," + newline +
                    "  \"appUser\" : {" + newline +
                    "    \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.AppUser\"," + newline +
                    "    \"id\" : \"" + appUser1.getId() + "\"" + newline +
                    "  }," + newline +
                    "  \"description\" : \"CL2-1\"," + newline +
                    "  \"id\" : \"" + classList1For2.getId() + "\"," + newline +
                    "  \"archived\" : false" + newline +
                    "}");
            put(oc1For1, "{" + newline +
                    "  \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.ObservationCategory\"," + newline +
                    "  \"appUser\" : {" + newline +
                    "    \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.AppUser\"," + newline +
                    "    \"id\" : \"" + appUser1.getId() + "\"" + newline +
                    "  }," + newline +
                    "  \"description\" : \"Description\"," + newline +
                    "  \"id\" : \"" + oc1For1.getId() + "\"," + newline +
                    "  \"shortName\" : \"OC-1\"" + newline +
                    "}");
            put(oc2For1, "{" + newline +
                    "  \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.ObservationCategory\"," + newline +
                    "  \"appUser\" : {" + newline +
                    "    \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.AppUser\"," + newline +
                    "    \"id\" : \"" + appUser1.getId() + "\"" + newline +
                    "  }," + newline +
                    "  \"description\" : \"Description\"," + newline +
                    "  \"id\" : \"" + oc2For1.getId() + "\"," + newline +
                    "  \"shortName\" : \"OC-2\"" + newline +
                    "}");
            put(student1For1, "{" + newline +
                    "  \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.Student\"," + newline +
                    "  \"appUser\" : {" + newline +
                    "    \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.AppUser\"," + newline +
                    "    \"id\" : \"" + appUser1.getId() + "\"" + newline +
                    "  }," + newline +
                    "  \"classLists\" : [ {" + newline +
                    "    \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.ClassList\"," + newline +
                    "    \"id\" : \"" + classList1For1.getId() + "\"" + newline +
                    "  } ]," + newline +
                    "  \"firstName\" : \"First\"," + newline +
                    "  \"id\" : \"" + student1For1.getId() + "\"," + newline +
                    "  \"lastName\" : \"Last\"," + newline +
                    "  \"archived\" : true" + newline +
                    "}");
            put(student2For1, "{" + newline +
                    "  \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.Student\"," + newline +
                    "  \"appUser\" : {" + newline +
                    "    \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.AppUser\"," + newline +
                    "    \"id\" : \"" + appUser1.getId() + "\"" + newline +
                    "  }," + newline +
                    "  \"classLists\" : [ {" + newline +
                    "    \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.ClassList\"," + newline +
                    "    \"id\" : \"" + classList2For1.getId() + "\"" + newline +
                    "  }, {" + newline +
                    "    \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.ClassList\"," + newline +
                    "    \"id\" : \"" + classList1For1.getId() + "\"" + newline +
                    "  } ]," + newline +
                    "  \"firstName\" : \"First\"," + newline +
                    "  \"id\" : \"" + student2For1.getId() + "\"," + newline +
                    "  \"lastName\" : \"Last\"," + newline +
                    "  \"archived\" : false" + newline +
                    "}");
            put(o1ForS1, "{" + newline +
                    "  \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.Observation\"," + newline +
                    "  \"appUser\" : {" + newline +
                    "    \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.AppUser\"," + newline +
                    "    \"id\" : \"" + appUser1.getId() + "\"" + newline +
                    "  }," + newline +
                    "  \"categories\" : [ {" + newline +
                    "    \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.ObservationCategory\"," + newline +
                    "    \"id\" : \"" + oc1For1.getId() + "\"" + newline +
                    "  } ]," + newline +
                    "  \"comment\" : \"Comment\"," + newline +
                    "  \"followUpObservation\" : {" + newline +
                    "    \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.Observation\"," + newline +
                    "    \"id\" : null" + newline +
                    "  }," + newline +
                    "  \"followUpReminder\" : null," + newline +
                    "  \"id\" : \"" + o1ForS1.getId() + "\"," + newline +
                    "  \"observationSubject\" : {" + newline +
                    "    \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.Student\"," + newline +
                    "    \"id\" : \"" + student1For1.getId() + "\"" + newline +
                    "  }," + newline +
                    "  \"observationTimestamp\" : [ 2013, 1, 18, 15, 12, 0, 0 ]," + newline +
                    "  \"archived\" : false," + newline +
                    "  \"followUpNeeded\" : false," + newline +
                    "  \"significant\" : false" + newline +
                    "}");
            put(o2ForS1, "{" + newline +
                    "  \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.Observation\"," + newline +
                    "  \"appUser\" : {" + newline +
                    "    \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.AppUser\"," + newline +
                    "    \"id\" : \"" + appUser1.getId() + "\"" + newline +
                    "  }," + newline +
                    "  \"categories\" : [ {" + newline +
                    "    \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.ObservationCategory\"," + newline +
                    "    \"id\" : \"" + oc1For1.getId() + "\"" + newline +
                    "  } ]," + newline +
                    "  \"comment\" : \"Comment\"," + newline +
                    "  \"followUpObservation\" : {" + newline +
                    "    \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.Observation\"," + newline +
                    "    \"id\" : \"" + o1ForS1.getId() + "\"" + newline +
                    "  }," + newline +
                    "  \"followUpReminder\" : null," + newline +
                    "  \"id\" : \"" + o2ForS1.getId() + "\"," + newline +
                    "  \"observationSubject\" : {" + newline +
                    "    \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.Student\"," + newline +
                    "    \"id\" : \"" + student1For1.getId() + "\"" + newline +
                    "  }," + newline +
                    "  \"observationTimestamp\" : [ 2012, 1, 18, 15, 12, 0, 0 ]," + newline +
                    "  \"archived\" : false," + newline +
                    "  \"followUpNeeded\" : false," + newline +
                    "  \"significant\" : false" + newline +
                    "}");
            put(photo1for1, "{" + newline +
                    "  \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.Photo\"," + newline +
                    "  \"appUser\" : {" + newline +
                    "    \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.AppUser\"," + newline +
                    "    \"id\" : \"" + appUser1.getId() + "\"" + newline +
                    "  }," + newline +
                    "  \"description\" : \"Photo1\"," + newline +
                    "  \"id\" : \"" + photo1for1.getId() + "\"," + newline +
                    "  \"photoFor\" : {" + newline +
                    "    \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.Student\"," + newline +
                    "    \"id\" : \"" + student1For1.getId() + "\"" + newline +
                    "  }," + newline +
                    "  \"timestamp\" : [ 2011, 11, 11, 11, 11, 11, 0 ]," + newline +
                    "  \"archived\" : false" + newline +
                    "}");
            put(photo2for1, "{" + newline +
                    "  \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.Photo\"," + newline +
                    "  \"appUser\" : {" + newline +
                    "    \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.AppUser\"," + newline +
                    "    \"id\" : \"" + appUser1.getId() + "\"" + newline +
                    "  }," + newline +
                    "  \"description\" : \"Photo2\"," + newline +
                    "  \"id\" : \"" + photo2for1.getId() + "\"," + newline +
                    "  \"photoFor\" : {" + newline +
                    "    \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.Observation\"," + newline +
                    "    \"id\" : \"" + o2ForS1.getId() + "\"" + newline +
                    "  }," + newline +
                    "  \"timestamp\" : [ 2012, 12, 12, 12, 12, 12, 0 ]," + newline +
                    "  \"archived\" : false" + newline +
                    "}");
            put(photo3for1, "{" + newline +
                    "  \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.Photo\"," + newline +
                    "  \"appUser\" : {" + newline +
                    "    \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.AppUser\"," + newline +
                    "    \"id\" : \"" + appUser1.getId() + "\"" + newline +
                    "  }," + newline +
                    "  \"description\" : \"Photo3\"," + newline +
                    "  \"id\" : \"" + photo3for1.getId() + "\"," + newline +
                    "  \"photoFor\" : {" + newline +
                    "    \"entityType\" : \"com.jtbdevelopment.e_eye_o.entities.ClassList\"," + newline +
                    "    \"id\" : \"" + classList1For1.getId() + "\"" + newline +
                    "  }," + newline +
                    "  \"timestamp\" : [ 2013, 1, 1, 15, 12, 45, 0 ]," + newline +
                    "  \"archived\" : false" + newline +
                    "}");
        }};
    }
}
