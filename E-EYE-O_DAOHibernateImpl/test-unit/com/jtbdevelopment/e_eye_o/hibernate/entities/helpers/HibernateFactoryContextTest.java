package com.jtbdevelopment.e_eye_o.hibernate.entities.helpers;

import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.entities.wrapper.DAOIdObjectWrapperFactory;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.springframework.context.ApplicationContext;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertSame;

/**
 * Date: 1/6/13
 * Time: 3:03 PM
 */
public class HibernateFactoryContextTest {
    private final Mockery context = new Mockery();
    private final ApplicationContext appContext = context.mock(ApplicationContext.class);
    private final IdObjectFactory idObjectFactory = context.mock(IdObjectFactory.class);
    private final DAOIdObjectWrapperFactory daoIdObjectWrapperFactory = context.mock(DAOIdObjectWrapperFactory.class);
    private final HibernateFactoryContext factoryContext = new HibernateFactoryContext();

    @Test()
    public void testInitialValues() throws Exception {
        assertNull(HibernateFactoryContext.getDaoIdObjectWrapperFactory());
        assertNull(HibernateFactoryContext.getImplFactory());
    }

    @Test(dependsOnMethods = {"testInitialValues"})
    public void testValuesAfterAppContext() throws Exception {
        context.checking(new Expectations(){{
            one(appContext).getBean(IdObjectFactory.class);
            will(returnValue(idObjectFactory));
            one(appContext).getBean(DAOIdObjectWrapperFactory.class);
            will(returnValue(daoIdObjectWrapperFactory));
        }});

        factoryContext.setApplicationContext(appContext);
        assertSame(idObjectFactory, HibernateFactoryContext.getImplFactory());
        assertSame(daoIdObjectWrapperFactory, HibernateFactoryContext.getDaoIdObjectWrapperFactory());
    }
}
