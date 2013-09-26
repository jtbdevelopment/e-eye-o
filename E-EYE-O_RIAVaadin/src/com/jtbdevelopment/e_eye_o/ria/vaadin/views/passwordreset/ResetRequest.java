package com.jtbdevelopment.e_eye_o.ria.vaadin.views.passwordreset;

import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO;
import com.jtbdevelopment.e_eye_o.DAO.helpers.UserHelper;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.Logo;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.ComponentUtils;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
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
    private Logo logo;

    @Autowired
    private PasswordResetEmailGenerator passwordResetEmailGenerator;

    @Autowired
    private ReadOnlyDAO readOnlyDAO;

    @Autowired
    private UserHelper userHelper;

    @PostConstruct
    public void setUp() {
        setMargin(true);
        setSpacing(true);
        setSizeFull();

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeUndefined();

        layout.addComponent(logo);
        layout.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);

        Label title = new Label("<H2>Password Reset / Account Reactivation</H2", ContentMode.HTML);
        title.setSizeUndefined();
        layout.addComponent(title);
        layout.setComponentAlignment(title, Alignment.MIDDLE_CENTER);


        FormLayout form = new FormLayout();
        final TextField email = new TextField("Email Address");
        form.addComponent(email);
        form.setSizeUndefined();
        layout.addComponent(form);
        layout.setComponentAlignment(form, Alignment.MIDDLE_CENTER);

        Button reset = new Button("Request Reset");
        layout.addComponent(reset);
        reset.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                AppUser appUser = readOnlyDAO.getUser(email.getValue());
                if (appUser == null) {
                    Notification.show(email.getValue() + " doesn't exist.");
                    return;
                }

                TwoPhaseActivity twoPhaseActivity;
                try {
                    twoPhaseActivity = userHelper.requestResetPassword(appUser);
                } catch (UserHelper.EmailChangeTooRecent e) {
                    Notification.show("Email was changed too recently to also change password.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                passwordResetEmailGenerator.generatePasswordResetEmail(twoPhaseActivity);
                getSession().setAttribute(TwoPhaseActivity.class, twoPhaseActivity);
                getSession().getAttribute(Navigator.class).navigateTo(PostResetRequest.VIEW_NAME);
            }
        });
        layout.setComponentAlignment(reset, Alignment.MIDDLE_CENTER);

        addComponent(layout);
        setComponentAlignment(layout, Alignment.MIDDLE_CENTER);
        ComponentUtils.setImmediateForAll(this, true);
        ComponentUtils.setTextFieldWidths(this, 20, Unit.EM);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
}
