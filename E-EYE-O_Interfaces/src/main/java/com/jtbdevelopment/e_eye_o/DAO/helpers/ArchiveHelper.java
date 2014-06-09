package com.jtbdevelopment.e_eye_o.DAO.helpers;

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;

/**
 * Date: 12/26/13
 * Time: 7:01 PM
 */
public interface ArchiveHelper {
    /**
     * Flip the archive status of the entity
     * Also flip the status of any related objects IF they match the initial status
     * <p/>
     * So if you archive a student, all observations (and photos) for that student, which are not already archived,
     * will be archived.
     * <p/>
     * Archive rules:
     * (Un)Archive an observation - (Un)archive all photos for that observation
     * (Un)Archive a student - (Un)archive all observations (and their photos) plus any photos attached to student
     * if feasible in implementation and the student
     * (Un)Archive a semester - (Un)archive all observations (and their photos) from the semesters time period as well as semester
     * (Un)Archive a classlist - (Un)archive all observatins (and their photos) plus any photos attached to classlist
     * uf feasible in addition to the class list
     * In addition:
     * Archive a classlist - Archive all students (and observations, etc) where this was their only active class list
     * Unarchive a classlist - Unarchive any archived student (and observations, etc)
     * <p/>
     * If you archive
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T extends AppUserOwnedObject> void flipArchiveStatus(final T entity);
}
