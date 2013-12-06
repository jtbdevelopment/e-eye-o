package com.jtbdevelopment.e_eye_o.hibernate.DAO;

/**
 * Date: 1/6/13
 * Time: 3:32 PM
 */
public class HibernateReadWriteDAOTest {
    //  TODO - revisit
    /*
    public static final String TN = "TN";
    public static final String DN = "DN";
    private Mockery context;
    private SessionFactory sessionFactory;
    private IdObjectReflectionHelper idObjectReflectionHelper;
    private IdObjectUpdateHelper idObjectUpdateHelper;
    private Session session;
    private IdObjectWrapperFactory daoIdObjectWrapperFactory;
    private HibernateReadWriteDAO dao;
    private ClassMetadata metadata, deletedMetaData;
    private ApplicationContext appContext;
    private IdObjectSerializer serializer;
    private Query query1, query3;

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
    private Map<IdObject, IdObject> toWrapped = new HashMap<>();

    private static final LocalDateTime now = new LocalDateTime();
    private static final DateTime dtNow = new DateTime();
    private static final LocalDateTime past = now.minusHours(1);

    @BeforeMethod
    public void setUp() throws Exception {
        context = new Mockery();
        serializer = context.mock(IdObjectSerializer.class);
        appContext = context.mock(ApplicationContext.class);
        sessionFactory = context.mock(SessionFactory.class);
        idObjectReflectionHelper = context.mock(IdObjectReflectionHelper.class);
        session = context.mock(Session.class);
        idObjectUpdateHelper = context.mock(IdObjectUpdateHelper.class);
        query1 = context.mock(Query.class, "Q1");
        query3 = context.mock(Query.class, "Q3");
        daoIdObjectWrapperFactory = context.mock(IdObjectWrapperFactory.class);
        classListImpl = context.mock(ClassList.class, "CLI");
        classListLoaded = context.mock(ClassList.class, "CLL");
        classListWrapped = context.mock(ClassList.class, "CLW");
        toWrapped.put(classListImpl, classListWrapped);
        toWrapped.put(classListLoaded, classListWrapped);
        toWrapped.put(classListWrapped, classListWrapped);

        observationImpl = context.mock(Observation.class, "OI");
        observationLoaded = context.mock(Observation.class, "OL");
        observationWrapped = context.mock(Observation.class, "OW");
        toWrapped.put(observationImpl, observationWrapped);
        toWrapped.put(observationLoaded, observationWrapped);
        toWrapped.put(observationWrapped, observationWrapped);

        observationCategoryImpl = context.mock(ObservationCategory.class, "OCI");
        observationCategoryLoaded = context.mock(ObservationCategory.class, "OCL");
        observationCategoryWrapped = context.mock(ObservationCategory.class, "OCW");
        toWrapped.put(observationCategoryImpl, observationCategoryWrapped);
        toWrapped.put(observationCategoryLoaded, observationCategoryWrapped);
        toWrapped.put(observationCategoryWrapped, observationCategoryWrapped);

        photoImpl = context.mock(Photo.class, "PI");
        photoLoaded = context.mock(Photo.class, "PL");
        photoWrapped = context.mock(Photo.class, "PW");
        toWrapped.put(photoImpl, photoWrapped);
        toWrapped.put(photoLoaded, photoWrapped);
        toWrapped.put(photoWrapped, photoWrapped);

        studentImpl = context.mock(Student.class, "SI");
        studentLoaded = context.mock(Student.class, "SL");
        studentWrapped = context.mock(Student.class, "SW");
        toWrapped.put(studentImpl, studentWrapped);
        toWrapped.put(studentLoaded, studentWrapped);
        toWrapped.put(studentWrapped, studentWrapped);

        appUserImpl = context.mock(AppUser.class, "AUI");
        appUserLoaded = context.mock(AppUser.class, "AUL");
        appUserWrapped = context.mock(AppUser.class, "AUW");
        toWrapped.put(appUserImpl, appUserWrapped);
        toWrapped.put(appUserLoaded, appUserWrapped);
        toWrapped.put(appUserWrapped, appUserWrapped);

        deletedImpl = context.mock(DeletedObject.class, "DOI");
        deletedLoaded = context.mock(DeletedObject.class, "DOL");
        deletedWrapped = context.mock(DeletedObject.class, "DOW");
        toWrapped.put(deletedImpl, deletedWrapped);
        toWrapped.put(deletedLoaded, deletedWrapped);
        toWrapped.put(deletedWrapped, deletedWrapped);

        metadata = context.mock(ClassMetadata.class, "MD");
        deletedMetaData = context.mock(ClassMetadata.class, "DMD");

        impl.clear();
        Collections.addAll(impl, classListImpl, appUserImpl, studentImpl, photoImpl, observationImpl, observationCategoryImpl);
        loaded.clear();
        Collections.addAll(loaded, classListLoaded, appUserLoaded, studentLoaded, photoLoaded, observationLoaded, observationCategoryLoaded);
        wrapped.clear();
        Collections.addAll(wrapped, classListWrapped, appUserWrapped, studentWrapped, photoWrapped, observationWrapped, observationCategoryWrapped);
        context.checking(new Expectations() {{
            allowing(appContext).getBean(IdObjectSerializer.class);
            will(returnValue(serializer));
            allowing(idObjectUpdateHelper).validateUpdates(with(any(AppUser.class)), with(any(IdObject.class)), with(any(IdObject.class)));
            allowing(idObjectReflectionHelper).getIdObjectInterfaceForClass(deletedImpl.getClass());
            will(returnValue(DeletedObject.class));
            allowing(idObjectReflectionHelper).getIdObjectInterfaceForClass(deletedLoaded.getClass());
            will(returnValue(DeletedObject.class));
            allowing(idObjectReflectionHelper).getIdObjectInterfaceForClass(deletedWrapped.getClass());
            will(returnValue(DeletedObject.class));
            allowing(idObjectReflectionHelper).getIdObjectInterfaceForClass(observationLoaded.getClass());
            will(returnValue(Observation.class));
            allowing(idObjectReflectionHelper).getIdObjectInterfaceForClass(observationWrapped.getClass());
            will(returnValue(Observation.class));
            allowing(idObjectReflectionHelper).getIdObjectInterfaceForClass(classListLoaded.getClass());
            will(returnValue(ClassList.class));
            allowing(idObjectReflectionHelper).getIdObjectInterfaceForClass(classListWrapped.getClass());
            will(returnValue(ClassList.class));
            allowing(idObjectReflectionHelper).getIdObjectInterfaceForClass(studentWrapped.getClass());
            will(returnValue(Student.class));
            allowing(idObjectReflectionHelper).getIdObjectInterfaceForClass(studentLoaded.getClass());
            will(returnValue(Student.class));
            allowing(idObjectReflectionHelper).getIdObjectInterfaceForClass(observationCategoryLoaded.getClass());
            will(returnValue(ObservationCategory.class));
            allowing(idObjectReflectionHelper).getIdObjectInterfaceForClass(observationCategoryWrapped.getClass());
            will(returnValue(ObservationCategory.class));
            allowing(idObjectReflectionHelper).getIdObjectInterfaceForClass(photoWrapped.getClass());
            will(returnValue(Photo.class));
            allowing(idObjectReflectionHelper).getIdObjectInterfaceForClass(photoLoaded.getClass());
            will(returnValue(Photo.class));
            allowing(idObjectReflectionHelper).getIdObjectInterfaceForClass(appUserLoaded.getClass());
            will(returnValue(AppUser.class));
            allowing(idObjectReflectionHelper).getIdObjectInterfaceForClass(appUserWrapped.getClass());
            will(returnValue(AppUser.class));
            allowing(idObjectReflectionHelper).getIdObjectInterfaceForClass(AppUser.class);
            will(returnValue(AppUser.class));
            allowing(idObjectReflectionHelper).getIdObjectInterfaceForClass(Student.class);
            will(returnValue(Student.class));
            allowing(idObjectReflectionHelper).getIdObjectInterfaceForClass(ClassList.class);
            will(returnValue(ClassList.class));
            allowing(idObjectReflectionHelper).getIdObjectInterfaceForClass(ObservationCategory.class);
            will(returnValue(ObservationCategory.class));
            allowing(idObjectReflectionHelper).getIdObjectInterfaceForClass(TwoPhaseActivity.class);
            will(returnValue(TwoPhaseActivity.class));
            allowing(idObjectReflectionHelper).getIdObjectInterfaceForClass(DeletedObject.class);
            will(returnValue(DeletedObject.class));
            allowing(idObjectReflectionHelper).getIdObjectInterfaceForClass(AppUserOwnedObject.class);
            will(returnValue(AppUserOwnedObject.class));
            allowing(sessionFactory).getCurrentSession();
            will(returnValue(session));
            allowing(daoIdObjectWrapperFactory).wrap(deletedImpl);
            will(returnValue(deletedWrapped));
            allowing(daoIdObjectWrapperFactory).wrap(deletedWrapped);
            will(returnValue(deletedWrapped));
            allowing(daoIdObjectWrapperFactory).wrap(classListImpl);
            will(returnValue(classListWrapped));
            allowing(daoIdObjectWrapperFactory).wrap(classListLoaded);
            will(returnValue(classListLoaded));
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

            allowing(classListWrapped).getAppUser();
            will(returnValue(appUserWrapped));
            allowing(studentWrapped).getAppUser();
            will(returnValue(appUserWrapped));
            allowing(photoWrapped).getAppUser();
            will(returnValue(appUserWrapped));
            allowing(studentWrapped).getAppUser();
            will(returnValue(appUserWrapped));
            allowing(observationCategoryWrapped).getAppUser();
            will(returnValue(appUserWrapped));
            allowing(observationWrapped).getAppUser();
            will(returnValue(appUserWrapped));
            allowing(classListWrapped).getModificationTimestamp();
            will(returnValue(dtNow));
            allowing(photoWrapped).getModificationTimestamp();
            will(returnValue(dtNow));
            allowing(studentWrapped).getModificationTimestamp();
            will(returnValue(dtNow));
            allowing(studentWrapped).getModificationTimestamp();
            will(returnValue(dtNow));
            allowing(observationCategoryWrapped).getModificationTimestamp();
            will(returnValue(dtNow));
            allowing(observationWrapped).getModificationTimestamp();
            will(returnValue(dtNow));
        }});

        dao = new HibernateReadWriteDAO(null, null, sessionFactory, daoIdObjectWrapperFactory, idObjectReflectionHelper, idObjectUpdateHelper);
        dao.setApplicationContext(appContext);
    }

    @Test
    public void testCreateSingleWithImpls() {

        for (IdObject i : impl) {
            setCreateExpectations(i, toWrapped.get(i));
            IdObject r = dao.create(i);
            assertNotSame(i, r);
            assertTrue(wrapped.contains(r));
        }
    }

    private void setCreateExpectations(final IdObject initial, final IdObject wrapped) {
        if (wrapped == null) {
            throw new RuntimeException("wrapped is null");
        }
        final String content = "X";
        final String id = "ID";
        final String id2 = "ID2";
        context.checking(new Expectations() {{
            if (initial instanceof AppUser) {
                exactly(1).of(wrapped).getId();
                will(returnValue(id));
                exactly(1).of(session).get(TN, id);
                will(returnValue(wrapped));
            } else {
                exactly(2).of(wrapped).getId();
                will(returnValue(id));
                exactly(2).of(session).get(TN, id);
                will(returnValue(wrapped));
            }

            if (initial instanceof Observation) {
                one(wrapped).getId();
                will(returnValue(id));
                one(session).get(TN, id);
                will(returnValue(wrapped));
                one(idObjectReflectionHelper).getIdObjectInterfaceForClass(Observation.class);
                will(returnValue(Observation.class));
                one(session).update(studentWrapped);
                exactly(2).of(studentWrapped).getId();
                will(returnValue(id2));
                exactly(2).of(session).get(TN, id2);
                will(returnValue(studentWrapped));
                one(serializer).writeEntity(studentWrapped);
                will(returnValue(content));
            }
            one(session).save(wrapped);
            if (!(wrapped instanceof AppUser) && !(wrapped instanceof AppUserSettings) && !(wrapped instanceof TwoPhaseActivity)) {
                one(serializer).writeEntity(wrapped);
                will(returnValue(content));
            }
            allowing(session).save(with(any(HibernateHistory.class)));
            allowing(session).flush();
            allowing(session).clear();
        }});
    }

    @Test
    public void testCreateSingleWithWrapped() {

        for (IdObject i : wrapped) {
            setCreateExpectations(i, toWrapped.get(i));
            IdObject r = dao.create(i);
            assertSame(i, r);
            assertTrue(wrapped.contains(r));
        }
    }

    @Test
    public void testUpdateSingleWithImpls() {
        for (IdObject i : impl) {
            setUpdateExpecations(i, toWrapped.get(i));
            IdObject r = dao.update(appUserImpl, i);
            assertNotSame(i, r);
            assertTrue(wrapped.contains(r));
        }
    }

    private void setUpdateExpecations(final IdObject initial, final IdObject wrapped) {
        final String id = "id";
        final String content = "Content";
        context.checking(new Expectations() {{
            allowing(session).clear();
            one(initial).getId();
            will(returnValue(id));
            exactly(2).of(wrapped).getId();
            will(returnValue(id));
            exactly(3).of(session).get(TN, id);
            will(returnValue(wrapped));
            one(session).update(wrapped);
            if (initial instanceof Observation) {
                one(session).createQuery("select max(observationTimestamp) from Observation where observationSubject = :observationSubject");
                will(returnValue(query3));
                one(query3).setParameter("observationSubject", studentWrapped);
                will(returnValue(query3));
                one(query3).uniqueResult();
                will(returnValue(now));
                one(session).update(studentWrapped);
                exactly(2).of(studentWrapped).getId();
                will(returnValue("SID"));
                exactly(2).of(session).get("TN", "SID");
                will(returnValue(studentWrapped));
            }
            allowing(session).flush();
            one(serializer).writeEntity(classListWrapped);
            will(returnValue(content));
            allowing(serializer).writeEntity(studentWrapped);
            will(returnValue(content));
            one(serializer).writeEntity(photoWrapped);
            will(returnValue(content));
            one(serializer).writeEntity(observationWrapped);
            will(returnValue(content));
            one(serializer).writeEntity(observationCategoryWrapped);
            will(returnValue(content));
            allowing(session).save(with(any(HibernateHistory.class)));
        }});

    }

    @Test
    public void testUpdateSingleWithWrapped() {
        for (IdObject i : wrapped) {
            setUpdateExpecations(i, toWrapped.get(i));
            IdObject r = dao.update(appUserImpl, i);
            assertSame(i, r);
            assertTrue(wrapped.contains(r));
        }
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
            one(observationLoaded).getId();
            will(returnValue("OID"));
            one(session).get("TN", "OID");
            will(returnValue(observationLoaded));

            one(observationLoaded).getAppUser();
            will(returnValue(appUserWrapped));
            one(observationLoaded).getModificationTimestamp();
            will(returnValue(new DateTime()));
            one(serializer).writeEntity(observationLoaded);
            will(returnValue("X"));
            one(session).save(with(any(HibernateHistory.class)));
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
            one(studentLoaded).getId();
            will(returnValue("Sid"));
            one(session).get("TN", "Sid");
            will(returnValue(studentLoaded));

            one(studentLoaded).getAppUser();
            will(returnValue(appUserWrapped));
            one(studentLoaded).getModificationTimestamp();
            will(returnValue(new DateTime()));
            one(serializer).writeEntity(studentLoaded);
            will(returnValue("X"));
            one(session).save(with(any(HibernateHistory.class)));
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

            exactly(2).of(studentWrapped).getId();
            will(returnValue("SID"));
            exactly(2).of(session).get("TN", "SID");
            will(returnValue(studentWrapped));
            one(studentWrapped).getAppUser();
            will(returnValue(appUserWrapped));
            one(studentWrapped).getModificationTimestamp();
            will(returnValue(new DateTime()));
            one(serializer).writeEntity(studentWrapped);
            will(returnValue("X"));
            one(session).save(with(any(HibernateHistory.class)));
            allowing(session).flush();
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
            allowing(session).flush();
            allowing(session).clear();
        }});
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testCreatingDeletedObjectExceptions() {
        dao.create(deletedImpl);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testDeletingDeletedObjectExceptions() {
        dao.delete(deletedImpl);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUdpdateingDeletedObjectExceptions() {
        dao.update(appUserImpl, deletedImpl);
    }
    */
}
