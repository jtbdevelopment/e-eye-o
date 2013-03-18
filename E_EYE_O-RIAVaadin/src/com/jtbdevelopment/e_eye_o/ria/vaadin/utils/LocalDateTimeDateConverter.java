package com.jtbdevelopment.e_eye_o.ria.vaadin.utils;

import com.vaadin.data.util.converter.Converter;
import org.joda.time.LocalDateTime;

import java.util.Date;
import java.util.Locale;

/**
 * Date: 3/17/13
 * Time: 8:03 PM
 */
public class LocalDateTimeDateConverter implements Converter<Date, LocalDateTime> {

    @Override
    public LocalDateTime convertToModel(final Date value, final Locale locale) throws ConversionException {
        return value == null ? null : new LocalDateTime(value);
    }

    @Override
    public Date convertToPresentation(final LocalDateTime value, final Locale locale) throws ConversionException {
        return value == null ? null : value.toDate();
    }

    @Override
    public Class<LocalDateTime> getModelType() {
        return LocalDateTime.class;
    }

    @Override
    public Class<Date> getPresentationType() {
        return Date.class;
    }
}
