package com.jtbdevelopment.e_eye_o.jersey.rest.v2;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectEntitySettings;
import com.jtbdevelopment.e_eye_o.entities.reflection.IdObjectReflectionHelper;
import com.jtbdevelopment.e_eye_o.entities.security.AppUserUserDetails;
import com.jtbdevelopment.e_eye_o.jersey.rest.v2.helpers.SecurityHelper;
import com.jtbdevelopment.e_eye_o.serialization.JSONIdObjectSerializer;
import org.joda.time.DateTime;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.StringUtils;

import javax.annotation.Nullable;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.*;

/**
 * Date: 2/10/13
 * Time: 12:33 PM
 */
public class AppUserResourceV2 {
    private final static int PAGE_SIZE = 10;
    private final ReadWriteDAO readWriteDAO;
    private final JSONIdObjectSerializer jsonIdObjectSerializer;
    private final IdObjectReflectionHelper idObjectReflectionHelper;
    private final AppUser appUser;
    private final SecurityHelper securityHelper;
    private Boolean archiveFlag;
    private Class<? extends AppUserOwnedObject> entityType;

    public AppUserResourceV2(final ReadWriteDAO readWriteDAO,
                             final JSONIdObjectSerializer jsonIdObjectSerializer,
                             final IdObjectReflectionHelper idObjectReflectionHelper,
                             final SecurityHelper securityHelper,
                             final String userId,
                             final Boolean archiveFlag,
                             final Class<? extends AppUserOwnedObject> entityType) {
        this.readWriteDAO = readWriteDAO;
        this.securityHelper = securityHelper;
        this.jsonIdObjectSerializer = jsonIdObjectSerializer;
        this.appUser = readWriteDAO.get(AppUser.class, userId);
        this.idObjectReflectionHelper = idObjectReflectionHelper;
        this.archiveFlag = archiveFlag;
        this.entityType = entityType == null ? AppUserOwnedObject.class : entityType;
    }

    private AppUserResourceV2(final AppUserResourceV2 appUserResource) {
        this.readWriteDAO = appUserResource.readWriteDAO;
        this.jsonIdObjectSerializer = appUserResource.jsonIdObjectSerializer;
        this.idObjectReflectionHelper = appUserResource.idObjectReflectionHelper;
        this.appUser = appUserResource.appUser;
        this.securityHelper = appUserResource.securityHelper;
    }

    private AppUserResourceV2(final AppUserResourceV2 appUserResource,
                              final Boolean archiveFlag) {
        this(appUserResource);
        this.archiveFlag = archiveFlag;
        this.entityType = appUserResource.entityType;
    }

    private AppUserResourceV2(final AppUserResourceV2 appUserResource,
                              final Class<? extends AppUserOwnedObject> entityType) {
        this(appUserResource);
        this.archiveFlag = appUserResource.archiveFlag;
        this.entityType = entityType;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured({AppUserUserDetails.ROLE_USER, AppUserUserDetails.ROLE_ADMIN})
    public Response createEntity(final AppUserOwnedObject newObject) {
        AppUser sessionAppUser = securityHelper.getSessionAppUser();
        if (!sessionAppUser.isAdmin()) {
            if (!idObjectReflectionHelper.getIdObjectInterfaceForClass(newObject.getClass()).getAnnotation(IdObjectEntitySettings.class).editable()) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            newObject.setAppUser(sessionAppUser);
        }
        final AppUserOwnedObject entity = readWriteDAO.create(newObject);

        return Response.created(URI.create(entity.getId() + "/")).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured({AppUserUserDetails.ROLE_USER, AppUserUserDetails.ROLE_ADMIN})
    public Response updateUser(final AppUser updateAppUser) {
        AppUser sessionAppUser = securityHelper.getSessionAppUser();

        if (updateAppUser != null) {
            if (!StringUtils.hasLength(updateAppUser.getId())) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            if (readWriteDAO.get(AppUser.class, updateAppUser.getId()) == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            if (sessionAppUser.isAdmin() || sessionAppUser.equals(updateAppUser)) {
                AppUser updatedUser = readWriteDAO.update(sessionAppUser, updateAppUser);
                if (updatedUser.isActive() != updateAppUser.isActive()) {
                    if (updateAppUser.isActive()) {
                        //  Not allowed currently
                    } else {
                        readWriteDAO.deactivateUser(updatedUser);
                    }
                    updatedUser = readWriteDAO.get(AppUser.class, updatedUser.getId());
                }
                return Response.ok(updatedUser).build();
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured({AppUserUserDetails.ROLE_USER, AppUserUserDetails.ROLE_ADMIN})
    public Response deleteUser() {
        AppUser sessionAppUser = securityHelper.getSessionAppUser();

        if (sessionAppUser.isAdmin() || sessionAppUser.equals(appUser)) {
            readWriteDAO.deleteUser(appUser);
        }
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({AppUserUserDetails.ROLE_USER, AppUserUserDetails.ROLE_ADMIN})
    public Response getEntitiesForUser(@QueryParam("page") final Integer fromPage) {
        int from = computePage(fromPage);
        Set<? extends AppUserOwnedObject> set;
        if (archiveFlag == null)
            set = readWriteDAO.getEntitiesForUser(entityType, appUser, from, PAGE_SIZE);
        else {
            set = archiveFlag ? readWriteDAO.getArchivedEntitiesForUser(entityType, appUser, from, PAGE_SIZE) :
                    readWriteDAO.getActiveEntitiesForUser(entityType, appUser, from, PAGE_SIZE);
        }
        return Response.ok(computePaginatedResults(set, PAGE_SIZE)).build();
    }

    @GET
    @Path("ModifiedSince/{modifiedSince}")
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({AppUserUserDetails.ROLE_USER, AppUserUserDetails.ROLE_ADMIN})
    public Response getModifiedSince(@PathParam("modifiedSince") final String dateTimeString) {
        DateTime dateTime = DateTime.parse(dateTimeString);
        List<String> modificationsSince = readWriteDAO.getModificationsSince(appUser, dateTime, PAGE_SIZE);
        Collection<Map<String, Object>> listMap = Collections2.transform(modificationsSince, new Function<String, Map<String, Object>>() {
            @Nullable
            @Override
            public Map<String, Object> apply(@Nullable String input) {
                return input != null ? jsonIdObjectSerializer.readToMap(input) : null;
            }
        });
        return Response.ok(computePaginatedResults(listMap, PAGE_SIZE)).build();
    }

    @Path("{entityId}")
    @Secured({AppUserUserDetails.ROLE_USER, AppUserUserDetails.ROLE_ADMIN})
    public Object getAppUserEntityResource(@PathParam("entityId") final String entityId) {
        return new AppUserEntityResourceV2(readWriteDAO, idObjectReflectionHelper, securityHelper, entityId);
    }

    @Path("archived")
    @Secured({AppUserUserDetails.ROLE_USER, AppUserUserDetails.ROLE_ADMIN})
    public Object getArchived() {
        if (archiveFlag == null)
            return new AppUserResourceV2(this, Boolean.TRUE);
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @Path("active")
    @Secured({AppUserUserDetails.ROLE_USER, AppUserUserDetails.ROLE_ADMIN})
    public Object getActive() {
        if (archiveFlag == null)
            return new AppUserResourceV2(this, Boolean.FALSE);
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @Path("photos")
    @Secured({AppUserUserDetails.ROLE_USER, AppUserUserDetails.ROLE_ADMIN})
    public Object getPhotos() {
        return getEntityRefinedResource(Photo.class);
    }

    @Path("students")
    @Secured({AppUserUserDetails.ROLE_USER, AppUserUserDetails.ROLE_ADMIN})
    public Object getStudents() {
        return getEntityRefinedResource(Student.class);
    }

    @Path("classes")
    @Secured({AppUserUserDetails.ROLE_USER, AppUserUserDetails.ROLE_ADMIN})
    public Object getClassLists() {
        return getEntityRefinedResource(ClassList.class);
    }

    @Path("observations")
    @Secured({AppUserUserDetails.ROLE_USER, AppUserUserDetails.ROLE_ADMIN})
    public Object getObservations() {
        return getEntityRefinedResource(Observation.class);
    }

    @Path("categories")
    @Secured({AppUserUserDetails.ROLE_USER, AppUserUserDetails.ROLE_ADMIN})
    public Object getObservationCategories() {
        return getEntityRefinedResource(ObservationCategory.class);
    }

    private Object getEntityRefinedResource(final Class<? extends AppUserOwnedObject> entityType) {
        if (this.entityType.equals(AppUserOwnedObject.class))
            return new AppUserResourceV2(this, entityType);
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    private Map<String, Object> computePaginatedResults(final Collection<?> set, final int pageSize) {
        Map<String, Object> results = new HashMap<>();
        results.put("entities", set);
        results.put("more", set.size() == pageSize);
        return results;
    }

    private int computePage(final Integer fromPage) {
        return (Math.max(fromPage == null ? 0 : fromPage, 1) - 1) * PAGE_SIZE;
    }
}
