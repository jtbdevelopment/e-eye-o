package com.jtbdevelopment.e_eye_o.entities.wrappers.testinginterfacesandclasses

/**
 * Date: 12/8/13
 * Time: 3:59 PM
 */
//  broken with no constructor that accepts entity
public class TestOWFInterfaceWrapperGImplPrivateConstructor extends TestOWFInterfaceWrapperGImpl implements TestOWFInterface {
    private TestOWFInterfaceWrapperGImplPrivateConstructor() {
        super(new TestOWFInterfaceGImpl());
    }
}
