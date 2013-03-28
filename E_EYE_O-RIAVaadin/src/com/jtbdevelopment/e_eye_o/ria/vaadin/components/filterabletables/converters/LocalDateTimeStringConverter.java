package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters;

import com.vaadin.data.util.converter.Converter;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Date: 3/17/13
 * Time: 3:16 PM
 */
@Component
public class LocalDateTimeStringConverter implements Converter<String, LocalDateTime> {
    private static final String DEFAULT_FORMAT = "YYYY-MM-dd HH:mm";

    //  TODO - support non-default formats
    private final String format;

    public LocalDateTimeStringConverter() {
        format = DEFAULT_FORMAT;
    }


    @Override
    public LocalDateTime convertToModel(final String value, final Locale locale) throws ConversionException {
        return value == null ? null : LocalDateTime.parse(value, DateTimeFormat.forPattern(format).withLocale(locale));
    }

    @Override
    public String convertToPresentation(final LocalDateTime value, final Locale locale) throws ConversionException {
        return value == null ? null : value.toString(format, locale);
    }

    @Override
    public Class<LocalDateTime> getModelType() {
        return LocalDateTime.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}
