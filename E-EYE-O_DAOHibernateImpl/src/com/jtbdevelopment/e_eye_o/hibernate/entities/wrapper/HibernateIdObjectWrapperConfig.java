package com.jtbdevelopment.e_eye_o.hibernate.entities.wrapper;

import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.wrapper.IdObjectWrapperFactory;
import com.jtbdevelopment.e_eye_o.hibernate.entities.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HibernateIdObjectWrapperConfig {

    @Autowired
    public HibernateIdObjectWrapperConfig(final IdObjectWrapperFactory wrapperFactory, final IdObjectFactory implFactory) {
        wrapperFactory.addBaseClass(IdObjectWrapperFactory.WrapperKind.DAO, HibernateIdObject.class);
        wrapperFactory.addMapping(IdObjectWrapperFactory.WrapperKind.DAO, AppUser.class, HibernateAppUser.class);
        wrapperFactory.addMapping(IdObjectWrapperFactory.WrapperKind.DAO, Student.class, HibernateStudent.class);
        wrapperFactory.addMapping(IdObjectWrapperFactory.WrapperKind.DAO, Photo.class, HibernatePhoto.class);
        wrapperFactory.addMapping(IdObjectWrapperFactory.WrapperKind.DAO, ObservationCategory.class, HibernateObservationCategory.class);
        wrapperFactory.addMapping(IdObjectWrapperFactory.WrapperKind.DAO, Observation.class, HibernateObservation.class);
        wrapperFactory.addMapping(IdObjectWrapperFactory.WrapperKind.DAO, ClassList.class, HibernateClassList.class);
        wrapperFactory.addMapping(IdObjectWrapperFactory.WrapperKind.DAO, Observable.class, HibernateObservable.class);
        wrapperFactory.addMapping(IdObjectWrapperFactory.WrapperKind.DAO, TwoPhaseActivity.class, HibernateTwoPhaseActivity.class);
        wrapperFactory.addMapping(IdObjectWrapperFactory.WrapperKind.DAO, Semester.class, HibernateSemester.class);
        wrapperFactory.addMapping(IdObjectWrapperFactory.WrapperKind.DAO, AppUserSettings.class, HibernateAppUserSettings.class);
        wrapperFactory.addMapping(IdObjectWrapperFactory.WrapperKind.DAO, AppUserOwnedObject.class, HibernateAppUserOwnedObject.class);

        HibernateIdObject.setImplFactory(implFactory);
        HibernateIdObject.setWrapperFactory(wrapperFactory);
    }
}