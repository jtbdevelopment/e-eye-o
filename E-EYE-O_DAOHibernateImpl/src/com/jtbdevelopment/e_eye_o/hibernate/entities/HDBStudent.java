package com.jtbdevelopment.e_eye_o.hibernate.entities;

import com.jtbdevelopment.e_eye_o.entities.Observation;
import com.jtbdevelopment.e_eye_o.entities.Photo;
import com.jtbdevelopment.e_eye_o.entities.Student;
import com.jtbdevelopment.e_eye_o.entities.impl.StudentImpl;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.Collection;
import java.util.Set;

/**
 * Date: 11/4/12
 * Time: 7:24 PM
 */
@Entity(name = "Student")
public class HDBStudent extends HDBArchivableAppUserOwnedObject<Student> implements Student {
    @SuppressWarnings("unused")
    protected HDBStudent() {
        super(new StudentImpl());
        //  For hibernate
    }

    public HDBStudent(final Student student) {
        super(student);
    }

    @Override
    public String getFirstName() {
        return wrapped.getFirstName();
    }

    @Override
    public Student setFirstName(String firstName) {
        wrapped.setFirstName(firstName);
        return this;
    }

    @Override
    public String getLastName() {
        return wrapped.getLastName();
    }

    @Override
    public Student setLastName(final String lastName) {
        wrapped.setLastName(lastName);
        return this;
    }

    @Override
    @ElementCollection(targetClass = HDBObservation.class)
    public Set<Observation> getObservations() {
        return wrapped.getObservations();
    }

    @Override
    public Student setObservations(final Set<? extends Observation> observations) {
        wrapped.setObservations(wrap(observations));
        return this;
    }

    @Override
    public Student addObservation(final Observation observation) {
        wrapped.addObservation(wrap(observation));
        return this;
    }

    @Override
    public Student addObservations(final Collection<? extends Observation> observations) {
        wrapped.addObservations(wrap(observations));
        return this;
    }

    @Override
    public Student removeObservation(final Observation observation) {
        wrapped.removeObservation(observation);
        return this;
    }

    @Override
    @OneToOne(targetEntity = HDBPhoto.class)
    public Photo getStudentPhoto() {
        return wrapped.getStudentPhoto();
    }

    @Override
    public Student setStudentPhoto(final Photo studentPhoto) {
        wrapped.setStudentPhoto(wrap(studentPhoto));
        return this;
    }
}
