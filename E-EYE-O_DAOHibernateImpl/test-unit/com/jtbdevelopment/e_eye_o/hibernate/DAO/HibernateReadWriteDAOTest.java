package com.jtbdevelopment.e_eye_o.hibernate.DAO;

import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.wrapper.DAOIdObjectWrapperFactory;
import com.jtbdevelopment.e_eye_o.hibernate.entities.impl.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.*;

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
    private Query query;

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
        query = context.mock(Query.class);
        daoIdObjectWrapperFactory = context.mock(DAOIdObjectWrapperFactory.class);
        classListImpl = context.mock(ClassList.class, "CLI");
        classListLoaded = context.mock(ClassList.class, "CLL");
        classListWrapped = context.mock(ClassList.class, "CLW");
        observationImpl = context.mock(Observation.class, "OI");
        observationLoaded = context.mock(Observation.class, "OL");
        observationWrapped = context.mock(Observation.class, "OW");
        observationCategoryImpl = context.mock(ObservationCategory.class, "OCI");
        observationCategoryLoaded = context.mock(ObservationCategory.class, "OCL");
        observationCategoryWrapped = context.mock(ObservationCategory.class, "OCW");
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
            allowing(daoIdObjectWrapperFactory).wrap(photoLoaded);
            will(returnValue(photoWrapped));
            allowing(daoIdObjectWrapperFactory).wrap(studentImpl);
            will(returnValue(studentWrapped));
            allowing(daoIdObjectWrapperFactory).wrap(studentWrapped);
            will(returnValue(studentWrapped));
            allowing(daoIdObjectWrapperFactory).wrap(studentLoaded);
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
            allowing(daoIdObjectWrapperFactory).getWrapperForEntity(studentImpl.getClass());
            will(returnValue(HibernateStudent.class));
            allowing(daoIdObjectWrapperFactory).getWrapperForEntity(photoImpl.getClass());
            will(returnValue(HibernatePhoto.class));
            allowing(daoIdObjectWrapperFactory).getWrapperForEntity(classListImpl.getClass());
            will(returnValue(HibernateClassList.class));
            allowing(daoIdObjectWrapperFactory).getWrapperForEntity(observationImpl.getClass());
            will(returnValue(HibernateObservation.class));
            allowing(daoIdObjectWrapperFactory).getWrapperForEntity(observationCategoryImpl.getClass());
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

        for (IdObject i : impl) {
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

        for (IdObject i : wrapped) {
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

        for (IdObject i : impl) {
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

        for (IdObject i : wrapped) {
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

    @Test
    public void testDeleteNonLoadableObjectReturnsFromImpl() {
        context.checking(new Expectations() {{
            one(observationCategoryWrapped).getId();
            will(returnValue("X"));
            one(session).get(TN, "X");
            will(returnValue(null));
        }});

        dao.delete(observationCategoryImpl);
    }

    @Test
    public void testDeleteNonLoadableObjectReturnsFromWrapped() {
        context.checking(new Expectations() {{
            one(studentWrapped).getId();
            will(returnValue("X"));
            one(session).get(TN, "X");
            will(returnValue(null));
        }});

        dao.delete(studentWrapped);
    }

    @Test
    public void testDeleteObsCategoryLooksForObsWithItAndUpdatesThem() {
        final ObservationCategory impl = observationCategoryImpl;
        final ObservationCategory wrapped = observationCategoryWrapped;
        final ObservationCategory loaded = observationCategoryLoaded;
        final List<Observation> relatedOCObservations = Arrays.asList(observationLoaded);
        final List<Photo> relatedPhotos = Collections.emptyList();
        final List<Observation> relatedObservations = Collections.emptyList();

        context.checking(new Expectations() {{
            one(session).createQuery("from Observation as O where :category member of O.categories");
            will(returnValue(query));
            one(query).setParameter("category", loaded);
            will(returnValue(query));
            one(query).list();
            will(returnValue(relatedOCObservations));
            one(observationLoaded).removeCategory(loaded);
            will(returnValue(observationLoaded));
            one(session).update(observationLoaded);
        }});
        createStandardDeleteExpectations(wrapped, loaded, relatedPhotos, relatedObservations);

        dao.delete(impl);
    }

    @Test
    public void testDeleteClassListLooksForRelatedObjectsAndUpdatesOrDeletesThem() {
        final ClassList impl = classListImpl;
        final ClassList wrapped = classListWrapped;
        final ClassList loaded = classListLoaded;
        final List<Student> relatedStudents = Arrays.asList(studentLoaded);
        final List<Photo> relatedPhotos = Arrays.asList(photoLoaded);
        final List<Observation> relatedObservations = Arrays.asList(observationLoaded);

        context.checking(new Expectations() {{
            one(session).createQuery("from Student as S where :classList member of S.classLists");
            will(returnValue(query));
            one(query).setParameter("classList", loaded);
            will(returnValue(query));
            one(query).list();
            will(returnValue(relatedStudents));
            one(studentLoaded).removeClassList(loaded);
            will(returnValue(studentLoaded));
            one(session).update(studentLoaded);
        }});
        createStandardDeleteExpectations(wrapped, loaded, relatedPhotos, relatedObservations);

        dao.delete(impl);
    }

    @Test
    public void testDeleteObservationLooksForRelatedObjectsAndUpdatesOrDeletesThem() {
        final Observation impl = observationImpl;
        final Observation wrapped = observationWrapped;
        final Observation loaded = observationLoaded;
        final List<Observation> relatedFollowUpObservations = Arrays.asList(observationLoaded);
        final List<Photo> relatedPhotos = Arrays.asList(photoLoaded);
        final List<Observation> relatedObservations = Arrays.asList(observationLoaded);

        context.checking(new Expectations() {{
            one(session).createQuery("from Observation as O where followUpObservation = :followUpObservation");
            will(returnValue(query));
            one(query).setParameter("followUpObservation", loaded);
            will(returnValue(query));
            one(query).list();
            will(returnValue(relatedFollowUpObservations));
            one(observationLoaded).setFollowUpObservation(null);
            will(returnValue(observationLoaded));
            one(session).update(observationLoaded);
        }});

        createStandardDeleteExpectations(wrapped, loaded, relatedPhotos, relatedObservations);

        dao.delete(impl);
    }

    @Test
    public void testDeleteStudentLooksForRelatedObjectsAndUpdatesOrDeletesThem() {
        final Student impl = studentImpl;
        final Student wrapped = studentWrapped;
        final Student loaded = studentLoaded;
        final List<Photo> relatedPhotos = Arrays.asList(photoLoaded);
        final List<Observation> relatedObservations = Arrays.asList(observationLoaded);

        createStandardDeleteExpectations(wrapped, loaded, relatedPhotos, relatedObservations);

        dao.delete(impl);
    }

    @Test
    public void testDeletePhotoLooksForRelatedObjectsAndUpdatesOrDeletesThem() {
        final Photo impl = photoImpl;
        final Photo wrapped = photoWrapped;
        final Photo loaded = photoLoaded;
        final List<Photo> relatedPhotos = Collections.emptyList();
        final List<Observation> relatedObservations = Collections.emptyList();

        createStandardDeleteExpectations(wrapped, loaded, relatedPhotos, relatedObservations);

        dao.delete(impl);
    }

    @Test
    public void testDeletingMultipleItems() {
        final Photo pImpl = photoImpl;
        final Photo pWrapped = photoWrapped;
        final Photo pLoaded = photoLoaded;
        final List<Photo> pRelatedPhotos = Collections.emptyList();
        final List<Observation> pRelatedObservations = Collections.emptyList();

        createStandardDeleteExpectations(pWrapped, pLoaded, pRelatedPhotos, pRelatedObservations);

        final Student sImpl = studentImpl;
        final Student sWrapped = studentWrapped;
        final Student sLoaded = studentLoaded;
        final List<Photo> sRelatedPhotos = Arrays.asList(photoLoaded);
        final List<Observation> sRelatedObservations = Arrays.asList(observationLoaded);

        createStandardDeleteExpectations(sWrapped, sLoaded, sRelatedPhotos, sRelatedObservations);

        final List<ArchivableAppUserOwnedObject> entities = Arrays.asList(pImpl, sImpl);
        dao.delete(entities);
    }

    @Test
    public void testDeletingAUser() {
        createDeleteUserExpectations();

        dao.deleteUser(appUserImpl);
    }

    @Test
    public void testDeletingUsers() {
        createDeleteUserExpectations();

        dao.deleteUsers(Arrays.asList(appUserImpl));
    }

    private void createDeleteUserExpectations() {
        final List<ArchivableAppUserOwnedObject> ownedObjects = Arrays.asList(photoLoaded, studentLoaded);
        context.checking(new Expectations() {{
            one(appUserWrapped).getId();
            will(returnValue("X"));
            one(session).get(TN, "X");
            will(returnValue(appUserLoaded));
            one(session).createQuery("from AppUserOwnedObject where appUser = :appUser");
            will(returnValue(query));
            one(query).setParameter("appUser", appUserLoaded);
            will(returnValue(query));
            one(query).list();
            will(returnValue(ownedObjects));
            one(session).delete(appUserLoaded);
        }});

        final Photo pWrapped = photoWrapped;
        final Photo pLoaded = photoLoaded;
        final List<Photo> pRelatedPhotos = Collections.emptyList();
        final List<Observation> pRelatedObservations = Collections.emptyList();

        createStandardDeleteExpectations(pWrapped, pLoaded, pRelatedPhotos, pRelatedObservations);

        final Student sWrapped = studentWrapped;
        final Student sLoaded = studentLoaded;
        final List<Photo> sRelatedPhotos = Arrays.asList(photoLoaded);
        final List<Observation> sRelatedObservations = Arrays.asList(observationLoaded);

        createStandardDeleteExpectations(sWrapped, sLoaded, sRelatedPhotos, sRelatedObservations);
    }

    private void createStandardDeleteExpectations(final IdObject wrapped, final IdObject loaded, final List<Photo> relatedPhotos, final List<Observation> relatedObservations) {
        context.checking(new Expectations() {{
            one(wrapped).getId();
            will(returnValue("X"));
            one(session).get(TN, "X");
            will(returnValue(loaded));
            one(session).createQuery("from Photo where photoFor = :photoFor");
            will(returnValue(query));
            one(query).setParameter("photoFor", loaded);
            will(returnValue(query));
            one(query).list();
            will(returnValue(relatedPhotos));
            for (Photo p : relatedPhotos) {
                one(session).delete(p);
            }
            one(session).createQuery("from Observation where observationSubject = :observationSubject");
            will(returnValue(query));
            one(query).setParameter("observationSubject", loaded);
            will(returnValue(query));
            one(query).list();
            will(returnValue(relatedObservations));
            for (Observation o : relatedObservations) {
                one(session).delete(o);
            }
            one(session).delete(loaded);
        }});
    }
}