package com.jtbdevelopment.e_eye_o.ria.vaadin.components.usersettings;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.AbstractEmailGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ChangePasswordEmailGenerator extends AbstractEmailGenerator {
    @Value("${email.passwordreset}")
    private String resetEmailFrom;

    private static final String EMAIL_ADDRESS_CHANGE = "E-EYE-O Password Change Confirmation";

    public void generatePasswordChangeEmail(final AppUser appUser) {
        final String bodyText = "<html><body>" +
                "Your E-EYE-O password has been changed." +
                "<p>If you have not made any such request please know your existing id is still valid and you should immediately reset the password and you should contact us immediately." +
                "</body></html>";
        sendEmail(appUser.getEmailAddress(), resetEmailFrom, bodyText, EMAIL_ADDRESS_CHANGE);
    }

    public String getResetEmailFrom() {
        return resetEmailFrom;
    }
}