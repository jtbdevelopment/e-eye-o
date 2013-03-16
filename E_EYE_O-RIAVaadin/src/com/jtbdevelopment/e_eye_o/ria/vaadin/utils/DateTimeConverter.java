package com.jtbdevelopment.e_eye_o.ria.vaadin.utils;

import com.vaadin.data.util.converter.Converter;
import org.joda.time.DateTime;

import java.util.Locale;

/**
 * Date: 3/16/13
 * Time: 6:30 PM
 */
public class DateTimeConverter implements Converter<String, DateTime> {
    private static final String DEFAULT_FORMAT = "YYYY-MM-dd HH:mm";

    private final String format;

    public DateTimeConverter() {
        format = DEFAULT_FORMAT;
    }

    @Override
    public DateTime convertToModel(final String value, final Locale locale) throws ConversionException {
        return null;
    }

    @Override
    public String convertToPresentation(final DateTime value, final Locale locale) throws ConversionException {
        return value.toString(format);
    }

    @Override
    public Class<DateTime> getModelType() {
        return DateTime.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}
