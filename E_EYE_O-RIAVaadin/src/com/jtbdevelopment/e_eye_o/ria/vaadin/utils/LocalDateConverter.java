package com.jtbdevelopment.e_eye_o.ria.vaadin.utils;

import com.vaadin.data.util.converter.Converter;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.util.Locale;

/**
 * Date: 3/17/13
 * Time: 3:16 PM
 */
public class LocalDateConverter implements Converter<String, LocalDate> {
    private static final String DEFAULT_FORMAT = "YYYY-MM-dd";

    //  TODO - support non-default formats
    private final String format;

    public LocalDateConverter() {
        format = DEFAULT_FORMAT;
    }


    @Override
    public LocalDate convertToModel(final String value, final Locale locale) throws ConversionException {
        return value == null ? null : LocalDate.parse(value, DateTimeFormat.forPattern(format));
    }

    @Override
    public String convertToPresentation(final LocalDate value, final Locale locale) throws ConversionException {
        return value == null ? null : value.toString(format);
    }

    @Override
    public Class<LocalDate> getModelType() {
        return LocalDate.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}
