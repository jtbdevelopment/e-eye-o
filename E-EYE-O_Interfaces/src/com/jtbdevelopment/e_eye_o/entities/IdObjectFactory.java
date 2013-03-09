package com.jtbdevelopment.e_eye_o.entities;

import com.jtbdevelopment.e_eye_o.entities.builders.AppUserOwnedObjectBuilder;
import com.jtbdevelopment.e_eye_o.entities.builders.IdObjectBuilder;

/**
 * Date: 1/1/13
 * Time: 7:47 PM
 */
public interface IdObjectFactory {
    <T extends IdObject> T newIdObject(Class<T> idObjectType);

    <T extends IdObject, B extends IdObjectBuilder<T>> B newIdObjectBuilder(Class<T> idObjectType);

    <T extends AppUserOwnedObject> T newAppUserOwnedObject(Class<T> idObjectType, final AppUser appUser);

    <T extends AppUserOwnedObject, B extends AppUserOwnedObjectBuilder<T>> B newAppUserOwnedObjectBuilder(Class<T> idObjectType);

    AppUser newAppUser();

    ClassList newClassList(final AppUser appUser);

    Observation newObservation(final AppUser appUser);

    ObservationCategory newObservationCategory(final AppUser appUser);

    Photo newPhoto(final AppUser appUser);

    Student newStudent(final AppUser appUser);

    DeletedObject newDeletedObject(final AppUser appUser);
}
