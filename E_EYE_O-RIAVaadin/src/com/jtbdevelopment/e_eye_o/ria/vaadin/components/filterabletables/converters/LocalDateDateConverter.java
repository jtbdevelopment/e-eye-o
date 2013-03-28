package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters;

import com.vaadin.data.util.converter.Converter;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Locale;

/**
 * Date: 3/17/13
 * Time: 8:06 PM
 */
@Component
public class LocalDateDateConverter implements Converter<Date, LocalDate> {
    @Override
    public LocalDate convertToModel(final Date value, final Locale locale) throws ConversionException {
        return value == null ? null : new LocalDate(value);
    }

    @Override
    public Date convertToPresentation(final LocalDate value, final Locale locale) throws ConversionException {
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
