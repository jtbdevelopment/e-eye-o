package com.jtbdevelopment.e_eye_o.jersey.rest;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.serialization.JSONIdObjectSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

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
    //  TODO - paging?
    public String getUsers() {
        AppUser appUser = getSessionAppUser();
        if (appUser == null) {
            //  TODO - no session
            return null;
        }

        if (appUser.isAdmin()) {
            return jsonIdObjectSerializer.write(readWriteDAO.getUsers());
        } else {
            return jsonIdObjectSerializer.write(appUser);
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String updateUser(@FormParam("appUser") final String appUserString) {
        AppUser updateAppUser = jsonIdObjectSerializer.read(appUserString);
        if (updateAppUser != null) {
            AppUser sessionAppUser = getSessionAppUser();
            if (sessionAppUser.isAdmin() || sessionAppUser.equals(updateAppUser)) {
                AppUser dbAppUser = readWriteDAO.get(AppUser.class, updateAppUser.getId());
                if (dbAppUser == null) {
                    //  TODO - updates only - no creates via JSON
                    return null;
                }
                //  Do not accept certain fields from POST from non-admins
                if (!sessionAppUser.isAdmin()) {
                    updateAppUser.setActivated(dbAppUser.isActivated());
                    updateAppUser.setActive(dbAppUser.isActive());
                    updateAppUser.setAdmin(dbAppUser.isAdmin());
                }
                updateAppUser.setPassword(dbAppUser.getPassword());
                updateAppUser.setLastLogout(dbAppUser.getLastLogout());

                return jsonIdObjectSerializer.write(readWriteDAO.update(updateAppUser));
            } else {
                //  TODO - unauthorized
                return null;
            }
        } else {
            //  TODO - no session
            return null;
        }
    }

    @Path("{userId}")
    public AppUserResource getUserEntities(@PathParam("userId") final String userId) {
        AppUser appUser = getSessionAppUser();
        if (appUser == null) {
            //  TODO - no session
            return null;
        }

        if (appUser.isAdmin() || appUser.getId().equals(userId)) {
            return new AppUserResource(readWriteDAO, jsonIdObjectSerializer, userId, null, null);
        } else {
            //  TODO - unauthorized
            return null;
        }
    }
}
