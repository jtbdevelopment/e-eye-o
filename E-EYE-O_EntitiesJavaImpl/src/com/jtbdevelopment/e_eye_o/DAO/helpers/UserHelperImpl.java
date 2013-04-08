package com.jtbdevelopment.e_eye_o.DAO.helpers;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.*;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * Date: 4/6/13
 * Time: 6:26 PM
 */
@Component
@SuppressWarnings("unused")
public class UserHelperImpl implements UserHelper {
    @Autowired
    private ReadWriteDAO readWriteDAO;

    @Autowired
    private ObservationCategoryHelper observationCategoryHelper;

    @Autowired
    private IdObjectFactory idObjectFactory;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public TwoPhaseActivity setUpNewUser(final AppUser appUser) {
        securePassword(appUser, appUser.getPassword());
        AppUser savedUser = readWriteDAO.create(appUser);
        createSamplesForUser(savedUser);
        return readWriteDAO.create(idObjectFactory.newTwoPhaseActivityBuilder(savedUser).withActivityType(TwoPhaseActivity.Activity.ACCOUNT_ACTIVATION).withExpirationTime(new DateTime().plusDays(1)).build());
    }

    private void securePassword(final AppUser appUser, final String clearCasePassword) {
        appUser.setPassword(passwordEncoder.encode(clearCasePassword));
    }

    @Override
    public void activateUser(final TwoPhaseActivity twoPhaseActivity) {
        twoPhaseActivity.setArchived(true);
        final AppUser appUser = twoPhaseActivity.getAppUser();
        appUser.setActivated(true);
        appUser.setActive(true);
        readWriteDAO.update(Arrays.asList(appUser, twoPhaseActivity));
    }

    @Override
    public TwoPhaseActivity requestResetPassword(AppUser appUser) {
        TwoPhaseActivity requestReset = idObjectFactory.newTwoPhaseActivityBuilder(appUser).withActivityType(TwoPhaseActivity.Activity.PASSWORD_RESET).withExpirationTime(new DateTime().plusDays(1)).build();
        requestReset = readWriteDAO.create(requestReset);
        return requestReset;
    }

    @Override
    public void resetPassword(final TwoPhaseActivity twoPhaseActivity, final String newPassword) {
        twoPhaseActivity.setArchived(true);
        final AppUser appUser = twoPhaseActivity.getAppUser();
        appUser.setActivated(true);
        appUser.setActive(true);
        securePassword(appUser, newPassword);
        readWriteDAO.update(Arrays.asList(appUser, twoPhaseActivity));
    }

    private void createSamplesForUser(final AppUser savedUser) {
        observationCategoryHelper.createDefaultCategoriesForUser(savedUser);
        Map<String, ObservationCategory> map = observationCategoryHelper.getObservationCategoriesAsMap(savedUser);
        ClassList cl = readWriteDAO.create(idObjectFactory.newClassListBuilder(savedUser).withDescription("Example Class").build());
        Student s1 = readWriteDAO.create(idObjectFactory.newStudentBuilder(savedUser).withFirstName("Student").withLastName("A").addClassList(cl).build());
        Student s2 = readWriteDAO.create(idObjectFactory.newStudentBuilder(savedUser).withFirstName("Student").withLastName("B").addClassList(cl).build());
        final Iterator<Map.Entry<String, ObservationCategory>> entryIterator = map.entrySet().iterator();
        ObservationCategory c1 = entryIterator.next().getValue();
        ObservationCategory c2 = entryIterator.next().getValue();
        Observation o1 = readWriteDAO.create(idObjectFactory.newObservationBuilder(savedUser).withObservationTimestamp(new LocalDateTime().minusDays(7)).withObservationSubject(s1).withComment("Observation 1").addCategory(c1).build());
        Observation o2 = readWriteDAO.create(idObjectFactory.newObservationBuilder(savedUser).withObservationTimestamp(new LocalDateTime().minusDays(3)).withObservationSubject(s1).withComment("Observation 2 as Followup to 1").addCategory(c1).addCategory(c2).build());
        readWriteDAO.linkFollowUpObservation(o1, o2);
        readWriteDAO.create(idObjectFactory.newObservationBuilder(savedUser).withObservationTimestamp(new LocalDateTime().minusDays(10)).withObservationSubject(s2).withFollowUpNeeded(true).withFollowUpReminder(new LocalDate().plusDays(1)).withComment("Observation 3").build());
        readWriteDAO.create(idObjectFactory.newObservationBuilder(savedUser).withObservationSubject(cl).withObservationTimestamp(new LocalDateTime().minusDays(1)).addCategory(c2).withComment("You can put general class observations too.").build());
    }
}
