package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters;

import com.vaadin.data.util.converter.Converter;
import org.joda.time.LocalDate;

import java.util.Date;
import java.util.Locale;

/**
 * Date: 11/11/13
 * Time: 3:15 PM
 */
public class LocalDateDateConverter implements Converter<Date, LocalDate> {

    @Override
    public LocalDate convertToModel(final Date value, Class<? extends LocalDate> targetType, final Locale locale) throws ConversionException {
        return value == null ? null : new LocalDate(value);
    }

    @Override
    public Date convertToPresentation(final LocalDate value, Class<? extends Date> targetType, final Locale locale) throws ConversionException {
        return value == null ? null : value.toDate();
    }

    @Override
    public Class<LocalDate> getModelType() {
        return LocalDate.class;
    }

    @Override
    public Class<Date> getPresentationType() {
        return Date.class;
    }
}
