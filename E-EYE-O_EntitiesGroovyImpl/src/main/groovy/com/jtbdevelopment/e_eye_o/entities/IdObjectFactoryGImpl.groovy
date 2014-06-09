package com.jtbdevelopment.e_eye_o.entities

import com.jtbdevelopment.e_eye_o.DAO.helpers.PhotoHelper
import com.jtbdevelopment.e_eye_o.entities.builders.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Date: 11/30/13
 * Time: 12:27 PM
 */
//@CompileStatic
@Service
class IdObjectFactoryGImpl implements IdObjectFactory {
    @Autowired
    PhotoHelper photoHelper;

    @Override
    def <T extends IdObject> T newIdObject(final Class<T> idObjectType) {
        switch (idObjectType) {
            case AppUser.class:
                return (T) newAppUser()
            case ClassList.class:
                return (T) newClassList(null)
            case Semester.class:
                return (T) newSemester(null)
            case ObservationCategory.class:
                return (T) newObservationCategory(null)
            case Observation.class:
                return (T) newObservation(null)
            case Student.class:
                return (T) newStudent(null)
            case DeletedObject.class:
                return (T) newDeletedObject(null)
            case TwoPhaseActivity.class:
                return (T) newTwoPhaseActivity(null)
            case Photo.class:
                return (T) newPhoto(null)
            case AppUserSettings.class:
                return (T) newAppUserSettings(null)
        }
        throw new IllegalArgumentException("Unknown class type " + idObjectType.getSimpleName());
    }

    @Override
    def <T extends AppUserOwnedObject> T newAppUserOwnedObject(final Class<T> idObjectType, final AppUser appUser) {
        if (!AppUserOwnedObject.class.isAssignableFrom(idObjectType)) {
            throw new IllegalArgumentException("You cannot use this method to create non-app user owned objects.");
        }
        T entity = newIdObject(idObjectType)
        entity.appUser = appUser
        return entity
    }

    @Override
    AppUser newAppUser() {
        return new AppUserGImpl()
    }

    @Override
    AppUserBuilder newAppUserBuilder() {
        return new AppUserBuilderGImpl(entity: newAppUser())
    }

    @Override
    PaginatedIdObjectList newPaginatedIdObjectList() {
        return new PaginatedIdObjectListGImpl()
    }

    @Override
    PaginatedIdObjectListBuilder newPaginatedIdObjectListBuilder() {
        return new PaginatedIdObjectListBuilderGImpl(entity: newPaginatedIdObjectList())
    }

    @Override
    AppUserSettings newAppUserSettings(final AppUser appUser) {
        return new AppUserSettingsGImpl(appUser: appUser)
    }

    @Override
    AppUserSettingsBuilder newAppUserSettingsBuilder(final AppUser appUser) {
        return new AppUserSettingsBuilderGImpl(entity: newAppUserSettings(appUser))
    }

    @Override
    ClassList newClassList(final AppUser appUser) {
        return new ClassListGImpl(appUser: appUser)
    }

    @Override
    ClassListBuilder newClassListBuilder(final AppUser appUser) {
        return new ClassListBuilderGImpl(entity: newClassList(appUser))
    }

    @Override
    Observation newObservation(final AppUser appUser) {
        return new ObservationGImpl(appUser: appUser)
    }

    @Override
    ObservationBuilder newObservationBuilder(final AppUser appUser) {
        return new ObservationBuilderGImpl(entity: newObservation(appUser))
    }

    @Override
    ObservationCategory newObservationCategory(final AppUser appUser) {
        return new ObservationCategoryGImpl(appUser: appUser)
    }

    @Override
    ObservationCategoryBuilder newObservationCategoryBuilder(final AppUser appUser) {
        return new ObservationCategoryBuilderGImpl(entity: newObservationCategory(appUser))
    }

    @Override
    Photo newPhoto(final AppUser appUser) {
        return new PhotoGImpl(appUser: appUser)
    }

    @Override
    PhotoBuilder newPhotoBuilder(final AppUser appUser) {
        return new PhotoBuilderGImpl(photoHelper: photoHelper, entity: newPhoto(appUser))
    }

    @Override
    Student newStudent(final AppUser appUser) {
        return new StudentGImpl(appUser: appUser)
    }

    @Override
    StudentBuilder newStudentBuilder(final AppUser appUser) {
        return new StudentBuilderGImpl(entity: newStudent(appUser))
    }

    @Override
    DeletedObject newDeletedObject(final AppUser appUser) {
        return new DeletedObjectGImpl(appUser: appUser)
    }

    @Override
    DeletedObjectBuilder newDeletedObjectBuilder(final AppUser appUser) {
        return new DeletedObjectBuilderGImpl(entity: newDeletedObject(appUser))
    }

    @Override
    TwoPhaseActivity newTwoPhaseActivity(final AppUser appUser) {
        return new TwoPhaseActivityGImpl(appUser: appUser)
    }

    @Override
    TwoPhaseActivityBuilder newTwoPhaseActivityBuilder(final AppUser appUser) {
        return new TwoPhaseActivityBuilderGImpl(entity: newTwoPhaseActivity(appUser))
    }

    @Override
    Semester newSemester(final AppUser appUser) {
        return new SemesterGImpl(appUser: appUser)
    }

    @Override
    SemesterBuilder newSemesterBuilder(final AppUser appUser) {
        return new SemesterBuilderGImpl(entity: newSemester(appUser))
    }
}
