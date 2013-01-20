package com.jtbdevelopment.e_eye_o.DAO.helpers;

import com.google.common.collect.Sets;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import com.jtbdevelopment.e_eye_o.entities.impl.IdObjectImplFactory;
import org.hamcrest.Description;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    private static class ReturnMatchingObsCatAction implements Action {
        private final Map<String, ObservationCategory> ocMap;

        private ReturnMatchingObsCatAction(final Map<String, ObservationCategory> ocMap) {
            this.ocMap = ocMap;
        }

        @Override
        public Object invoke(final Invocation invocation) throws Throwable {
            return ocMap.get(((ObservationCategory) invocation.getParameter(0)).getShortName());
        }

        @Override
        public void describeTo(final Description description) {
            description.appendText("looks up short code from map and returns expected impl version");
        }

        public static Action returnMatchingObservationCategory(final Map<String, ObservationCategory> ocMap) {
            return new ReturnMatchingObsCatAction(ocMap);
        }
    }

    @Test
    public void testCreatesDefault() {
        final Map<String, ObservationCategory> shortMap = new HashMap<>();
        for (Map.Entry<String, String> entry : ObservationCategoryHelperImpl.NEW_USER_DEFAULT_CATEGORIES.entrySet()) {
            final ObservationCategory impl = factory.newObservationCategory(user).setShortName(entry.getKey()).setDescription(entry.getValue());
            impl.setId(entry.getKey());
            shortMap.put(entry.getKey(), impl);
        }

        context.checking(new Expectations() {{
            allowing(dao).create(with(any(ObservationCategory.class)));
            will(ReturnMatchingObsCatAction.returnMatchingObservationCategory(shortMap));
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
