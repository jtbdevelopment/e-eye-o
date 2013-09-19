package com.jtbdevelopment.e_eye_o.ria.vaadin.components.quicktour;

import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.ComponentUtils;
import com.vaadin.event.MouseEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * Date: 9/17/13
 * Time: 11:13 PM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QuickTour extends CustomComponent {

    static final private ThemeResource left = new ThemeResource("../runo/icons/64/arrow-left.png");
    static final private ThemeResource right = new ThemeResource("../runo/icons/64/arrow-right.png");
    static final List<ThemeResource> images = Arrays.asList(
            new ThemeResource("quicktour/categories.png"),
            new ThemeResource("quicktour/classes.png"),
            new ThemeResource("quicktour/students.png"),
            new ThemeResource("quicktour/observe.png"),
            new ThemeResource("quicktour/report.png")

    );
    static final List<String> captions = Arrays.asList(
            "Create your tracking categories..",
            "Setup your classes, especially if you have more than one..",
            "Setup your student list..",
            "Observe..",
            "Report!"
    );
    private int counter = 0;
    private Embedded embedded = new Embedded(captions.get(0), images.get(0));

    @PostConstruct
    public void postConstruct() {
        setSizeFull();
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        final Embedded leftEmbedded = new Embedded(null, left);
        leftEmbedded.setEnabled(false);
        horizontalLayout.addComponent(leftEmbedded);
        horizontalLayout.addComponent(embedded);
        final Embedded rightEmbedded = new Embedded(null, right);
        horizontalLayout.addComponent(rightEmbedded);
        horizontalLayout.setComponentAlignment(leftEmbedded, Alignment.MIDDLE_CENTER);
        horizontalLayout.setComponentAlignment(embedded, Alignment.MIDDLE_CENTER);
        horizontalLayout.setComponentAlignment(rightEmbedded, Alignment.MIDDLE_CENTER);
        horizontalLayout.setExpandRatio(embedded, 1);
        horizontalLayout.setSizeFull();
        horizontalLayout.setMargin(true);

        verticalLayout.addComponent(horizontalLayout);
        verticalLayout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);
        setCompositionRoot(verticalLayout);
        ComponentUtils.setImmediateForAll(this, true);

        leftEmbedded.addClickListener(new MouseEvents.ClickListener() {
            @Override
            public void click(MouseEvents.ClickEvent event) {
                --counter;
                updateImage(leftEmbedded, rightEmbedded);
            }
        });
        rightEmbedded.addClickListener(new MouseEvents.ClickListener() {
            @Override
            public void click(MouseEvents.ClickEvent event) {
                ++counter;
                updateImage(leftEmbedded, rightEmbedded);
            }
        });
    }

    private void updateImage(Embedded leftEmbedded, Embedded rightEmbedded) {
        embedded.setSource(images.get(counter));
        embedded.setCaption(captions.get(counter));
        if (counter == 0) {
            leftEmbedded.setEnabled(false);
        } else {
            leftEmbedded.setEnabled(true);
        }
        if (counter < images.size() - 1) {
            rightEmbedded.setEnabled(true);
        } else {
            rightEmbedded.setEnabled(false);
        }
    }
}
