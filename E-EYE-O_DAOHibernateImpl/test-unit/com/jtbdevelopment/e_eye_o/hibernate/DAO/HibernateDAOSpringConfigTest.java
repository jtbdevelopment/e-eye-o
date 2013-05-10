package com.jtbdevelopment.e_eye_o.hibernate.DAO;

import org.hibernate.SessionFactory;
import org.jmock.Mockery;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.testng.annotations.Test;

import javax.sql.DataSource;
import java.util.Properties;

import static org.testng.AssertJUnit.assertSame;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Date: 1/6/13
 * Time: 9:28 PM
 */
public class HibernateDAOSpringConfigTest {
    private final HibernateDAOSpringConfig config = new HibernateDAOSpringConfig();
    private final Mockery context = new Mockery();
    private final SessionFactory sessionFactory = context.mock(SessionFactory.class);
    private final DataSource dataSource = context.mock(DataSource.class);

    @Test
    public void testTimestampIntercepter() {
        assertTrue(config.timestampInterceptor() instanceof ModificationTimestampGenerator);
    }

    @Test
    public void testSessionFactory() throws Exception {
        Properties props = new Properties();
        LocalSessionFactoryBean sf = config.sessionFactory(dataSource, props, null, null);
        assertSame(props, sf.getHibernateProperties());
        //  Can't currently verify packages to scan or data source
    }

    @Test
    public void testTransactionManager() throws Exception {
        AbstractPlatformTransactionManager mgr = config.transactionManager(sessionFactory);
        assertTrue(mgr instanceof HibernateTransactionManager);
    }
}
