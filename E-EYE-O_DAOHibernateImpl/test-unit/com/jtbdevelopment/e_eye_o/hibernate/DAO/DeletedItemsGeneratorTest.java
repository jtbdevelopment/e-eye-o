package com.jtbdevelopment.e_eye_o.hibernate.DAO;

import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.builders.DeletedObjectBuilder;
import com.jtbdevelopment.e_eye_o.entities.wrapper.IdObjectWrapperFactory;
import com.jtbdevelopment.e_eye_o.serialization.IdObjectSerializer;
import org.hibernate.event.spi.EventSource;
import org.hibernate.event.spi.PreDeleteEvent;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.joda.time.DateTime;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertFalse;

/**
 * Date: 2/17/13
 * Time: 9:45 PM
 */
public class DeletedItemsGeneratorTest {
    private Mockery context;
    private IdObjectWrapperFactory wrapper;
    private IdObjectFactory factory;
    private DeletedItemsGenerator listener;
    private DeletedObject impl, wrapped;
    private DeletedObjectBuilder builder;
    private AppUser appUser;
    private EventSource eventSource;
    private IdObjectSerializer serializer;

    @BeforeMethod
    public void setUp() {
        context = new Mockery();
        wrapper = context.mock(IdObjectWrapperFactory.class);
        factory = context.mock(IdObjectFactory.class);
        impl = context.mock(DeletedObject.class, "I");
        wrapped = context.mock(DeletedObject.class, "W");
        appUser = context.mock(AppUser.class);
        serializer = context.mock(IdObjectSerializer.class);
        listener = new DeletedItemsGenerator(wrapper, factory, null, serializer);
        eventSource = context.mock(EventSource.class);
        builder = context.mock(DeletedObjectBuilder.class);
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
            one(factory).newDeletedObjectBuilder(appUser);
            will(returnValue(builder));
            one(builder).withDeletedId(localId);
            will(returnValue(builder));
            one(builder).build();
            will(returnValue(impl));
            one(wrapper).wrap(IdObjectWrapperFactory.WrapperKind.DAO, impl);
            will(returnValue(wrapped));
            one(eventSource).save(wrapped);
            one(wrapped).getAppUser();
            will(returnValue(appUser));
            one(serializer).write(wrapped);
            will(returnValue("Content"));
            one(wrapped).getModificationTimestamp();
            will(returnValue(new DateTime()));
            one(eventSource).save(with(any(HibernateHistory.class)));
        }});

        PreDeleteEvent pde = new PreDeleteEvent(local, null, null, null, eventSource);
        assertFalse(listener.onPreDelete(pde));
    }

    @Test
    public void testOnPreDeleteTwoPhaseActivity() throws Exception {
        final TwoPhaseActivity local = context.mock(TwoPhaseActivity.class);
        final String localId = "X";

        context.checking(new Expectations() {{
            one(local).getAppUser();
            will(returnValue(appUser));
            one(local).getId();
            will(returnValue(localId));
            one(factory).newDeletedObjectBuilder(appUser);
            will(returnValue(builder));
            one(builder).withDeletedId(localId);
            will(returnValue(builder));
            one(builder).build();
            will(returnValue(impl));
            one(wrapper).wrap(IdObjectWrapperFactory.WrapperKind.DAO, impl);
            will(returnValue(wrapped));
            one(eventSource).save(wrapped);

        }});

        PreDeleteEvent pde = new PreDeleteEvent(local, null, null, null, eventSource);
        assertFalse(listener.onPreDelete(pde));
    }
}
