package com.jtbdevelopment.e_eye_o.ria.vaadin.components.workareas;

import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectDisplayPreferences;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.*;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.photoalbum.PhotoAlbum;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * Date: 3/10/13
 * Time: 4:40 PM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ClassListsWorkArea extends CustomComponent {
    @Autowired
    private ClassListTable classListTable;
    @Autowired
    private StudentTable studentTable;
    @Autowired
    private ObservationWithoutSubjectTable observationWithoutSubjectTable;
    @Autowired
    private ObservationWithoutSubjectTable observationsForStudentTable;
    @Autowired
    private PhotoAlbum photoAlbum;
    @Autowired
    private PhotoAlbum photosForObservations;
    @Autowired
    private PhotoAlbum photosForStudentsObservations;

    @PostConstruct
    public void postConstruct() {
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setImmediate(true);
        mainLayout.setSpacing(true);

        classListTable.setClickedOnListener(new IdObjectTable.ClickedOnListener<ClassList>() {
            @Override
            public void handleClickEvent(final ClassList entity) {
                studentTable.setDisplayDriver(entity);
                observationWithoutSubjectTable.setDisplayDriver(entity);
                photoAlbum.setDisplayDriver(entity);
            }
        });
        mainLayout.addComponent(classListTable);

        TabSheet tabSheet = new TabSheet();
        tabSheet.addStyleName(Runo.TABSHEET_SMALL);
        tabSheet.addTab(createTabLayout(Arrays.asList(studentTable, observationsForStudentTable, photosForStudentsObservations)))
                .setCaption(Student.class.getAnnotation(IdObjectDisplayPreferences.class).plural());
        tabSheet.addTab(createTabLayout(Arrays.asList(observationWithoutSubjectTable, photosForObservations)))
                .setCaption(Observation.class.getAnnotation(IdObjectDisplayPreferences.class).plural());
        tabSheet.addTab(photoAlbum).setCaption(Photo.class.getAnnotation(IdObjectDisplayPreferences.class).plural());
        mainLayout.addComponent(tabSheet);

        setCompositionRoot(mainLayout);
    }

    @Override
    public void attach() {
        super.attach();
        final AppUser appUser = getSession().getAttribute(AppUser.class);
        classListTable.setDisplayDriver(appUser);
        getUI().setFocusedComponent(classListTable.getSearchFor());
    }

    protected VerticalLayout createTabLayout(final List<? extends IdObjectFilterableDisplay<? extends AppUserOwnedObject>> displays) {
        IdObjectFilterableDisplay previous = null;
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        for (final IdObjectFilterableDisplay display : displays) {
            verticalLayout.addComponent(display);
            if (previous != null) {
                previous.setClickedOnListener(new IdObjectFilterableDisplay.ClickedOnListener<AppUserOwnedObject>() {
                    @Override
                    public void handleClickEvent(final AppUserOwnedObject entity) {
                        display.setDisplayDriver(entity);
                    }
                });
            }
            previous = display;
        }
        return verticalLayout;
    }
}
