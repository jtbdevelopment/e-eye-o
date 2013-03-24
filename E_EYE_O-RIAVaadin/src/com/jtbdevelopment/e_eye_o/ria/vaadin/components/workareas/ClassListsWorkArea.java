package com.jtbdevelopment.e_eye_o.ria.vaadin.components.workareas;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.ClassList;
import com.jtbdevelopment.e_eye_o.entities.Student;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.ClassListTable;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.IdObjectTable;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.StudentTable;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.photoalbum.PhotoAlbum;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.TabSheet;
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
public class ClassListsWorkArea extends CustomComponent {
    private ClassListTable classListTable;

    @Autowired
    public ClassListsWorkArea(final ClassListTable classListTable, final StudentTable studentTable, final PhotoAlbum photoAlbum) {
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setImmediate(true);
        mainLayout.setSpacing(true);

        this.classListTable = classListTable;
        classListTable.setClickedOnListener(new IdObjectTable.ClickedOnListener<ClassList>() {
            @Override
            public void handleClickEvent(final ClassList entity) {
                studentTable.setTableDriver(entity);
            }
        });
        mainLayout.addComponent(classListTable);

        studentTable.setClickedOnListener(new IdObjectTable.ClickedOnListener<Student>() {
            @Override
            public void handleClickEvent(final Student entity) {
                //  TODO if even needed
            }
        });
        TabSheet tabSheet = new TabSheet();
        tabSheet.addTab(studentTable).setCaption("Students");
        tabSheet.addTab(photoAlbum).setCaption("Photos");
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