package com.jtbdevelopment.e_eye_o.jasper.reports.designer;

import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.Observable;
import com.jtbdevelopment.e_eye_o.entities.impl.reflection.IdObjectReflectionHelperImpl;
import com.jtbdevelopment.e_eye_o.jasper.reports.ByStudentCategoryDataSourceProvider;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.*;

/**
 * Date: 8/29/13
 * Time: 8:28 PM
 */
public class DesignerByStudentCategoryDataSourceProvider extends ByStudentCategoryDataSourceProvider {

    public DesignerByStudentCategoryDataSourceProvider() {
        super(new DAO(), new IdObjectReflectionHelperImpl(), BaseData.appUser1, Collections.<ClassList>emptySet(), Collections.<Student>emptySet(), Collections.<ObservationCategory>emptySet(), null, null);
    }

    private static class DAO implements ReadOnlyDAO {

        @Override
        public Set<AppUser> getUsers() {
            return null;
        }

        @Override
        public AppUser getUser(String emailAddress) {
            return null;
        }

        @Override
        public <T extends IdObject> T get(Class<T> entityType, String id) {
            return null;
        }

        @Override
        public <T extends AppUserOwnedObject> Set<T> getEntitiesForUser(Class<T> entityType, AppUser appUser) {
            return null;
        }

        @Override
        public <T extends AppUserOwnedObject> Set<T> getEntitiesForUser(Class<T> entityType, AppUser appUser, boolean activeFlag) {
            return null;
        }

        @Override
        public <T extends AppUserOwnedObject> Set<T> getActiveEntitiesForUser(Class<T> entityType, AppUser appUser) {
            if (ObservationCategory.class.isAssignableFrom(entityType)) {
                return (Set<T>) new HashSet<>(Arrays.<ObservationCategory>asList(BaseData.oc1For1, BaseData.oc2For1));
            }
            if (Student.class.isAssignableFrom(entityType)) {
                return (Set<T>) new HashSet<>(Arrays.asList(BaseData.student1For1, BaseData.student2For1, BaseData.student3For1));
            }
            return null;
        }

        @Override
        public <T extends AppUserOwnedObject> Set<T> getArchivedEntitiesForUser(Class<T> entityType, AppUser appUser) {
            return null;
        }

        @Override
        public <T extends AppUserOwnedObject> List<String> getModificationsSince(AppUser appUser, DateTime since) {
            return null;
        }

        @Override
        public List<Photo> getAllPhotosForEntity(AppUserOwnedObject ownedObject) {
            return null;
        }

        @Override
        public LocalDateTime getLastObservationTimestampForEntity(Observable observable) {
            return null;
        }

        @Override
        public List<Observation> getAllObservationsForEntity(Observable observable) {
            return null;
        }

        @Override
        public List<Observation> getAllObservationsForObservationCategory(ObservationCategory observationCategory) {
            return null;
        }

        @Override
        public List<Observation> getAllObservationsForEntityAndCategory(final Observable observable, final ObservationCategory observationCategory, final LocalDate from, final LocalDate to) {
            if (observable.equals(BaseData.student1For1)) {
                if (BaseData.oc1For1.equals(observationCategory)) {
                    return Arrays.asList(BaseData.observation1For1For1, BaseData.observation3For1For1);
                }
                if (BaseData.oc2For1.equals(observationCategory)) {
                    return Arrays.asList(BaseData.observation2For1For1, BaseData.observation3For1For1);
                }
            }
            if (observable.equals(BaseData.student2For1)) {
                if (BaseData.oc1For1.equals(observationCategory)) {
                    return Arrays.asList(BaseData.observation1For2For1);
                }
                if (BaseData.oc2For1.equals(observationCategory)) {
                    return Arrays.asList(BaseData.observation2For2For1);
                }
            }
            if (observable.equals(BaseData.student3For1)) {
                if (BaseData.oc1For1.equals(observationCategory)) {
                    return Arrays.asList(BaseData.observation1For3For1);
                }
                if (observationCategory == null) {
                    return Arrays.asList(BaseData.observation2For3For1);
                }
            }
            return Collections.emptyList();
        }

        @Override
        public List<Student> getAllStudentsForClassList(ClassList classList) {
            return null;
        }
    }
}
