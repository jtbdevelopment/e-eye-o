package com.jtbdevelopment.e_eye_o.entities.impl;

import com.jtbdevelopment.e_eye_o.DAO.helpers.PhotoHelper;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.builders.*;
import com.jtbdevelopment.e_eye_o.entities.impl.builders.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Date: 1/1/13
 * Time: 7:53 PM
 */
@Service
public class IdObjectImplFactory implements IdObjectFactory {
    @Autowired
    private PhotoHelper photoHelper;

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
            case "TwoPhaseActivity":
                return (T) newTwoPhaseActivity(null);
            default:
                throw new IllegalArgumentException("Unknown class type " + idObjectType.getSimpleName());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IdObject, B extends IdObjectBuilder<T>> B newIdObjectBuilder(final Class<T> idObjectType) {
        switch (idObjectType.getSimpleName()) {
            case "AppUser":
                return (B) newAppUserBuilder();
            case "Observation":
                return (B) newObservationBuilder(null);
            case "ObservationCategory":
                return (B) newObservationCategoryBuilder(null);
            case "Student":
                return (B) newStudentBuilder(null);
            case "Photo":
                return (B) newPhotoBuilder(null);
            case "ClassList":
                return (B) newClassListBuilder(null);
            case "DeletedObject":
                return (B) newDeletedObjectBuilder(null);
            case "TwoPhaseActivity":
                return (B) newTwoPhaseActivityBuilder(null);
            default:
                throw new IllegalArgumentException("Unknown class type " + idObjectType.getSimpleName());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends AppUserOwnedObject> T newAppUserOwnedObject(final Class<T> idObjectType, final AppUser appUser) {
        if (!AppUserOwnedObject.class.isAssignableFrom(idObjectType)) {
            throw new IllegalArgumentException("You cannot use this method to create non-app user owned objects.");
        }
        T entity = newIdObject(idObjectType);
        entity.setAppUser(appUser);
        return entity;
    }

    @Override
    public <T extends AppUserOwnedObject, B extends AppUserOwnedObjectBuilder<T>> B newAppUserOwnedObjectBuilder(final Class<T> idObjectType, final AppUser appUser) {
        if (!AppUserOwnedObject.class.isAssignableFrom(idObjectType)) {
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
    public AppUserBuilder newAppUserBuilder() {
        return new AppUserBuilderImpl(newAppUser());
    }

    @Override
    public ClassList newClassList(final AppUser appUser) {
        return new ClassListImpl(appUser);
    }

    @Override
    public ClassListBuilder newClassListBuilder(final AppUser appUser) {
        return new ClassListBuilderImpl(newClassList(appUser));
    }

    @Override
    public Observation newObservation(final AppUser appUser) {
        return new ObservationImpl(appUser);
    }

    @Override
    public ObservationBuilder newObservationBuilder(final AppUser appUser) {
        return new ObservationBuilderImpl(newObservation(appUser));
    }

    @Override
    public ObservationCategory newObservationCategory(final AppUser appUser) {
        return new ObservationCategoryImpl(appUser);
    }

    @Override
    public ObservationCategoryBuilder newObservationCategoryBuilder(final AppUser appUser) {
        return new ObservationCategoryBuilderImpl(newObservationCategory(appUser));
    }

    @Override
    public Photo newPhoto(final AppUser appUser) {
        return new PhotoImpl(appUser);
    }

    @Override
    public PhotoBuilder newPhotoBuilder(final AppUser appUser) {
        return new PhotoBuilderImpl(photoHelper, newPhoto(appUser));
    }

    @Override
    public Student newStudent(final AppUser appUser) {
        return new StudentImpl(appUser);
    }

    @Override
    public StudentBuilder newStudentBuilder(final AppUser appUser) {
        return new StudentBuilderImpl(newStudent(appUser));
    }

    @Override
    public DeletedObject newDeletedObject(final AppUser appUser) {
        return new DeletedObjectImpl(appUser);
    }

    @Override
    public DeletedObjectBuilder newDeletedObjectBuilder(final AppUser appUser) {
        return new DeletedObjectBuilderImpl(newDeletedObject(appUser));
    }

    @Override
    public TwoPhaseActivity newTwoPhaseActivity(final AppUser appUser) {
        return new TwoPhaseActivityImpl(appUser);
    }

    @Override
    public TwoPhaseActivityBuilder newTwoPhaseActivityBuilder(final AppUser appUser) {
        return new TwoPhaseActivityBuilderImpl(newTwoPhaseActivity(appUser));
    }
}
