package com.jtbdevelopment.e_eye_o.ria.vaadin.utils;

import com.vaadin.data.util.converter.Converter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Locale;

/**
 * Date: 3/16/13
 * Time: 6:30 PM
 */
public class DateTimeStringConverter implements Converter<String, DateTime> {
    private static final String DEFAULT_FORMAT = "YYYY-MM-dd HH:mm";

    //  TODO - support non-default formats
    private final String format;

    public DateTimeStringConverter() {
        format = DEFAULT_FORMAT;
    }

    @Override
    public DateTime convertToModel(final String value, final Locale locale) throws ConversionException {
        return value == null ? null : DateTime.parse(value, DateTimeFormat.forPattern(format).withLocale(locale));
    }

    @Override
    public String convertToPresentation(final DateTime value, final Locale locale) throws ConversionException {
        return value == null ? null : value.toString(format, locale);
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
