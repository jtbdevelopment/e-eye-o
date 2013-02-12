package com.jtbdevelopment.e_eye_o.jersey.rest;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.serialization.JSONIdObjectSerializer;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

/**
 * Date: 2/10/13
 * Time: 12:33 PM
 */
public class AppUserResource {
    private final ReadWriteDAO readWriteDAO;
    private final JSONIdObjectSerializer jsonIdObjectSerializer;
    private final AppUser appUser;
    private final UriInfo uriInfo;
    private final Request request;

    public AppUserResource(final ReadWriteDAO readWriteDAO, final JSONIdObjectSerializer jsonIdObjectSerializer, final UriInfo uriInfo, final Request request, final String userId) {
        this.readWriteDAO = readWriteDAO;
        this.jsonIdObjectSerializer = jsonIdObjectSerializer;
        this.uriInfo = uriInfo;
        this.request = request;
        this.appUser = readWriteDAO.get(AppUser.class, userId);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getEntitiesForUser() {
        return jsonIdObjectSerializer.write(readWriteDAO.getEntitiesForUser(AppUserOwnedObject.class, appUser));
    }

    /*
    @Path("/photo")
    public getPhotoes
    return AppUserEntitiesResource()
    */
}
