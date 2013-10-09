package com.jtbdevelopment.e_eye_o.jersey.rest;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.reflection.IdObjectReflectionHelper;
import com.jtbdevelopment.e_eye_o.entities.security.AppUserUserDetails;
import com.jtbdevelopment.e_eye_o.jersey.rest.v2.helpers.SecurityHelper;
import com.jtbdevelopment.e_eye_o.serialization.JSONIdObjectSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
public class AppUsersResource {
    @Autowired
    protected ReadWriteDAO readWriteDAO;
    @Autowired
    protected JSONIdObjectSerializer jsonIdObjectSerializer;
    @Autowired
    protected IdObjectReflectionHelper idObjectReflectionHelper;
    @Autowired
    protected SecurityHelper securityHelper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({AppUserUserDetails.ROLE_USER, AppUserUserDetails.ROLE_ADMIN})
    //  TODO - paging?
    public Response getUsers() {
        AppUser appUser = securityHelper.getSessionAppUser();

        if (appUser.isAdmin()) {
            return Response.ok(jsonIdObjectSerializer.writeEntities(readWriteDAO.getUsers())).build();
        } else {
            return Response.ok(jsonIdObjectSerializer.writeEntity(readWriteDAO.get(AppUser.class, appUser.getId()))).build();
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured({AppUserUserDetails.ROLE_USER, AppUserUserDetails.ROLE_ADMIN})
    public Response updateUser(final String appUserString) {
        AppUser sessionAppUser = securityHelper.getSessionAppUser();

        AppUser updateAppUser = jsonIdObjectSerializer.read(appUserString);
        if (updateAppUser != null) {
            if (!StringUtils.hasLength(updateAppUser.getId())) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            AppUser dbAppUser = readWriteDAO.get(AppUser.class, updateAppUser.getId());
            if (dbAppUser == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            if (sessionAppUser.isAdmin() || sessionAppUser.equals(updateAppUser)) {
                return Response.ok(jsonIdObjectSerializer.writeEntity(readWriteDAO.update(sessionAppUser, updateAppUser))).build();
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @Path("{userId}")
    @Secured({AppUserUserDetails.ROLE_USER, AppUserUserDetails.ROLE_ADMIN})
    public Object getUserEntities(@PathParam("userId") final String userId) {
        AppUser appUser = securityHelper.getSessionAppUser();
        if (appUser.isAdmin() || appUser.getId().equals(userId)) {
            return new AppUserResource(readWriteDAO, jsonIdObjectSerializer, idObjectReflectionHelper, securityHelper, userId, null, null);
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
}
