package com.jtbdevelopment.e_eye_o.entities;

import org.springframework.cache.annotation.Cacheable;

import java.util.Map;

/**
 * Date: 1/1/13
 * Time: 7:47 PM
 */
public interface IdObjectFactory {
    @Cacheable("idObjectInterfaceToImplementation")
    <T extends IdObject> Class<T> implementationForInterface(Class<T> interfaceType);

    @Cacheable("idObjectInterfacesToImplementations")
    Map<Class<? extends IdObject>, Class<? extends IdObject>> implementationsForInterfaces();

    <T extends IdObject> T newIdObject(Class<T> idObjectType);

    <T extends AppUserOwnedObject> T newAppUserOwnedObject(Class<T> idObjectType, final AppUser appUser);

    AppUser newAppUser();

    ClassList newClassList(final AppUser appUser);

    Observation newObservation(final AppUser appUser);

    ObservationCategory newObservationCategory(final AppUser appUser);

    Photo newPhoto(final AppUser appUser);

    Student newStudent(final AppUser appUser);

    DeletedObject newDeletedObject(final AppUser appUser);
}
