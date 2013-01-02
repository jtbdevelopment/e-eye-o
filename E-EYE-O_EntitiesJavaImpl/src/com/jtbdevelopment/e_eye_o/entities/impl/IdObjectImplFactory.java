package com.jtbdevelopment.e_eye_o.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.*;
import org.springframework.stereotype.Service;

/**
 * Date: 1/1/13
 * Time: 7:53 PM
 */
@Service
public class IdObjectImplFactory implements IdObjectFactory {
    @Override
    public <T extends IdObject> T newIdObject(final Class<T> idObjectType) {
        switch ( idObjectType.getSimpleName() ) {
            case "AppUser":
                return (T) newAppUser();
            case "Observation":
                return (T) newObservation();
            case "ObservationCategory":
                return (T) newObservationCategory();
            case "Student":
                return (T) newStudent();
            case "Photo":
                return (T) newPhoto();
            case "ClassList":
                return (T) newClassList();
            default:
                throw new IllegalArgumentException("Unknown class type " + idObjectType.getSimpleName() );
        }
    }

    @Override
    public AppUser newAppUser() {
        return new AppUserImpl();
    }

    @Override
    public ClassList newClassList() {
        return new ClassListImpl();
    }

    @Override
    public ClassList newClassList(final AppUser appUser) {
        return new ClassListImpl(appUser);
    }

    @Override
    public Observation newObservation() {
        return new ObservationImpl();
    }

    @Override
    public Observation newObservation(final AppUser appUser) {
        return new ObservationImpl(appUser);
    }

    @Override
    public ObservationCategory newObservationCategory() {
        return new ObservationCategoryImpl();
    }

    @Override
    public ObservationCategory newObservationCategory(final AppUser appUser) {
        return new ObservationCategoryImpl(appUser);
    }

    @Override
    public Photo newPhoto() {
        return new PhotoImpl();
    }

    @Override
    public Photo newPhoto(final AppUser appUser) {
        return new PhotoImpl(appUser);
    }

    @Override
    public Student newStudent() {
        return new StudentImpl();
    }

    @Override
    public Student newStudent(final AppUser appUser) {
        return new StudentImpl(appUser);
    }
}
