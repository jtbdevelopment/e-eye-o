package com.jtbdevelopment.e_eye_o.entities.wrappers

import com.google.common.collect.LinkedHashMultiset
import com.jtbdevelopment.e_eye_o.entities.IdObject
import com.jtbdevelopment.e_eye_o.entities.impl.reflection.IdObjectReflectionHelperGImpl
import com.jtbdevelopment.e_eye_o.entities.wrapper.IdObjectWrapperFactory
import com.jtbdevelopment.e_eye_o.entities.wrappers.testinginterfacesandclasses.*
import org.testng.annotations.Test

import java.util.concurrent.ConcurrentSkipListSet

class IdObjectWrapperFactoryGImplTest extends GroovyTestCase {
    private static final IdObjectWrapperFactory wrapperFactory = makeFactory();
    private final TestOWFInterfaceGImpl impl = new TestOWFInterfaceGImpl();
    private final TestOWFInterface implAsInterface = impl;
    private final TestOWFInterfaceWrapperGImpl wrapper = new TestOWFInterfaceWrapperGImpl(impl);
    private final TestOWFInterface wrapperAsInterface = wrapper;
    private final TestOWFAlternateWrapper otherWrapper = new TestOWFAlternateWrapper(impl);
    private final TestOWFInterface otherWrapperAsInterface = otherWrapper;

    private static IdObjectWrapperFactory makeFactory() {
        IdObjectWrapperFactoryGImpl impl = new IdObjectWrapperFactoryGImpl(new IdObjectReflectionHelperGImpl())
        impl.addBaseClass(IdObjectWrapperFactory.WrapperKind.DAO, TestOWFIdObjectWrapperGImpl.class);
        impl.addMapping(IdObjectWrapperFactory.WrapperKind.DAO, TestOWFInterface.class, TestOWFInterfaceWrapperGImpl.class)
        return impl;
    }

    @Test
    public void testGetWrapperForEntity() throws Exception {
        assert TestOWFInterface.class == wrapperFactory.getEntityForWrapper(IdObjectWrapperFactory.WrapperKind.DAO, TestOWFInterfaceWrapperGImpl.class)
    }

    @Test
    public void testGetEntityForWrapper() throws Exception {
        assert TestOWFInterfaceWrapperGImpl.class == wrapperFactory.getWrapperForEntity(IdObjectWrapperFactory.WrapperKind.DAO, TestOWFInterface.class)
    }

    @Test
    public void testNeedsWrapping() throws Exception {
        assert wrapperFactory.needsWrapping(IdObjectWrapperFactory.WrapperKind.DAO, impl);
        assert wrapperFactory.needsWrapping(IdObjectWrapperFactory.WrapperKind.DAO, implAsInterface);
        assert !wrapperFactory.needsWrapping(IdObjectWrapperFactory.WrapperKind.DAO, wrapper);
        assert !wrapperFactory.needsWrapping(IdObjectWrapperFactory.WrapperKind.DAO, wrapperAsInterface);
        assert wrapperFactory.needsWrapping(IdObjectWrapperFactory.WrapperKind.DAO, otherWrapper);
        assert wrapperFactory.needsWrapping(IdObjectWrapperFactory.WrapperKind.DAO, otherWrapperAsInterface);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testAddMappingBlowsUpWhenBaseIsNull() throws Exception {
        IdObjectWrapperFactory testFactory = makeFactory()
        testFactory.addBaseClass(IdObjectWrapperFactory.WrapperKind.DAO, null)
        testFactory.addMapping(IdObjectWrapperFactory.WrapperKind.DAO, TestOWFInterface.class, TestOWFInterfaceWrapperGImpl.class)
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddMappingBlowsUpWhenEntityIsNotInterface() throws Exception {
        IdObjectWrapperFactory testFactory = makeFactory()
        testFactory.addMapping(IdObjectWrapperFactory.WrapperKind.DAO, TestOWFInterfaceWrapperGImpl.class, TestOWFInterfaceWrapperGImpl.class);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddMappingBlowsUpWhenMappingIsInterface() throws Exception {
        IdObjectWrapperFactory testFactory = makeFactory()
        testFactory.addMapping(IdObjectWrapperFactory.WrapperKind.DAO, TestOWFInterface.class, TestOWFInterface.class);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddMappingBlowsUpWhenMappingNotOfWrapper() throws Exception {
        IdObjectWrapperFactory testFactory = makeFactory()
        testFactory.addMapping(IdObjectWrapperFactory.WrapperKind.DAO, TestOWFInterface.class, TestOWFInterfaceGImpl.class);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddMappingBlowsUpWhenWrapperButNotFromBaseClass() throws Exception {
        IdObjectWrapperFactory testFactory = makeFactory()
        testFactory.addMapping(IdObjectWrapperFactory.WrapperKind.DAO, TestOWFInterface.class, TestOWFAlternateWrapper.class);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddMappingBlowsUpWhenClassesDoNotMatch() throws Exception {
        IdObjectWrapperFactory testFactory = makeFactory()
        testFactory.addMapping(IdObjectWrapperFactory.WrapperKind.DAO, TestOWFInterface.class, TestOWFInterfaceSubclassWrapperGImpl.class);
    }

    @Test
    public void testWrapOnNull() {
        assert null == wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, (IdObject) null)
    }

    @Test
    public void testWrapOnNullCollection() {
        assert null == wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, (Collection<? extends IdObject>) null)
    }

    @Test
    public void testWrapOnImplClass() throws Exception {
        TestOWFInterface returned;
        returned = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, impl);
        assert !returned.is(impl);
        assert returned instanceof TestOWFInterfaceWrapperGImpl;
        returned = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, implAsInterface);
        assert !returned.is(implAsInterface);
        assert returned instanceof TestOWFInterfaceWrapperGImpl;
    }

    @Test
    public void testWrapOnGoodAlternateWrappers() throws Exception {
        TestOWFInterface returned;
        returned = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, otherWrapper);
        assert !returned.is(otherWrapper);
        assert returned instanceof TestOWFInterfaceWrapperGImpl;
        returned = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, otherWrapperAsInterface);
        assert !returned.is(otherWrapperAsInterface);
        assert returned instanceof TestOWFInterfaceWrapperGImpl;
    }

    @Test
    public void testWrapOnAlreadyCorrectlyWrapped() throws Exception {
        TestOWFInterface returned;
        returned = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, wrapper);
        assert returned.is(wrapper);
        returned = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, wrapperAsInterface);
        assert returned.is(wrapper);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testWrapBlowsUpOnOtherWrapperWithNullWrapped() {
        TestOWFAlternateWrapper badWrapper = new TestOWFAlternateWrapper(null);
        wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, badWrapper);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testWrapBlowsUpOnOtherWrapperWithInconsistentWrapped() {
        //  forcing a bad cast
        TestOWFAlternateWrapper badWrapper = new TestOWFAlternateWrapper(new TestOWFAppUser());
        wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, badWrapper);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testWrapBlowsUpWhenUnderlyingInterfaceIsUnknown() {
        wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, new TestOWFAppUser());
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testWrapBlowsUpIfWrapperHasNoCopyConstructor() {
        IdObjectWrapperFactory testFactory = makeFactory()
        testFactory.addMapping(IdObjectWrapperFactory.WrapperKind.DAO, TestOWFInterface.class, TestOWFInterfaceWrapperGImplBadConstructorArgs.class);
        testFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, new TestOWFInterfaceGImpl());
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testWrapBlowsUpIfWrapperHasNoPublicCopyConstructor() {
        IdObjectWrapperFactory testFactory = makeFactory()
        testFactory.addMapping(IdObjectWrapperFactory.WrapperKind.DAO, TestOWFInterface.class, TestOWFInterfaceWrapperGImplPrivateConstructor.class);
        testFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, new TestOWFInterfaceGImpl());
    }

    @Test
    public void testCollectionList() {
        LinkedList<TestOWFInterface> inList = new LinkedList<TestOWFInterface>()
        List returnList = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, inList);
        assert returnList instanceof List;
        assert !inList.is(returnList)
        assert inList == returnList
    }

    @Test
    public void testCollectionSet() {
        ConcurrentSkipListSet<TestOWFInterface> inSet = new ConcurrentSkipListSet<TestOWFInterface>()
        Set returnSet = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, inSet);
        assert returnSet instanceof Set;
        assert !inSet.is(returnSet)
        assert inSet == returnSet
    }

    @Test
    public void testCollectionBlowsUpOnUnsupportedCollection() {
        List<Collection<TestOWFInterface>> collections = [
                new ArrayDeque<TestOWFInterface>(),
                LinkedHashMultiset.<TestOWFInterface> create()
        ];
        collections.each { c ->
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
    public void testWrapCollectionImplSupportedCollections() throws Exception {
        Set<TestOWFInterface> original = [] as Set;
        for (int i = 0; i < 5; ++i) {
            original.add(new TestOWFInterfaceGImpl());
        }

        final List<TestOWFInterface> listInput = original.toList();
        List<TestOWFInterface> listOutput = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, listInput);
        assert !listOutput.is(listInput);
        List<TestOWFInterface> listWrapped = [];
        listOutput.each { returned ->
            assert returned instanceof TestOWFInterfaceWrapperGImpl
            listWrapped.add(((TestOWFInterfaceWrapperGImpl) returned).wrapped)
        }
        assert listInput == listWrapped

        final Set<TestOWFInterface> setInput = (Set<TestOWFInterface>) original.clone();
        Set<TestOWFInterface> setOutput = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, setInput);
        assert !setOutput.is(setInput);
        Set<TestOWFInterface> setWrapped = [] as Set;
        setOutput.each { returned ->
            assert returned instanceof TestOWFInterfaceWrapperGImpl
            setWrapped.add(((TestOWFInterfaceWrapperGImpl) returned).wrapped)
        }
        assert setInput == setWrapped;
    }

    @Test
    public void testWrapCollectionAlternateWrappedSupportedCollections() throws Exception {
        List<TestOWFInterface> original = [];
        for (int i = 0; i < 5; ++i) {
            original.add(new TestOWFInterfaceGImpl());
        }
        List<TestOWFInterface> originalWrapped = original.collect({ new TestOWFAlternateWrapper(it) });

        final List<TestOWFInterface> listInput = (List<TestOWFInterface>) originalWrapped.clone();
        List<TestOWFInterface> listOutput = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, listInput);
        assert !listOutput.is(listInput);
        List<TestOWFInterface> listWrapped = [];
        listOutput.each { returned ->
            assert returned instanceof TestOWFInterfaceWrapperGImpl
            listWrapped.add(((TestOWFInterfaceWrapperGImpl) returned).wrapped)
        }
        assert original == listWrapped

        final Set<TestOWFInterface> setInput = originalWrapped as Set;
        Set<TestOWFInterface> setOutput = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, setInput);
        assert !setOutput.is(setInput);
        Set<TestOWFInterface> setWrapped = [] as Set;
        setOutput.each { returned ->
            assert returned instanceof TestOWFInterfaceWrapperGImpl
            setWrapped.add(((TestOWFInterfaceWrapperGImpl) returned).wrapped)
        }
        assert (original as Set) == setWrapped;
    }

    @Test
    public void testWrapCollectionAlreadyWrappedSupportedCollections() throws Exception {
        List<TestOWFInterface> original = [];
        for (int i = 0; i < 5; ++i) {
            original.add(new TestOWFInterfaceGImpl());
        }
        List<TestOWFInterface> originalWrapped = original.collect({ new TestOWFInterfaceWrapperGImpl(it) });

        final List<TestOWFInterface> listInput = (List<TestOWFInterface>) originalWrapped.clone();
        List<TestOWFInterface> listOutput = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, listInput);
        assert !listOutput.is(listInput);
        assert originalWrapped == listOutput

        final Set<TestOWFInterface> setInput = originalWrapped as Set;
        Set<TestOWFInterface> setOutput = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, setInput);
        assert !setOutput.is(setInput);
        assert (originalWrapped as Set) == setOutput;
    }
}
