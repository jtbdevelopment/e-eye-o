package com.jtbdevelopment.e_eye_o.jersey.rest;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.serialization.JSONIdObjectSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

/**
 * Date: 2/10/13
 * Time: 11:57 AM
 */
@Service
@Path("/users")
public class AppUsersResource {
    @Autowired
    private ReadWriteDAO readWriteDAO;
    @Autowired
    private JSONIdObjectSerializer jsonIdObjectSerializer;

    @Context
    private UriInfo uriInfo;
    @Context
    private Request request;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    //  TODO - dangerous only let super users do this
    public String getUsers() {
        return jsonIdObjectSerializer.write(readWriteDAO.getUsers());
    }

    @Path("{userId}")
    public AppUserResource getUserEntities(@PathParam("userId") final String userId) {
        return new AppUserResource(readWriteDAO, jsonIdObjectSerializer, uriInfo, request, userId);
    }
}
