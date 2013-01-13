package com.jtbdevelopment.e_eye_o.hibernate.entities.wrapper;

import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.hibernate.entities.impl.*;
import org.jmock.Mockery;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;

/**
 * Date: 1/6/13
 * Time: 2:52 PM
 */
@Test
public class HibernateIdObjectWrapperFactoryTest {
    private Mockery context;
    private IdObjectFactory implFactory;
    private HibernateIdObjectWrapperFactory factory;

    @BeforeClass
    public void setUp() {
        context = new Mockery();
        implFactory = context.mock(IdObjectFactory.class);
        factory = new HibernateIdObjectWrapperFactory(implFactory);
    }

    private static final Map<Class<? extends IdObject>, Class<? extends IdObject>> expectedEntries = new HashMap<Class<? extends IdObject>, Class<? extends IdObject>>() {{
        put(AppUser.class, HibernateAppUser.class);
        put(Student.class, HibernateStudent.class);
        put(Photo.class, HibernatePhoto.class);
        put(ObservationCategory.class, HibernateObservationCategory.class);
        put(Observation.class, HibernateObservation.class);
        put(ClassList.class, HibernateClassList.class);

    }};

    @Test
    public void testSetsHibernateIdObjectFactories() {
        assertSame(implFactory, HibernateIdObject.getImplFactory());
        assertSame(factory, HibernateIdObject.getDaoFactory());
    }

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
}
