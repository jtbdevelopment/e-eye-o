package com.jtbdevelopment.e_eye_o.DAO.helpers;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

/**
 * Date: 1/30/14
 * Time: 6:41 AM
 */
public class AbstractDeletionHelperImpl implements DeletionHelper {
    @Autowired
    protected ReadWriteDAO readWriteDAO;

    //  TODO - allow undelete - feasible now with audit

    @Override
    public void deactivateUser(final AppUser user) {
        AppUser loaded = readWriteDAO.get(AppUser.class, user.getId());
        loaded.setActive(false);
        readWriteDAO.trustedUpdate(loaded);
    }

    @Override
    public void deleteUser(final AppUser user) {
        AppUser loaded = readWriteDAO.get(AppUser.class, user.getId());
        if (loaded == null) {
            return;  //  Already deleted?
        }

        for (Class<? extends AppUserOwnedObject> appUserClass : Arrays.asList(Student.class, ClassList.class, ObservationCategory.class, Semester.class, TwoPhaseActivity.class, AppUserSettings.class)) {
            Collection<? extends AppUserOwnedObject> deleteList = readWriteDAO.getEntitiesForUser(appUserClass, loaded, 0, 0);
            for (AppUserOwnedObject entity : deleteList) {
                delete(entity);
            }
        }

        //  TODO - delete audit tables

        readWriteDAO.trustedDelete(loaded);
    }

    @Override
    public <T extends AppUserOwnedObject> void delete(final T entity) {
        if (entity instanceof DeletedObject) {
            throw new IllegalArgumentException("You can not manually delete DeletedObjects.  These are only cleaned up by deleting user.");
        }
        T loaded = readWriteDAO.get((Class<T>) entity.getClass(), entity.getId());
        if (loaded == null) {
            //  Already deleted
            return;
        }

        removeCategoryFromObservations(loaded);
        removeClassListFromStudents(loaded);
        deletePhotosForEntity(loaded);
        deleteObservationsForEntity(loaded);

        readWriteDAO.trustedDelete(loaded);
    }

    private <T extends AppUserOwnedObject> void deleteObservationsForEntity(final T loaded) {
        if (loaded instanceof Observable) {
            for (Observation o : readWriteDAO.getAllObservationsForEntity((Observable) loaded)) {
                deletePhotosForEntity(o);
                readWriteDAO.trustedDelete(o);
            }
        }
    }

    private <T extends AppUserOwnedObject> void deletePhotosForEntity(final T loaded) {
        final Set<Photo> allPhotosForEntity = readWriteDAO.getAllPhotosForEntity(loaded, 0, 0);
        for (Photo p : allPhotosForEntity) {
            readWriteDAO.trustedDelete(p);
        }
    }

    private <T extends AppUserOwnedObject> void removeClassListFromStudents(final T loaded) {
        if (loaded instanceof ClassList) {
            for (Student student : readWriteDAO.getAllStudentsForClassList((ClassList) loaded)) {
                student.removeClassList((ClassList) loaded);
                readWriteDAO.trustedUpdate(student);
            }
        }
    }

    /*
         *
         *  Note that deleting a semester does not delete observations for semester
         *
         */
    private <T extends AppUserOwnedObject> void removeCategoryFromObservations(final T loaded) {
        if (loaded instanceof ObservationCategory) {
            ObservationCategory category = (ObservationCategory) loaded;
            for (Observation observation : readWriteDAO.getAllObservationsForObservationCategory(category.getAppUser(), category)) {
                observation.removeCategory(category);
                readWriteDAO.trustedUpdate(observation);
            }
        }
    }
}
