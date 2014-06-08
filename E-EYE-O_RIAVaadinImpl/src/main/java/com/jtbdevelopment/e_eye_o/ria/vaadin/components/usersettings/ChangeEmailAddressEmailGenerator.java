package com.jtbdevelopment.e_eye_o.ria.vaadin.components.usersettings;

import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.AbstractEmailGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ChangeEmailAddressEmailGenerator extends AbstractEmailGenerator {
    @Value("${email.emailchange}")
    private String emailChangeFrom;

    private static final String EMAIL_ADDRESS_CHANGE = "E-EYE-O Email Account Change Confirmation";

    public void generateEmailAddressChangeEmail(final String oldEmail, final String newEmail) {
        final String bodyText = "<html><body>" +
                "Your E-EYE-O login email address has been changed from " + oldEmail + " to " + newEmail + "." +
                "<p>If you have not made any such request please know your existing password is still valid for this new account and you should contact us immediately." +
                "</body></html>";
        sendEmail(oldEmail, emailChangeFrom, bodyText, EMAIL_ADDRESS_CHANGE);
        sendEmail(newEmail, emailChangeFrom, bodyText, EMAIL_ADDRESS_CHANGE);
    }

    public String getEmailChangeFrom() {
        return emailChangeFrom;
    }
}