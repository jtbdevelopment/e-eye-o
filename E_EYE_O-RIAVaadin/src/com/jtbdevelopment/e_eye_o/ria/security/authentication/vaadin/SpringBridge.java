package com.jtbdevelopment.e_eye_o.ria.security.authentication.vaadin;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * Date: 2/25/13
 * Time: 10:55 PM
 */
@Service
public class SpringBridge implements ApplicationContextAware {
    private static ApplicationContext context;

    public static <T> T getBean(final Class<T> beanType) {
        return context.getBean(beanType);
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
