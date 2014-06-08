package com.jtbdevelopment.e_eye_o.DAO.helpers

import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO
import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Date: 12/11/13
 * Time: 8:59 PM
 */
@Component
@CompileStatic
class ObservationCategoryHelperGImpl implements ObservationCategoryHelper {
    @Autowired
    protected ReadOnlyDAO readOnlyDAO

    @Override
    Map<String, ObservationCategory> getObservationCategoriesAsMap(final AppUser appUser) {
        Set<ObservationCategory> categories = readOnlyDAO.getEntitiesForUser(ObservationCategory.class, appUser, 0, 0)
        categories.collectEntries { ObservationCategory it -> [(it.shortName): it] }
    }
}
