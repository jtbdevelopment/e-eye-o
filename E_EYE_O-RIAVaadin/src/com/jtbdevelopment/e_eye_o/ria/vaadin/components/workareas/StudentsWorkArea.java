package com.jtbdevelopment.e_eye_o.ria.vaadin.components.workareas;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.Observation;
import com.jtbdevelopment.e_eye_o.entities.Student;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.IdObjectTable;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.ObservationTable;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.StudentTable;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.photoalbum.PhotoAlbum;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Date: 3/10/13
 * Time: 4:40 PM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class StudentsWorkArea extends CustomComponent {
    private StudentTable studentTable;

    @Autowired
    public StudentsWorkArea(final StudentTable studentTable, final ObservationTable observationTable, final PhotoAlbum photoAlbum) {
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setImmediate(true);

        this.studentTable = studentTable;
        studentTable.setClickedOnListener(new IdObjectTable.ClickedOnListener<Student>() {
            @Override
            public void handleClickEvent(Student entity) {
                observationTable.setDisplayDriver(entity);
            }
        });
        mainLayout.addComponent(studentTable);

        mainLayout.addComponent(observationTable);
        observationTable.setClickedOnListener(new IdObjectTable.ClickedOnListener<Observation>() {
            @Override
            public void handleClickEvent(final Observation entity) {
                photoAlbum.setAlbumDriver(entity);
            }
        });

        mainLayout.addComponent(photoAlbum);

        setCompositionRoot(mainLayout);
    }

    @Override
    public void attach() {
        super.attach();
        final AppUser appUser = getSession().getAttribute(AppUser.class);
        studentTable.setDisplayDriver(appUser);
        getUI().setFocusedComponent(studentTable.getSearchFor());
    }

    @Override
    public void detach() {
        super.detach();
    }
}
