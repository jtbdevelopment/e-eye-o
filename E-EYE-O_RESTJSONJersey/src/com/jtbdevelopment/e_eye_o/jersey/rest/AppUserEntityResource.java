package com.jtbdevelopment.e_eye_o.jersey.rest;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.serialization.JSONIdObjectSerializer;

import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

/**
 * Date: 2/10/13
 * Time: 7:07 PM
 */
public class AppUserEntityResource {
    private final ReadWriteDAO readWriteDAO;
    private final JSONIdObjectSerializer jsonIdObjectSerializer;
    private final AppUser appUser;
    private final UriInfo uriInfo;
    private final Request request;

    public AppUserEntityResource(final ReadWriteDAO readWriteDAO, final JSONIdObjectSerializer jsonIdObjectSerializer, final UriInfo uriInfo, final Request request, final String userId, final String entityId) {
        this.readWriteDAO = readWriteDAO;
        this.jsonIdObjectSerializer = jsonIdObjectSerializer;
        this.uriInfo = uriInfo;
        this.request = request;
        this.appUser = readWriteDAO.get(AppUser.class, userId);
        readWriteDAO
    }
}
