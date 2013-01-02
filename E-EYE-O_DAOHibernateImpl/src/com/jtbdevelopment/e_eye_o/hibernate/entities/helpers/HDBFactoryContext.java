package com.jtbdevelopment.e_eye_o.hibernate.entities.helpers;

import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.entities.impl.IdObjectImplFactory;
import com.jtbdevelopment.e_eye_o.hibernate.entities.HDBIdObjectWrapperFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * Date: 1/1/13
 * Time: 8:18 PM
 */
@Service
//  TODO - this is a mess due to hibernate apparantly instantiating dummy objects as part of load time
public class HDBFactoryContext implements ApplicationContextAware {
    private static IdObjectFactory implFactory;
    private static HDBIdObjectWrapperFactory hibernateFactory;
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        //implFactory = applicationContext.getBean(IdObjectFactory.class);
        //hibernateFactory = applicationContext.getBean(HDBIdObjectWrapperFactory.class);
        HDBFactoryContext.applicationContext = applicationContext;
    }

    public static IdObjectFactory getImplFactory() {
        return applicationContext != null ? applicationContext.getBean(IdObjectFactory.class) : new IdObjectImplFactory();
        //return implFactory;
    }


    public static HDBIdObjectWrapperFactory getHibernateFactory() {
        return applicationContext != null ? applicationContext.getBean(HDBIdObjectWrapperFactory.class) : new HDBIdObjectWrapperFactory();
        //return hibernateFactory;
    }

}
