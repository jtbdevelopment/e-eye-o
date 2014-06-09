package com.jtbdevelopment.e_eye_o.entities.wrapper

import com.jtbdevelopment.e_eye_o.entities.wrapper.testinginterfacesandclasses.TestOWFIdObjectWrapperGImpl
import com.jtbdevelopment.e_eye_o.entities.wrapper.testinginterfacesandclasses.TestOWFInterface
import com.jtbdevelopment.e_eye_o.entities.wrapper.testinginterfacesandclasses.TestOWFInterfaceWrapperGImpl
import com.jtbdevelopment.e_eye_o.reflection.IdObjectReflectionHelperImpl

class IdObjectWrapperFactoryImplTest extends AbstractIdObjectWrapperFactoryTest {

    @Override
    IdObjectWrapperFactory makeFactory() {
        IdObjectWrapperFactoryImpl impl = new IdObjectWrapperFactoryImpl(new IdObjectReflectionHelperImpl())
        impl.addBaseClass(IdObjectWrapperFactory.WrapperKind.DAO, TestOWFIdObjectWrapperGImpl.class);
        impl.addMapping(IdObjectWrapperFactory.WrapperKind.DAO, TestOWFInterface.class, TestOWFInterfaceWrapperGImpl.class)
        return impl;
    }
}

