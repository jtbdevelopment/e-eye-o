package com.jtbdevelopment.e_eye_o.DerbyDAO;

import com.jtbdevelopment.e_eye_o.DAO.WriteOnlyDataLayer;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import junit.framework.Assert;
import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Set;

/**
 * Date: 11/18/12
 * Time: 11:18 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
public class DerbyDataProviderImplTest {
    private static Logger logger = LoggerFactory.getLogger(DerbyDataProviderImplTest.class);

    @Autowired
    private WriteOnlyDataLayer derbyWriteOnlyDataLayer;


    private static AppUser testAppUser;

    @Before
    public void initialize() {
        testAppUser = new AppUser();
        testAppUser.setEmailAddress("test@test.com");
        testAppUser.setFirstName("Testy");
        testAppUser.setLastName("Tester");
        testAppUser.setLogin("TT");
        derbyWriteOnlyDataLayer.create(testAppUser);
        testAppUser = derbyWriteOnlyDataLayer.get(AppUser.class, testAppUser.getId());
        logger.info("Created Test Tester with ID " + testAppUser.getId());
    }

    @After
    public void cleanup() {
        logger.info("Cleaning up");
        derbyWriteOnlyDataLayer.deleteUser(testAppUser);
        logger.info("Done");
    }

    @Test
    public void testCreateDefaultCategories() {
        List<ObservationCategory> categories = ObservationCategory.createDefaultCategoriesForUser(testAppUser);
        derbyWriteOnlyDataLayer.create(categories);
        Set<ObservationCategory> reloaded = derbyWriteOnlyDataLayer.getEntitiesForUser(ObservationCategory.class, testAppUser);
        Assert.assertTrue(categories.containsAll(reloaded));
        Assert.assertTrue(reloaded.containsAll(categories));
    }
}
