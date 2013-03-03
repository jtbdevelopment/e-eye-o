package com.jtbdevelopment.e_eye_o.ria.security.authentication.vaadin;

import com.vaadin.navigator.ViewChangeListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Date: 2/24/13
 * Time: 8:24 PM
 * <p/>
 * Largely based on Spring Security Example by Nicolas Frankel at
 * https://github.com/nfrankel/More-Vaadin/tree/master/springsecurity-integration
 */
public class ViewChangeSecurityChecker implements ViewChangeListener {
    @Override
    public boolean beforeViewChange(final ViewChangeEvent event) {
        if (event.getNewView() instanceof LoginView) {
            return true;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication != null && authentication.isAuthenticated();
    }

    @Override
    public void afterViewChange(final ViewChangeEvent event) {
    }
}
