package com.jtbdevelopment.e_eye_o.entities;

import java.util.Set;

/**
 * Date: 11/25/12
 * Time: 3:14 PM
 */
public interface Student extends ArchivableAppUserOwnedObject {
    String getFirstName();

    Student setFirstName(final String firstName);

    String getLastName();

    Student setLastName(final String lastName);

    Set<Observation> getObservations();

    Student setObservations(final Set<Observation> observations);

    Student addObservation(final Observation observation);

    Student removeObservation(final Observation observation);

    Photo getStudentPhoto();

    Student setStudentPhoto(final Photo studentPhoto);
}
