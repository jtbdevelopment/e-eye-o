package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters;

import com.vaadin.data.util.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Date: 3/31/13
 * Time: 7:41 AM
 */
//  TODO - possible to size based on width of table?
@Component
public class ShortenedCommentConverter implements Converter<String, String> {
    @Override
    public String convertToModel(final String value, final Locale locale) throws ConversionException {
        return null;
    }

    @Override
    public String convertToPresentation(final String value, final Locale locale) throws ConversionException {
        String shortenedComment = value;
        if (shortenedComment.length() > 70) {
            shortenedComment = shortenedComment.substring(0, 67) + "...";
        }
        return shortenedComment;
    }

    @Override
    public Class<String> getModelType() {
        return String.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}
