package com.jtbdevelopment.e_eye_o.entities.wrappers.testinginterfacesandclasses

/**
 * Date: 12/8/13
 * Time: 3:59 PM
 */
//  broken with no constructor that accepts entity
public class TestOWFInterfaceWrapperGImplConstructorExceptions extends TestOWFInterfaceWrapperGImpl implements TestOWFInterface {
    public TestOWFInterfaceWrapperGImplConstructorExceptions(final TestOWFInterface entityToWrap) {
        super(entityToWrap);
        throw new IllegalAccessException("Test")
    }
}
