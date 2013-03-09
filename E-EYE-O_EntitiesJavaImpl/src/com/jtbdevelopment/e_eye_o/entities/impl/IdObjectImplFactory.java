package com.jtbdevelopment.e_eye_o.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.builders.AppUserOwnedObjectBuilder;
import com.jtbdevelopment.e_eye_o.entities.builders.IdObjectBuilder;
import com.jtbdevelopment.e_eye_o.entities.impl.builders.*;
import org.springframework.stereotype.Service;

/**
 * Date: 1/1/13
 * Time: 7:53 PM
 */
@Service
public class IdObjectImplFactory implements IdObjectFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IdObject> T newIdObject(final Class<T> idObjectType) {
        switch (idObjectType.getSimpleName()) {
            case "AppUser":
                return (T) newAppUser();
            case "Observation":
                return (T) newObservation(null);
            case "ObservationCategory":
                return (T) newObservationCategory(null);
            case "Student":
                return (T) newStudent(null);
            case "Photo":
                return (T) newPhoto(null);
            case "ClassList":
                return (T) newClassList(null);
            case "DeletedObject":
                return (T) newDeletedObject(null);
            default:
                throw new IllegalArgumentException("Unknown class type " + idObjectType.getSimpleName());
        }
    }

    @Override
    public <T extends IdObject, B extends IdObjectBuilder<T>> B newIdObjectBuilder(final Class<T> idObjectType) {
        T entity = newIdObject(idObjectType);
        switch (idObjectType.getSimpleName()) {
            case "AppUser":
                return (B) new AppUserBuilderImpl((AppUser) entity);
            case "Observation":
                return (B) new ObservationBuilderImpl((Observation) entity);
            case "ObservationCategory":
                return (B) new ObservationCategoryBuilderImpl((ObservationCategory) entity);
            case "Student":
                return (B) new StudentBuilderImpl((Student) entity);
            case "Photo":
                return (B) new PhotoBuilderImpl((Photo) entity);
            case "ClassList":
                return (B) new ClassListBuilderImpl((ClassList) entity);
            case "DeletedObject":
                return (B) new DeletedObjectBuilderImpl((DeletedObject) entity);
            default:
                throw new IllegalArgumentException("Unknown class type " + idObjectType.getSimpleName());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends AppUserOwnedObject> T newAppUserOwnedObject(final Class<T> idObjectType, final AppUser appUser) {
        if(!AppUserOwnedObject.class.isAssignableFrom(idObjectType)) {
            throw new IllegalArgumentException("You cannot use this method to create non-app user owned objects.");
        }
        T entity = newIdObject(idObjectType);
        entity.setAppUser(appUser);
        return entity;
    }

    @Override
    public <T extends AppUserOwnedObject, B extends AppUserOwnedObjectBuilder<T>> B newAppUserOwnedObjectBuilder(final Class<T> idObjectType, final AppUser appUser) {
        if(!AppUserOwnedObject.class.isAssignableFrom(idObjectType)) {
            throw new IllegalArgumentException("You cannot use this method to create non-app user owned builder objects.");
        }
        B builder = newIdObjectBuilder(idObjectType);
        builder.withAppUser(appUser);
        return builder;
    }

    @Override
    public AppUser newAppUser() {
        return new AppUserImpl();
    }

    @Override
    public ClassList newClassList(final AppUser appUser) {
        return new ClassListImpl(appUser);
    }

    @Override
    public Observation newObservation(final AppUser appUser) {
        return new ObservationImpl(appUser);
    }

    @Override
    public ObservationCategory newObservationCategory(final AppUser appUser) {
        return new ObservationCategoryImpl(appUser);
    }

    @Override
    public Photo newPhoto(final AppUser appUser) {
        return new PhotoImpl(appUser);
    }

    @Override
    public Student newStudent(final AppUser appUser) {
        return new StudentImpl(appUser);
    }

    @Override
    public DeletedObject newDeletedObject(final AppUser appUser) {
        return new DeletedObjectImpl(appUser);
    }
}
