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
 * Time: 11:56 AM
 */
public class AppUserEntitiesResource {
    private final ReadWriteDAO readWriteDAO;
    private final JSONIdObjectSerializer jsonIdObjectSerializer;
    private final AppUser appUser;
    private final Class<? extends AppUserOwnedObject> entityType;
    private final UriInfo uriInfo;
    private final Request request;

    public AppUserEntitiesResource(final ReadWriteDAO readWriteDAO, final JSONIdObjectSerializer jsonIdObjectSerializer, final UriInfo uriInfo, final Request request, final AppUser appUser, final String entityType) {
        this.readWriteDAO = readWriteDAO;
        this.jsonIdObjectSerializer = jsonIdObjectSerializer;
        this.uriInfo = uriInfo;
        this.request = request;
        this.appUser = appUser;
        try {
            this.entityType = (Class<? extends AppUserOwnedObject>) Class.forName("com.jtbdevelopment.e_eye_o.entities." + entityType);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getEntities() {
        return jsonIdObjectSerializer.write(readWriteDAO.getEntitiesForUser(entityType, appUser));
    }
}
