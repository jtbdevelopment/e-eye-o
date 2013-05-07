package com.jtbdevelopment.e_eye_o.ria.vaadin.views.registration;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.Logo;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Date: 4/6/13
 * Time: 7:36 PM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//  TODO - make pretty
public class PostRegistrationView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "PostRegistration";
    private Label action;
    private Label dummy;

    @Autowired
    private Logo logo;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${email.registrationverification}")
    private String registrationEmailFrom;

    @Value("${url.root}")
    private String urlRoot;

    @PostConstruct
    public void setUp() {
        setSizeFull();
        setMargin(true);
        setSpacing(true);

        //  TODO - actually send email
        action = new Label("An email will be sent");

        dummy = new Label("");

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeUndefined();
        verticalLayout.addComponent(logo);
        verticalLayout.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
        verticalLayout.addComponent(action);
        verticalLayout.setComponentAlignment(action, Alignment.MIDDLE_CENTER);
        verticalLayout.addComponent(dummy);
        verticalLayout.setComponentAlignment(dummy, Alignment.MIDDLE_CENTER);


        addComponent(verticalLayout);
        setComponentAlignment(verticalLayout, Alignment.MIDDLE_CENTER);
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
        TwoPhaseActivity twoPhaseActivity = getSession().getAttribute(TwoPhaseActivity.class);
        final AppUser appUser = twoPhaseActivity.getAppUser();
        action.setValue("Welcome aboard " + appUser.getFirstName() + "!  An email has been sent to " + appUser.getEmailAddress() + " to verify it.  Please follow the instructions to activate your account.");
        dummy.setValue(twoPhaseActivity.getSummaryDescription());
        //  TODO - get rid of me
        Link link = new Link("Click", new ExternalResource("#!" + AccountConfirmationView.VIEW_NAME + "/" + twoPhaseActivity.getId()));
        addComponent(link);
        generateEmail(twoPhaseActivity);
    }

    //  TODO - got to template generator
    private void generateEmail(final TwoPhaseActivity activity) {
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

}
