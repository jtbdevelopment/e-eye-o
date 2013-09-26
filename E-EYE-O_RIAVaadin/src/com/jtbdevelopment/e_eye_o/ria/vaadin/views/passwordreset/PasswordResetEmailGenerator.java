package com.jtbdevelopment.e_eye_o.ria.vaadin.views.passwordreset;

import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.AbstractEmailGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
//  TODO - move to core and impl
public class PasswordResetEmailGenerator extends AbstractEmailGenerator {
    @Value("${url.root}")
    private String urlRoot;
    @Value("${email.passwordreset}")
    private String resetEmailFrom;

    private static final String PASSWORD_RESET_SUBJECT = "E-EYE-O Password Reset Confirmation";

    //  TODO - go to template generator
    public void generatePasswordResetEmail(final TwoPhaseActivity activity) {
        final String bodyText = "<html><body>" +
                "A request has been made to reset your E-EYE-O password!  To complete the reset, follow this <a href=\"" + urlRoot + "Login#!" + PasswordReset.VIEW_NAME + "/" + activity.getId() + "\">link</a>." +
                "<p>If you have not made any such request please know your existing password is still valid." +
                "</body></html>";
        sendEmail(activity.getAppUser().getEmailAddress(), resetEmailFrom, bodyText, PASSWORD_RESET_SUBJECT);
    }

    public String getResetEmailFrom() {
        return resetEmailFrom;
    }
}