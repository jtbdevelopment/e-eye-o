package com.jtbdevelopment.e_eye_o.ria.vaadin.components;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.entities.Student;
import com.jtbdevelopment.e_eye_o.ria.vaadin.events.TreeSelectionChanged;
import com.jtbdevelopment.e_eye_o.ria.vaadin.widgets.IdObjectTreeView;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;

/**
 * Date: 3/6/13
 * Time: 12:13 AM
 */
public class WorkAreaComposite extends CustomComponent {

    private HorizontalSplitPanel mainLayout;

    private ReadOnlyDAO readOnlyDAO;

    private IdObjectFactory idObjectFactory;

    private final EventBus eventBus = new EventBus();

    /**
     * The constructor should first build the main layout, set the
     * composition root and then do any custom initialization.
     * <p/>
     * The constructor will not be automatically regenerated by the
     * visual editor.
     */
    public WorkAreaComposite(final ReadOnlyDAO readOnlyDAO, final IdObjectFactory idObjectFactory, final AppUser appUser) {
        eventBus.register(this);
        this.readOnlyDAO = readOnlyDAO;
        this.idObjectFactory = idObjectFactory;
        buildMainLayout(appUser);
        setCompositionRoot(mainLayout);
    }

    private void buildMainLayout(final AppUser appUser) {
        setSizeFull();
        // the main layout and components will be created here
        mainLayout = new HorizontalSplitPanel();
        mainLayout.setSplitPosition(15);
        mainLayout.setSizeFull();
        mainLayout.setStyleName(Runo.SPLITPANEL_SMALL);

        VerticalLayout trees = new VerticalLayout();
        mainLayout.setFirstComponent(trees);
        trees.addComponent(new IdObjectTreeView(appUser, readOnlyDAO, eventBus, "Active", false));
        trees.addComponent(new IdObjectTreeView(appUser, readOnlyDAO, eventBus, "Archived", true));
    }

    @Subscribe
    public void treeSelectionChange(final TreeSelectionChanged treeSelectionChanged) {
        if (Student.class.isAssignableFrom(treeSelectionChanged.getEntityType())) {
            Student s = (Student) readOnlyDAO.get(treeSelectionChanged.getEntityType(), treeSelectionChanged.getEntityId());
            if (s == null) {
                s = idObjectFactory.newStudent(getSession().getAttribute(AppUser.class));
            }
            mainLayout.setSecondComponent(new StudentForm(s));
        } else {
            Label eventLabel = new Label("Something");
            eventLabel.setValue(treeSelectionChanged.getEntityId());
            mainLayout.setSecondComponent(eventLabel);
        }
    }
}
