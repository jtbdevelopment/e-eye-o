package com.jtbdevelopment.e_eye_o.hibernate.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.reflection.IdObjectInterfaceResolver;
import com.jtbdevelopment.e_eye_o.hibernate.entities.wrapper.HibernateIdObjectWrapperFactory;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

/**
 * Date: 1/29/13
 * Time: 9:13 PM
 */
public class HibernateAbstractIdObjectTest {
    protected final DateTime DATETIME_VALUE = new DateTime();
    protected final LocalDateTime LOCALDATETIME_VALUE = new LocalDateTime();
    protected final LocalDate LOCALDATE_VALUE = new LocalDate();
    protected final String STRING_VALUE = "S";
    protected Mockery context;
    protected IdObjectFactory implFactory;
    protected IdObjectInterfaceResolver interfaceResolver;
    protected HibernateIdObjectWrapperFactory daoFactory;

    protected void setUp() {
        context = new Mockery();
        implFactory = context.mock(IdObjectFactory.class);
        interfaceResolver = context.mock(IdObjectInterfaceResolver.class);
        context.checking(new Expectations() {{
            allowing(interfaceResolver).getIdObjectInterfaceForClass(with(new IsInstanceOf(AppUser.class)));
            will(returnValue(AppUser.class));
            allowing(interfaceResolver).getIdObjectInterfaceForClass(with(new IsInstanceOf(Observation.class)));
            will(returnValue(Observation.class));
            allowing(interfaceResolver).getIdObjectInterfaceForClass(with(new IsInstanceOf(ObservationCategory.class)));
            will(returnValue(ObservationCategory.class));
            allowing(interfaceResolver).getIdObjectInterfaceForClass(with(new IsInstanceOf(ClassList.class)));
            will(returnValue(ClassList.class));
            allowing(interfaceResolver).getIdObjectInterfaceForClass(with(new IsInstanceOf(Student.class)));
            will(returnValue(Student.class));
            allowing(interfaceResolver).getIdObjectInterfaceForClass(with(new IsInstanceOf(Photo.class)));
            will(returnValue(Photo.class));
            allowing(interfaceResolver).getIdObjectInterfaceForClass(with(new IsInstanceOf(AppUserOwnedObject.class)));
            will(returnValue(AppUserOwnedObject.class));
        }});
        daoFactory = new HibernateIdObjectWrapperFactory(implFactory, interfaceResolver);
    }
}
