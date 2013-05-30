package com.jtbdevelopment.e_eye_o.hibernate.DAO;

import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.Observable;
import com.jtbdevelopment.e_eye_o.entities.reflection.IdObjectInterfaceResolver;
import com.jtbdevelopment.e_eye_o.entities.wrapper.DAOIdObjectWrapperFactory;
import com.jtbdevelopment.e_eye_o.hibernate.entities.impl.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.joda.time.LocalDateTime;
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
    public static final String DN = "DN";
    private Mockery context;
    private SessionFactory sessionFactory;
    private IdObjectInterfaceResolver idObjectInterfaceResolver;
    private Session session;
    private DAOIdObjectWrapperFactory daoIdObjectWrapperFactory;
    private HibernateReadWriteDAO dao;
    private ClassMetadata metadata, deletedMetaData;
    private Query query1, query2, query3;

    private ClassList classListImpl, classListWrapped, classListLoaded;
    private Observation observationImpl, observationWrapped, observationLoaded;
    private ObservationCategory observationCategoryImpl, observationCategoryWrapped, observationCategoryLoaded;
    private Photo photoImpl, photoWrapped, photoLoaded;
    private Student studentImpl, studentWrapped, studentLoaded;
    private AppUser appUserImpl, appUserWrapped, appUserLoaded;
    private DeletedObject deletedImpl, deletedWrapped, deletedLoaded;
    private List<IdObject> wrapped = new ArrayList<>();
    private List<IdObject> impl = new ArrayList<>();
    private List<IdObject> loaded = new ArrayList<>();

    private static final LocalDateTime now = new LocalDateTime();
    private static final LocalDateTime past = now.minusHours(1);

    @BeforeMethod
    public void setUp() throws Exception {
        context = new Mockery();
        sessionFactory = context.mock(SessionFactory.class);
        idObjectInterfaceResolver = context.mock(IdObjectInterfaceResolver.class);
        session = context.mock(Session.class);
        query1 = context.mock(Query.class, "Q1");
        query2 = context.mock(Query.class, "Q2");
        query3 = context.mock(Query.class, "Q3");
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
        deletedImpl = context.mock(DeletedObject.class, "DOI");
        deletedLoaded = context.mock(DeletedObject.class, "DOL");
        deletedWrapped = context.mock(DeletedObject.class, "DOW");
        metadata = context.mock(ClassMetadata.class, "MD");
        deletedMetaData = context.mock(ClassMetadata.class, "DMD");

        impl.clear();
        Collections.addAll(impl, classListImpl, appUserImpl, studentImpl, photoImpl, observationImpl, observationCategoryImpl);
        loaded.clear();
        Collections.addAll(loaded, classListLoaded, appUserLoaded, studentLoaded, photoLoaded, observationLoaded, observationCategoryLoaded);
        wrapped.clear();
        Collections.addAll(wrapped, classListWrapped, appUserWrapped, studentWrapped, photoWrapped, observationWrapped, observationCategoryWrapped);
        context.checking(new Expectations() {{
            one(idObjectInterfaceResolver).getIdObjectInterfaceForClass(observationLoaded.getClass());
            will(returnValue(Observation.class));
            one(idObjectInterfaceResolver).getIdObjectInterfaceForClass(observationWrapped.getClass());
            will(returnValue(Observation.class));
            one(idObjectInterfaceResolver).getIdObjectInterfaceForClass(classListLoaded.getClass());
            will(returnValue(ClassList.class));
            one(idObjectInterfaceResolver).getIdObjectInterfaceForClass(classListWrapped.getClass());
            will(returnValue(ClassList.class));
            allowing(idObjectInterfaceResolver).getIdObjectInterfaceForClass(studentWrapped.getClass());
            will(returnValue(Student.class));
            allowing(idObjectInterfaceResolver).getIdObjectInterfaceForClass(studentLoaded.getClass());
            will(returnValue(Student.class));
            allowing(idObjectInterfaceResolver).getIdObjectInterfaceForClass(observationCategoryLoaded.getClass());
            will(returnValue(ObservationCategory.class));
            allowing(idObjectInterfaceResolver).getIdObjectInterfaceForClass(observationCategoryWrapped.getClass());
            will(returnValue(ObservationCategory.class));
            allowing(idObjectInterfaceResolver).getIdObjectInterfaceForClass(photoWrapped.getClass());
            will(returnValue(Photo.class));
            allowing(idObjectInterfaceResolver).getIdObjectInterfaceForClass(photoLoaded.getClass());
            will(returnValue(Photo.class));
            allowing(idObjectInterfaceResolver).getIdObjectInterfaceForClass(appUserLoaded.getClass());
            will(returnValue(AppUser.class));
            allowing(idObjectInterfaceResolver).getIdObjectInterfaceForClass(appUserWrapped.getClass());
            will(returnValue(AppUser.class));
            allowing(idObjectInterfaceResolver).getIdObjectInterfaceForClass(DeletedObject.class);
            will(returnValue(DeletedObject.class));
            allowing(idObjectInterfaceResolver).getIdObjectInterfaceForClass(AppUser.class);
            will(returnValue(AppUser.class));
            allowing(idObjectInterfaceResolver).getIdObjectInterfaceForClass(AppUserOwnedObject.class);
            will(returnValue(AppUserOwnedObject.class));
            allowing(sessionFactory).getCurrentSession();
            will(returnValue(session));
            allowing(daoIdObjectWrapperFactory).wrap(deletedImpl);
            will(returnValue(deletedWrapped));
            allowing(daoIdObjectWrapperFactory).wrap(deletedWrapped);
            will(returnValue(deletedWrapped));
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
            allowing(daoIdObjectWrapperFactory).getWrapperForEntity(AppUserOwnedObject.class);
            will(returnValue(HibernateAppUserOwnedObject.class));
            allowing(daoIdObjectWrapperFactory).getWrapperForEntity(studentImpl.getClass());
            will(returnValue(HibernateStudent.class));
            allowing(daoIdObjectWrapperFactory).getWrapperForEntity(Student.class);
            will(returnValue(HibernateStudent.class));
            allowing(daoIdObjectWrapperFactory).getWrapperForEntity(Photo.class);
            will(returnValue(HibernatePhoto.class));
            allowing(daoIdObjectWrapperFactory).getWrapperForEntity(photoImpl.getClass());
            will(returnValue(HibernatePhoto.class));
            allowing(daoIdObjectWrapperFactory).getWrapperForEntity(ClassList.class);
            will(returnValue(HibernateClassList.class));
            allowing(daoIdObjectWrapperFactory).getWrapperForEntity(classListImpl.getClass());
            will(returnValue(HibernateClassList.class));
            allowing(daoIdObjectWrapperFactory).getWrapperForEntity(Observation.class);
            will(returnValue(HibernateObservation.class));
            allowing(daoIdObjectWrapperFactory).getWrapperForEntity(observationImpl.getClass());
            will(returnValue(HibernateObservation.class));
            allowing(daoIdObjectWrapperFactory).getWrapperForEntity(ObservationCategory.class);
            will(returnValue(HibernateObservationCategory.class));
            allowing(daoIdObjectWrapperFactory).getWrapperForEntity(observationCategoryImpl.getClass());
            will(returnValue(HibernateObservationCategory.class));
            allowing(daoIdObjectWrapperFactory).getWrapperForEntity(deletedImpl.getClass());
            will(returnValue(HibernateDeletedObject.class));
            allowing(daoIdObjectWrapperFactory).getWrapperForEntity(DeletedObject.class);
            will(returnValue(HibernateDeletedObject.class));
            allowing(sessionFactory).getClassMetadata(HibernateDeletedObject.class);
            will(returnValue(deletedMetaData));
            allowing(sessionFactory).getClassMetadata(HibernateClassList.class);
            will(returnValue(metadata));
            allowing(sessionFactory).getClassMetadata(HibernateAppUserOwnedObject.class);
            will(returnValue(metadata));
            allowing(sessionFactory).getClassMetadata(HibernatePhoto.class);
            will(returnValue(metadata));
            allowing(sessionFactory).getClassMetadata(HibernateObservationCategory.class);
            will(returnValue(metadata));
            allowing(sessionFactory).getClassMetadata(HibernateObservation.class);
            will(returnValue(metadata));
            allowing(sessionFactory).getClassMetadata(HibernateStudent.class);
            will(returnValue(metadata));
            allowing(sessionFactory).getClassMetadata(HibernateAppUser.class);
            will(returnValue(metadata));
            allowing(metadata).getEntityName();
            will(returnValue(TN));
            allowing(deletedMetaData).getEntityName();
            will(returnValue(DN));
            allowing(observationImpl).getObservationSubject();
            will(returnValue(studentImpl));
            allowing(observationWrapped).getObservationSubject();
            will(returnValue(studentWrapped));
            allowing(observationLoaded).getObservationSubject();
            will(returnValue(studentLoaded));
            allowing(observationImpl).getObservationTimestamp();
            will(returnValue(now));
            allowing(observationWrapped).getObservationTimestamp();
            will(returnValue(now));
            allowing(observationLoaded).getObservationTimestamp();
            will(returnValue(now));
            allowing(studentImpl).getLastObservationTimestamp();
            will(returnValue(past));
            allowing(studentWrapped).getLastObservationTimestamp();
            will(returnValue(past));
            allowing(studentLoaded).getLastObservationTimestamp();
            will(returnValue(past));
            allowing(studentLoaded).setLastObservationTimestamp(now);
            allowing(studentImpl).setLastObservationTimestamp(now);
            allowing(studentWrapped).setLastObservationTimestamp(now);
        }});

        dao = new HibernateReadWriteDAO(sessionFactory, daoIdObjectWrapperFactory, idObjectInterfaceResolver);
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
            one(session).save(deletedWrapped);
            one(session).update(studentWrapped);
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
            one(session).save(deletedWrapped);
            one(session).update(studentWrapped);
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
            one(session).createQuery("select max(observationTimestamp) from Observation where observationSubject = :observationSubject");
            will(returnValue(query3));
            one(query3).setParameter("observationSubject", studentWrapped);
            will(returnValue(query3));
            one(query3).uniqueResult();
            will(returnValue(now));
            one(session).update(studentWrapped);
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
            one(session).createQuery("select max(observationTimestamp) from Observation where observationSubject = :observationSubject");
            will(returnValue(query3));
            one(query3).setParameter("observationSubject", studentWrapped);
            will(returnValue(query3));
            one(query3).uniqueResult();
            will(returnValue(now));
            one(session).update(studentWrapped);
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
            will(returnValue(query1));
            one(query1).setParameter("category", loaded);
            will(returnValue(query1));
            one(query1).list();
            will(returnValue(relatedOCObservations));
            one(observationLoaded).removeCategory(loaded);
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
            will(returnValue(query1));
            one(query1).setParameter("classList", loaded);
            will(returnValue(query1));
            one(query1).list();
            will(returnValue(relatedStudents));
            one(studentLoaded).removeClassList(loaded);
            one(session).update(studentLoaded);
        }});
        createStandardDeleteExpectations(wrapped, loaded, relatedPhotos, relatedObservations);
        createPhotoQueryMopUp();

        dao.delete(impl);
    }

    @Test
    public void testDeleteObservationLooksForRelatedObjectsAndUpdatesOrDeletesThem() {
        final Observation impl = observationImpl;
        final Observation wrapped = observationWrapped;
        final Observation loaded = observationLoaded;
        final List<Photo> relatedPhotos = Arrays.asList(photoLoaded);
        final List<Observation> relatedObservations = Arrays.asList(observationLoaded);

        createStandardDeleteExpectations(wrapped, loaded, relatedPhotos, relatedObservations);
        context.checking(new Expectations() {{
            //  Last Observation Time Update
            one(session).update(studentWrapped);
        }});

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
        createPhotoQueryMopUp();

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

        createPhotoQueryMopUp();

        final List<AppUserOwnedObject> entities = Arrays.asList(pImpl, sImpl);
        dao.delete(entities);
    }

    @Test
    public void testDeletingAUser() {
        createDeleteUserExpectations();

        dao.deleteUser(appUserImpl);
    }

    private void createDeleteUserExpectations() {
        final List<AppUserOwnedObject> ownedObjects = Arrays.asList(photoLoaded, studentLoaded, deletedLoaded);
        final List<DeletedObject> deletedList = Arrays.asList(deletedLoaded);
        context.checking(new Expectations() {{
            one(appUserWrapped).getId();
            will(returnValue("X"));
            one(session).get(TN, "X");
            will(returnValue(appUserLoaded));
            one(session).createQuery("from TN where appUser = :user");
            will(returnValue(query1));
            one(query1).setParameter("user", appUserLoaded);
            will(returnValue(query1));
            one(query1).list();
            will(returnValue(ownedObjects));
            one(session).createQuery("from DN where appUser = :user");
            will(returnValue(query2));
            one(query2).setParameter("user", appUserLoaded);
            will(returnValue(query2));
            one(query2).list();
            will(returnValue(deletedList));
            one(session).delete(appUserLoaded);
            one(session).delete(deletedLoaded);
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

        createPhotoQueryMopUp();
    }

    private void createPhotoQueryMopUp() {
        context.checking(new Expectations() {{
            Query query = context.mock(Query.class, "Q" + new Random().nextInt());
            allowing(session).createQuery("from Photo where photoFor = :photoFor");
            will(returnValue(query));
            one(query).setParameter(with(equal("photoFor")), with(any(Object.class)));
            will(returnValue(query));
            one(query).list();
            will(returnValue(Collections.emptyList()));
        }});
    }

    private void createStandardDeleteExpectations(final IdObject wrapped, final IdObject loaded, final List<Photo> relatedPhotos, final List<Observation> relatedObservations) {
        context.checking(new Expectations() {{
            Query query = context.mock(Query.class, "Q" + new Random().nextInt());
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
            if (loaded instanceof Observable) {
                one(session).createQuery("from Observation where observationSubject = :observationSubject");
                will(returnValue(query1));
                one(query1).setParameter("observationSubject", loaded);
                will(returnValue(query1));
                one(query1).list();
                will(returnValue(relatedObservations));
                for (Observation o : relatedObservations) {
                    one(session).delete(o);
                }
            }
            one(session).createQuery("select max(observationTimestamp) from Observation where observationSubject = :observationSubject");
            will(returnValue(query3));
            one(query3).setParameter("observationSubject", studentLoaded);
            will(returnValue(query3));
            one(query3).uniqueResult();
            will(returnValue(now));
            one(session).delete(loaded);
            one(session).flush();
        }});
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testCreatingDeletedObjectExceptions() {
        dao.create(deletedImpl);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testCreatingDeletedObjectAsListExceptions() {
        final List<DeletedObject> entities = Arrays.asList(deletedImpl);
        context.checking(new Expectations() {{
            one(daoIdObjectWrapperFactory).wrap(entities);
            will(returnValue(entities));
        }});
        dao.create(entities);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testDeletingDeletedObjectExceptions() {
        dao.delete(deletedImpl);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testDeletingDeletedObjectAsListExceptions() {
        dao.delete(Arrays.asList(deletedImpl));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUdpdateingDeletedObjectExceptions() {
        dao.update(deletedImpl);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUpdatingDeletedObjectAsListExceptions() {
        final List<DeletedObject> entities = Arrays.asList(deletedImpl);
        context.checking(new Expectations() {{
            one(daoIdObjectWrapperFactory).wrap(entities);
            will(returnValue(entities));
        }});
        dao.update(entities);
    }
}
