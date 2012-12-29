package com.jtbdevelopment.e_eye_o.DAO.helpers;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import com.jtbdevelopment.e_eye_o.entities.impl.ObservationCategoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Date: 11/25/12
 * Time: 7:54 PM
 */
@Service
public class ObservationCategoryHelper {
    private ReadWriteDAO dao;

    @Autowired
    public ObservationCategoryHelper(final ReadWriteDAO dao) {
        this.dao = dao;
    }

    public static final Map<String, String> NEW_USER_DEFAULT_CATEGORIES = new HashMap<String, String>() {{
        put("SOCIAL", "Social Skills");
        put("MATH", "Mathematics");
        put("LANG", "Language");
        put("KAUW", "Knowledge & Understanding of the World");
        put("PHYS", "Physical");
        put("IDEA", "Creative");
    }};

    //  List not set as IDs not assigned so they are not yet unique
    public Set<ObservationCategory> createDefaultCategoriesForUser(final AppUser appUser) {
        Set<ObservationCategory> defaults = new HashSet<>();
        for (Map.Entry<String, String> entry : NEW_USER_DEFAULT_CATEGORIES.entrySet()) {
            defaults.add(dao.create(new ObservationCategoryImpl(appUser).setShortName(entry.getKey()).setDescription(entry.getValue())));
        }
        return defaults;
    }

    public Map<String, ObservationCategory> getObservationCategoriesAsMap(final AppUser appUser) {
        Set<ObservationCategory> ocs = dao.getEntitiesForUser(ObservationCategory.class, appUser);
        Map<String, ObservationCategory> map = new HashMap<>();
        for (ObservationCategory oc : ocs) {
            map.put(oc.getShortName(), oc);
        }
        return map;
    }
}
