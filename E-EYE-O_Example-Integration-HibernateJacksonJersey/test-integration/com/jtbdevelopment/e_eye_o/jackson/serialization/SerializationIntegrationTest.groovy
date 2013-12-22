package com.jtbdevelopment.e_eye_o.jackson.serialization

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO
import com.jtbdevelopment.e_eye_o.TestingPhotoHelper
import com.jtbdevelopment.e_eye_o.entities.*
import com.jtbdevelopment.e_eye_o.entities.wrapper.IdObjectWrapper
import com.jtbdevelopment.e_eye_o.serialization.JSONIdObjectSerializer
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * Date: 12/20/13
 * Time: 3:47 PM
 */
@ContextConfiguration("/test-integration-context.xml")
class SerializationIntegrationTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private JSONIdObjectSerializer serializer;

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
    private Map<IdObject, Map<String, Object>> jsonValues;

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

        appUser1 = factory.newAppUserBuilder().withEmailAddress("jtest@test.com").withFirstName("Testy").withPassword("pass")
                .withLastName("Tester").withLastLogout(new DateTime(2012, 12, 12, 12, 12, 13)).withAdmin(true).withActivated(false).build();
        appUser1 = readWriteDAO.create(appUser1);
        appUser2 = factory.newAppUserBuilder().withEmailAddress("jtest2@test.com").withFirstName("Testier").withLastName("Tester").withActivated(true).withActive(false).withPassword("pass").build();
        appUser2 = readWriteDAO.create(appUser2);
        classList1For1 = factory.newClassListBuilder(appUser1).withDescription("CL1-1").withLastObservationTimestamp(new LocalDateTime(2013, 3, 30, 13, 5, 0)).withAppUser(appUser1).build();
        classList1For1 = readWriteDAO.create(classList1For1);
        classList2For1 = factory.newClassListBuilder(appUser1).withDescription("CL1-2").withLastObservationTimestamp(new LocalDateTime(2012, 11, 3, 15, 0, 0)).withAppUser(appUser1).build();
        classList2For1 = readWriteDAO.create(classList2For1);
        classList1For2 = factory.newClassListBuilder(appUser1).withDescription("CL2-1").withAppUser(appUser1).build();
        classList1For2 = readWriteDAO.create(classList1For2);
        oc1For1 = factory.newObservationCategoryBuilder(appUser1).withDescription("Description").withShortName("OC-1").build();
        oc1For1 = readWriteDAO.create(oc1For1);
        oc2For1 = factory.newObservationCategoryBuilder(appUser1).withDescription("Description").withShortName("OC-2").build();
        oc2For1 = readWriteDAO.create(oc2For1);
        student1For1 = factory.newStudentBuilder(appUser1).withLastName("Last1-1").withFirstName("First1-1").addClassList(classList1For1).withArchived(true).build();
        student1For1 = readWriteDAO.create(student1For1);
        student2For1 = factory.newStudentBuilder(appUser1).withLastName("Last2-1").withFirstName("First2-1").addClassList(classList2For1).build();
        student2For1 = readWriteDAO.create(student2For1);
        o1ForS1 = factory.newObservationBuilder(appUser1).addCategory(oc1For1).withObservationSubject(student1For1)
                .withComment("Comment").withObservationTimestamp(new LocalDateTime(2013, 1, 18, 15, 12)).build();
        o1ForS1 = readWriteDAO.create(o1ForS1);
        o2ForS1 = factory.newObservationBuilder(appUser1).addCategory(oc1For1).withObservationSubject(student1For1)
                .withComment("Comment").withObservationTimestamp(new LocalDateTime(2012, 1, 18, 15, 12)).build();
        o2ForS1 = readWriteDAO.create(o2ForS1);
        student1For1 = readWriteDAO.get(Student.class, student1For1.getId());
        student2For1 = readWriteDAO.get(Student.class, student2For1.getId());
        photo1for1 = factory.newPhotoBuilder(appUser1).withTimestamp(new LocalDateTime(2011, 11, 11, 11, 11, 11)).withPhotoFor(student1For1).withDescription("Photo1").withMimeType(TestingPhotoHelper.PNG).withImageData(TestingPhotoHelper.simpleImageBytes).build();
        photo1for1 = readWriteDAO.create(photo1for1);
        photo2for1 = factory.newPhotoBuilder(appUser1).withTimestamp(new LocalDateTime(2012, 12, 12, 12, 12, 12)).withPhotoFor(o2ForS1).withDescription("Photo2").withMimeType(TestingPhotoHelper.PNG).withImageData(TestingPhotoHelper.simpleImageBytes).build();
        photo2for1 = readWriteDAO.create(photo2for1);
        photo3for1 = factory.newPhotoBuilder(appUser1).withTimestamp(new LocalDateTime(2013, 1, 1, 15, 12, 45)).withPhotoFor(classList1For1).withDescription("Photo3").withMimeType(TestingPhotoHelper.PNG).withImageData(TestingPhotoHelper.simpleImageBytes).build();
        photo3for1 = readWriteDAO.create(photo3for1);
        buildObjectToExpectedJSONMap();
    }

    @Test(groups = ["integration"])
    public void testReadSingleEntities() throws Exception {
        jsonValues.each {
            IdObject key, Map value ->
                final IdObject object = serializer.readAsObjects(new JsonBuilder(value).toString());
                IdObject wrapped = ((IdObjectWrapper) key).wrapped
                assert key == object;
                compareObjects(wrapped, object)
        }
    }

    @Test(groups = ["integration"])
    public void testWriteSingleEntities() throws Exception {
        jsonValues.each {
            IdObject key, Map value ->
                assert value == new JsonSlurper().parseText(serializer.write(key));
        }
    }

    @Test(groups = ["integration"])
    public void testReadCollection() throws Exception {
        final List<? extends IdObject> entities = jsonValues.collect { it.key };
        String json = "[" + jsonValues.collect { new JsonBuilder(it.value).toString() }.join(", ") + "]"
        List<? extends IdObject> objects = serializer.readAsObjects(json);
        assert entities == objects
        entities.each { compareObjects(((IdObjectWrapper) it).wrapped, objects.get(objects.indexOf(it))) }
    }

    @Test(groups = ["integration"])
    public void testWriteCollection() throws Exception {
        List<Map<String, Object>> values = jsonValues.collect { it.value }
        List<? extends IdObject> entities = jsonValues.collect { it.key }
        String output = serializer.writeEntities(entities);
        assert values == new JsonSlurper().parseText(output)
    }

    private void compareObjects(IdObject expected, IdObject compareTo) {
        expected.metaClass.properties.each { MetaProperty property ->
            String name = property.name
            assert expected."$name" == compareTo."$name" || "password" == name
        }
    }

    private void buildObjectToExpectedJSONMap() {
        jsonValues = [
                (appUser1): [
                        "entityType": "com.jtbdevelopment.e_eye_o.entities.AppUser",
                        "activated": false,
                        "active": true,
                        "admin": true,
                        "emailAddress": "jtest@test.com",
                        "firstName": "Testy",
                        "id": appUser1.getId(),
                        "lastLogout": 1355332333000,
                        "lastName": "Tester",
                        "modificationTimestamp": appUser1.getModificationTimestamp().getMillis()
                ],
                (appUser2): [
                        "entityType": "com.jtbdevelopment.e_eye_o.entities.AppUser",
                        "activated": true,
                        "active": false,
                        "admin": false,
                        "emailAddress": "jtest2@test.com",
                        "firstName": "Testier",
                        "id": appUser2.getId(),
                        "lastLogout": 946702800000,
                        "lastName": "Tester",
                        "modificationTimestamp": appUser2.getModificationTimestamp().getMillis()
                ],
                (classList1For1): [
                        "entityType": "com.jtbdevelopment.e_eye_o.entities.ClassList",
                        "appUser": [
                                "entityType": "com.jtbdevelopment.e_eye_o.entities.AppUser",
                                "id": appUser1.getId()
                        ],
                        "archived": false,
                        "description": "CL1-1",
                        "id": classList1For1.getId(),
                        "lastObservationTimestamp": [2013, 3, 30, 13, 5, 0, 0],
                        "modificationTimestamp": classList1For1.getModificationTimestamp().getMillis()
                ],
                (classList2For1): [
                        "entityType": "com.jtbdevelopment.e_eye_o.entities.ClassList",
                        "appUser": [
                                "entityType": "com.jtbdevelopment.e_eye_o.entities.AppUser",
                                "id": appUser1.getId()
                        ],
                        "archived": false,
                        "description": "CL1-2",
                        "id": classList2For1.getId(),
                        "lastObservationTimestamp": [2012, 11, 3, 15, 0, 0, 0],
                        "modificationTimestamp": classList2For1.getModificationTimestamp().getMillis()
                ],
                (classList1For2): [
                        "entityType": "com.jtbdevelopment.e_eye_o.entities.ClassList",
                        "appUser": [
                                "entityType": "com.jtbdevelopment.e_eye_o.entities.AppUser",
                                "id": appUser1.getId()
                        ],
                        "archived": false,
                        "description": "CL2-1",
                        "id": classList1For2.getId(),
                        "lastObservationTimestamp": [2000, 1, 1, 0, 0, 0, 0],
                        "modificationTimestamp": classList1For2.getModificationTimestamp().getMillis()
                ],
                (oc1For1): [
                        "entityType": "com.jtbdevelopment.e_eye_o.entities.ObservationCategory",
                        "appUser": [
                                "entityType": "com.jtbdevelopment.e_eye_o.entities.AppUser",
                                "id": appUser1.getId()
                        ],
                        "archived": false,
                        "description": "Description",
                        "id": oc1For1.getId(),
                        "modificationTimestamp": oc1For1.getModificationTimestamp().getMillis(),
                        "shortName": "OC-1"
                ],
                (oc2For1): [
                        "entityType": "com.jtbdevelopment.e_eye_o.entities.ObservationCategory",
                        "appUser": [
                                "entityType": "com.jtbdevelopment.e_eye_o.entities.AppUser",
                                "id": appUser1.getId()
                        ],
                        "archived": false,
                        "description": "Description",
                        "id": oc2For1.getId(),
                        "modificationTimestamp": oc2For1.getModificationTimestamp().getMillis(),
                        "shortName": "OC-2"
                ],
                (student1For1): [
                        "entityType": "com.jtbdevelopment.e_eye_o.entities.Student",
                        "appUser": [
                                "entityType": "com.jtbdevelopment.e_eye_o.entities.AppUser",
                                "id": appUser1.getId()
                        ],
                        "archived": true,
                        "classLists": [[
                                "entityType": "com.jtbdevelopment.e_eye_o.entities.ClassList",
                                "id": classList1For1.getId()
                        ]],
                        "firstName": "First1-1",
                        "id": student1For1.getId(),
                        "lastName": "Last1-1",
                        "lastObservationTimestamp": [2013, 1, 18, 15, 12, 0, 0],
                        "modificationTimestamp": student1For1.getModificationTimestamp().getMillis()
                ],
                (student2For1): [
                        "entityType": "com.jtbdevelopment.e_eye_o.entities.Student",
                        "appUser": [
                                "entityType": "com.jtbdevelopment.e_eye_o.entities.AppUser",
                                "id": appUser1.getId()
                        ],
                        "archived": false,
                        "classLists": [[
                                "entityType": "com.jtbdevelopment.e_eye_o.entities.ClassList",
                                "id": classList2For1.getId()
                        ]],
                        "firstName": "First2-1",
                        "id": student2For1.getId(),
                        "lastName": "Last2-1",
                        "lastObservationTimestamp": [2000, 1, 1, 0, 0, 0, 0],
                        "modificationTimestamp": student2For1.getModificationTimestamp().getMillis()
                ],
                (o1ForS1): [
                        "entityType": "com.jtbdevelopment.e_eye_o.entities.Observation",
                        "appUser": [
                                "entityType": "com.jtbdevelopment.e_eye_o.entities.AppUser",
                                "id": appUser1.getId()
                        ],
                        "archived": false,
                        "categories": [[
                                "entityType": "com.jtbdevelopment.e_eye_o.entities.ObservationCategory",
                                "id": oc1For1.getId()
                        ]],
                        "comment": "Comment",
                        "id": o1ForS1.getId(),
                        "modificationTimestamp": o1ForS1.getModificationTimestamp().getMillis(),
                        "observationSubject": [
                                "entityType": "com.jtbdevelopment.e_eye_o.entities.Student",
                                "id": student1For1.getId()
                        ],
                        "observationTimestamp": [2013, 1, 18, 15, 12, 0, 0],
                        "significant": true
                ],
                (o2ForS1): [
                        "entityType": "com.jtbdevelopment.e_eye_o.entities.Observation",
                        "appUser": [
                                "entityType": "com.jtbdevelopment.e_eye_o.entities.AppUser",
                                "id": appUser1.getId()
                        ],
                        "archived": false,
                        "categories": [[
                                "entityType": "com.jtbdevelopment.e_eye_o.entities.ObservationCategory",
                                "id": oc1For1.getId()
                        ]],
                        "comment": "Comment",
                        "id": o2ForS1.getId(),
                        "modificationTimestamp": o2ForS1.getModificationTimestamp().getMillis(),
                        "observationSubject": [
                                "entityType": "com.jtbdevelopment.e_eye_o.entities.Student",
                                "id": student1For1.getId()
                        ],
                        "observationTimestamp": [2012, 1, 18, 15, 12, 0, 0],
                        "significant": true
                ],
                (photo1for1): [
                        "entityType": "com.jtbdevelopment.e_eye_o.entities.Photo",
                        "appUser": [
                                "entityType": "com.jtbdevelopment.e_eye_o.entities.AppUser",
                                "id": appUser1.getId()
                        ],
                        "archived": false,
                        "description": "Photo1",
                        "id": photo1for1.getId(),
                        "imageData": "iVBORw0KGgoAAAANSUhEUgAAAGoAAABVCAIAAAAub+a4AAAA5UlEQVR42u3UwRGDIBRAQeqyIOqxGpqxGBMSjYBkNOPJyb4b8E87QJh1oYAAHz58+IQPHz58wocPHz7hw4cPn/Dhw4cPn/Dhw4dP+PDhwyd8+PDhEz58N+WLYV9M8zQO1dYwTnk6xXKoWof65L9uX+ZaiD7rluFFum6m2I6Xa3x9vi9G+I75lhfdY8K35+t+Z+tvV2PhO3H7mntYzOP7ia8dwHfI99zaJvIbdvvO/n3vs3LT3yd8+PDhEz58+PDhEz58+PAJHz58+IQPHz58wocPHz58wocPHz7hw4cPn/Dhw4dPWw8gxFB9W8w6HAAAAABJRU5ErkJggg==",
                        "mimeType": "image/png",
                        "modificationTimestamp": photo1for1.getModificationTimestamp().getMillis(),
                        "photoFor": [
                                "entityType": "com.jtbdevelopment.e_eye_o.entities.Student",
                                "id": student1For1.getId()
                        ],
                        "thumbnailImageData": "iVBORw0KGgoAAAANSUhEUgAAAGoAAABVCAIAAAAub+a4AAAA5UlEQVR42u3UwRGDIBRAQeqyIOqxGpqxGBMSjYBkNOPJyb4b8E87QJh1oYAAHz58+IQPHz58wocPHz7hw4cPn/Dhw4cPn/Dhw4dP+PDhwyd8+PDhEz58N+WLYV9M8zQO1dYwTnk6xXKoWof65L9uX+ZaiD7rluFFum6m2I6Xa3x9vi9G+I75lhfdY8K35+t+Z+tvV2PhO3H7mntYzOP7ia8dwHfI99zaJvIbdvvO/n3vs3LT3yd8+PDhEz58+PDhEz58+PAJHz58+IQPHz58wocPHz58wocPHz7hw4cPn/Dhw4dPWw8gxFB9W8w6HAAAAABJRU5ErkJggg==",
                        "timestamp": [2011, 11, 11, 11, 11, 11, 0]
                ],
                (photo2for1): [
                        "entityType": "com.jtbdevelopment.e_eye_o.entities.Photo",
                        "appUser": [
                                "entityType": "com.jtbdevelopment.e_eye_o.entities.AppUser",
                                "id": appUser1.getId()
                        ],
                        "archived": false,
                        "description": "Photo2",
                        "id": photo2for1.getId(),
                        "imageData": "iVBORw0KGgoAAAANSUhEUgAAAGoAAABVCAIAAAAub+a4AAAA5UlEQVR42u3UwRGDIBRAQeqyIOqxGpqxGBMSjYBkNOPJyb4b8E87QJh1oYAAHz58+IQPHz58wocPHz7hw4cPn/Dhw4cPn/Dhw4dP+PDhwyd8+PDhEz58N+WLYV9M8zQO1dYwTnk6xXKoWof65L9uX+ZaiD7rluFFum6m2I6Xa3x9vi9G+I75lhfdY8K35+t+Z+tvV2PhO3H7mntYzOP7ia8dwHfI99zaJvIbdvvO/n3vs3LT3yd8+PDhEz58+PDhEz58+PAJHz58+IQPHz58wocPHz58wocPHz7hw4cPn/Dhw4dPWw8gxFB9W8w6HAAAAABJRU5ErkJggg==",
                        "mimeType": "image/png",
                        "modificationTimestamp": photo2for1.getModificationTimestamp().getMillis(),
                        "photoFor": [
                                "entityType": "com.jtbdevelopment.e_eye_o.entities.Observation",
                                "id": o2ForS1.getId()
                        ],
                        "thumbnailImageData": "iVBORw0KGgoAAAANSUhEUgAAAGoAAABVCAIAAAAub+a4AAAA5UlEQVR42u3UwRGDIBRAQeqyIOqxGpqxGBMSjYBkNOPJyb4b8E87QJh1oYAAHz58+IQPHz58wocPHz7hw4cPn/Dhw4cPn/Dhw4dP+PDhwyd8+PDhEz58N+WLYV9M8zQO1dYwTnk6xXKoWof65L9uX+ZaiD7rluFFum6m2I6Xa3x9vi9G+I75lhfdY8K35+t+Z+tvV2PhO3H7mntYzOP7ia8dwHfI99zaJvIbdvvO/n3vs3LT3yd8+PDhEz58+PDhEz58+PAJHz58+IQPHz58wocPHz58wocPHz7hw4cPn/Dhw4dPWw8gxFB9W8w6HAAAAABJRU5ErkJggg==",
                        "timestamp": [2012, 12, 12, 12, 12, 12, 0]
                ],
                (photo3for1): [
                        "entityType": "com.jtbdevelopment.e_eye_o.entities.Photo",
                        "appUser": [
                                "entityType": "com.jtbdevelopment.e_eye_o.entities.AppUser",
                                "id": appUser1.getId()
                        ],
                        "archived": false,
                        "description": "Photo3",
                        "id": photo3for1.getId(),
                        "imageData": "iVBORw0KGgoAAAANSUhEUgAAAGoAAABVCAIAAAAub+a4AAAA5UlEQVR42u3UwRGDIBRAQeqyIOqxGpqxGBMSjYBkNOPJyb4b8E87QJh1oYAAHz58+IQPHz58wocPHz7hw4cPn/Dhw4cPn/Dhw4dP+PDhwyd8+PDhEz58N+WLYV9M8zQO1dYwTnk6xXKoWof65L9uX+ZaiD7rluFFum6m2I6Xa3x9vi9G+I75lhfdY8K35+t+Z+tvV2PhO3H7mntYzOP7ia8dwHfI99zaJvIbdvvO/n3vs3LT3yd8+PDhEz58+PDhEz58+PAJHz58+IQPHz58wocPHz58wocPHz7hw4cPn/Dhw4dPWw8gxFB9W8w6HAAAAABJRU5ErkJggg==",
                        "mimeType": "image/png",
                        "modificationTimestamp": photo3for1.getModificationTimestamp().getMillis(),
                        "photoFor": [
                                "entityType": "com.jtbdevelopment.e_eye_o.entities.ClassList",
                                "id": classList1For1.getId()
                        ],
                        "thumbnailImageData": "iVBORw0KGgoAAAANSUhEUgAAAGoAAABVCAIAAAAub+a4AAAA5UlEQVR42u3UwRGDIBRAQeqyIOqxGpqxGBMSjYBkNOPJyb4b8E87QJh1oYAAHz58+IQPHz58wocPHz7hw4cPn/Dhw4cPn/Dhw4dP+PDhwyd8+PDhEz58N+WLYV9M8zQO1dYwTnk6xXKoWof65L9uX+ZaiD7rluFFum6m2I6Xa3x9vi9G+I75lhfdY8K35+t+Z+tvV2PhO3H7mntYzOP7ia8dwHfI99zaJvIbdvvO/n3vs3LT3yd8+PDhEz58+PDhEz58+PAJHz58+IQPHz58wocPHz58wocPHz7hw4cPn/Dhw4dPWw8gxFB9W8w6HAAAAABJRU5ErkJggg==",
                        "timestamp": [2013, 1, 1, 15, 12, 45, 0]
                ],
        ];
    }

}
