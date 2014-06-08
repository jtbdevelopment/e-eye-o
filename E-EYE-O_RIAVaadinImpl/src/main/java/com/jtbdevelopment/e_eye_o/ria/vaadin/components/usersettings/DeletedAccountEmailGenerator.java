package com.jtbdevelopment.e_eye_o.ria.vaadin.components.usersettings;

import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.AbstractEmailGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DeletedAccountEmailGenerator extends AbstractEmailGenerator {
    @Value("${url.root}")
    private String urlRoot;
    @Value("${email.accountdeactivated}")
    private String emailChangeFrom;

    private static final String ACCOUNT_DELETED = "E-EYE-O Account Deletion Confirmation";

    public void generateAccountDeletedEmail(final String emailAddress) {
        final String bodyText = "<html><body>" +
                "Your E-EYE-O login has been deleted.  This is irrevocable." +
                "<p>If you have not made any such request please click the link above and change your password." +
                "</body></html>";
        sendEmail(emailAddress, emailChangeFrom, bodyText, ACCOUNT_DELETED);
    }

    public String getEmailChangeFrom() {
        return emailChangeFrom;
    }
}