package com.jtbdevelopment.e_eye_o.jersey.rest;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.security.AppUserUserDetails;
import org.jmock.Expectations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Date: 2/18/13
 * Time: 3:46 PM
 */
public class AppUsersResourceTest extends AbstractResourceTest {
    protected AppUsersResource resource;

    @BeforeMethod
    public void setUp() throws Exception {
        super.setUp();
        resource = new AppUsersResource();
        resource.readWriteDAO = dao;
        resource.jsonIdObjectSerializer = serializer;
    }

    @Test
    public void testGetUsersAnnotations() throws NoSuchMethodException {
        checkGETJSONForMethod(AppUsersResource.class, "getUsers");
    }

    @Test
    public void testGetUsers() throws Exception {
        final Set<AppUser> daoResults = new HashSet<>();
        final String serialString = "{ \"field\": 0 }";
        final AppUser appUser = context.mock(AppUser.class);
        context.checking(new Expectations() {{
            one(dao).getUsers();
            will(returnValue(daoResults));
            one(serializer).writeEntities(daoResults);
            will(returnValue(serialString));
            one(appUser).isAdmin();
            will(returnValue(true));
        }});
        mockSecurityContext(appUser);

        assertEquals(serialString, resource.getUsers().getEntity());
    }

    @Test
    public void testGetUserEntitiesAnnotations() throws NoSuchMethodException {
        checkPathForMethod(AppUsersResource.class, "getUserEntities", "{userId}");
    }

    @Test
    public void testGetUserEntities() throws Exception {
        final String userId = "an=id";
        final AppUser appUser = context.mock(AppUser.class);
        context.checking(new Expectations() {{
            one(dao).get(AppUser.class, userId);
            will(returnValue(appUser));
            one(appUser).isAdmin();
            will(returnValue(false));
            one(appUser).getId();
            will(returnValue(userId));
        }});

        mockSecurityContext(appUser);
        assertNotNull(resource.getUserEntities(userId));
    }

    private void mockSecurityContext(final AppUser appUser) {
        SecurityContextHolder.setContext(new SecurityContext() {
            @Override
            public Authentication getAuthentication() {
                return new Authentication() {
                    @Override
                    public Collection<? extends GrantedAuthority> getAuthorities() {
                        return null;
                    }

                    @Override
                    public Object getCredentials() {
                        return null;
                    }

                    @Override
                    public Object getDetails() {
                        return null;
                    }

                    @Override
                    public Object getPrincipal() {
                        return new AppUserUserDetails() {
                            @Override
                            public AppUser getAppUser() {
                                return appUser;
                            }

                            @Override
                            public Collection<? extends GrantedAuthority> getAuthorities() {
                                return null;
                            }

                            @Override
                            public String getPassword() {
                                return null;
                            }

                            @Override
                            public String getUsername() {
                                return null;
                            }

                            @Override
                            public boolean isAccountNonExpired() {
                                return false;
                            }

                            @Override
                            public boolean isAccountNonLocked() {
                                return false;
                            }

                            @Override
                            public boolean isCredentialsNonExpired() {
                                return false;
                            }

                            @Override
                            public boolean isEnabled() {
                                return false;
                            }
                        };
                    }

                    @Override
                    public boolean isAuthenticated() {
                        return false;
                    }

                    @Override
                    public void setAuthenticated(final boolean isAuthenticated) throws IllegalArgumentException {

                    }

                    @Override
                    public String getName() {
                        return null;
                    }
                };
            }

            @Override
            public void setAuthentication(final Authentication authentication) {

            }
        });
    }


}
