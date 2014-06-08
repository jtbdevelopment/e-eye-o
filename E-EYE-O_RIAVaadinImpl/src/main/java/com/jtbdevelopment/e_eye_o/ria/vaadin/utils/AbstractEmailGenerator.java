package com.jtbdevelopment.e_eye_o.ria.vaadin.utils;

import com.jtbdevelopment.e_eye_o.ria.vaadin.views.registration.RegistrationEmailGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;

/**
 * Date: 6/8/13
 * Time: 3:55 PM
 */
public abstract class AbstractEmailGenerator {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationEmailGenerator.class);
    @Autowired
    private JavaMailSender mailSender;

    protected void sendEmail(final String emailTo, final String emailFrom, final String bodyText, final String subject) {
        try {
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, false);
            helper.setTo(emailTo);
            helper.setFrom(emailFrom);
            helper.setSubject(subject);
            helper.setText(bodyText, true);
            logger.info(bodyText);
            mailSender.send(mail);
            logger.info("Sent to " + emailTo + " an email " + bodyText);
        } catch (Exception e) {
            logger.warn("Unable to generate email to " + emailTo + " with " + bodyText + " and subject " + subject, e);
        }
    }
}
