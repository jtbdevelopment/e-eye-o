package com.jtbdevelopment.e_eye_o.ria.vaadin.components.workareas;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.Observation;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.IdObjectTable;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.ObservationCategoryTable;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.ObservationWithSubjectTable;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.photoalbum.PhotoAlbum;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Date: 3/10/13
 * Time: 4:40 PM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ObservationCategoriesWorkArea extends CustomComponent {
    @Autowired
    private ObservationCategoryTable observationCategoryTable;

    @Autowired
    private ObservationWithSubjectTable observationTable;

    @Autowired
    private PhotoAlbum photoAlbum;

    @PostConstruct
    public void postConstruct() {
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setImmediate(true);
        mainLayout.setSpacing(true);

        observationCategoryTable.setClickedOnListener(new IdObjectTable.ClickedOnListener<ObservationCategory>() {
            @Override
            public void handleClickEvent(final ObservationCategory entity) {
                observationTable.setDisplayDriver(entity);
            }
        });
        mainLayout.addComponent(observationCategoryTable);

        observationTable.setClickedOnListener(new IdObjectTable.ClickedOnListener<Observation>() {
            @Override
            public void handleClickEvent(final Observation entity) {
                photoAlbum.setDisplayDriver(entity);
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
        observationCategoryTable.setDisplayDriver(appUser);
        getUI().setFocusedComponent(observationCategoryTable.getSearchFor());
    }
}
