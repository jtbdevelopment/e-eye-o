package com.jtbdevelopment.e_eye_o.hibernate.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.entities.wrapper.IdObjectWrapperFactory;
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
    private IdObjectWrapperFactory daoFactory;
    private LocalIdObject idObjectImpl;

    @BeforeMethod
    public void setUp() {
        context = new Mockery();
        idObjectImpl = context.mock(LocalIdObject.class, "default");
        implFactory = context.mock(IdObjectFactory.class);
        daoFactory = context.mock(IdObjectWrapperFactory.class);
        HibernateIdObject.setImplFactory(implFactory);
        HibernateIdObject.setWrapperFactory(daoFactory);

        context.checking(new Expectations() {{
            allowing(implFactory).newIdObject(LocalIdObject.class);
            will(returnValue(idObjectImpl));
            allowing(daoFactory).getEntityForWrapper(IdObjectWrapperFactory.WrapperKind.DAO, LocalIdObjectWrapper.class);
            will(returnValue(LocalIdObject.class));
            allowing(daoFactory).getWrapperForEntity(IdObjectWrapperFactory.WrapperKind.DAO, LocalIdObject.class);
            will(returnValue(LocalIdObjectWrapper.class));
        }});
    }

    @Test
    public void testDefaultConstructorWhenFactoriesAreNull() {
        HibernateIdObject.setWrapperFactory(null);
        HibernateIdObject.setImplFactory(null);

        assertNull(new LocalIdObjectWrapper().getWrapped());

        HibernateIdObject.setWrapperFactory(null);
        HibernateIdObject.setImplFactory(implFactory);

        assertNull(new LocalIdObjectWrapper().getWrapped());

        HibernateIdObject.setWrapperFactory(daoFactory);
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
            oneOf(idObjectImpl).getModificationTimestamp();
            will(returnValue(time));
        }});
        assertSame(time, new LocalIdObjectWrapper(idObjectImpl).getModificationTimestamp());
    }

    @Test
    public void tesSetModificationTimestamp() {
        final DateTime time = new DateTime();
        context.checking(new Expectations() {{
            oneOf(idObjectImpl).setModificationTimestamp(time);
        }});

        final LocalIdObjectWrapper localIdObjectWrapper = new LocalIdObjectWrapper(idObjectImpl);
        localIdObjectWrapper.setModificationTimestamp(time);
    }

    @Test
    public void testGetId() {
        final String id = "123";
        context.checking(new Expectations() {{
            oneOf(idObjectImpl).getId();
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
            oneOf(idObjectImpl).setId(id);
        }});
        new LocalIdObjectWrapper(idObjectImpl).setId(id);
    }

    @Test
    public void testWrapObject() {
        final LocalIdObjectWrapper returnObject = new LocalIdObjectWrapper();
        context.checking(new Expectations() {{
            oneOf(daoFactory).wrap(IdObjectWrapperFactory.WrapperKind.DAO, idObjectImpl);
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
            oneOf(daoFactory).wrap(IdObjectWrapperFactory.WrapperKind.DAO, inputList);
            will(returnValue(returnedList));
        }});

        final LocalIdObject localIdObject = new LocalIdObjectWrapper().setSubObjects(inputList);
        assertSame(returnedList, localIdObject.getSubObjects());
        assertEquals(1, localIdObject.getSubObjects().size());
        assertSame(returnedItem, localIdObject.getSubObjects().get(0));
    }
}
