package com.jtbdevelopment.e_eye_o.entities;

import com.jtbdevelopment.e_eye_o.entities.builders.*;

/**
 * Date: 1/1/13
 * Time: 7:47 PM
 */
public interface IdObjectFactory {
    <T extends IdObject> T newIdObject(final Class<T> idObjectType);

    <T extends IdObject, B extends IdObjectBuilder<T>> B newIdObjectBuilder(final Class<T> idObjectType);

    <T extends AppUserOwnedObject> T newAppUserOwnedObject(final Class<T> idObjectType, final AppUser appUser);

    <T extends AppUserOwnedObject, B extends AppUserOwnedObjectBuilder<T>> B newAppUserOwnedObjectBuilder(final Class<T> idObjectType, final AppUser appUser);

    AppUser newAppUser();

    AppUserBuilder newAppUserBuilder();

    AppUserSettings newAppUserSettings(final AppUser appUser);

    AppUserOwnedObjectBuilder newAppUserSettingsBuilder(final AppUser appUser);

    ClassList newClassList(final AppUser appUser);

    ClassListBuilder newClassListBuilder(final AppUser appUser);

    Observation newObservation(final AppUser appUser);

    ObservationBuilder newObservationBuilder(final AppUser appUser);

    ObservationCategory newObservationCategory(final AppUser appUser);

    ObservationCategoryBuilder newObservationCategoryBuilder(final AppUser appUser);

    Photo newPhoto(final AppUser appUser);

    PhotoBuilder newPhotoBuilder(final AppUser appUser);

    Student newStudent(final AppUser appUser);

    StudentBuilder newStudentBuilder(final AppUser appUser);

    DeletedObject newDeletedObject(final AppUser appUser);

    DeletedObjectBuilder newDeletedObjectBuilder(final AppUser appUser);

    TwoPhaseActivity newTwoPhaseActivity(final AppUser appUser);

    TwoPhaseActivityBuilder newTwoPhaseActivityBuilder(final AppUser appUser);
}
