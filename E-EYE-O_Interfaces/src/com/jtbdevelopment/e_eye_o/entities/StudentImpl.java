package com.jtbdevelopment.e_eye_o.entities;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Date: 11/4/12
 * Time: 7:24 PM
 */
public class StudentImpl extends ArchivableAppUserOwnedObjectImpl implements Student {
    private String firstName = "";
    private String lastName = "";
    private Set<Observation> observations = new TreeSet<>(new Comparator<Observation>() {
        @Override
        public int compare(Observation o1, Observation o2) {
            int date = o1.getObservationDate().compareTo(o2.getObservationDate());
            if (date != 0) {
                return date;
            }
            return o1.getId().compareTo(o2.getId());
        }
    });
    //  TODO - default stock photo
    private Photo studentPhoto;

    public StudentImpl() {
    }

    public StudentImpl(final AppUser appUser) {
        super(appUser);
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public Student setFirstName(final String firstName) {
        validateNonNullValue(firstName);
        this.firstName = firstName;
        return this;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public Student setLastName(final String lastName) {
        validateNonNullValue(lastName);
        this.lastName = lastName;
        return this;
    }

    @Override
    public Set<Observation> getObservations() {
        return Collections.unmodifiableSet(observations);
    }

    @Override
    public Student setObservations(final Set<Observation> observations) {
        validateSameAppUsers(observations);
        this.observations = observations;
        return this;
    }

    @Override
    public Student addObservation(final Observation observation) {
        validateSameAppUser(observation);
        observations.add(observation);
        return this;
    }

    @Override
    public Student removeObservation(final Observation observation) {
        observations.remove(observation);
        return this;
    }

    @Override
    public Photo getStudentPhoto() {
        return studentPhoto;
    }

    @Override
    public Student setStudentPhoto(final Photo studentPhoto) {
        validateSameAppUser(studentPhoto);
        this.studentPhoto = studentPhoto;
        return this;
    }
}
