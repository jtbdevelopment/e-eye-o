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
public class IdObjectFactoryImpl implements IdObjectFactory {
    @Autowired
    private PhotoHelper photoHelper;

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IdObject> T newIdObject(final Class<T> idObjectType) {
        switch (idObjectType.getSimpleName()) {
            case "AppUser":
                return (T) newAppUser();
            case "AppUserSettings":
                return (T) newAppUserSettings(null);
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
            case "Semester":
                return (T) newSemester(null);
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
    public AppUser newAppUser() {
        return new AppUserImpl();
    }

    @Override
    public AppUserBuilder newAppUserBuilder() {
        return new AppUserBuilderImpl(newAppUser());
    }

    @Override
    public AppUserSettings newAppUserSettings(final AppUser appUser) {
        return new AppUserSettingsImpl(appUser);
    }

    @Override
    public AppUserSettingsBuilder newAppUserSettingsBuilder(final AppUser appUser) {
        return new AppUserSettingsBuilderImpl(newAppUserSettings(appUser));
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

    @Override
    public Semester newSemester(final AppUser appUser) {
        return new SemesterImpl(appUser);
    }

    @Override
    public SemesterBuilder newSemesterBuilder(final AppUser appUser) {
        return new SemesterBuilderImpl(newSemester(appUser));
    }
}
