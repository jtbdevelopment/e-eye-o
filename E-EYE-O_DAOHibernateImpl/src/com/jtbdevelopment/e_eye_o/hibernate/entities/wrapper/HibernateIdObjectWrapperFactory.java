package com.jtbdevelopment.e_eye_o.hibernate.entities.wrapper;

import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.wrapper.AbstractIdObjectWrapperFactoryImpl;
import com.jtbdevelopment.e_eye_o.entities.wrapper.DAOIdObjectWrapperFactory;
import com.jtbdevelopment.e_eye_o.hibernate.entities.impl.*;
import org.springframework.stereotype.Service;

@Service
public class HibernateIdObjectWrapperFactory extends AbstractIdObjectWrapperFactoryImpl implements DAOIdObjectWrapperFactory {
    public HibernateIdObjectWrapperFactory() {
        super(HibernateIdObject.class);
        addMapping(AppUser.class, HibernateAppUser.class);
        addMapping(Student.class, HibernateStudent.class);
        addMapping(Photo.class, HibernatePhoto.class);
        addMapping(ObservationCategory.class, HibernateObservationCategory.class);
        addMapping(Observation.class, HibernateObservation.class);
        addMapping(ClassList.class, HibernateClassList.class);
    }
}