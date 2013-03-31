package com.jtbdevelopment.e_eye_o.ria.vaadin.utils;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;

/**
 * Date: 3/30/13
 * Time: 8:59 PM
 */
public class FieldUtils {
    private static interface Callback {
        public void doWork(final AbstractField component);
    }

    public static void removeAllValidators(final Component component) {
        processAllComponents(component, new Callback() {
            @Override
            public void doWork(final AbstractField component) {
                component.removeAllValidators();
            }
        });
    }

    public static void setImmediateForAll(final Component component, final boolean immediate) {
        processAllComponents(component, new Callback() {
            @Override
            public void doWork(final AbstractField component) {
                component.setImmediate(immediate);
            }
        });
    }

    private static void processAllComponents(final Component topComponent, final Callback callback) {
        if (topComponent instanceof AbstractField) {
            callback.doWork((AbstractField) topComponent);
        }
        if (topComponent instanceof HasComponents) {
            for (Component childComponent : ((HasComponents) topComponent)) {
                processAllComponents(childComponent, callback);
            }
        }
    }
}
