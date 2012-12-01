package com.jtbdevelopment.e_eye_o.hibernate.entities;

import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.wrapper.AbstractIdObjectWrapperFactoryImpl;
import com.jtbdevelopment.e_eye_o.entities.wrapper.IdObjectWrapperFactory;
import org.springframework.stereotype.Service;

@Service
public class HDBIdObjectWrapperFactory extends AbstractIdObjectWrapperFactoryImpl<HDBIdObject> implements IdObjectWrapperFactory<HDBIdObject> {
    private static HDBIdObjectWrapperFactory instance;

    public static HDBIdObjectWrapperFactory getInstance() {
        return instance;
    }

    public HDBIdObjectWrapperFactory() {
        super();
        addMapping(AppUser.class, HDBAppUser.class);
        addMapping(Student.class, HDBStudent.class);
        addMapping(Photo.class, HDBPhoto.class);
        addMapping(ObservationCategory.class, HDBObservationCategory.class);
        addMapping(Observation.class, HDBObservation.class);
        addMapping(ClassList.class, HDBClassList.class);
        instance = this;
    }

    @Override
    protected boolean needsWrapping(final Object entity) {
        if (entity instanceof HDBIdObject) {
            return false;
        }
        return true;
    }
}