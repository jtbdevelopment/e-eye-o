package com.jtbdevelopment.e_eye_o.hibernate.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.utilities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.entities.wrapper.DAOIdObjectWrapperFactory;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.joda.time.DateTime;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.AssertJUnit.*;

/**
 * Date: 1/13/13
 * Time: 4:47 PM
 */
public class HibernateIdObjectTest {
    public static interface LocalIdObject extends IdObject {
        LocalIdObject getSubObject();

        LocalIdObject setSubObject(final LocalIdObject subObject);

        List<LocalIdObject> getSubObjects();

        LocalIdObject setSubObjects(final List<LocalIdObject> subObjects);
    }

    public static class LocalIdObjectWrapper extends HibernateIdObject<LocalIdObject> implements LocalIdObject {
        private LocalIdObject subObject;
        private List<LocalIdObject> subObjects = new ArrayList<>();

        public LocalIdObjectWrapper() {
        }

        public LocalIdObjectWrapper(final LocalIdObject wrapped) {
            super(wrapped);
        }

        @Override
        public LocalIdObject getSubObject() {
            return subObject;
        }

        @Override
        public LocalIdObject setSubObject(final LocalIdObject subObject) {
            this.subObject = wrap(subObject);
            return this;
        }

        @Override
        public List<LocalIdObject> getSubObjects() {
            return subObjects;
        }

        @Override
        public LocalIdObject setSubObjects(final List<LocalIdObject> subObjects) {
            this.subObjects = wrap(subObjects);
            return this;
        }
    }

    private Mockery context;
    private IdObjectFactory implFactory;
    private DAOIdObjectWrapperFactory daoFactory;
    private LocalIdObject idObjectImpl;

    @BeforeMethod
    public void setUp() {
        context = new Mockery();
        idObjectImpl = context.mock(LocalIdObject.class, "default");
        implFactory = context.mock(IdObjectFactory.class);
        daoFactory = context.mock(DAOIdObjectWrapperFactory.class);
        HibernateIdObject.setImplFactory(implFactory);
        HibernateIdObject.setDaoFactory(daoFactory);

        context.checking(new Expectations() {{
            allowing(implFactory).newIdObject(LocalIdObject.class);
            will(returnValue(idObjectImpl));
            allowing(daoFactory).getEntityForWrapper(LocalIdObjectWrapper.class);
            will(returnValue(LocalIdObject.class));
            allowing(daoFactory).getWrapperForEntity(LocalIdObject.class);
            will(returnValue(LocalIdObjectWrapper.class));
        }});
    }

    @Test
    public void testDefaultConstructorWhenFactoriesAreNull() {
        HibernateIdObject.setDaoFactory(null);
        HibernateIdObject.setImplFactory(null);

        assertNull(new LocalIdObjectWrapper().getWrapped());

        HibernateIdObject.setDaoFactory(null);
        HibernateIdObject.setImplFactory(implFactory);

        assertNull(new LocalIdObjectWrapper().getWrapped());

        HibernateIdObject.setDaoFactory(daoFactory);
        HibernateIdObject.setImplFactory(null);

        assertNull(new LocalIdObjectWrapper().getWrapped());
    }

    @Test
    public void testDefaultConstructorWhenFactoriesAreSet() {
        assertSame(idObjectImpl, new LocalIdObjectWrapper().getWrapped());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testWrappedConstructorWithNull() {
        new LocalIdObjectWrapper(null);
    }

    @Test
    public void testWrappedConstructorWithIdObject() {
        assertSame(idObjectImpl, new LocalIdObjectWrapper(idObjectImpl).getWrapped());
    }

    @Test
    public void testWrappedConstructorWithIdObjectWrapper() {
        final LocalIdObject local = context.mock(LocalIdObject.class, "local");
        assertSame(local, new LocalIdObjectWrapper(new LocalIdObjectWrapper(local)).getWrapped());
    }

    @Test
    public void testEqualsWithNonIdObject() {
        assertFalse(new LocalIdObjectWrapper().equals(new String()));
    }

    @Test
    public void testEqualsWithIdObject() {
        assertFalse(new LocalIdObjectWrapper(idObjectImpl).equals(new LocalIdObjectWrapper(idObjectImpl)));
    }

    @Test
    public void testHashCode() {
        assertEquals(new LocalIdObjectWrapper(idObjectImpl).hashCode(), new LocalIdObjectWrapper(idObjectImpl).hashCode());
    }

    @Test
    public void testGetModificationTimestamp() {
        final DateTime time = new DateTime();
        context.checking(new Expectations() {{
            one(idObjectImpl).getModificationTimestamp();
            will(returnValue(time));
        }});
        assertSame(time, new LocalIdObjectWrapper(idObjectImpl).getModificationTimestamp());
    }

    @Test
    public void tesSetModificationTimestamp() {
        final DateTime time = new DateTime();
        context.checking(new Expectations() {{
            one(idObjectImpl).setModificationTimestamp(time);
            will(returnValue(idObjectImpl));
        }});

        final LocalIdObjectWrapper localIdObjectWrapper = new LocalIdObjectWrapper(idObjectImpl);
        assertSame(localIdObjectWrapper, localIdObjectWrapper.setModificationTimestamp(time));
    }

    @Test
    public void testGetId() {
        final String id = "123";
        context.checking(new Expectations() {{
            one(idObjectImpl).getId();
            will(returnValue(id));
        }});
        assertEquals(id, new LocalIdObjectWrapper(idObjectImpl).getId());
    }

    @Test
    public void testGetIdWhenWrappedIsNull() {
        HibernateIdObject.setImplFactory(null);
        assertNull(new LocalIdObjectWrapper().getId());
    }

    @Test
    public void testSetId() {
        final String id = "123";
        context.checking(new Expectations() {{
            one(idObjectImpl).setId(id);
            will(returnValue(idObjectImpl));
        }});
        new LocalIdObjectWrapper(idObjectImpl).setId(id);
    }

    @Test
    public void testWrapObject() {
        final LocalIdObjectWrapper returnObject = new LocalIdObjectWrapper();
        context.checking(new Expectations() {{
            one(daoFactory).wrap(idObjectImpl);
            will(returnValue(returnObject));
        }});

        assertSame(returnObject, new LocalIdObjectWrapper().setSubObject(idObjectImpl).getSubObject());
    }

    @Test
    public void testWrapCollection() {
        final List<LocalIdObject> inputList = Arrays.asList(idObjectImpl);

        final LocalIdObjectWrapper returnedItem = new LocalIdObjectWrapper();
        final List<LocalIdObjectWrapper> returnedList = Arrays.asList(returnedItem);
        context.checking(new Expectations() {{
            one(daoFactory).wrap(inputList);
            will(returnValue(returnedList));
        }});

        final LocalIdObject localIdObject = new LocalIdObjectWrapper().setSubObjects(inputList);
        assertSame(returnedList, localIdObject.getSubObjects());
        assertEquals(1, localIdObject.getSubObjects().size());
        assertSame(returnedItem, localIdObject.getSubObjects().get(0));
    }
}
