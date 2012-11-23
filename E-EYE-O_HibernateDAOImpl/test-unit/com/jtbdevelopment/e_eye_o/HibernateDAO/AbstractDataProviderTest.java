package com.jtbdevelopment.e_eye_o.HibernateDAO;

import com.jtbdevelopment.e_eye_o.DAO.WriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;
import java.util.Set;

/**
 * Date: 11/21/12
 * Time: 1:45 AM
 *
 * Suite of tests that can be run against any data source provider to test hibernate.
 */
public abstract class AbstractDataProviderTest implements ApplicationContextAware {
    private static Logger logger = LoggerFactory.getLogger(AbstractDataProviderTest.class);

    private static WriteDAO writeDAO;
    private static AppUser testAppUser1;
    private static AppUser testAppUser2;

    //  Can't use junit beforeclass because we don't have app context or injected bean yet then
    //  We do use this to setup and then junit afterclass
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        writeDAO = applicationContext.getBean(WriteDAO.class);
        initialize();
    }

    public synchronized void initialize() {
        if(testAppUser1 != null) {
            return;
        }

        testAppUser1 = createUser("TT", "Testy", "Tester", "test@test.com");
        testAppUser2 = createUser("AT", "Another", "Tester", "another@test.com");
        logger.info("Created Test Tester with ID " + testAppUser1.getId());
        logger.info("Created Test Tester2 with ID " + testAppUser2.getId());
    }

    private AppUser createUser(final String login, final String first, final String last, final String email) {
        AppUser appUser = new AppUser();
        appUser.setLogin(login).setFirstName(first).setLastName(last).setEmailAddress(email);
        writeDAO.create(appUser);
        return appUser;
    }

    @Test
    public void duplicateLoginAppUserFails() {
        try {
            AppUser dupe = createUser("TT", "Testy", "Tester", "test@test.com");
        } catch (Exception e) {
            //  Expected
            return;
        }
        Assert.fail("Should have exceptioned");
    }

    @Test
    public void testCreateDefaultCategories() {
        List<ObservationCategory> initialCategories = ObservationCategory.createDefaultCategoriesForUser(testAppUser1);
        writeDAO.create(initialCategories);
        Set<ObservationCategory> reloaded = writeDAO.getEntitiesForUser(ObservationCategory.class, testAppUser1);
        Assert.assertTrue(reloaded.containsAll(initialCategories));
    }

    @Test
    public void testAddCategory() {
        ObservationCategory newCategory = new ObservationCategory(testAppUser1, "TEST", "Test Category");
        writeDAO.create(newCategory);
        ObservationCategory loadedByID = writeDAO.get(ObservationCategory.class, newCategory.getId());
        Assert.assertEquals(newCategory, loadedByID);
        Set<ObservationCategory> categories = writeDAO.getEntitiesForUser(ObservationCategory.class, testAppUser1);
        Assert.assertTrue(categories.contains(newCategory));
    }

    @Test
    public void addDuplicateCategoryCodeFail() {
        ObservationCategory newTest1 = new ObservationCategory(testAppUser1, "TESTDUPE", "desc 1");
        ObservationCategory newTest2 = new ObservationCategory(testAppUser1, "TESTDUPE", "desc 2");
        ObservationCategory newTest3 = new ObservationCategory(testAppUser2, "TESTDUPE", "desc 1");

        boolean exception = false;
        writeDAO.create(newTest1);
        try {
            writeDAO.create(newTest2);
        } catch (Exception e) {
            //  Expected
            exception = true;
        }
        Assert.assertTrue(exception);

        writeDAO.create(newTest3);
        Set<ObservationCategory> categories = writeDAO.getEntitiesForUser(ObservationCategory.class, testAppUser1);
        Assert.assertTrue(categories.contains(newTest1));
        Assert.assertFalse(categories.contains(newTest2));
        categories = writeDAO.getEntitiesForUser(ObservationCategory.class, testAppUser2);
        Assert.assertTrue(categories.contains(newTest3));
    }

}
