package com.jtbdevelopment.e_eye_o.jersey.rest;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.serialization.JSONIdObjectSerializer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import java.util.Set;

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
    private Boolean archiveFlag;
    private Class<? extends AppUserOwnedObject> entityType;

    public AppUserResource(final ReadWriteDAO readWriteDAO,
                           final JSONIdObjectSerializer jsonIdObjectSerializer,
                           final UriInfo uriInfo,
                           final Request request,
                           final String userId,
                           final Boolean archiveFlag,
                           final Class<? extends AppUserOwnedObject> entityType) {
        this.readWriteDAO = readWriteDAO;
        this.jsonIdObjectSerializer = jsonIdObjectSerializer;
        this.uriInfo = uriInfo;
        this.request = request;
        this.appUser = readWriteDAO.get(AppUser.class, userId);
        this.archiveFlag = archiveFlag;
        this.entityType = entityType == null ? AppUserOwnedObject.class : entityType;
    }

    private AppUserResource(final AppUserResource appUserResource) {
        this.readWriteDAO = appUserResource.readWriteDAO;
        this.jsonIdObjectSerializer = appUserResource.jsonIdObjectSerializer;
        this.uriInfo = appUserResource.uriInfo;
        this.request = appUserResource.request;
        this.appUser = appUserResource.appUser;
    }

    private AppUserResource(final AppUserResource appUserResource,
                            final Boolean archiveFlag) {
        this(appUserResource);
        this.archiveFlag = archiveFlag;
        this.entityType = appUserResource.entityType;
    }

    private AppUserResource(final AppUserResource appUserResource,
                            final Class<? extends AppUserOwnedObject> entityType) {
        this(appUserResource);
        this.archiveFlag = appUserResource.archiveFlag;
        this.entityType = entityType;
    }

    //  TODO - paging
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getEntitiesForUser() {
        Set<? extends AppUserOwnedObject> set;
        if (archiveFlag == null)
            set = readWriteDAO.getEntitiesForUser(entityType, appUser);
        else {
            set = archiveFlag ? readWriteDAO.getArchivedEntitiesForUser(entityType, appUser) :
                    readWriteDAO.getActiveEntitiesForUser(entityType, appUser);
        }
        return jsonIdObjectSerializer.write(set);
    }

    @Path("archived")
    public AppUserResource getArchived() {
        if(archiveFlag == null)
            return new AppUserResource(this, Boolean.TRUE);
        return null;
    }

    @Path("active")
    public AppUserResource getActive() {
        if(archiveFlag == null)
            return new AppUserResource(this, Boolean.FALSE);
        return null;
    }

    @Path("photos")
    public AppUserResource getPhotos() {
        return getEntityRefinedResource(Photo.class);
    }

    @Path("students")
    public AppUserResource getStudents() {
        return getEntityRefinedResource(Student.class);
    }

    @Path("classlists")
    public AppUserResource getClassLists() {
        return getEntityRefinedResource(ClassList.class);
    }

    @Path("observations")
    public AppUserResource getObservations() {
        return getEntityRefinedResource(Observation.class);
    }

    @Path("categories")
    public AppUserResource getObservationCategories() {
        return getEntityRefinedResource(ObservationCategory.class);
    }

    @Path("{entityId}")
    public AppUserEntityResource getAppUserEntityResource(@PathParam("entityId") final String entityId) {
        return new AppUserEntityResource(readWriteDAO, jsonIdObjectSerializer, uriInfo, request, appUser, entityId);
    }

    private AppUserResource getEntityRefinedResource(final Class<? extends AppUserOwnedObject> entityType) {
        if(this.entityType.equals(AppUserOwnedObject.class))
            return new AppUserResource(this, entityType);
        return null;
    }
}
