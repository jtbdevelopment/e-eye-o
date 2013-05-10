package com.jtbdevelopment.e_eye_o.hibernate.DAO;

import org.hibernate.Interceptor;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

import java.util.Properties;

/**
 * Date: 11/19/12
 * Time: 5:26 PM
 */

//  TODO - connection pool

@Configuration
@ImportResource(value = "classpath*:spring-context*.xml")
public class HibernateDAOSpringConfig {

    @Bean
    public Interceptor timestampInterceptor() {
        return new ModificationTimestampGenerator();
    }

    @Bean
    @Autowired
    public LocalSessionFactoryBean sessionFactory(final javax.sql.DataSource dataSource, final @Qualifier("hibernateProperties") Properties hibernateProperties, final @Qualifier("hibernateOverrideProperties") Properties hibernateOverrideProperties, final Interceptor timestampInterceptor) {
        Properties properties = new Properties();
        properties.putAll(hibernateProperties);
        if (hibernateOverrideProperties != null) {
            properties.putAll(hibernateOverrideProperties);
        }
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setHibernateProperties(properties);
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setEntityInterceptor(timestampInterceptor);
        sessionFactoryBean.setPackagesToScan("com.jtbdevelopment");
        return sessionFactoryBean;
    }

    @Bean
    public AbstractPlatformTransactionManager transactionManager(final SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }
}
