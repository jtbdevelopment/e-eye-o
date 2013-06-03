package com.jtbdevelopment.e_eye_o.DAO.helpers;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;

import java.util.Map;
import java.util.Set;

/**
 * Date: 11/25/12
 * Time: 7:54 PM
 * <p/>
 * Observation Category Helpers.
 */
public interface ObservationCategoryHelper {
    /**
     * Generates default categories for a user and saves them to DAO
     *
     * @param appUser user to create defaults for
     * @return returns generated and saved values
     */
    public Set<ObservationCategory> createDefaultCategoriesForUser(final AppUser appUser);

    /**
     * Retrieves observation categories from DAO and organizes them into a nice map of short code to OC
     *
     * @param appUser user to get OCs for
     * @return map of OC.shortCode to OC
     */
    public Map<String, ObservationCategory> getObservationCategoriesAsMap(final AppUser appUser);
}
