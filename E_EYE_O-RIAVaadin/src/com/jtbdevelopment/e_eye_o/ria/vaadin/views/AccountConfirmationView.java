package com.jtbdevelopment.e_eye_o.ria.vaadin.views;

import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO;
import com.jtbdevelopment.e_eye_o.DAO.helpers.UserHelper;
import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
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
    private ReadOnlyDAO readOnlyDAO;

    @Autowired
    private UserHelper userHelper;

    private Button newEmailButton;
    private Label messageText;
    private Link link;

    @PostConstruct
    public void setUp() {
        setSizeFull();
        setMargin(true);
        setSpacing(false);

        Label title = new Label("Account Confirmation");
        title.setSizeUndefined();
        addComponent(title);
        setComponentAlignment(title, Alignment.MIDDLE_CENTER);

        messageText = new Label("");
        messageText.setSizeUndefined();
        addComponent(messageText);
        setComponentAlignment(messageText, Alignment.MIDDLE_CENTER);

        newEmailButton = new Button("Generate Another Email.");
        newEmailButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //  TODO - generate another activity and email
            }
        });
        addComponent(newEmailButton);
        setComponentAlignment(newEmailButton, Alignment.MIDDLE_CENTER);

        link = new Link();
        addComponent(link);
        setComponentAlignment(link, Alignment.MIDDLE_CENTER);
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
        String id = event.getParameters();
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

        if (activity.isArchived()) {
            messageText.setValue("This was already completed.  You should try logging in.");
            link.setEnabled(true);
            link.setVisible(true);
            link.setResource(new ExternalResource("#!" + LoginView.VIEW_NAME));
            link.setCaption("Login.");
            return;
        }

        if (new DateTime().compareTo(activity.getExpirationTime()) > 0) {
            messageText.setValue("Sorry - this activation has expired.  Press the button to generate a new one.");
            newEmailButton.setVisible(true);
            newEmailButton.setEnabled(true);
            return;
        }

        userHelper.activateUser(activity);

        messageText.setValue("Congratulations.  " + activity.getAppUser().getEmailAddress() + " has been activated.  Try logging in now.");
        link.setEnabled(true);
        link.setVisible(true);
        link.setResource(new ExternalResource("#!" + LoginView.VIEW_NAME));
        link.setCaption("Login.");
    }
}
