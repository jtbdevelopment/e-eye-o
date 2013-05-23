package com.jtbdevelopment.e_eye_o.ria.vaadin.views.passwordreset;

import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
//  TODO - move to core and impl
public class PasswordResetEmailGenerator {
    private static final Logger logger = LoggerFactory.getLogger(PasswordResetEmailGenerator.class);

    @Autowired
    private JavaMailSender mailSender;
    @Value("${url.root}")
    private String urlRoot;
    @Value("${email.passwordreset}")
    private String resetEmailFrom;

    //  TODO - go to template generator
    public void generateEmail(final TwoPhaseActivity activity) {
        try {
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, false);
            helper.setTo(activity.getAppUser().getEmailAddress());
            helper.setFrom(resetEmailFrom);
            helper.setSubject("E-EYE-O Password Reset Confirmation");
            final String bodyText = "<html><body>" +
                    "A request has been made to reset your E-EYE-O password!  To complete the reset, follow this <a href=\"" + urlRoot + "Login#!" + PasswordReset.VIEW_NAME + "/" + activity.getId() + "\">link</a>." +
                    "<p>If you have not made any such request please know your existing password is still valid." +
                    "</body></html>";
            helper.setText(bodyText, true);
            mailSender.send(mail);
            logger.info("Sent to " + activity.getAppUser().getEmailAddress() + " an email " + bodyText);
        } catch (Exception e) {
            logger.warn("Unable to generate email to " + activity.getAppUser().getEmailAddress(), e);
        }
    }

    public String getResetEmailFrom() {
        return resetEmailFrom;
    }
}