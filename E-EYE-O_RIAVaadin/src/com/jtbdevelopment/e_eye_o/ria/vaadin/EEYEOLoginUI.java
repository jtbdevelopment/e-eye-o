package com.jtbdevelopment.e_eye_o.ria.vaadin;

import com.jtbdevelopment.e_eye_o.ria.vaadin.views.LoginView;
import com.jtbdevelopment.e_eye_o.ria.vaadin.views.passwordreset.PasswordReset;
import com.jtbdevelopment.e_eye_o.ria.vaadin.views.passwordreset.PostResetRequest;
import com.jtbdevelopment.e_eye_o.ria.vaadin.views.passwordreset.ResetRequest;
import com.jtbdevelopment.e_eye_o.ria.vaadin.views.registration.AccountConfirmationView;
import com.jtbdevelopment.e_eye_o.ria.vaadin.views.registration.LegalView;
import com.jtbdevelopment.e_eye_o.ria.vaadin.views.registration.PostRegistrationView;
import com.jtbdevelopment.e_eye_o.ria.vaadin.views.registration.RegistrationView;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Date: 3/3/13
 * Time: 11:30 AM
 */
//  TODO - add some about info
@Component(value = "eeyeoLoginUI")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Theme("eeyeo")
@SuppressWarnings("unused")
public class EEYEOLoginUI extends EEYEOErrorHandlingUI {
    @Autowired
    private LoginView loginView;

    @Autowired
    private LegalView legalView;

    @Autowired
    private RegistrationView registrationView;

    @Autowired
    private PostRegistrationView postRegistrationView;

    @Autowired
    private AccountConfirmationView accountConfirmationView;

    @Autowired
    private ResetRequest resetRequest;

    @Autowired
    private PostResetRequest postResetRequest;

    @Autowired
    private PasswordReset passwordReset;

    @Override
    protected void init(final VaadinRequest request) {
        super.init(request);

        getSession().setAttribute(VaadinRequest.class, request);
        setSizeFull();

        Navigator navigator = new Navigator(this, this);
        navigator.addView(LoginView.VIEW_NAME, loginView);
        navigator.addView(RegistrationView.VIEW_NAME, registrationView);
        navigator.addView(LegalView.VIEW_NAME, legalView);
        navigator.addView(PostRegistrationView.VIEW_NAME, postRegistrationView);
        navigator.addView(AccountConfirmationView.VIEW_NAME, accountConfirmationView);
        navigator.addView(PostResetRequest.VIEW_NAME, postResetRequest);
        navigator.addView(ResetRequest.VIEW_NAME, resetRequest);
        navigator.addView(PasswordReset.VIEW_NAME, passwordReset);
        getSession().setAttribute(Navigator.class, navigator);

        if (!StringUtils.hasLength(getPage().getUriFragment())) {
            navigator.navigateTo(LoginView.VIEW_NAME);
        }
    }

}
