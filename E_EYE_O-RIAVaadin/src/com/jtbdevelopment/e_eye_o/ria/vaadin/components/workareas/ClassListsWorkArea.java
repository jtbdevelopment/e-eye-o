package com.jtbdevelopment.e_eye_o.ria.vaadin.components.workareas;

import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.annotations.PreferredDescription;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.ClassListTable;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.IdObjectTable;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.ObservationTable;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.StudentTable;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.photoalbum.PhotoAlbum;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;
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
public class ClassListsWorkArea extends CustomComponent {
    private ClassListTable classListTable;

    @Autowired
    public ClassListsWorkArea(final ClassListTable classListTable, final StudentTable studentTable, final ObservationTable observationTable, final PhotoAlbum photoAlbum) {
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setImmediate(true);
        mainLayout.setSpacing(true);

        this.classListTable = classListTable;
        classListTable.setClickedOnListener(new IdObjectTable.ClickedOnListener<ClassList>() {
            @Override
            public void handleClickEvent(final ClassList entity) {
                studentTable.setTableDriver(entity);
                observationTable.setTableDriver(entity);
            }
        });
        mainLayout.addComponent(classListTable);

        TabSheet tabSheet = new TabSheet();
        tabSheet.addTab(studentTable).setCaption(Student.class.getAnnotation(PreferredDescription.class).plural());
        tabSheet.addTab(observationTable).setCaption(Observation.class.getAnnotation(PreferredDescription.class).plural());
        tabSheet.addTab(photoAlbum).setCaption(Photo.class.getAnnotation(PreferredDescription.class).plural());
        tabSheet.addStyleName(Runo.TABSHEET_SMALL);
        mainLayout.addComponent(tabSheet);

        setCompositionRoot(mainLayout);
    }

    @Override
    public void attach() {
        super.attach();
        final AppUser appUser = getSession().getAttribute(AppUser.class);
        classListTable.setTableDriver(appUser);
        getUI().setFocusedComponent(classListTable.getSearchFor());
    }

    @Override
    public void detach() {
        super.detach();
    }
}
