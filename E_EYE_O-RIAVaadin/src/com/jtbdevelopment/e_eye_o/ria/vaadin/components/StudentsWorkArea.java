package com.jtbdevelopment.e_eye_o.ria.vaadin.components;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.entities.Student;
import com.jtbdevelopment.e_eye_o.ria.vaadin.events.IdObjectChanged;
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

    private final EventBus eventBus;

    private final BeanItemContainer<Student> students;

    private final Table table;

    /**
     * The constructor should first build the main layout, set the
     * composition root and then do any custom initialization.
     * <p/>
     * The constructor will not be automatically regenerated by the
     * visual editor.
     */
    public StudentsWorkArea(final ReadWriteDAO readWriteDAO, final IdObjectFactory idObjectFactory, final EventBus eventBus, final AppUser appUser) {
        this.readWriteDAO = readWriteDAO;
        this.eventBus = eventBus;
        this.appUser = appUser;
        this.idObjectFactory = idObjectFactory;
        students = new BeanItemContainer<>(Student.class, readWriteDAO.getEntitiesForUser(Student.class, appUser));
        table = new Table();
        buildMainLayout();
        setCompositionRoot(mainLayout);

    }

    @AutoGenerated
    private void buildMainLayout() {
        // the main layout and components will be created here
        mainLayout = new VerticalLayout();
        mainLayout.setImmediate(true);

        table.setContainerDataSource(students);
        table.setColumnReorderingAllowed(true);
        table.setSortEnabled(true);
        table.setSelectable(true);
        table.setPageLength(getTableRows());
        table.setSizeFull();
        table.setNullSelectionAllowed(false);
        table.setVisibleColumns(new String[]{"firstName", "lastName", "archived", "modificationTimestamp"});
        table.setColumnHeaders(new String[]{"First Name", "Last Name", "Archived", "Last Update"});

        mainLayout.addComponent(table);
        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void itemClick(final ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    Object item = event.getItem();
                    if (item instanceof BeanItem) {
                        Student student = ((BeanItem<Student>) item).getBean();
                        popupStudentEditor(student);
                    } else {
                        Notification.show("Not sure what this is - " + (item == null ? "null" : item.toString()));
                    }
                }
            }
        });

        Button newStudentButton = new Button("New Student");
        newStudentButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                popupStudentEditor(null);
            }
        });
        mainLayout.addComponent(newStudentButton);
    }

    private int getTableRows() {
        return Math.min(10, students.size());
    }

    private void popupStudentEditor(final Student student) {
        Window window = new Window();
        window.setSizeUndefined();
        window.setModal(true);
        window.setWidth(75, Unit.PERCENTAGE);
        window.setHeight(30, Unit.PERCENTAGE);
        window.setContent(new StudentForm(readWriteDAO, idObjectFactory, eventBus, appUser, student));
        getUI().addWindow(window);
        getUI().setFocusedComponent(window);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void handleIdObjectChange(final IdObjectChanged msg) {
        if (Student.class.isAssignableFrom(msg.getEntityType())) {
            Student student = (Student) msg.getEntity();
            final Student idForBean = students.getBeanIdResolver().getIdForBean(student);
            if (idForBean != null)
                students.removeItem(idForBean);
            students.addBean(student);
            table.sort();
            table.setPageLength(getTableRows());
        }

    }

    @Override
    public void attach() {
        super.attach();
        eventBus.register(this);
    }

    @Override
    public void detach() {
        eventBus.unregister(this);
        super.detach();
    }
}
