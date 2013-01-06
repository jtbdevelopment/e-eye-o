package com.jtbdevelopment.e_eye_o.hibernate.DAO;

import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.wrapper.DAOIdObjectWrapperFactory;
import com.jtbdevelopment.e_eye_o.hibernate.entities.impl.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.testng.AssertJUnit.*;

/**
 * Date: 1/6/13
 * Time: 3:32 PM
 */
public class HibernateReadWriteDAOTest {
    public static final String TN = "TN";
    private Mockery context;
    private SessionFactory sessionFactory;
    private Session session;
    private DAOIdObjectWrapperFactory daoIdObjectWrapperFactory;
    private HibernateReadWriteDAO dao;
    private ClassMetadata metadata;

    private ClassList classListImpl, classListWrapped, classListLoaded;
    private Observation observationImpl, observationWrapped, observationLoaded;
    private ObservationCategory observationCategoryImpl, observationCategoryWrapped, observationCategoryLoaded;
    private Photo photoImpl, photoWrapped, photoLoaded;
    private Student studentImpl, studentWrapped, studentLoaded;
    private AppUser appUserImpl, appUserWrapped, appUserLoaded;
    private List<IdObject> wrapped = new ArrayList<>();
    private List<IdObject> impl = new ArrayList<>();
    private List<IdObject> loaded = new ArrayList<>();

    @BeforeMethod
    public void setUp() throws Exception {
        context = new Mockery();
        sessionFactory = context.mock(SessionFactory.class);
        session = context.mock(Session.class);
        daoIdObjectWrapperFactory = context.mock(DAOIdObjectWrapperFactory.class);
        classListImpl = context.mock(ClassList.class, "CLI");
        classListLoaded = context.mock(ClassList.class, "CLL");
        classListWrapped = context.mock(ClassList.class, "CLW");
        observationImpl = context.mock(Observation.class, "OI");
        observationLoaded = context.mock(Observation.class, "OL");
        observationWrapped = context.mock(Observation.class, "OW");
        observationCategoryImpl = context.mock(ObservationCategory.class, "OCI");
        observationCategoryWrapped = context.mock(ObservationCategory.class, "OCL");
        observationCategoryLoaded = context.mock(ObservationCategory.class, "OCW");
        photoImpl = context.mock(Photo.class, "PI");
        photoLoaded = context.mock(Photo.class, "PL");
        photoWrapped = context.mock(Photo.class, "PW");
        studentImpl = context.mock(Student.class, "SI");
        studentLoaded = context.mock(Student.class, "SL");
        studentWrapped = context.mock(Student.class, "SW");
        appUserImpl = context.mock(AppUser.class, "AUI");
        appUserWrapped = context.mock(AppUser.class, "AUL");
        appUserLoaded = context.mock(AppUser.class, "AUW");
        metadata = context.mock(ClassMetadata.class);

        impl.clear();
        Collections.addAll(impl, classListImpl, appUserImpl, studentImpl, photoImpl, observationImpl, observationCategoryImpl);
        loaded.clear();
        Collections.addAll(loaded, classListLoaded, appUserLoaded, studentLoaded, photoLoaded, observationLoaded, observationCategoryLoaded);
        wrapped.clear();
        Collections.addAll(wrapped, classListWrapped, appUserWrapped, studentWrapped, photoWrapped, observationWrapped, observationCategoryWrapped);
        context.checking(new Expectations() {{
            allowing(sessionFactory).getCurrentSession();
            will(returnValue(session));
            allowing(daoIdObjectWrapperFactory).wrap(classListImpl);
            will(returnValue(classListWrapped));
            allowing(daoIdObjectWrapperFactory).wrap(classListWrapped);
            will(returnValue(classListWrapped));
            allowing(daoIdObjectWrapperFactory).wrap(observationImpl);
            will(returnValue(observationWrapped));
            allowing(daoIdObjectWrapperFactory).wrap(observationWrapped);
            will(returnValue(observationWrapped));
            allowing(daoIdObjectWrapperFactory).wrap(observationCategoryImpl);
            will(returnValue(observationCategoryWrapped));
            allowing(daoIdObjectWrapperFactory).wrap(observationCategoryWrapped);
            will(returnValue(observationCategoryWrapped));
            allowing(daoIdObjectWrapperFactory).wrap(photoImpl);
            will(returnValue(photoWrapped));
            allowing(daoIdObjectWrapperFactory).wrap(photoWrapped);
            will(returnValue(photoWrapped));
            allowing(daoIdObjectWrapperFactory).wrap(studentImpl);
            will(returnValue(studentWrapped));
            allowing(daoIdObjectWrapperFactory).wrap(studentWrapped);
            will(returnValue(studentWrapped));
            allowing(daoIdObjectWrapperFactory).wrap(appUserImpl);
            will(returnValue(appUserWrapped));
            allowing(daoIdObjectWrapperFactory).wrap(appUserWrapped);
            will(returnValue(appUserWrapped));
            allowing(daoIdObjectWrapperFactory).wrap(impl);
            will(returnValue(wrapped));
            allowing(daoIdObjectWrapperFactory).wrap(wrapped);
            will(returnValue(wrapped));
            allowing(daoIdObjectWrapperFactory).getWrapperForEntity(AppUser.class);
            will(returnValue(HibernateAppUser.class));
            allowing(daoIdObjectWrapperFactory).getWrapperForEntity(Student.class);
            will(returnValue(HibernateStudent.class));
            allowing(daoIdObjectWrapperFactory).getWrapperForEntity(Photo.class);
            will(returnValue(HibernatePhoto.class));
            allowing(daoIdObjectWrapperFactory).getWrapperForEntity(ClassList.class);
            will(returnValue(HibernateClassList.class));
            allowing(daoIdObjectWrapperFactory).getWrapperForEntity(Observation.class);
            will(returnValue(HibernateObservation.class));
            allowing(daoIdObjectWrapperFactory).getWrapperForEntity(ObservationCategory.class);
            will(returnValue(HibernateObservationCategory.class));
            allowing(sessionFactory).getClassMetadata(with(any(Class.class)));
            will(returnValue(metadata));
            allowing(metadata).getEntityName();
            will(returnValue(TN));
        }});

        dao = new HibernateReadWriteDAO(sessionFactory, daoIdObjectWrapperFactory);
    }

    @Test
    public void testCreateSingleWithImpls() {
        context.checking(new Expectations() {{
            one(session).save(classListWrapped);
            one(session).save(studentWrapped);
            one(session).save(observationCategoryWrapped);
            one(session).save(observationWrapped);
            one(session).save(photoWrapped);
            one(session).save(appUserWrapped);
        }});

        for(IdObject i : impl) {
            IdObject r = dao.create(i);
            assertNotSame(i, r);
            assertTrue(wrapped.contains(r));
        }
    }

    @Test
    public void testCreateSingleWithWrapped() {
        context.checking(new Expectations() {{
            one(session).save(classListWrapped);
            one(session).save(studentWrapped);
            one(session).save(observationCategoryWrapped);
            one(session).save(observationWrapped);
            one(session).save(photoWrapped);
            one(session).save(appUserWrapped);
        }});

        for(IdObject i : wrapped) {
            IdObject r = dao.create(i);
            assertSame(i, r);
            assertTrue(wrapped.contains(r));
        }
    }

    @Test
    public void testCreateMultiWithImpls() {
        context.checking(new Expectations() {{
            one(session).save(classListWrapped);
            one(session).save(studentWrapped);
            one(session).save(observationCategoryWrapped);
            one(session).save(observationWrapped);
            one(session).save(photoWrapped);
            one(session).save(appUserWrapped);
        }});

        Collection<IdObject> r = dao.create(impl);
        assertSame(wrapped, r);
    }

    @Test
    public void testCreateMultiWithWrapped() {
        context.checking(new Expectations() {{
            one(session).save(classListWrapped);
            one(session).save(studentWrapped);
            one(session).save(observationCategoryWrapped);
            one(session).save(observationWrapped);
            one(session).save(photoWrapped);
            one(session).save(appUserWrapped);
        }});

        Collection<IdObject> r = dao.create(wrapped);
        assertSame(wrapped, r);
    }

    @Test
    public void testUpdateSingleWithImpls() {
        context.checking(new Expectations() {{
            one(session).update(classListWrapped);
            one(session).update(studentWrapped);
            one(session).update(observationCategoryWrapped);
            one(session).update(observationWrapped);
            one(session).update(photoWrapped);
            one(session).update(appUserWrapped);
        }});

        for(IdObject i : impl) {
            IdObject r = dao.update(i);
            assertNotSame(i, r);
            assertTrue(wrapped.contains(r));
        }
    }

    @Test
    public void testUpdateSingleWithWrapped() {
        context.checking(new Expectations() {{
            one(session).update(classListWrapped);
            one(session).update(studentWrapped);
            one(session).update(observationCategoryWrapped);
            one(session).update(observationWrapped);
            one(session).update(photoWrapped);
            one(session).update(appUserWrapped);
        }});

        for(IdObject i : wrapped) {
            IdObject r = dao.update(i);
            assertSame(i, r);
            assertTrue(wrapped.contains(r));
        }
    }

    @Test
    public void testUpdateMultiWithImpls() {
        context.checking(new Expectations() {{
            one(session).update(classListWrapped);
            one(session).update(studentWrapped);
            one(session).update(observationCategoryWrapped);
            one(session).update(observationWrapped);
            one(session).update(photoWrapped);
            one(session).update(appUserWrapped);
        }});

        Collection<IdObject> r = dao.update(impl);
        assertSame(wrapped, r);
    }

    @Test
    public void testUpdateMultiWithWrapped() {
        context.checking(new Expectations() {{
            one(session).update(classListWrapped);
            one(session).update(studentWrapped);
            one(session).update(observationCategoryWrapped);
            one(session).update(observationWrapped);
            one(session).update(photoWrapped);
            one(session).update(appUserWrapped);
        }});

        Collection<IdObject> r = dao.update(wrapped);
        assertSame(wrapped, r);
    }

    @Test
    public void testDeleteNonLoadableUserReturnsFromImpl() {
        context.checking(new Expectations() {{
            one(appUserWrapped).getId();
            will(returnValue("X"));
            one(session).get(TN, "X");
            will(returnValue(null));
        }});

        dao.deleteUser(appUserImpl);
    }

    @Test
    public void testDeleteNonLoadableUserReturnsFromWrapped() {
        context.checking(new Expectations() {{
            one(appUserWrapped).getId();
            will(returnValue("X"));
            one(session).get(TN, "X");
            will(returnValue(null));
        }});

        dao.deleteUser(appUserWrapped);
    }
}
