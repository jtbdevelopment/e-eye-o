package com.jtbdevelopment.e_eye_o.DAO.helpers

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO
import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory
import org.jmock.Expectations
import org.jmock.Mockery
import org.testng.annotations.Test

/**
 * Date: 12/12/13
 * Time: 9:38 PM
 */
class ObservationCategoryHelperGImplTest extends GroovyTestCase {
    ObservationCategoryHelperGImpl observationCategoryHelper = new ObservationCategoryHelperGImpl()

    //  TODO - better mock?
    private final Mockery context = new Mockery();
    private final ReadWriteDAO dao = context.mock(ReadWriteDAO.class);
    private final AppUser user = context.mock(AppUser.class);

    @Test
    void testNoCategoriesReturnsEmptyMap() {
        observationCategoryHelper.readOnlyDAO = dao
        context.checking(new Expectations() {
            {
                one(dao).getEntitiesForUser(ObservationCategory.class, user, 0, 0)
                will(returnValue([] as Set))
            }
        })

        LinkedHashMap<String, String> map = [:]
        assert map == observationCategoryHelper.getObservationCategoriesAsMap((AppUser) user);
    }

    @Test
    void testGetObservationCategoriesAsMap() {
        ObservationCategory cat1 = createCateogry("CAT1", "Cat 1");
        ObservationCategory cat2 = createCateogry("CAT2", "Cat 2 D");
        ObservationCategory cat3 = createCateogry("CAT3", "3")

        observationCategoryHelper.readOnlyDAO = dao
        context.checking(new Expectations() {
            {
                one(dao).getEntitiesForUser(ObservationCategory.class, user, 0, 0)
                will(returnValue([cat1, cat2, cat3] as Set))
            }
        })

        LinkedHashMap<String, ObservationCategory> map = [(cat1.shortName): cat1, (cat2.shortName): cat2, (cat3.shortName): cat3]
        assert map == observationCategoryHelper.getObservationCategoriesAsMap((AppUser) user);
    }


    ObservationCategory createCateogry(def String shortName, def String desc) {
        return [getShortName: { shortName }, getDescription: { desc }] as ObservationCategory
    }

}
