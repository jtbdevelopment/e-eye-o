package com.jtbdevelopment.e_eye_o.DAO.helpers;

import com.google.common.collect.Sets;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;

/**
 * Date: 11/25/12
 * Time: 8:40 PM
 */

public class ObservationCategoryHelperImplTest {
    private final Mockery context = new Mockery();
    private final ReadWriteDAO dao = context.mock(ReadWriteDAO.class);
    private final AppUser user = context.mock(AppUser.class);
    private final ObservationCategoryHelperImpl helper = new ObservationCategoryHelperImpl();

    @BeforeMethod
    public void setUp() {
        helper.dao = dao;
    }

    @Test
    public void testReturnsCategoriesAsMap() {
        final String one = "map1", two = "map2", three = "map3";
        final ObservationCategory oc1 = context.mock(ObservationCategory.class, one);
        final ObservationCategory oc2 = context.mock(ObservationCategory.class, two);
        final ObservationCategory oc3 = context.mock(ObservationCategory.class, three);

        context.checking(new Expectations() {{
            allowing(oc1).getId();
            will(returnValue(one));
            allowing(oc2).getId();
            will(returnValue(two));
            allowing(oc3).getId();
            will(returnValue(three));
            allowing(oc1).getShortName();
            will(returnValue(one));
            allowing(oc2).getShortName();
            will(returnValue(two));
            allowing(oc3).getShortName();
            will(returnValue(three));
            one(dao).getEntitiesForUser(ObservationCategory.class, user, 0, 0);
            will(returnValue(Sets.newHashSet(oc1, oc2, oc3)));
        }});

        Map<String, ObservationCategory> map = helper.getObservationCategoriesAsMap(user);
        assertEquals(3, map.size());
        assertEquals(oc1, map.get(one));
        assertEquals(oc2, map.get(two));
        assertEquals(oc3, map.get(three));
    }

}
