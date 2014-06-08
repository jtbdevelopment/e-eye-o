package com.jtbdevelopment.e_eye_o.ria.vaadin;

import com.vaadin.server.*;
import com.vaadin.ui.*;
import org.springframework.security.access.AccessDeniedException;

import javax.servlet.http.HttpServletRequest;

/**
 * Date: 3/3/13
 * Time: 9:10 PM
 */
public abstract class EEYEOErrorHandlingUI extends UI implements ErrorHandler {

    @Override
    protected void init(final VaadinRequest request) {
        getSession().setErrorHandler(this);
        if (request instanceof VaadinServletRequest) {
            HttpServletRequest httpRequest = ((VaadinServletRequest) request).getHttpServletRequest();
            String userAgent = httpRequest.getHeader("User-Agent").toLowerCase();

            if (!userAgent.contains("firefox") && !userAgent.contains("chrome") && !userAgent.contains("chromium")) {
                Notification.show("WARNING:  Currently optimized for Chrome or Firefox - views may be odd under other browsers.", Notification.Type.TRAY_NOTIFICATION);
            }

        }
    }

    @Override
    public void error(final com.vaadin.server.ErrorEvent event) {
        // connector event
        if (event.getThrowable().getCause() instanceof AccessDeniedException) {
            AccessDeniedException accessDeniedException = (AccessDeniedException) event.getThrowable().getCause();
            Notification.show(accessDeniedException.getMessage(), Notification.Type.ERROR_MESSAGE);

            // Cleanup view. Now Vaadin ignores errors and always shows the view.  :-(
            // since beta10
            setContent(null);
            return;
        }

        // Error on page load. Now it doesn't work. User sees standard error page.
        if (event.getThrowable() instanceof AccessDeniedException) {
            AccessDeniedException exception = (AccessDeniedException) event.getThrowable();

            Label label = new Label(exception.getMessage());
            label.setWidth(-1, Unit.PERCENTAGE);

            Link goToMain = new Link("Go to main", new ExternalResource("/"));

            VerticalLayout layout = new VerticalLayout();
            layout.addComponent(label);
            layout.addComponent(goToMain);
            layout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
            layout.setComponentAlignment(goToMain, Alignment.MIDDLE_CENTER);

            VerticalLayout mainLayout = new VerticalLayout();
            mainLayout.setSizeFull();
            mainLayout.addComponent(layout);
            mainLayout.setComponentAlignment(layout, Alignment.MIDDLE_CENTER);

            setContent(mainLayout);
            Notification.show(exception.getMessage(), Notification.Type.ERROR_MESSAGE);
            return;
        }

        DefaultErrorHandler.doDefault(event);
    }
}
