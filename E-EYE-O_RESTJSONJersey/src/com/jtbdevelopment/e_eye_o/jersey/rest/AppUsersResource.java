package com.jtbdevelopment.e_eye_o.jersey.rest;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.serialization.JSONIdObjectSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Date: 2/10/13
 * Time: 11:57 AM
 */
// TODO - delete user
@Service
@Path("/users")
public class AppUsersResource extends SecurityAwareResource {
    @Autowired
    protected ReadWriteDAO readWriteDAO;
    @Autowired
    protected JSONIdObjectSerializer jsonIdObjectSerializer;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    //  TODO - paging?
    public Response getUsers() {
        AppUser appUser = getSessionAppUser();

        if (appUser.isAdmin()) {
            return Response.ok(jsonIdObjectSerializer.write(readWriteDAO.getUsers())).build();
        } else {
            return Response.ok(jsonIdObjectSerializer.write(readWriteDAO.get(AppUser.class, appUser.getId()))).build();
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public Response updateUser(@FormParam("appUser") final String appUserString) {
        AppUser updateAppUser = jsonIdObjectSerializer.read(appUserString);
        if (updateAppUser != null) {
            AppUser sessionAppUser = getSessionAppUser();
            if (sessionAppUser.isAdmin() || sessionAppUser.equals(updateAppUser)) {
                AppUser dbAppUser = readWriteDAO.get(AppUser.class, updateAppUser.getId());
                if (dbAppUser == null) {
                    return Response.status(Response.Status.FORBIDDEN).build();
                }
                //  Do not accept certain fields from POST from non-admins
                if (!sessionAppUser.isAdmin()) {
                    updateAppUser.setActivated(dbAppUser.isActivated());
                    updateAppUser.setActive(dbAppUser.isActive());
                    updateAppUser.setAdmin(dbAppUser.isAdmin());
                }
                updateAppUser.setPassword(dbAppUser.getPassword());
                updateAppUser.setLastLogout(dbAppUser.getLastLogout());

                return Response.ok(jsonIdObjectSerializer.write(readWriteDAO.update(updateAppUser))).build();
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @Path("{userId}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public Object getUserEntities(@PathParam("userId") final String userId) {
        AppUser appUser = getSessionAppUser();
        if (appUser.isAdmin() || appUser.getId().equals(userId)) {
            return new AppUserResource(readWriteDAO, jsonIdObjectSerializer, userId, null, null);
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
}
