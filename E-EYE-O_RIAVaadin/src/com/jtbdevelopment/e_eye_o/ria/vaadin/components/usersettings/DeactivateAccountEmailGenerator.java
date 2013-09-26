package com.jtbdevelopment.e_eye_o.ria.vaadin.components.usersettings;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.AbstractEmailGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DeactivateAccountEmailGenerator extends AbstractEmailGenerator {
    @Value("${url.root}")
    private String urlRoot;
    @Value("${email.accountdeactivated}")
    private String emailChangeFrom;

    private static final String ACCOUNT_DEACTIVATED = "E-EYE-O Account Deactivated Confirmation";

    public void generateAccountDeactivatedEmail(final AppUser appUser) {
        final String bodyText = "<html><body>" +
                "Your E-EYE-O login has been disabled.  To re-activate, go to this <a href=\"" + urlRoot + "\">link</a> and reset your password to re-activate." +
                "<p>If you have not made any such request please click the link above and change your password." +
                "</body></html>";
        sendEmail(appUser.getEmailAddress(), emailChangeFrom, bodyText, ACCOUNT_DEACTIVATED);
    }

    public String getEmailChangeFrom() {
        return emailChangeFrom;
    }
}