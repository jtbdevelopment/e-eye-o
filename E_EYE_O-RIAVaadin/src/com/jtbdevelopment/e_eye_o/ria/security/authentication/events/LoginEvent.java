package com.jtbdevelopment.e_eye_o.ria.security.authentication.events;

/**
 * Date: 2/24/13
 * Time: 8:24 PM
 * <p/>
 * Largely based on Spring Security Example by Nicolas Frankel at
 * https://github.com/nfrankel/More-Vaadin/tree/master/springsecurity-integration
 */
public class LoginEvent {

    private final String login;

    private final String password;

    public LoginEvent(String login, String password) {

        this.login = login;
        this.password = password;
    }

    public String getLogin() {

        return login;
    }

    public String getPassword() {

        return password;
    }
}
