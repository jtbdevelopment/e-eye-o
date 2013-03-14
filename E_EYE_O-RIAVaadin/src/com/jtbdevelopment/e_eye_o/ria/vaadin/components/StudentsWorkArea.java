package com.jtbdevelopment.e_eye_o.ria.vaadin.components;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.entities.Student;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.*;

/**
 * Date: 3/10/13
 * Time: 4:40 PM
 */
public class StudentsWorkArea extends CustomComponent {

    @AutoGenerated
    private VerticalLayout mainLayout;

    private final ReadWriteDAO readWriteDAO;

    private final IdObjectFactory idObjectFactory;

    private final AppUser appUser;

    /**
     * The constructor should first build the main layout, set the
     * composition root and then do any custom initialization.
     * <p/>
     * The constructor will not be automatically regenerated by the
     * visual editor.
     */
    public StudentsWorkArea(final ReadWriteDAO readWriteDAO, final IdObjectFactory idObjectFactory, final AppUser appUser) {
        this.readWriteDAO = readWriteDAO;
        this.appUser = appUser;
        this.idObjectFactory = idObjectFactory;
        buildMainLayout();
        setCompositionRoot(mainLayout);

    }

    @AutoGenerated
    private void buildMainLayout() {
        // the main layout and components will be created here
        mainLayout = new VerticalLayout();
        mainLayout.setImmediate(true);

        final BeanItemContainer<Student> students = new BeanItemContainer<>(Student.class, readWriteDAO.getEntitiesForUser(Student.class, appUser));
        final Table table = new Table();
        table.setContainerDataSource(students);
        table.setColumnReorderingAllowed(true);
        table.setSortEnabled(true);
        table.setSelectable(true);
        table.setPageLength(Math.min(10, students.size()));
        table.setSizeFull();
        table.setNullSelectionAllowed(false);
        table.setVisibleColumns(new String[]{"firstName", "lastName", "archived", "modificationTimestamp"});
        table.setColumnHeaders(new String[]{"First Name", "Last Name", "Archived", "Last Update"});

//        final StudentForm studentForm = new StudentForm(readWriteDAO, idObjectFactory, appUser, null);

//        mainLayout.addComponent(studentForm);
        mainLayout.addComponent(table);
        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void itemClick(final ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    Object item = event.getItem();
                    if (item instanceof BeanItem) {
                        Window window = new Window();
                        window.setSizeUndefined();
                        window.setModal(true);
                        window.setWidth(75, Unit.PERCENTAGE);
                        window.setHeight(30, Unit.PERCENTAGE);
                        final Student student = ((BeanItem<Student>) item).getBean();
                        window.setContent(new StudentForm(readWriteDAO, idObjectFactory, appUser, student));
                        getUI().addWindow(window);
                        final Student idForBean = students.getBeanIdResolver().getIdForBean(student);
                        if (idForBean != null)
                            students.removeItem(idForBean);
                        students.addBean(student);
//                        studentForm.setStudent(student);
                    } else {
                        Notification.show("Not sure what this is - " + (item == null ? "null" : item.toString()));
                    }
                }
            }
        });

    }

}