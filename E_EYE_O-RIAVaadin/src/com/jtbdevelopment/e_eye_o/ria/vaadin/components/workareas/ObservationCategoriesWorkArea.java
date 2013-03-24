package com.jtbdevelopment.e_eye_o.ria.vaadin.components.workareas;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.Observation;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.IdObjectTable;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.ObservationCategoryTable;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.ObservationWithSubjectTable;
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
public class ObservationCategoriesWorkArea extends CustomComponent {
    private ObservationCategoryTable observationCategoryTable;

    @Autowired
    public ObservationCategoriesWorkArea(final ObservationCategoryTable observationCategoryTable, final ObservationWithSubjectTable observationTable) {
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setImmediate(true);
        mainLayout.setSpacing(true);

        this.observationCategoryTable = observationCategoryTable;
        observationCategoryTable.setClickedOnListener(new IdObjectTable.ClickedOnListener<ObservationCategory>() {
            @Override
            public void handleClickEvent(final ObservationCategory entity) {
                observationTable.setTableDriver(entity);
            }
        });
        mainLayout.addComponent(observationCategoryTable);

        observationTable.setClickedOnListener(new IdObjectTable.ClickedOnListener<Observation>() {
            @Override
            public void handleClickEvent(final Observation entity) {
                //  TODO if even needed
            }
        });
        mainLayout.addComponent(observationTable);

        setCompositionRoot(mainLayout);
    }

    @Override
    public void attach() {
        super.attach();
        final AppUser appUser = getSession().getAttribute(AppUser.class);
        observationCategoryTable.setTableDriver(appUser);
        getUI().setFocusedComponent(observationCategoryTable.getSearchFor());
    }

    @Override
    public void detach() {
        super.detach();
    }
}
