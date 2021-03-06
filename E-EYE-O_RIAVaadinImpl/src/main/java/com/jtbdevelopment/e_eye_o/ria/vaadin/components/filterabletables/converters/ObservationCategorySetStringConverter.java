package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.gwt.thirdparty.guava.common.base.Function;
import com.google.gwt.thirdparty.guava.common.collect.Collections2;
import com.jtbdevelopment.e_eye_o.DAO.helpers.ObservationCategoryHelper;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Date: 3/28/13
 * Time: 12:13 AM
 */
@Component
public class ObservationCategorySetStringConverter implements Converter<String, Set> {
    private static final Logger logger = LoggerFactory.getLogger(ObservationCategorySetStringConverter.class);

    @Autowired
    private ObservationCategoryHelper observationCategoryHelper;

    @Override
    public Set<ObservationCategory> convertToModel(final String value, Class<? extends Set> targetType, final Locale locale) throws ConversionException {

        Set<ObservationCategory> results = new HashSet<>();

        Map<String, ObservationCategory> map = observationCategoryHelper.getObservationCategoriesAsMap(UI.getCurrent().getSession().getAttribute(AppUser.class));
        for (String shortCode : Splitter.on(",").trimResults().omitEmptyStrings().split(value)) {
            ObservationCategory category = map.get(shortCode);
            if (category != null) {
                results.add(category);
            } else {
                logger.warn(UI.getCurrent().getSession().getAttribute(AppUser.class).getId() + ": unable to find observation category " + shortCode);
                Notification.show("Category " + shortCode + " seems to have disappeared.");
            }
        }
        return results;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String convertToPresentation(final Set value, Class<? extends String> targetType, final Locale locale) throws ConversionException {
        Set<ObservationCategory> ocs = (Set<ObservationCategory>) value;
        return Joiner.on(", ").skipNulls().join(Collections2.transform(ocs, new Function<ObservationCategory, String>() {
            @Override
            public String apply(@Nullable final ObservationCategory observationCategory) {
                return observationCategory == null ? null : observationCategory.getShortName();
            }
        }));
    }

    @Override
    public Class<Set> getModelType() {
        return Set.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}
