package com.jtbdevelopment.e_eye_o.entities.impl;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.Observable;
import com.jtbdevelopment.e_eye_o.entities.Observation;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import org.joda.time.LocalDateTime;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Date: 11/4/12
 * Time: 7:26 PM
 */
public class ObservationImpl extends AppUserOwnedObjectImpl implements Observation {
    private String comment = "";
    private LocalDateTime observationTimestamp = new LocalDateTime();
    private Observable observationSubject;
    private boolean significant = false;

    private Set<ObservationCategory> categories = new HashSet<>();

    ObservationImpl(final AppUser appUser) {
        super(appUser);
    }

    @Override
    public Observable getObservationSubject() {
        return observationSubject;
    }

    @Override
    public void setObservationSubject(final Observable observationSubject) {
        this.observationSubject = observationSubject;
    }

    @Override
    public LocalDateTime getObservationTimestamp() {
        return observationTimestamp;
    }

    @Override
    public void setObservationTimestamp(final LocalDateTime observationDate) {
        this.observationTimestamp = observationDate;
    }

    @Override
    public boolean isSignificant() {
        return significant;
    }

    @Override
    public void setSignificant(final boolean significant) {
        this.significant = significant;
    }

    @Override
    public Set<ObservationCategory> getCategories() {
        return Collections.unmodifiableSet(categories);
    }

    @Override
    public void setCategories(final Set<ObservationCategory> categories) {
        this.categories = new HashSet<>(categories);
    }

    @Override
    public void addCategory(final ObservationCategory observationCategory) {
        categories.add(observationCategory);
    }

    @Override
    public void addCategories(final Collection<ObservationCategory> observationCategories) {
        categories.addAll(observationCategories);
    }

    @Override
    public void removeCategory(final ObservationCategory observationCategory) {
        categories.remove(observationCategory);
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public void setComment(final String comment) {
        this.comment = comment;
    }

    @Override
    public String getSummaryDescription() {
        return ((observationSubject != null ? observationSubject.getSummaryDescription() : "?")
                + " on "
                + observationTimestamp.toString("YYY-MM-dd")
                + " for "
                + Joiner.on(", ").skipNulls().join(Collections2.transform(categories, new Function<ObservationCategory, String>() {
            @Nullable
            @Override
            public String apply(@Nullable final ObservationCategory input) {
                return input != null ? (input.getShortName() != null ? input.getShortName().trim() : null) : null;
            }
        }))
        ).trim();
    }
}
