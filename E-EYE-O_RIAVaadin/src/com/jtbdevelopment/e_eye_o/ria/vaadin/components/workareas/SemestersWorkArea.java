package com.jtbdevelopment.e_eye_o.ria.vaadin.components.workareas;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.Observation;
import com.jtbdevelopment.e_eye_o.entities.Semester;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.IdObjectTable;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.ObservationWithSubjectTable;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.SemesterTable;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.photoalbum.PhotoAlbum;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Date: 11/12/13
 * Time: 9:40 PM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SemestersWorkArea extends CustomComponent {
    @Autowired
    private SemesterTable semesterTable;
    @Autowired
    private ObservationWithSubjectTable observationWithSubjectTable;
    @Autowired
    private PhotoAlbum photoAlbum;

    @Autowired
    public void postConstruct() {
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setImmediate(true);

        Label label = new Label("Archiving or activating a semester archives or activates all observations for the time period.  Deleting a semester does NOT delete all observations in the time period.");
        label.setSizeUndefined();
        mainLayout.addComponent(label);
        mainLayout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
        semesterTable.setClickedOnListener(new IdObjectTable.ClickedOnListener<Semester>() {
            @Override
            public void handleClickEvent(final Semester entity) {
                observationWithSubjectTable.setDisplayDriver(entity);
//                observationWithSubjectTable.setFromFilter(entity.getStart().toLocalDateTime(LocalTime.MIDNIGHT));
//                observationWithSubjectTable.setToFilter(entity.getEnd().toLocalDateTime(LocalTime.MIDNIGHT).plusDays(1).minusSeconds(1));
            }
        });
        mainLayout.addComponent(semesterTable);

        mainLayout.addComponent(observationWithSubjectTable);
        observationWithSubjectTable.setClickedOnListener(new IdObjectTable.ClickedOnListener<Observation>() {
            @Override
            public void handleClickEvent(final Observation entity) {
                photoAlbum.setDisplayDriver(entity);
            }
        });

        mainLayout.addComponent(photoAlbum);

        setCompositionRoot(mainLayout);
    }

    @Override
    public void attach() {
        super.attach();
        final AppUser appUser = getSession().getAttribute(AppUser.class);
        semesterTable.setDisplayDriver(appUser);
        getUI().setFocusedComponent(semesterTable.getSearchFor());
    }
}
