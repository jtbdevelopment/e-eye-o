package com.jtbdevelopment.e_eye_o.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.Semester;
import org.joda.time.LocalDate;

/**
 * Date: 11/11/13
 * Time: 5:51 PM
 */
public class SemesterImpl extends AppUserOwnedObjectImpl implements Semester {
    private String description = "";
    private LocalDate start = new LocalDate();
    private LocalDate end = new LocalDate();

    SemesterImpl(final AppUser appUser) {
        super(appUser);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public LocalDate getStart() {
        return start;
    }

    @Override
    public void setStart(final LocalDate start) {
        this.start = start;
    }

    @Override
    public LocalDate getEnd() {
        return end;
    }

    @Override
    public void setEnd(final LocalDate end) {
        this.end = end;
    }

    @Override
    public String getSummaryDescription() {
        return description;
    }
}
