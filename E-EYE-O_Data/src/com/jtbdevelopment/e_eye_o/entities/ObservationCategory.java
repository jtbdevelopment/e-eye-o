package com.jtbdevelopment.e_eye_o.entities;

import com.jtbdevelopment.e_eye_o.superclasses.AppUserOwnedObject;

import javax.persistence.Entity;
import java.util.*;

/**
 * Date: 11/4/12
 * Time: 9:30 PM
 */
@Entity
public class ObservationCategory extends AppUserOwnedObject {
    private static final Map<String, String> NEW_USER_DEFAULT_CATEGORIES = new HashMap<String, String>() {{
        put("SOCIAL", "Social Skills");
        put("MATH", "Mathematics");
        put("LANG", "Language");
        put("KAUW", "Knowledge & Understanding of the World");
        put("PHYS", "Physical");
        put("IDEA", "Creative");
    }};

    //  List not set as IDs not assigned so they are not yet unique
    public static List<ObservationCategory> createDefaultCategoriesForUser(final AppUser appUser) {
        List<ObservationCategory> defaults = new LinkedList<>();
        for (Map.Entry<String, String> entry : NEW_USER_DEFAULT_CATEGORIES.entrySet()) {
            defaults.add(new ObservationCategory(appUser, entry.getKey(), entry.getValue()));
        }
        return defaults;
    }

    private String shortName = "";
    private String description = "";

    @SuppressWarnings("unused")
    private ObservationCategory() {
        //  For hibernate
    }

    public ObservationCategory(final AppUser appUser) {
        super(appUser);
    }

    public ObservationCategory(final AppUser appUser, final String shortName, final String description) {
        super(appUser);
        this.shortName = shortName;
        this.description = description;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

