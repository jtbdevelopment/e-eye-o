package com.jtbdevelopment.e_eye_o.ria.vaadin.components.photoalbum;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Runo;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.Arrays;

/**
 * Date: 3/23/13
 * Time: 8:17 PM
 */
@org.springframework.stereotype.Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//  TODO - pretty much everything
public class PhotoAlbum extends CustomComponent {
    public PhotoAlbum() {
        GridLayout photos = new GridLayout(4, 2);
        photos.setWidth("100%");
        photos.setMargin(true);

        for (String string : Arrays.asList(
                "dummyphotos/3-MostParts.JPG",
                "dummyphotos/4-MastAndBoom.JPG",
                "dummyphotos/3-MostParts.JPG",
                "dummyphotos/5-Drying.JPG",
                "dummyphotos/6-Finished.jpg",
                "dummyphotos/7-TensionControls.jpg"
        )) {
            final ThemeResource source = new ThemeResource(string);
            final Embedded photo = new Embedded(null, source);
            photo.setAlternateText(string);
            photo.setHeight(100, Unit.PIXELS);
            photo.setWidth(100, Unit.PIXELS);

            VerticalLayout photoAndText = new VerticalLayout();
            photoAndText.addComponent(photo);
            Label text = new Label(string);
            text.setWidth(null);
            photoAndText.addComponent(text);
            photoAndText.setComponentAlignment(photo, Alignment.MIDDLE_CENTER);
            photoAndText.setComponentAlignment(text, Alignment.MIDDLE_CENTER);

            CssLayout photoLayout = new CssLayout();
            photoLayout.addComponent(photoAndText);

            CssLayout select = new CssLayout();
            select.addStyleName(Runo.CSSLAYOUT_SELECTABLE);
            select.addComponent(photoLayout);

            photos.addComponent(select);
            photos.setComponentAlignment(select, Alignment.MIDDLE_CENTER);
        }
        photos.setImmediate(true);
        photos.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            private com.vaadin.ui.Component lastSelected = null;

            @Override
            public void layoutClick(final LayoutEvents.LayoutClickEvent event) {
                if (lastSelected != null) {
                    lastSelected.removeStyleName(Runo.CSSLAYOUT_SELECTABLE_SELECTED);
                }
                lastSelected = event.getChildComponent();
                if (lastSelected != null) {
                    lastSelected.addStyleName(Runo.CSSLAYOUT_SELECTABLE_SELECTED);
                }
                if (event.isDoubleClick()) {
                    com.vaadin.ui.Component clicked = event.getClickedComponent();
                    while (clicked instanceof CssLayout) {
                        clicked = ((CssLayout) clicked).getComponent(0);
                    }
                    if (clicked instanceof Embedded) {
                        Window window = new Window();
                        Panel panel = new Panel();
                        panel.setSizeFull();
                        Embedded bigPhoto = new Embedded();
                        bigPhoto.setSource(((Embedded) clicked).getSource());
                        bigPhoto.setAlternateText(((Embedded) clicked).getAlternateText());
                        bigPhoto.setSizeFull();
                        panel.setContent(bigPhoto);
                        window.setContent(panel);
                        window.setModal(false);
                        window.addStyleName(Runo.WINDOW_DIALOG);
                        window.setSizeFull();
                        window.setCaption(bigPhoto.getAlternateText());
                        getUI().addWindow(window);
                    }
                }
            }
        });
        setCompositionRoot(photos);
        setSizeFull();

    }
}
