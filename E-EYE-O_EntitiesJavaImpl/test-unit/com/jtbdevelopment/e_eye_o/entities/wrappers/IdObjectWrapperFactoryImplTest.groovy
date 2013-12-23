package com.jtbdevelopment.e_eye_o.entities.wrappers

import com.jtbdevelopment.e_eye_o.entities.impl.reflection.IdObjectReflectionHelperImpl
import com.jtbdevelopment.e_eye_o.entities.wrapper.IdObjectWrapperFactory
import com.jtbdevelopment.e_eye_o.entities.wrapper.IdObjectWrapperFactoryImpl
import com.jtbdevelopment.e_eye_o.entities.wrappers.testinginterfacesandclasses.TestOWFIdObjectWrapperGImpl
import com.jtbdevelopment.e_eye_o.entities.wrappers.testinginterfacesandclasses.TestOWFInterface
import com.jtbdevelopment.e_eye_o.entities.wrappers.testinginterfacesandclasses.TestOWFInterfaceWrapperGImpl

class IdObjectWrapperFactoryImplTest extends AbstractIdObjectWrapperFactoryTest {

    @Override
    IdObjectWrapperFactory makeFactory() {
        IdObjectWrapperFactoryImpl impl = new IdObjectWrapperFactoryImpl(new IdObjectReflectionHelperImpl())
        impl.addBaseClass(IdObjectWrapperFactory.WrapperKind.DAO, TestOWFIdObjectWrapperGImpl.class);
        impl.addMapping(IdObjectWrapperFactory.WrapperKind.DAO, TestOWFInterface.class, TestOWFInterfaceWrapperGImpl.class)
        return impl;
    }
}

