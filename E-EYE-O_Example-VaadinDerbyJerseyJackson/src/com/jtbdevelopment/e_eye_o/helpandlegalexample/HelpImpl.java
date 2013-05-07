package com.jtbdevelopment.e_eye_o.helpandlegalexample;

import com.jtbdevelopment.e_eye_o.helpandlegal.Help;
import org.springframework.stereotype.Component;

/**
 * Date: 5/7/13
 * Time: 10:31 AM
 */
@Component
@SuppressWarnings("unused")
public class HelpImpl implements Help {
    private static final String LABEL = "General Help";
    //  TODO - actual help
    private static final String TEXT =
            "<H2><CENTER>Help</H2></CENTER>" +
                    "<p>Not implemented yet.";

    @Override
    public String getLabel() {
        return LABEL;
    }

    @Override
    public String getText() {
        return TEXT;
    }
}
