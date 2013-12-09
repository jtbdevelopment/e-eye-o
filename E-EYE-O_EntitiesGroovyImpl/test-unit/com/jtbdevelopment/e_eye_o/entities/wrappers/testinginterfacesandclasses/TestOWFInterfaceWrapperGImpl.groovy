package com.jtbdevelopment.e_eye_o.entities.wrappers.testinginterfacesandclasses

/**
 * Date: 12/8/13
 * Time: 3:59 PM
 */
public class TestOWFInterfaceWrapperGImpl extends TestOWFIdObjectWrapperGImpl<TestOWFInterface> implements TestOWFInterface {
    public TestOWFInterfaceWrapperGImpl(final TestOWFInterface entityToWrap) {
        super(entityToWrap);
    }
}

