package com.jtbdevelopment.e_eye_o.DAO.helpers;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * Date: 11/25/12
 * Time: 7:54 PM
 */
@Component
public class ObservationCategoryHelperImpl implements ObservationCategoryHelper {
    @Autowired
    protected ReadWriteDAO dao;

    @Autowired
    protected IdObjectFactory objectFactory;

    @Resource(name = "newUserDefaultObservationCategories")
    Map<String, String> newUserDefaultObservationCategories;

    public Set<ObservationCategory> createDefaultCategoriesForUser(final AppUser appUser) {
        List<ObservationCategory> defaults = new ArrayList<>(newUserDefaultObservationCategories.size());
        for (Map.Entry<String, String> entry : newUserDefaultObservationCategories.entrySet()) {
            defaults.add(dao.create(objectFactory.newObservationCategoryBuilder(appUser).withShortName(entry.getKey()).withDescription(entry.getValue()).build()));
        }
        return new HashSet<>(defaults);
    }

    public Map<String, ObservationCategory> getObservationCategoriesAsMap(final AppUser appUser) {
        Set<ObservationCategory> ocs = dao.getEntitiesForUser(ObservationCategory.class, appUser, 0, 0);
        Map<String, ObservationCategory> map = new HashMap<>();
        for (ObservationCategory oc : ocs) {
            map.put(oc.getShortName(), oc);
        }
        return map;
    }
}
