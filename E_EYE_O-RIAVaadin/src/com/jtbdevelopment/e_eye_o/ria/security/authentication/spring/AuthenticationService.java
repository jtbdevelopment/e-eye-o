package com.jtbdevelopment.e_eye_o.ria.security.authentication.spring;

/**
 * Date: 2/24/13
 * Time: 8:45 PM
 */

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Date: 2/24/13
 * Time: 8:24 PM
 * <p/>
 * Largely based on Spring Security Example by Nicolas Frankel at
 * https://github.com/nfrankel/More-Vaadin/tree/master/springsecurity-integration
 */
public class AuthenticationService {

    public void handleAuthentication(String login, String password, HttpServletRequest httpRequest) {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(login, password);

        token.setDetails(new WebAuthenticationDetails(httpRequest));

        ServletContext servletContext = httpRequest.getSession().getServletContext();

        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);

        AuthenticationManager authManager = (AuthenticationManager) wac.getBean("authenticationManager");

        Authentication authentication = authManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void handleLogout(HttpServletRequest httpRequest) {

        ServletContext servletContext = httpRequest.getSession().getServletContext();

        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);

        LogoutHandler logoutHandler = (LogoutHandler) wac.getBean("logoutHandler");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Response should not be used?
        logoutHandler.logout(httpRequest, null, authentication);
    }
}
