package com.jtbdevelopment.e_eye_o.jersey.rest;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.security.AppUserUserDetails;
import com.jtbdevelopment.e_eye_o.serialization.JSONIdObjectSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Date: 2/10/13
 * Time: 11:57 AM
 */
@Service
@Path("/users")
public class AppUsersResource {
    @Autowired
    protected ReadWriteDAO readWriteDAO;
    @Autowired
    protected JSONIdObjectSerializer jsonIdObjectSerializer;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    //  TODO - paging?
    //  TODO - return an invalid session object
    public String getUsers() {
        AppUser appUser = getSessionAppUser();
        if (appUser == null) {
            return null;
        }

        if (appUser.isAdmin()) {
            return jsonIdObjectSerializer.write(readWriteDAO.getUsers());
        } else {
            return jsonIdObjectSerializer.write(appUser);
        }
    }

    //  TODO - return an invalid session object
    @Path("{userId}")
    public AppUserResource getUserEntities(@PathParam("userId") final String userId) {
        AppUser appUser = getSessionAppUser();
        if (appUser == null) {
            return null;
        }

        if (appUser.isAdmin() || appUser.getId().equals(userId)) {
            return new AppUserResource(readWriteDAO, jsonIdObjectSerializer, userId, null, null);
        } else {
            return null;
        }
    }

    private AppUser getSessionAppUser() {
        final Object principalAsObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principalAsObject instanceof AppUserUserDetails) {
            return ((AppUserUserDetails) principalAsObject).getAppUser();
        } else {
            return null;
        }
    }
}
