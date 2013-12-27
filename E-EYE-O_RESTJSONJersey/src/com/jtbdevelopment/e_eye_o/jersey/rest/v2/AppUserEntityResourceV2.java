package com.jtbdevelopment.e_eye_o.jersey.rest.v2;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.DAO.helpers.ArchiveHelper;
import com.jtbdevelopment.e_eye_o.DAO.helpers.IdObjectDeletionHelper;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectEntitySettings;
import com.jtbdevelopment.e_eye_o.entities.reflection.IdObjectReflectionHelper;
import com.jtbdevelopment.e_eye_o.entities.security.AppUserUserDetails;
import com.jtbdevelopment.e_eye_o.jersey.rest.v2.helpers.SecurityHelper;
import org.springframework.security.access.annotation.Secured;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Date: 2/10/13
 * Time: 7:07 PM
 */
public class AppUserEntityResourceV2 {
    private final ReadWriteDAO readWriteDAO;
    private final ArchiveHelper archiveHelper;
    private final IdObjectDeletionHelper idObjectDeletionHelper;
    private final IdObjectReflectionHelper idObjectReflectionHelper;
    private final SecurityHelper securityHelper;
    private final String entityId;

    public AppUserEntityResourceV2(final ReadWriteDAO readWriteDAO,
                                   final ArchiveHelper archiveHelper,
                                   final IdObjectDeletionHelper idObjectDeletionHelper,
                                   final IdObjectReflectionHelper idObjectReflectionHelper,
                                   final SecurityHelper securityHelper,
                                   final String entityId) {
        this.readWriteDAO = readWriteDAO;
        this.entityId = entityId;
        this.archiveHelper = archiveHelper;
        this.idObjectDeletionHelper = idObjectDeletionHelper;
        this.idObjectReflectionHelper = idObjectReflectionHelper;
        this.securityHelper = securityHelper;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({AppUserUserDetails.ROLE_USER, AppUserUserDetails.ROLE_ADMIN})
    public Response getEntity() {
        AppUserOwnedObject entity = readWriteDAO.get(AppUserOwnedObject.class, entityId);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(entity).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({AppUserUserDetails.ROLE_USER, AppUserUserDetails.ROLE_ADMIN})
    public Response updateEntity(AppUserOwnedObject updateObject) {
        AppUser sessionAppUser = securityHelper.getSessionAppUser();

        Class<? extends IdObject> idObjectInterface = idObjectReflectionHelper.getIdObjectInterfaceForClass(updateObject.getClass());
        if (!idObjectInterface.getAnnotation(IdObjectEntitySettings.class).editable()) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        AppUserOwnedObject dbEntity = readWriteDAO.get(updateObject.getClass(), entityId);
        if (dbEntity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (!dbEntity.equals(updateObject) || !dbEntity.getId().equals(entityId)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        boolean archiveRequested = updateObject.isArchived();
        if (sessionAppUser.isAdmin() || dbEntity.getAppUser().equals(sessionAppUser)) {
            updateObject = readWriteDAO.update(sessionAppUser, updateObject);
            if (updateObject.isArchived() != archiveRequested) {
                archiveHelper.flipArchiveStatus(updateObject);
                updateObject = readWriteDAO.get(updateObject.getClass(), updateObject.getId());
            }
        } else {
            if (!dbEntity.getAppUser().equals(sessionAppUser)) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }

        //  Ignoring any sort of chained updated from archive status change
        return Response.ok(updateObject).build();
    }

    @DELETE
    @Secured({AppUserUserDetails.ROLE_USER, AppUserUserDetails.ROLE_ADMIN})
    public Response deleteEntity() {
        AppUser sessionAppUser = securityHelper.getSessionAppUser();

        AppUserOwnedObject dbObject = readWriteDAO.get(AppUserOwnedObject.class, entityId);
        if (dbObject == null) {
            //  Probably OK
            return Response.ok().build();
        }

        Class<? extends IdObject> idObjectInterface = idObjectReflectionHelper.getIdObjectInterfaceForClass(dbObject.getClass());
        if (sessionAppUser.isAdmin() || dbObject.getAppUser().equals(sessionAppUser)) {
            if (!idObjectInterface.getAnnotation(IdObjectEntitySettings.class).editable()) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            idObjectDeletionHelper.delete(dbObject);
            return Response.ok().build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }
}
