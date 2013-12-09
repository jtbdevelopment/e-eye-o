package com.jtbdevelopment.e_eye_o.entities.impl.reflection
//class IdObjectReflectionHelperGImplTest {

//private static final Set<String> FIELDS = (["objectValue", "booleanValue", "intValue", "stringValues", "appUserOwnedObject", "modificationTimestamp", "id"] as Set)

//    IdObjectReflectionHelper r = new IdObjectReflectionHelperGImpl();
/*
    @Test
    public void testIdInterface() throws Exception {
        assert TestIORInterface1.class == resolver.getIdObjectInterfaceForClass(TestIORInterface1.class);
    }

    @Test
    public void testIdObjectClass() throws Exception {
        assert TestIORInterface1.class == resolver.getIdObjectInterfaceForClass(TestIOR1GImpl.class);
    }

    @Test
    public void testIdObjectDerived() throws Exception {
        assert TestIORInterface2.class == resolver.getIdObjectInterfaceForClass(TestIOR2GImpl.class);
    }
    @Test
    public void testIdObjectDerivedFromOwned() throws Exception {
        assert TestIORInterface3.class == resolver.getIdObjectInterfaceForClass(TestIOR3GImpl.class);
    }

    @Test
    public void testGetMethodsOnInterface() {
        Map<String, Method> lookup = resolver.getAllGetMethods(TestIORInterface1.class)
        assert FIELDS == lookup.keySet()
        assert expectedGetters() == (lookup.values() as Set)
    }

    @Test
    public void testGetMethodsOnClass() {
        Map<String, Method> lookup = resolver.getAllGetMethods(TestIOR2GImpl.class)
        assert FIELDS == lookup.keySet()
        assert expectedGetters() == (lookup.values() as Set)
    }
    @Test
    public void testSetMethodsOnInterface() {
        Map<String, Method> lookup = resolver.getAllSetMethods(TestIORInterface1.class)
        assert FIELDS == lookup.keySet()
        assert expectedSetters() == (lookup.values() as Set)
    }

    @Test
    public void testSetMethodsOnClass() {
        Map<String, Method> lookup = resolver.getAllSetMethods(TestIOR2GImpl.class)
        assert FIELDS == lookup.keySet()
        assert expectedSetters() == (lookup.values() as Set)
    }

    @Test
    public void testFieldPreferencesOnInterface() {
        def methods = expectedGetters()
        methods = methods.collect({ it.getAnnotation(IdObjectFieldSettings.class) }) as Set;
        Map<String, IdObjectFieldSettings> lookup = resolver.getAllFieldPreferences(TestIORInterface2.class)
        assert FIELDS == lookup.keySet()
        assert methods == (lookup.values() as Set)
    }

    @Test
    public void testFieldPreferencesOnClass() {
        def methods = expectedGetters()
        methods = methods.collect({ it.getAnnotation(IdObjectFieldSettings.class) }) as Set;
        Map<String, IdObjectFieldSettings> lookup = resolver.getAllFieldPreferences(TestIOR2GImpl.class)
        assert FIELDS == lookup.keySet()
        assert methods == (lookup.values() as Set)
    }

    private static Set<Method> expectedGetters() {
        def methods = TestIORInterface1.methods as Set
        methods.remove(TestIORInterface1.getMethod("getFirstStringValue"))
        methods.remove(TestIORInterface1.getMethod("getSummaryDescription"))
        methods = methods.findAll({ it.name.startsWith("get") || it.name.startsWith("is") }) as Set
        methods
    }

    private static Set<Method> expectedSetters() {
        def methods = TestIORInterface1.methods as Set
        methods = methods.findAll({ it.name.startsWith("set") }) as Set
        methods
    }
*/
//}
