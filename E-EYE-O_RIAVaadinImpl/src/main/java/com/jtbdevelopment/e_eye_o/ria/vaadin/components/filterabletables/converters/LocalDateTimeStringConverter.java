package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
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
    private static final String DEFAULT_VALUE = "";

    private String format = DEFAULT_FORMAT;

    public void setFormat(final String format) {
        this.format = format;
    }

    @Override
    public LocalDateTime convertToModel(final String value, Class<? extends LocalDateTime> targetType, final Locale locale) throws ConversionException {
        return value == null ? null :
                DEFAULT_VALUE.equals(value) ? IdObject.UNINITIALISED_LOCAL_DATE_TIME :
                        LocalDateTime.parse(value, DateTimeFormat.forPattern(format).withLocale(locale));
    }

    @Override
    public String convertToPresentation(final LocalDateTime value, Class<? extends String> targetType, final Locale locale) throws ConversionException {
        return value == null ? null :
                value.equals(IdObject.UNINITIALISED_LOCAL_DATE_TIME) ? DEFAULT_VALUE :
                        value.toString(format, locale);
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
