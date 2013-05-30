package com.jtbdevelopment.e_eye_o.jersey.rest;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.DeletedObject;
import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity;
import com.jtbdevelopment.e_eye_o.serialization.JSONIdObjectSerializer;
import org.springframework.security.access.annotation.Secured;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

/**
 * Date: 2/10/13
 * Time: 7:07 PM
 */
public class AppUserEntityResource extends SecurityAwareResource {
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
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public Response getEntity() {
        AppUserOwnedObject entity = readWriteDAO.get(AppUserOwnedObject.class, entityId);
        if(entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(jsonIdObjectSerializer.write(entity)).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public Response updateEntity(@FormParam("appUserOwnedObject") final String appUserOwnedObjectString) {
        AppUser sessionAppUser = getSessionAppUser();

        //  TODO - handle arrays?
        AppUserOwnedObject updateObject = jsonIdObjectSerializer.read(appUserOwnedObjectString);
        AppUserOwnedObject dbEntity = readWriteDAO.get(updateObject.getClass(), entityId);
        if (dbEntity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (!dbEntity.equals(updateObject) || !dbEntity.getId().equals(entityId)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if (!sessionAppUser.isAdmin()) {
            //  TODO - add this as annotation to classes
            if (updateObject instanceof DeletedObject || updateObject instanceof TwoPhaseActivity) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            if (!dbEntity.getAppUser().equals(sessionAppUser)) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            updateObject.setAppUser(dbEntity.getAppUser());
        }

        boolean updateArchive = updateObject.isArchived();
        updateObject.setArchived(dbEntity.isArchived());
        updateObject = readWriteDAO.update(updateObject);
        if (updateObject.isArchived() != updateArchive) {
            readWriteDAO.changeArchiveStatus(updateObject);
            updateObject = readWriteDAO.get(updateObject.getClass(), updateObject.getId());
        }

        //  Ignoring any sort of chained updated from archive status change
        return Response.ok(jsonIdObjectSerializer.write(updateObject)).build();
    }

    @DELETE
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public Response deleteEntity() {
        AppUser sessionAppUser = getSessionAppUser();

        AppUserOwnedObject dbObject = readWriteDAO.get(AppUserOwnedObject.class, entityId);
        if (dbObject == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (sessionAppUser.isAdmin() || dbObject.getAppUser().equals(sessionAppUser)) {
            //  TODO - annotate class
            if (dbObject instanceof DeletedObject || dbObject instanceof TwoPhaseActivity) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            readWriteDAO.delete(dbObject);
            return Response.ok().build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }
}
