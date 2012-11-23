package com.jtbdevelopment.e_eye_o.entities;

import com.jtbdevelopment.e_eye_o.superclasses.ArchivableAppUserOwnedObject;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.security.InvalidParameterException;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Date: 11/4/12
 * Time: 7:24 PM
 */
@Entity
public class Student extends ArchivableAppUserOwnedObject {
    private String firstName = "";
    private String lastName = "";
    private String displayName = "";
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

    @SuppressWarnings("unused")
    private Student() {
        //  For hibernate
    }

    public Student(final AppUser appUser) {
        super(appUser);
    }

    public String getFirstName() {
        return firstName == null ? "" : firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(nullable = false)
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @ElementCollection()
    public Set<Observation> getObservations() {
        return observations;
    }

    private void setObservations(Set<Observation> observations) {
        this.observations = observations;
    }

    public void addObservation(final Observation observation) {
        observations.add(observation);
    }

    public void removeObservation(final Observation observation) {
        observations.remove(observation);
    }

    @OneToOne(optional = false)
    public Photo getStudentPhoto() {
        return studentPhoto;
    }

    public void setStudentPhoto(Photo studentPhoto) {
        this.studentPhoto = studentPhoto;
    }
}
