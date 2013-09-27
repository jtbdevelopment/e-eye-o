package com.jtbdevelopment.e_eye_o.ria.vaadin.components.usersettings;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;

/**
 * Date: 9/26/13
 * Time: 10:23 PM
 */
public class PasswordConfirmingWindow extends Window {
    public PasswordConfirmingWindow(String caption) {
        super(caption);
        setCloseShortcut(ShortcutAction.KeyCode.ESCAPE);
    }

    protected boolean reconfirmPassword(final AuthenticationManager authenticationManager, final AppUser appUser, final String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(appUser.getEmailAddress(), password));
        } catch (AuthenticationException e) {
            Notification.show("Invalid password.");
            return true;
        }
        return false;
    }
}
