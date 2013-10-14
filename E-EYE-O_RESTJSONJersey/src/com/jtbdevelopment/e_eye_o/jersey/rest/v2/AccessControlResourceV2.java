package com.jtbdevelopment.e_eye_o.jersey.rest.v2;

import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Date: 5/17/13
 * Time: 6:53 AM
 */
@Service
@Path("/v2/security")
public class AccessControlResourceV2 {
    private static final Logger logger = LoggerFactory.getLogger(AccessControlResourceV2.class);

    public static final String VERSION = "1.0";
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILURE = "FAILURE";

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PersistentTokenBasedRememberMeServices rememberMeServices;

    @Autowired
    private ReadOnlyDAO readOnlyDAO;

    @POST
    @Path("login")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String login(
            @Context final HttpServletRequest httpServletRequest,
            @Context final HttpServletResponse httpServletResponse,
            @FormParam("login") final String login,
            @FormParam("password") final String password) {
        return privateLogin(httpServletRequest, httpServletResponse, login, password);
    }

    @GET
    @Path("login")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String loginQuery(
            @Context final HttpServletRequest httpServletRequest,
            @Context final HttpServletResponse httpServletResponse,
            @QueryParam("login") final String login,
            @QueryParam("password") final String password) {
        return privateLogin(httpServletRequest, httpServletResponse, login, password);
    }

    private String privateLogin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String login, String password) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
            rememberMeServices.loginSuccess(httpServletRequest, httpServletResponse, authentication);
        } catch (AuthenticationException e) {
            logger.info(login + " REST login failure");
            //  TODO - lock account after X attempts
            return FAILURE;
        }
        AppUser user = readOnlyDAO.getUser(login);
        if (user == null) {
            logger.warn(login + " REST login success but no user exists!");
            return FAILURE;
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        logger.info(login + " REST login success.");
        return SUCCESS;
    }

    @POST
    @Path("logout")
    public Response logout() {
        return Response.ok().build();
    }

    @GET
    @Path("version")
    @Produces(MediaType.TEXT_PLAIN)
    public Response version() {
        return Response.ok(VERSION).build();
    }
}
