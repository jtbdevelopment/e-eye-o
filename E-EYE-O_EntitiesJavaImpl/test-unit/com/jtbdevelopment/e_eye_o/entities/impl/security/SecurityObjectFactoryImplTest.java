package com.jtbdevelopment.e_eye_o.entities.impl.security;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.security.AppUserUserDetails;
import org.jmock.Mockery;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertSame;

/**
 * Date: 5/31/13
 * Time: 9:42 PM
 */
public class SecurityObjectFactoryImplTest {
    private Mockery context;
    private AppUser appUser;
    private final SecurityObjectFactoryImpl factory = new SecurityObjectFactoryImpl();

    @BeforeMethod
    public void setup() {
        context = new Mockery();
        appUser = context.mock(AppUser.class);
    }

    @Test
    public void testNewAppUserDetails() throws Exception {
        AppUserUserDetails userDetails = factory.newAppUserDetails(appUser);
        assertSame(appUser, userDetails.getAppUser());
    }
}
