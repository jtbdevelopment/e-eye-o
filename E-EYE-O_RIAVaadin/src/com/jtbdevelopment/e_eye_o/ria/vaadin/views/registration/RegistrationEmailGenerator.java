package com.jtbdevelopment.e_eye_o.ria.vaadin.views.registration;

import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.AbstractEmailGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
//  TODO - move to core and impl
public class RegistrationEmailGenerator extends AbstractEmailGenerator {

    @Value("${email.registrationverification}")
    private String registrationEmailFrom;
    @Value("${url.root}")
    private String urlRoot;

    private static final String SUBJECT = "E-EYE-O Registration Confirmation";

    //  TODO - go to template generator
    public void generateEmail(final TwoPhaseActivity activity) {
        final String bodyText = "<html><body>" +
                "Thank you for registering with E-EYE-O!  To complete your sign-up, please follow this <a href=\"" + urlRoot + "Login#!" + AccountConfirmationView.VIEW_NAME + "/" + activity.getId() + "\">link</a>." +
                "</body></html>";
        sendEmail(activity, registrationEmailFrom, bodyText, SUBJECT);
    }

    public String getRegistrationEmailFrom() {
        return registrationEmailFrom;
    }
}