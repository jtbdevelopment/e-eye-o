package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters;

import com.vaadin.data.util.converter.Converter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Date: 3/16/13
 * Time: 6:30 PM
 */
@Component
public class DateTimeStringConverter implements Converter<String, DateTime> {
    private static final String DEFAULT_FORMAT = "YYYY-MM-dd HH:mm";

    private String format = DEFAULT_FORMAT;

    public void setFormat(final String format) {
        this.format = format;
    }

    @Override
    public DateTime convertToModel(final String value, Class<? extends DateTime> targetType, final Locale locale) throws ConversionException {
        return value == null ? null : DateTime.parse(value, DateTimeFormat.forPattern(format).withLocale(locale));
    }

    @Override
    public String convertToPresentation(final DateTime value, Class<? extends String> targetType, final Locale locale) throws ConversionException {
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
