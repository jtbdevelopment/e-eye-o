package com.jtbdevelopment.e_eye_o.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 1/1/13
 * Time: 7:53 PM
 */
@Service
public class IdObjectImplFactory implements IdObjectFactory {
    private static Map<Class<? extends IdObject>, Class<? extends IdObject>> interfaceToImplementationMap = new HashMap<Class<? extends IdObject>, Class<? extends IdObject>>() {{
        put(AppUser.class, AppUserImpl.class);
        put(Observation.class, ObservationImpl.class);
        put(ObservationCategory.class, ObservationCategoryImpl.class);
        put(Student.class, StudentImpl.class);
        put(Photo.class, PhotoImpl.class);
        put(ClassList.class, ClassListImpl.class);
        put(DeletedObject.class, DeletedObjectImpl.class);
    }};


    @Override
    @SuppressWarnings("unchecked")
    public <T extends IdObject> Class<T> implementationForInterface(final Class<T> interfaceType) {
        return (Class<T>) interfaceToImplementationMap.get(interfaceType);
    }

    @Override
    public  Map<Class<? extends IdObject>, Class<? extends IdObject>> implementationsForInterfaces() {
        return Collections.unmodifiableMap(interfaceToImplementationMap);
    }

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
    @SuppressWarnings("unchecked")
    public <T extends AppUserOwnedObject> T newAppUserOwnedObject(final Class<T> idObjectType, final AppUser appUser) {
        switch (idObjectType.getSimpleName()) {
            case "Observation":
                return (T) newObservation(appUser);
            case "ObservationCategory":
                return (T) newObservationCategory(appUser);
            case "Student":
                return (T) newStudent(appUser);
            case "Photo":
                return (T) newPhoto(appUser);
            case "ClassList":
                return (T) newClassList(appUser);
            case "DeletedObject":
                return (T) newDeletedObject(appUser);
            default:
                if (IdObject.class.isAssignableFrom(idObjectType)) {
                    throw new IllegalArgumentException("You cannot use this method to create non-app user owned objects.");
                } else {
                    throw new IllegalArgumentException("Unknown class type " + idObjectType.getSimpleName());
                }
        }
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
