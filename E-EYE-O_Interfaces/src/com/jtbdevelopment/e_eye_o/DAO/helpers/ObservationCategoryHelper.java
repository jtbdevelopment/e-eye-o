package com.jtbdevelopment.e_eye_o.DAO.helpers;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Date: 11/25/12
 * Time: 7:54 PM
 * <p/>
 * Observation Category Helpers.
 */
public interface ObservationCategoryHelper {
    public static final Map<String, String> NEW_USER_DEFAULT_CATEGORIES = new HashMap<String, String>() {{
        put("PSE", "Personal, Social & Emotional");
        put("MD", "Mathematics");
        put("CLL", "Communication, Language & Literacy");
        put("KUW", "Knowledge & Understanding of the World");
        put("PD", "Physical");
        put("CD", "Creative");
        put("RD", "Religious");
    }};

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
