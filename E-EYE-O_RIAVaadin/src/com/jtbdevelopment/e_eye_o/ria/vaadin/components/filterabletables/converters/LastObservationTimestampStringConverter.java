package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters;

import com.jtbdevelopment.e_eye_o.entities.Observable;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Date: 3/31/13
 * Time: 8:05 AM
 */
@Component
public class LastObservationTimestampStringConverter extends LocalDateTimeStringConverter {
    private static final String NEVER_OBSERVED = "No Observations";

    @Override
    public LocalDateTime convertToModel(final String value, final Locale locale) throws ConversionException {
        if (NEVER_OBSERVED.equals(value)) {
            return Observable.NEVER_OBSERVED;
        }
        return super.convertToModel(value, locale);
    }

    @Override
    public String convertToPresentation(final LocalDateTime value, final Locale locale) throws ConversionException {
        if (value == null || value.equals(Observable.NEVER_OBSERVED)) {
            return NEVER_OBSERVED;
        }
        System.out.println(value.toString());
        return super.convertToPresentation(value, locale);
    }
}
