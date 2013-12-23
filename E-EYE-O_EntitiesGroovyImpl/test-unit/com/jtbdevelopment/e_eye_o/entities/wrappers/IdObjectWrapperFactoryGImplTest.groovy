package com.jtbdevelopment.e_eye_o.entities.wrappers

import com.jtbdevelopment.e_eye_o.entities.impl.reflection.IdObjectReflectionHelperGImpl
import com.jtbdevelopment.e_eye_o.entities.wrapper.IdObjectWrapperFactory
import com.jtbdevelopment.e_eye_o.entities.wrappers.testinginterfacesandclasses.TestOWFIdObjectWrapperGImpl
import com.jtbdevelopment.e_eye_o.entities.wrappers.testinginterfacesandclasses.TestOWFInterface
import com.jtbdevelopment.e_eye_o.entities.wrappers.testinginterfacesandclasses.TestOWFInterfaceWrapperGImpl

class IdObjectWrapperFactoryGImplTest extends AbstractIdObjectWrapperFactoryTest {

    @Override
    IdObjectWrapperFactory makeFactory() {
        IdObjectWrapperFactoryImpl impl = new IdObjectWrapperFactoryImpl(new IdObjectReflectionHelperGImpl())
        impl.addBaseClass(IdObjectWrapperFactory.WrapperKind.DAO, TestOWFIdObjectWrapperGImpl.class);
        impl.addMapping(IdObjectWrapperFactory.WrapperKind.DAO, TestOWFInterface.class, TestOWFInterfaceWrapperGImpl.class)
        return impl;
    }
}

