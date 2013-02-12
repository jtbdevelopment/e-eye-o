package com.jtbdevelopment.e_eye_o.hibernate.DAO;

import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.impl.AppUserOwnedObjectImpl;
import com.jtbdevelopment.e_eye_o.entities.wrapper.DAOIdObjectWrapperFactory;
import com.jtbdevelopment.e_eye_o.hibernate.entities.impl.HibernateAppUserOwnedObject;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.testng.AssertJUnit.*;

/**
 * Date: 1/5/13
 * Time: 9:39 PM
 */
public class HibernateReadOnlyDAOTest {
    private Mockery context;
    private DAOIdObjectWrapperFactory wrapperFactory;
    private SessionFactory sessionFactory;
    private ClassMetadata hibernateData;
    private AppUser appUser;
    private Session session;
    private Query query;
    private HibernateReadOnlyDAO dao;

    private static interface LocalInterface extends AppUserOwnedObject {
    }

    private static class LocalImpl extends AppUserOwnedObjectImpl implements LocalInterface {
    }

    private static class HibernateWrapper extends HibernateAppUserOwnedObject<LocalInterface> implements LocalInterface, AppUserOwnedObject {
        public HibernateWrapper() {
            super(new LocalImpl());
            setId("1");
        }
    }

    private static final String HQLNAME = "HQLNAME";
    private static final String SOMEID = "AnId";
    private static final HibernateWrapper A_WRAPPER = new HibernateWrapper();

    @BeforeMethod
    public void initializeMockery() {
        context = new Mockery();
        wrapperFactory = context.mock(DAOIdObjectWrapperFactory.class);
        sessionFactory = context.mock(SessionFactory.class);
        hibernateData = context.mock(ClassMetadata.class);
        session = context.mock(Session.class);
        appUser = context.mock(AppUser.class);
        query = context.mock(Query.class);
        dao = new HibernateReadOnlyDAO(sessionFactory, wrapperFactory);
        context.checking(new Expectations() {{
            allowing(wrapperFactory).getWrapperForEntity(LocalInterface.class);
            will(returnValue(HibernateWrapper.class));
            allowing(wrapperFactory).getWrapperForEntity(HibernateWrapper.class);
            will(returnValue(null));
            allowing(sessionFactory).getClassMetadata(HibernateWrapper.class);
            will(returnValue(hibernateData));
            allowing(hibernateData).getEntityName();
            will(returnValue(HQLNAME));
            allowing(sessionFactory).getCurrentSession();
            will(returnValue(session));
        }});
    }

    @Test
    public void testGet() throws Exception {
        context.checking(new Expectations() {{
            allowing(session).get(HQLNAME, SOMEID);
            will(returnValue(A_WRAPPER));
        }});
        assertSame(A_WRAPPER, dao.get(LocalInterface.class, SOMEID));
        assertSame(A_WRAPPER, dao.get(HibernateWrapper.class, SOMEID));
    }

    @Test
    public void testGetStudentsForClass() throws Exception {
        final List<Student> result = Collections.unmodifiableList(Arrays.asList(context.mock(Student.class)));
        final ClassList cl = context.mock(ClassList.class);
        context.checking(new Expectations() {{
            allowing(session).createQuery("from Student as S where :classList member of S.classLists");
            will(returnValue(query));
            allowing(query).setParameter("classList", cl);
            allowing(query).list();
            will(returnValue(result));
        }});
        assertSame(result, dao.getAllStudentsForClassList(cl));
    }

    @Test
    public void testGetObservationsForCategory() throws Exception {
        final List<Observation> result = Collections.unmodifiableList(Arrays.asList(context.mock(Observation.class)));
        final ObservationCategory oc = context.mock(ObservationCategory.class);
        context.checking(new Expectations() {{
            allowing(session).createQuery("from Observation as O where :category member of O.categories");
            will(returnValue(query));
            allowing(query).setParameter("category", oc);
            allowing(query).list();
            will(returnValue(result));
        }});
        assertSame(result, dao.getAllObservationsForObservationCategory(oc));
    }

    @Test
    public void testGetObservationsForEntity() throws Exception {
        final List<Observation> result = Collections.unmodifiableList(Arrays.asList(context.mock(Observation.class)));
        final AppUserOwnedObject oc = context.mock(AppUserOwnedObject.class);
        context.checking(new Expectations() {{
            allowing(session).createQuery("from Observation where observationSubject = :observationSubject");
            will(returnValue(query));
            allowing(query).setParameter("observationSubject", oc);
            allowing(query).list();
            will(returnValue(result));
        }});
        assertSame(result, dao.getAllObservationsForEntity(oc));
    }

    @Test
    public void testGetObservationsForFollowup() throws Exception {
        final List<Observation> result = Collections.unmodifiableList(Arrays.asList(context.mock(Observation.class)));
        final Observation o = context.mock(Observation.class, "2");
        context.checking(new Expectations() {{
            allowing(session).createQuery("from Observation as O where followUpObservation = :followUpObservation");
            will(returnValue(query));
            allowing(query).setParameter("followUpObservation", o);
            allowing(query).list();
            will(returnValue(result));
        }});
        assertSame(result, dao.getAllObservationsForFollowup(o));
    }

    @Test
    public void testGetPhotosForEntity() throws Exception {
        final List<Photo> result = Collections.unmodifiableList(Arrays.asList(context.mock(Photo.class)));
        final AppUserOwnedObject o = context.mock(AppUserOwnedObject.class);
        context.checking(new Expectations() {{
            allowing(session).createQuery("from Photo where photoFor = :photoFor");
            will(returnValue(query));
            allowing(query).setParameter("photoFor", o);
            allowing(query).list();
            will(returnValue(result));
        }});
        assertSame(result, dao.getAllPhotosForEntity(o));
    }

    @Test
    public void testGetEntitiesForUser() throws Exception {
        final List<HibernateWrapper> result = Arrays.asList(A_WRAPPER);
        context.checking(new Expectations() {{
            allowing(session).createQuery("from " + HQLNAME + " where appUser = :user");
            will(returnValue(query));
            allowing(query).setParameter("user", appUser);
            allowing(query).list();
            will(returnValue(result));
        }});
        for (Class<? extends LocalInterface> c : Arrays.asList(LocalInterface.class, HibernateWrapper.class)) {
            Set entitiesForUser = dao.getEntitiesForUser(c, appUser);
            assertTrue(entitiesForUser.containsAll(result));
            assertTrue(result.containsAll(entitiesForUser));
        }
    }

    @Test
    public void testGetActiveEntitiesForUser() throws Exception {
        testGetArchivableEntitiesForUser(false);
    }

    @Test
    public void testGetArchivedEntitiesForUser() throws Exception {
        testGetArchivableEntitiesForUser(true);
    }

    private void testGetArchivableEntitiesForUser(final boolean archived) {
        final List<HibernateWrapper> result = Arrays.asList(A_WRAPPER);
        context.checking(new Expectations() {{
            allowing(session).createQuery("from " + HQLNAME + " where appUser = :user and archived = :archived");
            will(returnValue(query));
            allowing(query).setParameter("user", appUser);
            allowing(query).setParameter("archived", archived);
            allowing(query).list();
            will(returnValue(result));
        }});
        for (Class<? extends LocalInterface> c : Arrays.asList(LocalInterface.class, HibernateWrapper.class)) {
            Set entitiesForUser = archived ? dao.getArchivedEntitiesForUser(c, appUser) : dao.getActiveEntitiesForUser(c, appUser);
            assertTrue(entitiesForUser.containsAll(result));
            assertTrue(result.containsAll(entitiesForUser));

        }
    }

    @Test
    public void testGetHibernateEntityNameWhereFactoryProvidesMapping() throws Exception {
        assertEquals(HQLNAME, dao.getHibernateEntityName(LocalInterface.class));
    }

    @Test
    public void testGetHibernateEntityNameWhereFactoryDoesNotProvidesMapping() throws Exception {
        assertEquals(HQLNAME, dao.getHibernateEntityName(HibernateWrapper.class));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetHibernateEntityNameWhereFactoryDoesNotProvidesMappingAndNeitherDoesHibernate() throws Exception {
        context.checking(new Expectations() {{
            one(wrapperFactory).getWrapperForEntity(LocalImpl.class);
            will(returnValue(null));
            one(sessionFactory).getClassMetadata(LocalImpl.class);
            will(returnValue(null));
        }});
        dao.getHibernateEntityName(LocalImpl.class);
    }
}
