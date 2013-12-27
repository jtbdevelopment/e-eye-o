package com.jtbdevelopment.e_eye_o.ria.vaadin.views.registration;

import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO;
import com.jtbdevelopment.e_eye_o.DAO.helpers.UserHelper;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.Logo;
import com.jtbdevelopment.e_eye_o.ria.vaadin.views.LoginView;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

/**
 * Date: 4/7/13
 * Time: 7:11 AM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AccountConfirmationView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "ConfirmAccount";

    @Autowired
    private Logo logo;

    @Autowired
    private ReadOnlyDAO readOnlyDAO;

    @Autowired
    private UserHelper userHelper;
    private AppUser appUserWithExpiredRequest;

    @Autowired
    private RegistrationEmailGenerator registrationEmailGenerator;

    private Button newEmailButton;
    private Label messageText;
    private Link link;

    @PostConstruct
    public void setUp() {
        setSizeFull();
        setMargin(true);
        setSpacing(true);

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeUndefined();

        layout.addComponent(logo);
        layout.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);

        Label title = new Label("<H2>Account Confirmation</H2>", ContentMode.HTML);
        title.setSizeUndefined();
        layout.addComponent(title);
        layout.setComponentAlignment(title, Alignment.MIDDLE_CENTER);

        messageText = new Label("");
        messageText.setSizeUndefined();
        layout.addComponent(messageText);
        layout.setComponentAlignment(messageText, Alignment.MIDDLE_CENTER);

        newEmailButton = new Button("Generate Another Activation Request.");
        newEmailButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                newEmailButton.setEnabled(false);
                if (appUserWithExpiredRequest != null) {
                    TwoPhaseActivity activity = userHelper.generateActivationRequest(appUserWithExpiredRequest);
                    registrationEmailGenerator.generateEmail(activity);
                    appUserWithExpiredRequest = null;
                    getSession().setAttribute(TwoPhaseActivity.class, activity);
                    getSession().getAttribute(Navigator.class).navigateTo(PostRegistrationView.VIEW_NAME);
                }
            }
        });
        layout.addComponent(newEmailButton);
        layout.setComponentAlignment(newEmailButton, Alignment.MIDDLE_CENTER);

        link = new Link();
        layout.addComponent(link);
        layout.setComponentAlignment(link, Alignment.MIDDLE_CENTER);

        addComponent(layout);
        setComponentAlignment(layout, Alignment.MIDDLE_CENTER);
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
        String id = event.getParameters();
        appUserWithExpiredRequest = null;
        newEmailButton.setEnabled(false);
        newEmailButton.setVisible(false);
        link.setVisible(false);
        link.setEnabled(false);
        if (!StringUtils.hasLength(id)) {
            getSession().getAttribute(Navigator.class).navigateTo(LoginView.VIEW_NAME);
            return;
        }

        TwoPhaseActivity activity = readOnlyDAO.get(TwoPhaseActivity.class, id);
        if (activity == null) {
            messageText.setValue("This is embarrassing.  We can't find your open account activation.");
            //  TODO - more instructions on what to do next
            return;
        }

        if (!TwoPhaseActivity.Activity.ACCOUNT_ACTIVATION.equals(activity.getActivityType())) {
            messageText.setValue("This is embarrassing.  We can't find an open account activation.");
            //  TODO - more instructions on what to do next
            return;
        }

        if (activity.isArchived()) {
            messageText.setValue("This was previously completed.  You should try logging in.  Or try resetting your password.");
            link.setEnabled(true);
            link.setVisible(true);
            link.setResource(new ExternalResource("#!" + LoginView.VIEW_NAME));
            link.setCaption("Return to Login Screen");
            return;
        }

        if (new DateTime().compareTo(activity.getExpirationTime()) > 0) {
            messageText.setValue("Sorry - this activation has expired.  Press the button to generate a new one.");
            appUserWithExpiredRequest = activity.getAppUser();
            newEmailButton.setVisible(true);
            newEmailButton.setEnabled(true);
            return;
        }

        userHelper.activateUser(activity);

        messageText.setValue("Congratulations.  " + activity.getAppUser().getEmailAddress() + " has been activated.  Try logging in now.");
        link.setEnabled(true);
        link.setVisible(true);
        link.setResource(new ExternalResource("#!" + LoginView.VIEW_NAME));
        link.setCaption("Go to Login Screen");
    }
}
