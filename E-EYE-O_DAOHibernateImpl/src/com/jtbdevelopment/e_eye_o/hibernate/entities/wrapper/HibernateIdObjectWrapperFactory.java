package com.jtbdevelopment.e_eye_o.hibernate.entities.wrapper;

import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.helpers.IdObjectInterfaceResolver;
import com.jtbdevelopment.e_eye_o.entities.wrapper.AbstractIdObjectWrapperFactoryImpl;
import com.jtbdevelopment.e_eye_o.entities.wrapper.DAOIdObjectWrapperFactory;
import com.jtbdevelopment.e_eye_o.hibernate.entities.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HibernateIdObjectWrapperFactory extends AbstractIdObjectWrapperFactoryImpl implements DAOIdObjectWrapperFactory {

    @Autowired
    public HibernateIdObjectWrapperFactory(final IdObjectFactory implFactory, final IdObjectInterfaceResolver idObjectInterfaceResolver) {
        super(HibernateIdObject.class, idObjectInterfaceResolver);
        addMapping(AppUser.class, HibernateAppUser.class);
        addMapping(Student.class, HibernateStudent.class);
        addMapping(Photo.class, HibernatePhoto.class);
        addMapping(ObservationCategory.class, HibernateObservationCategory.class);
        addMapping(Observation.class, HibernateObservation.class);
        addMapping(ClassList.class, HibernateClassList.class);

        HibernateIdObject.setImplFactory(implFactory);
        HibernateIdObject.setDaoFactory(this);
    }
}