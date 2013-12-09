package com.jtbdevelopment.e_eye_o.entities.wrappers.testinginterfacesandclasses

/**
 * Date: 12/8/13
 * Time: 3:59 PM
 */
//  broken with no constructor that accepts entity
public class TestOWFInterfaceWrapperGImplBadConstructorArgs extends TestOWFInterfaceWrapperGImpl implements TestOWFInterface {
    public TestOWFInterfaceWrapperGImplBadConstructorArgs() {
        super(new TestOWFInterfaceGImpl());
    }
}
