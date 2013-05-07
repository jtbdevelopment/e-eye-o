package com.jtbdevelopment.e_eye_o.ria.vaadin.views.registration;

import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
//  TODO - move to core and impl
public class RegistrationEmailGenerator {
    @Autowired
    private JavaMailSender mailSender;
    @Value("${email.registrationverification}")
    private String registrationEmailFrom;
    @Value("${url.root}")
    private String urlRoot;

    //  TODO - go to template generator
    public void generateEmail(final TwoPhaseActivity activity) {
        try {
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, false);
            helper.setTo(activity.getAppUser().getEmailAddress());
            helper.setFrom(registrationEmailFrom);
            helper.setSubject("E-EYE-O Registration Confirmation");
            helper.setText("<html><body>" +
                    "Thank you for registering with E-EYE-O!  To complete your sign-up, please follow this <a href=\"" + urlRoot + "Login#!" + AccountConfirmationView.VIEW_NAME + "/" + activity.getId() + "\">link</a>." +
                    "</body></html>", true);
            mailSender.send(mail);
        } catch (MessagingException e) {
            //  TODO
        }
    }

    public String getRegistrationEmailFrom() {
        return registrationEmailFrom;
    }
}