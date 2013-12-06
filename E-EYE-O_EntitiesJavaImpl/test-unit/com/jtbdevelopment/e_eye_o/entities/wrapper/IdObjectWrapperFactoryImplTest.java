package com.jtbdevelopment.e_eye_o.entities.wrapper;

import com.google.common.collect.LinkedHashMultiset;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.impl.IdObjectImpl;
import com.jtbdevelopment.e_eye_o.entities.impl.reflection.IdObjectReflectionHelperImpl;
import org.joda.time.DateTime;
import org.testng.annotations.Test;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

import static org.testng.AssertJUnit.*;

/**
 * Date: 1/5/13
 * Time: 1:34 PM
 */
public class IdObjectWrapperFactoryImplTest {
    public static int idCounter = 0;

    public static interface LocalSomeIdObject extends IdObject {
    }

    public static class LocalAppUserImpl implements AppUser {
        //  not necessary to implement anything just have something
        @Override
        public String getFirstName() {
            return null;
        }

        @Override
        public void setFirstName(final String firstName) {
        }

        @Override
        public String getLastName() {
            return null;
        }

        @Override
        public void setLastName(final String lastName) {
        }

        @Override
        public String getEmailAddress() {
            return null;
        }

        @Override
        public void setEmailAddress(final String emailAddress) {
        }

        @Override
        public String getPassword() {
            return null;
        }

        @Override
        public void setPassword(final String password) {

        }

        @Override
        public DateTime getLastLogout() {
            return null;
        }

        @Override
        public void setLastLogout(final DateTime lastLogout) {
        }

        @Override
        public boolean isActivated() {
            return false;
        }

        @Override
        public void setActivated(final boolean activated) {

        }

        @Override
        public boolean isActive() {
            return false;
        }

        @Override
        public void setActive(final boolean active) {

        }

        @Override
        public boolean isAdmin() {
            return false;
        }

        @Override
        public void setAdmin(final boolean admin) {

        }

        @Override
        public String getId() {
            return null;
        }

        @Override
        public void setId(final String id) {
        }

        @Override
        public DateTime getModificationTimestamp() {
            return null;
        }

        @Override
        public void setModificationTimestamp(final DateTime modificationTimestamp) {
        }

        @Override
        public String getSummaryDescription() {
            return null;
        }
    }

    //  Handy to extend IdObjectmpl to get equals/hash implementations
    public static class LocalSomeIdObjectImpl extends IdObjectImpl implements LocalSomeIdObject {

        public LocalSomeIdObjectImpl() {
            setId("" + idCounter++);
        }
    }

    public static class LocalIdObjectWrapper<T extends IdObject> implements IdObjectWrapper<T>, IdObject {
        protected T wrapped;

        public LocalIdObjectWrapper(final T entityToWrap) {
            wrapped = entityToWrap;
        }

        @Override
        public T getWrapped() {
            return wrapped;
        }

        @Override
        public String getId() {
            return wrapped.getId();
        }

        @Override
        public void setId(final String id) {
            wrapped.setId(id);
        }

        @Override
        public DateTime getModificationTimestamp() {
            return wrapped.getModificationTimestamp();
        }

        @Override
        public void setModificationTimestamp(final DateTime modificationTimestamp) {
            wrapped.setModificationTimestamp(modificationTimestamp);
        }

        @Override
        public String getSummaryDescription() {
            return wrapped.getSummaryDescription();
        }


    }

    public static class LocalSomeIdObjectWrapper extends LocalIdObjectWrapper<LocalSomeIdObject> implements LocalSomeIdObject {
        public LocalSomeIdObjectWrapper(final LocalSomeIdObject entityToWrap) {
            super(entityToWrap);
        }
    }

    //  broken with no constructor that accepts wrapped
    public static class LocalBrokenNoCCSomeIdObjectWrapper extends LocalSomeIdObjectWrapper implements LocalSomeIdObject {
        public LocalBrokenNoCCSomeIdObjectWrapper() {
            super(new LocalSomeIdObjectImpl());
        }
    }

    //  broken with no public constructor that accepts wrapped
    public static class LocalBrokenNoPublicCSomeIdObjectWrapper extends LocalSomeIdObjectWrapper implements LocalSomeIdObject {
        public LocalBrokenNoPublicCSomeIdObjectWrapper(final LocalSomeIdObject wrapped) throws IllegalAccessException {
            super(wrapped);
            throw new IllegalAccessException();
        }
    }

    public static class LocalBrokenImplements extends LocalSomeIdObjectWrapper implements IdObject {
        public LocalBrokenImplements() {
            super(null);
        }
    }

    //  Oddly defined to allow some bad injections
    public static class SomeOtherWrapperClass implements IdObjectWrapper<IdObject>, LocalSomeIdObject {
        protected IdObject wrapped;

        public SomeOtherWrapperClass(final IdObject entityToWrap) {
            wrapped = entityToWrap;
        }

        @Override
        public IdObject getWrapped() {
            return wrapped;
        }

        @Override
        public String getId() {
            return wrapped.getId();
        }

        @Override
        public void setId(final String id) {
            wrapped.setId(id);
        }

        @Override
        public DateTime getModificationTimestamp() {
            return wrapped.getModificationTimestamp();
        }

        @Override
        public void setModificationTimestamp(final DateTime modificationTimestamp) {
            wrapped.setModificationTimestamp(modificationTimestamp);
        }

        @Override
        public String getSummaryDescription() {
            return wrapped.getSummaryDescription();
        }
    }

    private static class LocalIdObjectWrapperFactory extends IdObjectWrapperFactoryImpl {
        public LocalIdObjectWrapperFactory() {
            super(new IdObjectReflectionHelperImpl());
            addBaseClass(WrapperKind.DAO, LocalSomeIdObjectWrapper.class);
            addMapping(WrapperKind.DAO, LocalSomeIdObject.class, LocalSomeIdObjectWrapper.class);
        }
    }

    private static final LocalIdObjectWrapperFactory wrapperFactory = new LocalIdObjectWrapperFactory();
    private final LocalSomeIdObjectImpl impl = new LocalSomeIdObjectImpl();
    private final LocalSomeIdObject implAsInterface = impl;
    private final LocalSomeIdObjectWrapper wrapper = new LocalSomeIdObjectWrapper(impl);
    private final LocalSomeIdObject wrapperAsInterface = wrapper;
    private final SomeOtherWrapperClass otherWrapper = new SomeOtherWrapperClass(impl);
    private final LocalSomeIdObject otherWrapperAsInterface = otherWrapper;

    @Test
    public void testNeedsWrapping() throws Exception {
        assertTrue(wrapperFactory.needsWrapping(IdObjectWrapperFactory.WrapperKind.DAO, impl));
        assertTrue(wrapperFactory.needsWrapping(IdObjectWrapperFactory.WrapperKind.DAO, implAsInterface));
        assertFalse(wrapperFactory.needsWrapping(IdObjectWrapperFactory.WrapperKind.DAO, wrapper));
        assertFalse(wrapperFactory.needsWrapping(IdObjectWrapperFactory.WrapperKind.DAO, wrapperAsInterface));
        assertTrue(wrapperFactory.needsWrapping(IdObjectWrapperFactory.WrapperKind.DAO, otherWrapper));
        assertTrue(wrapperFactory.needsWrapping(IdObjectWrapperFactory.WrapperKind.DAO, otherWrapperAsInterface));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddMappingBlowsUpWhenEntityIsNotInterface() throws Exception {
        LocalIdObjectWrapperFactory testFactory = new LocalIdObjectWrapperFactory();
        testFactory.addMapping(IdObjectWrapperFactory.WrapperKind.DAO, LocalSomeIdObjectWrapper.class, LocalSomeIdObjectWrapper.class);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddMappingBlowsUpWhenMappingNotOfWrapper() throws Exception {
        LocalIdObjectWrapperFactory testFactory = new LocalIdObjectWrapperFactory();
        testFactory.addMapping(IdObjectWrapperFactory.WrapperKind.DAO, LocalSomeIdObject.class, LocalSomeIdObjectImpl.class);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddMappingBlowsUpWhenWrapperButNotFromBaseClass() throws Exception {
        LocalIdObjectWrapperFactory testFactory = new LocalIdObjectWrapperFactory();
        testFactory.addMapping(IdObjectWrapperFactory.WrapperKind.DAO, LocalSomeIdObject.class, SomeOtherWrapperClass.class);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddMappingBlowsUpWhenClassesDontMatch() throws Exception {
        LocalIdObjectWrapperFactory testFactory = new LocalIdObjectWrapperFactory();
        testFactory.addMapping(IdObjectWrapperFactory.WrapperKind.DAO, LocalSomeIdObject.class, LocalBrokenImplements.class);
    }

    @Test
    public void testWrapOnImpls() throws Exception {
        LocalSomeIdObject returned;
        returned = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, impl);
        assertFalse(returned == impl);
        assertTrue(returned instanceof LocalSomeIdObjectWrapper);
        returned = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, implAsInterface);
        assertFalse(returned == impl);
        assertTrue(returned instanceof LocalSomeIdObjectWrapper);
    }

    @Test
    public void testWrapOnGoodAlternateWrappers() throws Exception {
        LocalSomeIdObject returned;
        returned = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, otherWrapper);
        assertFalse(returned == otherWrapper);
        assertTrue(returned instanceof LocalSomeIdObjectWrapper);
        returned = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, otherWrapperAsInterface);
        assertFalse(returned == otherWrapper);
        assertTrue(returned instanceof LocalSomeIdObjectWrapper);
    }

    @Test
    public void testWrapOnGoodCorrectlyWrapped() throws Exception {
        LocalSomeIdObject returned;
        returned = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, wrapper);
        assertTrue(returned == wrapper);
        returned = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, wrapperAsInterface);
        assertTrue(returned == wrapper);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testWrapBlowsUpOnOtherWrapperWithNullWrapped() {
        SomeOtherWrapperClass badWrapper = new SomeOtherWrapperClass(null);
        wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, badWrapper);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testWrapBlowsUpOnOtherWrapperWithInconsistentWrapped() {
        //  forcing a bad cast
        SomeOtherWrapperClass badWrapper = new SomeOtherWrapperClass(new LocalAppUserImpl());
        wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, badWrapper);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testWrapBlowsUpWhenUnderlyingInterfaceIsUnknown() {
        wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, new LocalAppUserImpl());
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testWrapBlowsUpIfWrapperHasNoCopyConstructor() {
        LocalIdObjectWrapperFactory factory = new LocalIdObjectWrapperFactory();
        factory.addMapping(IdObjectWrapperFactory.WrapperKind.DAO, LocalSomeIdObject.class, LocalBrokenNoCCSomeIdObjectWrapper.class);
        factory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, new LocalSomeIdObjectImpl());
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testWrapBlowsUpIfWrapperHasNoPublicCopyConstructor() {
        LocalIdObjectWrapperFactory factory = new LocalIdObjectWrapperFactory();
        factory.addMapping(IdObjectWrapperFactory.WrapperKind.DAO, LocalSomeIdObject.class, LocalBrokenNoPublicCSomeIdObjectWrapper.class);
        factory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, new LocalSomeIdObjectImpl());
    }

    @Test
    public void testWrapNullObjectReturnsNull() {
        assertNull(wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, (IdObject) null));
        assertNull(wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, (Collection) null));
    }

    @Test
    public void testCollectionReturnedTypes() {
        List returnList = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, new LinkedList<LocalSomeIdObject>());
        assertTrue(returnList instanceof ArrayList);
        Set returnSet = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, new ConcurrentSkipListSet<LocalSomeIdObject>());
        assertTrue(returnSet instanceof HashSet);
    }

    @Test
    public void testCollectionBlowsUpOnUnsupportedCollection() {
        List<Collection<LocalSomeIdObject>> collections = Arrays.<Collection<LocalSomeIdObject>>asList(
                new ArrayDeque<LocalSomeIdObject>(),
                LinkedHashMultiset.<LocalSomeIdObject>create()
        );
        for (Collection<LocalSomeIdObject> c : collections) {
            boolean exception = false;
            try {
                wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, c);
            } catch (RuntimeException e) {
                exception = true;
                assertEquals("Don't know how to construct collection of " + c.getClass().getSimpleName(), e.getMessage());
            }
            assertTrue("Should have had exception", exception);
        }
    }

    @Test
    public void testWrapCollectionImplsSupportedCollections() throws Exception {
        Set<LocalSomeIdObject> original = new HashSet<>();
        for (int i = 0; i < 5; ++i) {
            original.add(new LocalSomeIdObjectImpl());
        }

        final ArrayList<LocalSomeIdObject> listInput = new ArrayList<>(original);
        List<LocalSomeIdObject> listOutput = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, listInput);
        assertFalse(listOutput == listInput);
        assertEquals(original.size(), listOutput.size());
        List<LocalSomeIdObject> listWrapped = new ArrayList<>();
        for (LocalSomeIdObject returned : listOutput) {
            assertTrue(returned instanceof LocalSomeIdObjectWrapper);
            listWrapped.add(((LocalSomeIdObjectWrapper) returned).getWrapped());
        }
        assertTrue(listWrapped.containsAll(original));
        assertTrue(original.containsAll(listWrapped));

        final HashSet<LocalSomeIdObject> setInput = new HashSet<>(original);
        Set<LocalSomeIdObject> setOutput = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, setInput);
        assertFalse(setOutput == setInput);
        assertEquals(original.size(), setOutput.size());
        List<LocalSomeIdObject> setWrapped = new ArrayList<>();
        for (LocalSomeIdObject returned : setOutput) {
            assertTrue(returned instanceof LocalSomeIdObjectWrapper);
            setWrapped.add(((LocalSomeIdObjectWrapper) returned).getWrapped());
        }
        assertTrue(setWrapped.containsAll(original));
        assertTrue(original.containsAll(setWrapped));
    }

    @Test
    public void testWrapCollectionOtherWrappedSupportedCollections() throws Exception {
        Set<LocalSomeIdObject> originalImpl = new HashSet<>();
        for (int i = 0; i < 5; ++i) {
            originalImpl.add(new LocalSomeIdObjectImpl());
        }
        Set<LocalSomeIdObject> originalOther = new HashSet<>();
        for (LocalSomeIdObject impl : originalImpl) {
            originalOther.add(new SomeOtherWrapperClass(impl));
        }

        final ArrayList<LocalSomeIdObject> listInput = new ArrayList<>(originalOther);
        List<LocalSomeIdObject> listOutput = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, listInput);
        assertFalse(listOutput == listInput);
        assertEquals(originalImpl.size(), listOutput.size());
        List<LocalSomeIdObject> listWrapped = new ArrayList<>();
        for (LocalSomeIdObject returned : listOutput) {
            assertTrue(returned instanceof LocalSomeIdObjectWrapper);
            listWrapped.add(((LocalSomeIdObjectWrapper) returned).getWrapped());
        }
        assertTrue(listWrapped.containsAll(originalImpl));
        assertTrue(originalImpl.containsAll(listWrapped));

        final HashSet<LocalSomeIdObject> setInput = new HashSet<>(originalOther);
        Set<LocalSomeIdObject> setOutput = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, setInput);
        assertFalse(setOutput == setInput);
        assertEquals(originalImpl.size(), setOutput.size());
        List<LocalSomeIdObject> setWrapped = new ArrayList<>();
        for (LocalSomeIdObject returned : setOutput) {
            assertTrue(returned instanceof LocalSomeIdObjectWrapper);
            setWrapped.add(((LocalSomeIdObjectWrapper) returned).getWrapped());
        }
        assertTrue(setWrapped.containsAll(originalImpl));
        assertTrue(originalImpl.containsAll(setWrapped));
    }

    @Test
    public void testWrapCollectionAlreadyWrappedSupportedCollections() throws Exception {
        Set<LocalSomeIdObject> originalImpl = new HashSet<>();
        for (int i = 0; i < 5; ++i) {
            originalImpl.add(new LocalSomeIdObjectImpl());
        }
        Set<LocalSomeIdObject> originalWrapped = new HashSet<>();
        for (LocalSomeIdObject impl : originalImpl) {
            originalWrapped.add(new LocalSomeIdObjectWrapper(impl));
        }

        final ArrayList<LocalSomeIdObject> listInput = new ArrayList<>(originalWrapped);
        List<LocalSomeIdObject> listOutput = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, listInput);
        assertFalse(listOutput == listInput);
        assertEquals(originalImpl.size(), listOutput.size());
        for (LocalSomeIdObject returned : listOutput) {
            assertTrue(returned instanceof LocalSomeIdObjectWrapper);
        }
        assertTrue(listOutput.containsAll(originalWrapped));
        assertTrue(originalWrapped.containsAll(listOutput));

        final HashSet<LocalSomeIdObject> setInput = new HashSet<>(originalWrapped);
        Set<LocalSomeIdObject> setOutput = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, setInput);
        assertFalse(setOutput == setInput);
        assertEquals(originalImpl.size(), setOutput.size());
        for (LocalSomeIdObject returned : setOutput) {
            assertTrue(returned instanceof LocalSomeIdObjectWrapper);
        }
        assertTrue(setOutput.containsAll(originalWrapped));
        assertTrue(originalWrapped.containsAll(setOutput));
    }

    @Test
    public void testGetWrapperForEntity() throws Exception {
        assertEquals(LocalSomeIdObject.class, wrapperFactory.getEntityForWrapper(IdObjectWrapperFactory.WrapperKind.DAO, LocalSomeIdObjectWrapper.class));
    }

    @Test
    public void testGetEntityForWrapper() throws Exception {
        assertEquals(LocalSomeIdObjectWrapper.class, wrapperFactory.getWrapperForEntity(IdObjectWrapperFactory.WrapperKind.DAO, LocalSomeIdObject.class));
    }
}
