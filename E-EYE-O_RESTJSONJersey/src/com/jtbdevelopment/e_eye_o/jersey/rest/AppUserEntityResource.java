package com.jtbdevelopment.e_eye_o.jersey.rest;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.serialization.JSONIdObjectSerializer;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Date: 2/10/13
 * Time: 7:07 PM
 */
public class AppUserEntityResource {
    private final ReadWriteDAO readWriteDAO;
    private final JSONIdObjectSerializer jsonIdObjectSerializer;
    private final String entityId;

    public AppUserEntityResource(final ReadWriteDAO readWriteDAO, final JSONIdObjectSerializer jsonIdObjectSerializer, final String entityId) {
        this.readWriteDAO = readWriteDAO;
        this.jsonIdObjectSerializer = jsonIdObjectSerializer;
        this.entityId = entityId;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getEntity() {
        return jsonIdObjectSerializer.write(readWriteDAO.get(AppUserOwnedObject.class, entityId));
    }
}
