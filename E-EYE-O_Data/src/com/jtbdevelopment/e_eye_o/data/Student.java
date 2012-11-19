package com.jtbdevelopment.e_eye_o.data;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Date: 11/4/12
 * Time: 7:24 PM
 */
@Entity
public class Student extends IdObject {
    private String firstName = "";
    private String lastName = "";
    private String displayName = "";
    private Set<Observation> observations = new TreeSet<Observation>(new Comparator<Observation>() {
        @Override
        public int compare(Observation o1, Observation o2) {
            int date = o1.getObservationDate().compareTo(o2.getObservationDate());
            if (date != 0) {
                return date;
            }
            return o1.getId().compareTo(o2.getId());
        }
    });

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @ElementCollection()
    public Set<Observation> getObservations() {
        return observations;
    }

    public void setObservations(Set<Observation> observations) {
        this.observations = observations;
    }

    public void addObservation(final Observation observation) {
        observations.add(observation);
    }

    public void updateObservation(final Observation observation) {
        observations.remove(observation);
        observations.add(observation);
    }

    public void removeObservation(final Observation observation) {
        observations.remove(observation);
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
