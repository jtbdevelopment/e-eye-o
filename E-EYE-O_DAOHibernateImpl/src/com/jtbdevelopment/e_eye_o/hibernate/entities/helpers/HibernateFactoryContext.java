package com.jtbdevelopment.e_eye_o.hibernate.entities.helpers;

import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.entities.wrapper.DAOIdObjectWrapperFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * Date: 1/1/13
 * Time: 8:18 PM
 */
@Service
public class HibernateFactoryContext implements ApplicationContextAware {
    private static IdObjectFactory implFactory;
    private static DAOIdObjectWrapperFactory daoIdObjectWrapperFactory;

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        implFactory = applicationContext.getBean(IdObjectFactory.class);
        daoIdObjectWrapperFactory = applicationContext.getBean(DAOIdObjectWrapperFactory.class);
    }

    public static IdObjectFactory getImplFactory() {
        return implFactory;
    }


    public static DAOIdObjectWrapperFactory getDaoIdObjectWrapperFactory() {
        return daoIdObjectWrapperFactory;
    }
}
