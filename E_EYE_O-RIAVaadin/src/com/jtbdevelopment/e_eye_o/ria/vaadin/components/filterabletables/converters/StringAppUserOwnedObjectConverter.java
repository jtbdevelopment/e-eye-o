package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters;

import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.vaadin.data.util.converter.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Date: 3/28/13
 * Time: 12:07 AM
 */
@Component
public class StringAppUserOwnedObjectConverter implements Converter<String, AppUserOwnedObject> {
    @Autowired
    private ReadOnlyDAO readOnlyDAO;

    @Override
    public AppUserOwnedObject convertToModel(final String value, final Locale locale) throws ConversionException {
        //  TODO
        return null;
    }

    @Override
    public String convertToPresentation(final AppUserOwnedObject value, final Locale locale) throws ConversionException {
        return value == null ? null : value.getSummaryDescription();
    }

    @Override
    public Class<AppUserOwnedObject> getModelType() {
        return AppUserOwnedObject.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}
