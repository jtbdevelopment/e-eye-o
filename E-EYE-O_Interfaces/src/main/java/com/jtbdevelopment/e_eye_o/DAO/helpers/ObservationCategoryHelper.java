package com.jtbdevelopment.e_eye_o.DAO.helpers;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;

import java.util.Map;

/**
 * Date: 11/25/12
 * Time: 7:54 PM
 * <p/>
 * Observation Category Helpers.
 */
public interface ObservationCategoryHelper {
    /**
     * Retrieves observation categories from DAO and organizes them into a nice map of short code to OC
     *
     * @param appUser user to get OCs for
     * @return map of OC.shortCode to OC
     */
    public Map<String, ObservationCategory> getObservationCategoriesAsMap(final AppUser appUser);
}
