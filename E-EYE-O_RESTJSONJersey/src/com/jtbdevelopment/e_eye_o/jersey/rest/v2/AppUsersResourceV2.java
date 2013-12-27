package com.jtbdevelopment.e_eye_o.jersey.rest.v2;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.DAO.helpers.ArchiveHelper;
import com.jtbdevelopment.e_eye_o.DAO.helpers.IdObjectDeletionHelper;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.entities.reflection.IdObjectReflectionHelper;
import com.jtbdevelopment.e_eye_o.entities.security.AppUserUserDetails;
import com.jtbdevelopment.e_eye_o.jersey.rest.v2.helpers.SecurityHelper;
import com.jtbdevelopment.e_eye_o.serialization.JSONIdObjectSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;

/**
 * Date: 2/10/13
 * Time: 11:57 AM
 */
@Service
@Path("/v2/users")
public class AppUsersResourceV2 {
    @Autowired
    protected ReadWriteDAO readWriteDAO;
    @Autowired
    protected ArchiveHelper archiveHelper;
    @Autowired
    protected IdObjectDeletionHelper idObjectDeletionHelper;
    @Autowired
    protected JSONIdObjectSerializer jsonIdObjectSerializer;
    @Autowired
    protected IdObjectReflectionHelper idObjectReflectionHelper;
    @Autowired
    protected SecurityHelper securityHelper;
    @Autowired
    protected IdObjectFactory idObjectFactory;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({AppUserUserDetails.ROLE_USER, AppUserUserDetails.ROLE_ADMIN})
    //  TODO - paging?
    public Response getUsers() {
        AppUser appUser = securityHelper.getSessionAppUser();

        return Response.ok(
                appUser.isAdmin() ? readWriteDAO.getUsers() : Arrays.<AppUser>asList(readWriteDAO.get(AppUser.class, appUser.getId())))
                .build();
    }

    @Path("{userId}")
    @Secured({AppUserUserDetails.ROLE_USER, AppUserUserDetails.ROLE_ADMIN})
    public Object getUserEntities(@PathParam("userId") final String userId) {
        AppUser appUser = securityHelper.getSessionAppUser();
        if (appUser.isAdmin() || appUser.getId().equals(userId)) {
            return new AppUserResourceV2(readWriteDAO, archiveHelper, idObjectDeletionHelper,
                    jsonIdObjectSerializer, idObjectReflectionHelper, idObjectFactory, securityHelper, userId, null, null);
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
}
