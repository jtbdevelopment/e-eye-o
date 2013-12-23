package com.jtbdevelopment.e_eye_o.entities.wrappers.testinginterfacesandclasses

/**
 * Date: 12/8/13
 * Time: 6:22 PM
 */
class TestOWFInterfaceSubclassWrapperGImpl extends TestOWFIdObjectWrapperGImpl<TestOWFInterfaceSubclass> implements TestOWFInterfaceSubclass {
    TestOWFInterfaceSubclassWrapperGImpl(final TestOWFInterfaceSubclass entityToWrap) {
        super(entityToWrap)
    }
}
