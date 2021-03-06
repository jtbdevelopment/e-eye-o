package com.jtbdevelopment.e_eye_o.jersey.rest.v2;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.DAO.helpers.ArchiveHelper;
import com.jtbdevelopment.e_eye_o.DAO.helpers.DeletionHelper;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectEntitySettings;
import com.jtbdevelopment.e_eye_o.entities.builders.PaginatedIdObjectListBuilder;
import com.jtbdevelopment.e_eye_o.jersey.rest.v2.helpers.SecurityHelper;
import com.jtbdevelopment.e_eye_o.reflection.IdObjectReflectionHelper;
import com.jtbdevelopment.e_eye_o.security.AppUserUserDetails;
import com.jtbdevelopment.e_eye_o.serialization.JSONIdObjectSerializer;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.StringUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Date: 2/10/13
 * Time: 12:33 PM
 */
public class AppUserResourceV2 {
    static final Logger LOGGER = LoggerFactory.getLogger(AppUserResourceV2.class);

    private final static int DEFAULT_PAGE_SIZE = 200;
    private final ReadWriteDAO readWriteDAO;
    private final ArchiveHelper archiveHelper;
    private final DeletionHelper deletionHelper;
    private final JSONIdObjectSerializer jsonIdObjectSerializer;
    private final IdObjectReflectionHelper idObjectReflectionHelper;
    private final IdObjectFactory idObjectFactory;
    private final AppUser appUser;
    private final SecurityHelper securityHelper;
    private Boolean archiveFlag;
    private Class<? extends AppUserOwnedObject> entityType;

    public AppUserResourceV2(final ReadWriteDAO readWriteDAO,
                             final ArchiveHelper archiveHelper,
                             final DeletionHelper deletionHelper,
                             final JSONIdObjectSerializer jsonIdObjectSerializer,
                             final IdObjectReflectionHelper idObjectReflectionHelper,
                             final IdObjectFactory idObjectFactory,
                             final SecurityHelper securityHelper,
                             final String userId,
                             final Boolean archiveFlag,
                             final Class<? extends AppUserOwnedObject> entityType) {
        this.readWriteDAO = readWriteDAO;
        this.archiveHelper = archiveHelper;
        this.deletionHelper = deletionHelper;
        this.securityHelper = securityHelper;
        this.idObjectFactory = idObjectFactory;
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
        this.idObjectFactory = appUserResource.idObjectFactory;
        this.archiveHelper = appUserResource.archiveHelper;
        this.deletionHelper = appUserResource.deletionHelper;
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
                        deletionHelper.deactivateUser(updatedUser);
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
            deletionHelper.deleteUser(appUser);
        }
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({AppUserUserDetails.ROLE_USER, AppUserUserDetails.ROLE_ADMIN})
    public Response getEntitiesForUser(
            @QueryParam("page") final Integer fromPage,
            @QueryParam("pageSize") final String pageSizeString
    ) {
        int pageSize = getPageSize(pageSizeString);
        int from = computePage(fromPage, pageSize);
        Set<? extends AppUserOwnedObject> set;
        if (archiveFlag == null)
            set = readWriteDAO.getEntitiesForUser(entityType, appUser, from, pageSize);
        else {
            set = archiveFlag ? readWriteDAO.getArchivedEntitiesForUser(entityType, appUser, from, pageSize) :
                    readWriteDAO.getActiveEntitiesForUser(entityType, appUser, from, pageSize);
        }
        return Response.ok(computePaginatedResults(set, pageSize, fromPage == null ? 0 : fromPage)).build();
    }

    @GET
    @Path("ModifiedSince/{modifiedSince}")
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({AppUserUserDetails.ROLE_USER, AppUserUserDetails.ROLE_ADMIN})
    public Response getModifiedSince(
            @PathParam("modifiedSince") final String dateTimeString,
            @QueryParam("lastIdSeen") final String lastIdSeen,
            @QueryParam("pageSize") final String pageSizeString) {
        LOGGER.warn("Modified Since Params RAW:  modifiedSince =" + dateTimeString + ", lasIdSeen=" + lastIdSeen + ", pageSize=" + pageSizeString);
        DateTime dateTime = DateTime.parse(dateTimeString);
        int pageSize = getPageSize(pageSizeString);
        final String sinceId = lastIdSeen != null ? lastIdSeen : "";
        LOGGER.warn("Modified Since Params READ:  modifiedSince =" + dateTime + ", lasIdSeen=" + sinceId + ", pageSize=" + pageSize);
        List<? extends IdObject> modificationsSince = readWriteDAO.getModificationsSince(appUser, dateTime, sinceId, pageSize);
        return Response.ok(computePaginatedResults(modificationsSince, pageSize, 0)).build();
    }

    private int getPageSize(final String pageSizeString) {
        return pageSizeString != null ? Integer.parseInt(pageSizeString) : DEFAULT_PAGE_SIZE;
    }

    @Path("{entityId}")
    @Secured({AppUserUserDetails.ROLE_USER, AppUserUserDetails.ROLE_ADMIN})
    public Object getAppUserEntityResource(@PathParam("entityId") final String entityId) {
        return new AppUserEntityResourceV2(readWriteDAO, archiveHelper, deletionHelper, idObjectReflectionHelper, securityHelper, entityId);
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

    @Path("semesters")
    @Secured({AppUserUserDetails.ROLE_USER, AppUserUserDetails.ROLE_ADMIN})
    public Object getSemesters() {
        return getEntityRefinedResource(Semester.class);
    }

    private Object getEntityRefinedResource(final Class<? extends AppUserOwnedObject> entityType) {
        if (this.entityType.equals(AppUserOwnedObject.class))
            return new AppUserResourceV2(this, entityType);
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    private PaginatedIdObjectList computePaginatedResults(final Collection<? extends IdObject> set, final int pageSize, final int currentPage) {
        PaginatedIdObjectListBuilder builder = idObjectFactory.newPaginatedIdObjectListBuilder();
        return builder.withEntities(set).withPageSize(pageSize).withMoreAvailable(set.size() == pageSize).withCurrentPage(currentPage).build();
    }

    private int computePage(final Integer fromPage, int pageSize) {
        return (Math.max(fromPage == null ? 0 : fromPage, 1) - 1) * pageSize;
    }
}
