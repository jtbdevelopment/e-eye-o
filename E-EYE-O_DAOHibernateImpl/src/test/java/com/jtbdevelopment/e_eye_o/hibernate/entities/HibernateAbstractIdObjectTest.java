package com.jtbdevelopment.e_eye_o.hibernate.entities;

import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.wrapper.IdObjectWrapperFactory;
import com.jtbdevelopment.e_eye_o.hibernate.entities.wrapper.HibernateIdObjectWrapperConfig;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

/**
 * Date: 1/29/13
 * Time: 9:13 PM
 */
public class HibernateAbstractIdObjectTest {
    protected final DateTime DATETIME_VALUE = new DateTime();
    protected final LocalDateTime LOCALDATETIME_VALUE = new LocalDateTime();
    protected final String STRING_VALUE = "S";
    protected Mockery context;
    protected IdObjectFactory implFactory;
    protected IdObjectWrapperFactory idObjectWrapperFactory;
    protected HibernateIdObjectWrapperConfig daoFactory;

    protected void setUp() {
        context = new Mockery();
        implFactory = context.mock(IdObjectFactory.class);
        idObjectWrapperFactory = context.mock(IdObjectWrapperFactory.class);
        context.checking(new Expectations() {{
            oneOf(idObjectWrapperFactory).addBaseClass(IdObjectWrapperFactory.WrapperKind.DAO, HibernateIdObject.class);
            oneOf(idObjectWrapperFactory).addMapping(IdObjectWrapperFactory.WrapperKind.DAO, AppUser.class, HibernateAppUser.class);
            oneOf(idObjectWrapperFactory).addMapping(IdObjectWrapperFactory.WrapperKind.DAO, ClassList.class, HibernateClassList.class);
            oneOf(idObjectWrapperFactory).addMapping(IdObjectWrapperFactory.WrapperKind.DAO, Semester.class, HibernateSemester.class);
            oneOf(idObjectWrapperFactory).addMapping(IdObjectWrapperFactory.WrapperKind.DAO, Student.class, HibernateStudent.class);
            oneOf(idObjectWrapperFactory).addMapping(IdObjectWrapperFactory.WrapperKind.DAO, Photo.class, HibernatePhoto.class);
            oneOf(idObjectWrapperFactory).addMapping(IdObjectWrapperFactory.WrapperKind.DAO, Observation.class, HibernateObservation.class);
            oneOf(idObjectWrapperFactory).addMapping(IdObjectWrapperFactory.WrapperKind.DAO, ObservationCategory.class, HibernateObservationCategory.class);
            oneOf(idObjectWrapperFactory).addMapping(IdObjectWrapperFactory.WrapperKind.DAO, TwoPhaseActivity.class, HibernateTwoPhaseActivity.class);
            oneOf(idObjectWrapperFactory).addMapping(IdObjectWrapperFactory.WrapperKind.DAO, Observable.class, HibernateObservable.class);
            oneOf(idObjectWrapperFactory).addMapping(IdObjectWrapperFactory.WrapperKind.DAO, AppUserSettings.class, HibernateAppUserSettings.class);
            oneOf(idObjectWrapperFactory).addMapping(IdObjectWrapperFactory.WrapperKind.DAO, AppUserOwnedObject.class, HibernateAppUserOwnedObject.class);

            allowing(idObjectWrapperFactory).getEntityForWrapper(IdObjectWrapperFactory.WrapperKind.DAO, AppUser.class);
            will(returnValue(AppUser.class));
            allowing(idObjectWrapperFactory).getEntityForWrapper(IdObjectWrapperFactory.WrapperKind.DAO, HibernateAppUser.class);
            will(returnValue(AppUser.class));
            allowing(idObjectWrapperFactory).getEntityForWrapper(IdObjectWrapperFactory.WrapperKind.DAO, ClassList.class);
            will(returnValue(ClassList.class));
            allowing(idObjectWrapperFactory).getEntityForWrapper(IdObjectWrapperFactory.WrapperKind.DAO, HibernateClassList.class);
            will(returnValue(ClassList.class));
            allowing(idObjectWrapperFactory).getEntityForWrapper(IdObjectWrapperFactory.WrapperKind.DAO, DeletedObject.class);
            will(returnValue(DeletedObject.class));
            allowing(idObjectWrapperFactory).getEntityForWrapper(IdObjectWrapperFactory.WrapperKind.DAO, Student.class);
            will(returnValue(Student.class));
            allowing(idObjectWrapperFactory).getEntityForWrapper(IdObjectWrapperFactory.WrapperKind.DAO, HibernateStudent.class);
            will(returnValue(Student.class));
            allowing(idObjectWrapperFactory).getEntityForWrapper(IdObjectWrapperFactory.WrapperKind.DAO, ObservationCategory.class);
            will(returnValue(ObservationCategory.class));
            allowing(idObjectWrapperFactory).getEntityForWrapper(IdObjectWrapperFactory.WrapperKind.DAO, HibernateObservationCategory.class);
            will(returnValue(ObservationCategory.class));
            allowing(idObjectWrapperFactory).getEntityForWrapper(IdObjectWrapperFactory.WrapperKind.DAO, Observation.class);
            will(returnValue(Observation.class));
            allowing(idObjectWrapperFactory).getEntityForWrapper(IdObjectWrapperFactory.WrapperKind.DAO, HibernateObservation.class);
            will(returnValue(Observation.class));
            allowing(idObjectWrapperFactory).getEntityForWrapper(IdObjectWrapperFactory.WrapperKind.DAO, Photo.class);
            will(returnValue(Photo.class));
            allowing(idObjectWrapperFactory).getEntityForWrapper(IdObjectWrapperFactory.WrapperKind.DAO, HibernatePhoto.class);
            will(returnValue(Photo.class));
        }});
        daoFactory = new HibernateIdObjectWrapperConfig(idObjectWrapperFactory, implFactory);
    }
}
