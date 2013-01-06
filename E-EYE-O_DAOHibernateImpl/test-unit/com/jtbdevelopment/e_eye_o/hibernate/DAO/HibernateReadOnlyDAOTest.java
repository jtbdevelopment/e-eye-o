package com.jtbdevelopment.e_eye_o.hibernate.DAO;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.ArchivableAppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.impl.ArchivableAppUserOwnedObjectImpl;
import com.jtbdevelopment.e_eye_o.entities.wrapper.DAOIdObjectWrapperFactory;
import com.jtbdevelopment.e_eye_o.hibernate.entities.HDBArchivableAppUserOwnedObject;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
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

    private static interface LocalInterface extends ArchivableAppUserOwnedObject {
    }

    private static class LocalImpl extends ArchivableAppUserOwnedObjectImpl implements LocalInterface {
    }

    private static class HDBWrapper extends HDBArchivableAppUserOwnedObject<LocalInterface> implements LocalInterface {
        public HDBWrapper() {
            super(new LocalImpl());
            setId("1");
        }
    }

    private static final String HQLNAME = "HQLNAME";
    private static final String SOMEID = "AnId";
    private static final HDBWrapper A_WRAPPER = new HDBWrapper();

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
            will(returnValue(HDBWrapper.class));
            allowing(wrapperFactory).getWrapperForEntity(HDBWrapper.class);
            will(returnValue(null));
            allowing(sessionFactory).getClassMetadata(HDBWrapper.class);
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
        assertSame(A_WRAPPER, dao.get(HDBWrapper.class, SOMEID));
    }

    @Test
    public void testGetEntitiesForUser() throws Exception {
        final List<HDBWrapper> result = Arrays.asList(A_WRAPPER);
        context.checking(new Expectations() {{
            allowing(session).createQuery("from " + HQLNAME + " where appUser = :user");
            will(returnValue(query));
            allowing(query).setParameter("user", appUser);
            allowing(query).list();
            will(returnValue(result));
        }});
        for (Class<? extends LocalInterface> c : Arrays.asList(LocalInterface.class, HDBWrapper.class)) {
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
        final List<HDBWrapper> result = Arrays.asList(A_WRAPPER);
        context.checking(new Expectations() {{
            allowing(session).createQuery("from " + HQLNAME + " where appUser = :user and archived = :archived");
            will(returnValue(query));
            allowing(query).setParameter("user", appUser);
            allowing(query).setParameter("archived", archived);
            allowing(query).list();
            will(returnValue(result));
        }});
        for (Class<? extends LocalInterface> c : Arrays.asList(LocalInterface.class, HDBWrapper.class)) {
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
        assertEquals(HQLNAME, dao.getHibernateEntityName(HDBWrapper.class));
    }

    @Test(expectedExceptions = NullPointerException.class)
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
