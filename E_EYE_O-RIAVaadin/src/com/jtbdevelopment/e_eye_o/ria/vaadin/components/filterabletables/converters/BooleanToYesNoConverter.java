package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters;

import com.vaadin.data.util.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Date: 3/16/13
 * Time: 6:33 PM
 */
@Component
public class BooleanToYesNoConverter implements Converter<String, Boolean> {

    private static final String YES = "Yes";
    private static final String NO = "No";

    @Override
    public Boolean convertToModel(final String value, final Locale locale) throws ConversionException {
        return YES.equalsIgnoreCase(value) ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public String convertToPresentation(final Boolean value, final Locale locale) throws ConversionException {
        return value ? YES : NO;
    }

    @Override
    public Class<Boolean> getModelType() {
        return Boolean.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}