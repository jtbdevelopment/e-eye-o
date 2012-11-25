package com.jtbdevelopment.e_eye_o.entities;

import com.jtbdevelopment.e_eye_o.superclasses.ArchivableAppUserOwnedObject;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
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
    protected Student() {
        //  For hibernate
    }

    public Student(final AppUser appUser) {
        super(appUser);
    }

    public String getFirstName() {
        return firstName == null ? "" : firstName;
    }

    public Student setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Student setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    @ElementCollection()
    public Set<Observation> getObservations() {
        return observations;
    }

    @SuppressWarnings("unused") // hibernate
    private Student setObservations(Set<Observation> observations) {
        this.observations = observations;
        return this;
    }

    public Student addObservation(final Observation observation) {
        observations.add(observation);
        return this;
    }

    public Student removeObservation(final Observation observation) {
        observations.remove(observation);
        return this;
    }

    @OneToOne(optional = false)
    public Photo getStudentPhoto() {
        return studentPhoto;
    }

    public void setStudentPhoto(Photo studentPhoto) {
        this.studentPhoto = studentPhoto;
    }
}
