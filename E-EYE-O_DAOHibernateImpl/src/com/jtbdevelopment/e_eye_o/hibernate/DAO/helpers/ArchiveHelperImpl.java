package com.jtbdevelopment.e_eye_o.hibernate.DAO.helpers;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.DAO.helpers.ArchiveHelper;
import com.jtbdevelopment.e_eye_o.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Date: 12/26/13
 * Time: 7:25 PM
 */
@Component
@Transactional(readOnly = false)
public class ArchiveHelperImpl implements ArchiveHelper {
    @Autowired
    protected ReadWriteDAO readWriteDAO;

    @Override
    public <T extends AppUserOwnedObject> void flipArchiveStatus(final T entity) {
        T loaded = readWriteDAO.get((Class<T>) entity.getClass(), entity.getId());
        if (loaded == null) {
            return;  //  Already deleted?
        }

        boolean initialArchiveStatus = loaded.isArchived();
        boolean terminalArchiveStatus = !initialArchiveStatus;

        if (initialArchiveStatus) {
            //  If un-archiving, un-archive this one first
            loaded.setArchived(terminalArchiveStatus);
            loaded = readWriteDAO.trustedUpdate(loaded);
        }

        flipStatusForRelatedPhotos(loaded, initialArchiveStatus);
        flipStatusForObservationsIfSemester(loaded, initialArchiveStatus);
        flipStatusForObservationsIfObservable(loaded, initialArchiveStatus);
        flipStatusForStudentsIfClassList(loaded, initialArchiveStatus, terminalArchiveStatus);

        if (terminalArchiveStatus) {
            //  If archiving, archive this one last
            loaded.setArchived(terminalArchiveStatus);
            readWriteDAO.trustedUpdate(loaded);
        }
    }

    private <T extends AppUserOwnedObject> void flipStatusForStudentsIfClassList(final T loaded, final boolean initialArchiveStatus, final boolean terminalArchiveStatus) {
        if (loaded instanceof ClassList) {
            for (Student student : readWriteDAO.getAllStudentsForClassList((ClassList) loaded)) {
                if (student.isArchived() == initialArchiveStatus) {
                    if (terminalArchiveStatus) {
                        if (student.getActiveClassLists().size() == 1) {
                            flipArchiveStatus(student);
                        }
                    } else {
                        flipArchiveStatus(student);
                    }
                }
            }
        }
    }

    private <T extends AppUserOwnedObject> void flipStatusForObservationsIfObservable(final T loaded, final boolean initialArchiveStatus) {
        if (loaded instanceof Observable) {
            for (Observation observation : readWriteDAO.getAllObservationsForEntity((Observable) loaded)) {
                if (observation.isArchived() == initialArchiveStatus) {
                    flipArchiveStatus(observation);
                }
            }
        }
    }

    private <T extends AppUserOwnedObject> void flipStatusForObservationsIfSemester(final T loaded, final boolean initialArchiveStatus) {
        if (loaded instanceof Semester) {
            for (Observation observation : readWriteDAO.getAllObservationsForSemester((Semester) loaded, 0, 0)) {
                if (observation.isArchived() == initialArchiveStatus) {
                    flipArchiveStatus(observation);
                }
            }
        }
    }

    private <T extends AppUserOwnedObject> void flipStatusForRelatedPhotos(final T loaded, final boolean initialArchiveStatus) {
        for (Photo photo : readWriteDAO.getAllPhotosForEntity(loaded, 0, 0)) {
            if (photo.isArchived() == initialArchiveStatus) {
                flipArchiveStatus(photo);
            }
        }
    }
}
