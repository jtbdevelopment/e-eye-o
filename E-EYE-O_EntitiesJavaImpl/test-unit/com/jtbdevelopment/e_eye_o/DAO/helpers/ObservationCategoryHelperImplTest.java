package com.jtbdevelopment.e_eye_o.DAO.helpers;

import com.google.common.collect.Sets;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import com.jtbdevelopment.e_eye_o.entities.impl.IdObjectImplFactory;
import com.jtbdevelopment.e_eye_o.entities.utilities.IdObjectFactory;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Date: 11/25/12
 * Time: 8:40 PM
 */

public class ObservationCategoryHelperImplTest {
    private final Mockery context = new Mockery();
    private final ReadWriteDAO dao = context.mock(ReadWriteDAO.class);
    private final AppUser user = context.mock(AppUser.class);
    private final IdObjectFactory factory = new IdObjectImplFactory();
    private final ObservationCategoryHelperImpl helper = new ObservationCategoryHelperImpl(dao, factory);

    @Test
    @SuppressWarnings("unchecked")
    public void testCreatesDefault() {
        final Map<String, ObservationCategory> shortMap = new HashMap<>();
        final List<ObservationCategory> initialList = new ArrayList<>();
        for (Map.Entry<String, String> entry : ObservationCategoryHelperImpl.NEW_USER_DEFAULT_CATEGORIES.entrySet()) {
            final ObservationCategory impl = factory.newObservationCategory(user).setShortName(entry.getKey()).setDescription(entry.getValue());
            impl.setId(entry.getKey());
            initialList.add(impl);
            shortMap.put(entry.getKey(), impl);
        }

        context.checking(new Expectations() {{
            allowing(dao).create(with(any(List.class)));
            will(returnValue(initialList));
        }});
        Set<ObservationCategory> ocs = helper.createDefaultCategoriesForUser(user);
        assertEquals(ObservationCategoryHelperImpl.NEW_USER_DEFAULT_CATEGORIES.size(), ocs.size());
        assertTrue(ocs.containsAll(shortMap.values()));
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
            one(dao).getEntitiesForUser(ObservationCategory.class, user);
            will(returnValue(Sets.newHashSet(oc1, oc2, oc3)));
        }});

        Map<String, ObservationCategory> map = helper.getObservationCategoriesAsMap(user);
        assertEquals(3, map.size());
        assertEquals(oc1, map.get(one));
        assertEquals(oc2, map.get(two));
        assertEquals(oc3, map.get(three));
    }

}
