package com.jtbdevelopment.e_eye_o.hibernate.entities.wrapper;

import com.jtbdevelopment.e_eye_o.hibernate.entities.HibernateAbstractIdObjectTest;
import org.testng.annotations.Test;

/**
 * Date: 1/6/13
 * Time: 2:52 PM
 */
@Test
public class HibernateIdObjectWrapperConfigTest extends HibernateAbstractIdObjectTest {
    //  TODO - redo
    /*
    private HibernateIdObjectWrapperConfig factory;

    @BeforeClass
    public void setUp() {
        super.setUp();
        factory = new HibernateIdObjectWrapperConfig(implFactory, idObjectReflectionHelper);
    }

    private static final Map<Class<? extends IdObject>, Class<? extends IdObject>> expectedEntries = new HashMap<Class<? extends IdObject>, Class<? extends IdObject>>() {{
        put(AppUser.class, HibernateAppUser.class);
        put(Student.class, HibernateStudent.class);
        put(Photo.class, HibernatePhoto.class);
        put(ObservationCategory.class, HibernateObservationCategory.class);
        put(Observation.class, HibernateObservation.class);
        put(ClassList.class, HibernateClassList.class);
        put(DeletedObject.class, HibernateDeletedObject.class);
    }};

    @Test
    public void testEntitiesToWrappers() {
        for (Map.Entry<Class<? extends IdObject>, Class<? extends IdObject>> entry : expectedEntries.entrySet()) {
            assertEquals(entry.getValue(), factory.getWrapperForEntity(entry.getKey()));
        }
    }

    @Test
    public void testWrappersToEntities() {
        for (Map.Entry<Class<? extends IdObject>, Class<? extends IdObject>> entry : expectedEntries.entrySet()) {
            assertEquals(entry.getKey(), factory.getEntityForWrapper(entry.getValue()));
        }
    }
    */
}
