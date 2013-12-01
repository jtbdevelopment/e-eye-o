package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.Observable
import com.jtbdevelopment.e_eye_o.entities.Observation
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory
import groovy.transform.CompileStatic
import org.joda.time.LocalDateTime

/**
 * Date: 11/27/13
 * Time: 6:42 AM
 */
@CompileStatic
class ObservationGImpl extends AppUserOwnedObjectGImpl implements Observation {
    Observable observationSubject
    LocalDateTime observationTimestamp = new LocalDateTime()
    boolean significant = true
    Set<ObservationCategory> categories = new HashSet<>()
    String comment = ""

    @Override
    void setCategories(final Set<ObservationCategory> categories) {
        this.categories.clear()
        this.categories.addAll(categories)
    }

    @Override
    Set<ObservationCategory> getCategories() {
        return categories.asImmutable()
    }

    @Override
    void removeCategory(final ObservationCategory observationCategory) {
        this.categories.remove(observationCategory)
    }

    @Override
    void addCategories(final Collection<ObservationCategory> observationCategories) {
        this.categories.addAll(observationCategories)
    }

    @Override
    void addCategory(final ObservationCategory observationCategory) {
        this.categories.add(observationCategory)
    }

    @Override
    String getSummaryDescription() {
        List<String> shortNames = categories.findAll({ ObservationCategory oc -> oc != null && oc.shortName != null }).collect { ObservationCategory oc -> oc.shortName.trim() };
        String summary = (observationSubject != null ? observationSubject.summaryDescription : "?") +
                " on " +
                observationTimestamp.toString("YYY-MM-dd") +
                " for " +
                shortNames.join(", ");
        return summary.trim();
    }
}
