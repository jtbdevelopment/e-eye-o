package com.jtbdevelopment.e_eye_o.jersey.rest.v2;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.reflection.IdObjectReflectionHelper;
import com.jtbdevelopment.e_eye_o.entities.security.AppUserUserDetails;
import com.jtbdevelopment.e_eye_o.jersey.rest.SecurityAwareResource;
import com.jtbdevelopment.e_eye_o.serialization.JSONIdObjectSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Date: 2/10/13
 * Time: 11:57 AM
 */
// TODO - delete user
// TODO - deactivate user
@Service
@Path("/v2/users")
public class AppUsersResourceV2 extends SecurityAwareResource {
    @Autowired
    protected ReadWriteDAO readWriteDAO;
    @Autowired
    protected JSONIdObjectSerializer jsonIdObjectSerializer;
    @Autowired
    protected IdObjectReflectionHelper idObjectReflectionHelper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({AppUserUserDetails.ROLE_USER, AppUserUserDetails.ROLE_ADMIN})
    //  TODO - paging?
    public Response getUsers() {
        AppUser appUser = getSessionAppUser();

        if (appUser.isAdmin()) {
            return Response.ok(jsonIdObjectSerializer.writeEntities(readWriteDAO.getUsers())).build();
        } else {
            return Response.ok(jsonIdObjectSerializer.writeEntity(readWriteDAO.get(AppUser.class, appUser.getId()))).build();
        }
    }

    @Path("{userId}")
    @Secured({AppUserUserDetails.ROLE_USER, AppUserUserDetails.ROLE_ADMIN})
    public Object getUserEntities(@PathParam("userId") final String userId) {
        AppUser appUser = getSessionAppUser();
        if (appUser.isAdmin() || appUser.getId().equals(userId)) {
            return new AppUserResourceV2(readWriteDAO, jsonIdObjectSerializer, idObjectReflectionHelper, userId, null, null);
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
}
