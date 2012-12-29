package com.jtbdevelopment.e_eye_o.entities;

import java.util.*;

/**
 * Date: 11/4/12
 * Time: 7:24 PM
 */
public class StudentImpl extends ArchivableAppUserOwnedObjectImpl implements Student {
    private String firstName = "";
    private String lastName = "";
    private Set<Observation> observations = new HashSet<>();
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
        this.firstName = firstName;
        return this;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public Student setLastName(final String lastName) {
        this.lastName = lastName;
        return this;
    }

    @Override
    public Set<Observation> getObservations() {
        return Collections.unmodifiableSet(observations);
    }

    @Override
    public Student setObservations(final Set<? extends Observation> observations) {
        this.observations.clear();
        this.observations.addAll(observations);
        return this;
    }

    @Override
    public Student addObservation(final Observation observation) {
        observations.add(observation);
        return this;
    }

    @Override
    public Student addObservations(final Collection<? extends Observation> observations) {
        this.observations.addAll(observations);
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
        if (studentPhoto != null) {
            this.studentPhoto = studentPhoto;
        } else {
            //  TODO - default stock photo
            this.studentPhoto = null;
        }
        return this;
    }
}
