package com.jtbdevelopment.e_eye_o.DAO.helpers;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import com.jtbdevelopment.e_eye_o.entities.utilities.IdObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Date: 11/25/12
 * Time: 7:54 PM
 */
@Service
public class ObservationCategoryHelperImpl implements ObservationCategoryHelper {
    private final ReadWriteDAO dao;
    private final IdObjectFactory objectFactory;

    @Autowired
    public ObservationCategoryHelperImpl(final ReadWriteDAO dao, final IdObjectFactory objectFactory) {
        this.dao = dao;
        this.objectFactory = objectFactory;
    }

    public Set<ObservationCategory> createDefaultCategoriesForUser(final AppUser appUser) {
        List<ObservationCategory> defaults = new ArrayList<>(NEW_USER_DEFAULT_CATEGORIES.size());
        for (Map.Entry<String, String> entry : NEW_USER_DEFAULT_CATEGORIES.entrySet()) {
            defaults.add(objectFactory.newObservationCategory(appUser).setShortName(entry.getKey()).setDescription(entry.getValue()));
        }
        return new HashSet<>(dao.create(defaults));
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
