package com.jtbdevelopment.e_eye_o.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.ClassList;
import com.jtbdevelopment.e_eye_o.entities.Photo;
import com.jtbdevelopment.e_eye_o.entities.Student;

import java.util.HashSet;
import java.util.Set;

/**
 * Date: 11/17/12
 * Time: 2:05 PM
 */
public class ClassListImpl extends ArchivableAppUserOwnedObjectImpl implements ClassList {
    private String description = "";
    private Set<Student> students = new HashSet<>();
    private Set<Photo> photos = new HashSet<>();

    ClassListImpl() {
    }

    ClassListImpl(final AppUser appUser) {
        super(appUser);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public ClassList setDescription(final String description) {
        this.description = description;
        return this;
    }
}
