package com.jtbdevelopment.e_eye_o.jersey.rest;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.DeletedObject;
import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity;
import com.jtbdevelopment.e_eye_o.serialization.JSONIdObjectSerializer;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

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
    public String getEntity() {
        return jsonIdObjectSerializer.write(readWriteDAO.get(AppUserOwnedObject.class, entityId));
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createEntity(@FormParam("{appUserOwnedObject}") final String appUserOwnedObjectString) {
        AppUser sessionAppUser = getSessionAppUser();
        if (sessionAppUser == null) {
            //  TODO - unauthenticated
            return null;
        }

        //  TODO - handle arrays
        AppUserOwnedObject newObject = jsonIdObjectSerializer.read(appUserOwnedObjectString);
        if (!sessionAppUser.isAdmin()) {
            //  TODO - add this as annotation to classes
            if (newObject instanceof DeletedObject || newObject instanceof TwoPhaseActivity) {
                //  TODO - unauthorized
                return null;
            }
            newObject.setAppUser(sessionAppUser);
        }

        return jsonIdObjectSerializer.write(readWriteDAO.create(newObject));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateEntity(@FormParam("{appUserOwnedObject}") final String appUserOwnedObjectString) {
        AppUser sessionAppUser = getSessionAppUser();
        if (sessionAppUser == null) {
            //  TODO - unauthenticated
            return null;
        }

        AppUserOwnedObject dbEntity = readWriteDAO.get(AppUserOwnedObject.class, entityId);
        //  TODO - handle arrays
        AppUserOwnedObject updateObject = jsonIdObjectSerializer.read(appUserOwnedObjectString);
        if (!sessionAppUser.isAdmin()) {
            //  TODO - add this as annotation to classes
            if (updateObject instanceof DeletedObject || updateObject instanceof TwoPhaseActivity) {
                //  TODO - unauthorized
                return null;
            }
            updateObject.setAppUser(sessionAppUser);
        }
        if (!dbEntity.equals(updateObject) || !dbEntity.getId().equals(entityId)) {
            //  TODO - invalid op
            return null;
        }

        //  TODO - deal with archive change
        return jsonIdObjectSerializer.write(readWriteDAO.update(updateObject));
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteEntity() {
        AppUser sessionAppUser = getSessionAppUser();
        if (sessionAppUser == null) {
            //  TODO - unauthenticated
            return null;
        }

        AppUserOwnedObject dbObject = readWriteDAO.get(AppUserOwnedObject.class, entityId);
        if (sessionAppUser.isAdmin() || dbObject.getAppUser().equals(sessionAppUser)) {
            //  TODO - annotate class
            if (dbObject instanceof DeletedObject || dbObject instanceof TwoPhaseActivity) {
                // TODO - invalid op
                return null;
            }
            ReadWriteDAO.ChainedUpdateSet<AppUserOwnedObject> related = readWriteDAO.delete(dbObject);
            return jsonIdObjectSerializer.write(related.getDeletedItems());
        }
        //  TODO - unauthoerized
        return null;
    }
}
