package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Date: 3/28/13
 * Time: 12:19 AM
 */
//  TODO - this is crap - really should convert table objects to do init on post construct callback instead of constructor
@Component
public class ConverterCollection {

    @Autowired
    private BooleanToYesNoConverter booleanToYesNoConverter;
    @Autowired
    private DateTimeStringConverter dateTimeStringConverter;
    @Autowired
    private LocalDateDateConverter localDateDateConverter;
    @Autowired
    private LocalDateStringConverter localDateStringConverter;
    @Autowired
    private LocalDateTimeDateConverter localDateTimeDateConverter;
    @Autowired
    private StringAppUserOwnedObjectConverter stringAppUserOwnedObjectConverter;
    @Autowired
    private StringObservationCategorySetConverter stringObservationCategorySetConverter;

    public BooleanToYesNoConverter getBooleanToYesNoConverter() {
        return booleanToYesNoConverter;
    }

    public DateTimeStringConverter getDateTimeStringConverter() {
        return dateTimeStringConverter;
    }

    public LocalDateDateConverter getLocalDateDateConverter() {
        return localDateDateConverter;
    }

    public LocalDateStringConverter getLocalDateStringConverter() {
        return localDateStringConverter;
    }

    public StringAppUserOwnedObjectConverter getStringAppUserOwnedObjectConverter() {
        return stringAppUserOwnedObjectConverter;
    }

    public StringObservationCategorySetConverter getStringObservationCategorySetConverter() {
        return stringObservationCategorySetConverter;
    }

    public LocalDateTimeDateConverter getLocalDateTimeDateConverter() {
        return localDateTimeDateConverter;
    }
}
