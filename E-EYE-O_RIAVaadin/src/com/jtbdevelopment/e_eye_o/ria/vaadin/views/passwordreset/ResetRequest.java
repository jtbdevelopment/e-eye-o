package com.jtbdevelopment.e_eye_o.ria.vaadin.views.passwordreset;

import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO;
import com.jtbdevelopment.e_eye_o.DAO.helpers.UserHelper;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.ComponentUtils;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Date: 4/7/13
 * Time: 7:42 PM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ResetRequest extends VerticalLayout implements View {
    public static final String VIEW_NAME = "PasswordResetRequest";

    @Autowired
    private ReadOnlyDAO readOnlyDAO;

    @Autowired
    private UserHelper userHelper;

    @PostConstruct
    public void setUp() {
        setMargin(true);
        setSpacing(true);
        setSizeFull();

        Label title = new Label("Password Reset/Account Reactivation");
        addComponent(title);
        setComponentAlignment(title, Alignment.MIDDLE_CENTER);


        FormLayout form = new FormLayout();
        final TextField email = new TextField("Email Address");
        form.addComponent(email);
        addComponent(form);
        setComponentAlignment(form, Alignment.MIDDLE_CENTER);

        Button reset = new Button("Request Reset");
        addComponent(reset);
        reset.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                AppUser appUser = readOnlyDAO.getUser(email.getValue());
                if (appUser == null) {
                    Notification.show(email.getValue() + " doesn't exist.");
                    return;
                }

                TwoPhaseActivity twoPhaseActivity = userHelper.requestResetPassword(appUser);

                //  TODO - actually send mail
                getSession().setAttribute(TwoPhaseActivity.class, twoPhaseActivity);
                getSession().getAttribute(Navigator.class).navigateTo(PostResetRequest.VIEW_NAME);
            }
        });
        setComponentAlignment(reset, Alignment.MIDDLE_CENTER);
        ComponentUtils.setImmediateForAll(this, true);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
}
