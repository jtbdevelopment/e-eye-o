package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.*
import com.jtbdevelopment.e_eye_o.entities.builders.AppUserOwnedObjectBuilder
import groovy.mock.interceptor.MockFor
import org.testng.annotations.Test

/**
 * Date: 12/3/13
 * Time: 7:01 AM
 */
class IdObjectFactoryGImplTest extends GroovyTestCase {
    public static final List<Class<?>> NOT_APP_USER_CLASSES_ARRAY = [Object.class, List.class, String.class, Integer.class, Set.class, HashMap.class];
    //  TODO - dynamic
    public static final List<Class<? extends AppUserOwnedObject>> APP_USER_OWNED_CLASSES_ARRAY = [ClassList.class, Student.class, DeletedObject.class, Photo.class, Observation.class, ObservationCategory.class, TwoPhaseActivity.class, AppUserSettings.class, Semester.class];
    public static String APPEND = "GImpl"
    private static final MockFor context = new MockFor(AppUser.class);
    private static final AppUser appUser = context.proxyInstance();
    private static final IdObjectFactoryGImpl factory = new IdObjectFactoryGImpl();

    protected String getExpectedBuilderClassName(String name) {
        name + "Builder" + APPEND
    }

    protected String getExpectedImplClassName(String name) {
        name + APPEND
    }

    @Test
    public void testNewFactoryForKnownClasses() throws Exception {
        assert getExpectedImplClassName("AppUser") == factory.newIdObject(AppUser.class).class.simpleName;
        assert getExpectedImplClassName("AppUser") == factory.newAppUser().class.simpleName;
        assert getExpectedBuilderClassName("AppUser") == factory.newAppUserBuilder().class.simpleName
        assert getExpectedImplClassName("PaginatedIdObjectList") == factory.newPaginatedIdObjectList().class.simpleName;
        assert getExpectedBuilderClassName("PaginatedIdObjectList") == factory.newPaginatedIdObjectListBuilder().class.simpleName
        APP_USER_OWNED_CLASSES_ARRAY.each { classType ->
            String name = classType.simpleName
            String expectedType = getExpectedImplClassName(name)
            String expectedBuilder = getExpectedBuilderClassName(name)

            AppUserOwnedObject indirect = factory.newAppUserOwnedObject(classType, appUser)
            assert expectedType == indirect.class.simpleName
            assert appUser == indirect.appUser

            AppUserOwnedObject direct = factory."new${name}"(appUser)
            assert expectedType == direct.class.simpleName
            assert appUser == direct.appUser

            AppUserOwnedObjectBuilder builder = factory."new${name}Builder"(appUser)
            assert expectedBuilder == builder.class.simpleName
            assert appUser == builder.build().appUser
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testNewIdObjectForUnknownClasses() throws Exception {
        NOT_APP_USER_CLASSES_ARRAY.each { notIdObject ->
            boolean exception = false
            try {
                factory.newIdObject(notIdObject);
            } catch (IllegalArgumentException e) {
                exception = true;
                assert "Unknown class type " + notIdObject.getSimpleName() == e.getMessage();
            }
            assert exception: "Expected exception for " + notIdObject.getSimpleName();
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testNewAppUserObjectForNonAppUserOwnedObjects() {
        [AppUser.class].each { notIdObject ->
            boolean exception = false
            try {
                factory.newAppUserOwnedObject(notIdObject, null);
            } catch (IllegalArgumentException e) {
                exception = true;
                assert "You cannot use this method to create non-app user owned objects." == e.getMessage();
            }
            assert exception: "Expected exception for " + notIdObject.getSimpleName();
        }

    }

    @Test
    @SuppressWarnings("unchecked")
    public void testNewAppUserObjectForUnknownClasses() throws Exception {
        NOT_APP_USER_CLASSES_ARRAY.each { notAppUserObject ->
            boolean exception = false;
            try {
                factory.newAppUserOwnedObject(notAppUserObject, null);
            } catch (IllegalArgumentException e) {
                exception = true;
                assert "You cannot use this method to create non-app user owned objects." == e.getMessage();
            }
            assert exception: "Expected exception for " + notAppUserObject.getSimpleName();
        }
    }
}
