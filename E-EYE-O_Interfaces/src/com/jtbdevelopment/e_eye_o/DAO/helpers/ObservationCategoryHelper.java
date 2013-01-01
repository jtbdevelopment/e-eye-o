package com.jtbdevelopment.e_eye_o.DAO.helpers;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Date: 11/25/12
 * Time: 7:54 PM
 */
@Service
public interface ObservationCategoryHelper {
    public static final Map<String, String> NEW_USER_DEFAULT_CATEGORIES = new HashMap<String, String>() {{
        put("SOCIAL", "Social Skills");
        put("MATH", "Mathematics");
        put("LANG", "Language");
        put("KAUW", "Knowledge & Understanding of the World");
        put("PHYS", "Physical");
        put("IDEA", "Creative");
    }};

    public Set<ObservationCategory> createDefaultCategoriesForUser(final AppUser appUser);

    public Map<String, ObservationCategory> getObservationCategoriesAsMap(final AppUser appUser);
}
