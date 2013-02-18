package com.jtbdevelopment.e_eye_o.hibernate.DAO;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.DeletedObject;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.entities.wrapper.DAOIdObjectWrapperFactory;
import org.hibernate.Session;
import org.hibernate.event.spi.EventSource;
import org.hibernate.event.spi.PreDeleteEvent;
import org.hibernate.internal.SessionFactoryImpl;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertFalse;

/**
 * Date: 2/17/13
 * Time: 9:45 PM
 */
public class HibernatePreDeleteEventListenerTest {
    private Mockery context;
    private DAOIdObjectWrapperFactory wrapper;
    private IdObjectFactory factory;
    private SessionFactoryImpl sfi;
    private HibernatePreDeleteEventListener listener;
    private DeletedObject impl, wrapped;
    private AppUser appUser;
    private Session session;
    private EventSource eventSource;

    @BeforeMethod
    public void setUp() {
        context = new Mockery();
        wrapper = context.mock(DAOIdObjectWrapperFactory.class);
        factory = context.mock(IdObjectFactory.class);
        impl = context.mock(DeletedObject.class, "I");
        wrapped = context.mock(DeletedObject.class, "W");
        appUser = context.mock(AppUser.class);
        session = context.mock(Session.class);
        listener = new HibernatePreDeleteEventListener(wrapper, factory, null);
        eventSource = context.mock(EventSource.class);
        context.checking(new Expectations() {{
        }});
    }


    @Test
    public void testRegisterMe() throws Exception {
        //  Not testing - testing via integration only
    }

    @Test
    public void testOnPreDeleteOnDeleteObjectDoesNothing() throws Exception {
        final DeletedObject local = context.mock(DeletedObject.class);

        PreDeleteEvent pde = new PreDeleteEvent(local, null, null, null, eventSource);
        assertFalse(listener.onPreDelete(pde));
    }

    @Test
    public void testOnPreDeleteOnAppUserDoesNothing() throws Exception {
        PreDeleteEvent pde = new PreDeleteEvent(appUser, null, null, null, eventSource);
        assertFalse(listener.onPreDelete(pde));
    }

    @Test
    public void testOnPreDeleteAppUserObject() throws Exception {
        final AppUserOwnedObject local = context.mock(AppUserOwnedObject.class);
        final String localId = "X";

        context.checking(new Expectations() {{
            one(local).getAppUser();
            will(returnValue(appUser));
            one(local).getId();
            will(returnValue(localId));
            one(factory).newAppUserOwnedObject(DeletedObject.class, appUser);
            will(returnValue(impl));
            one(impl).setDeletedId(localId);
            will(returnValue(impl));
            one(wrapper).wrap(impl);
            will(returnValue(wrapped));
            one(eventSource).save(wrapped);
        }});

        PreDeleteEvent pde = new PreDeleteEvent(local, null, null, null, eventSource);
        assertFalse(listener.onPreDelete(pde));
    }
}
