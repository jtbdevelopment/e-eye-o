package com.jtbdevelopment.e_eye_o.ria.vaadin.components.workareas;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.Observation;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.IdObjectTable;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.ObservationTable;
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
public class ObservationsWorkArea extends CustomComponent {
    private ObservationTable observationTable;

    @Autowired
    public ObservationsWorkArea(final ObservationTable observationTable, final PhotoAlbum photoAlbum) {
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setImmediate(true);
        mainLayout.setSpacing(true);

        this.observationTable = observationTable;
        observationTable.setClickedOnListener(new IdObjectTable.ClickedOnListener<Observation>() {
            @Override
            public void handleClickEvent(final Observation entity) {
                //  TODO change photos
            }
        });
        mainLayout.addComponent(observationTable);

        mainLayout.addComponent(photoAlbum);

        setCompositionRoot(mainLayout);
    }

    @Override
    public void attach() {
        super.attach();
        final AppUser appUser = getSession().getAttribute(AppUser.class);
        observationTable.setTableDriver(appUser);
        getUI().setFocusedComponent(observationTable.getSearchFor());
    }

    @Override
    public void detach() {
        super.detach();
    }
}
