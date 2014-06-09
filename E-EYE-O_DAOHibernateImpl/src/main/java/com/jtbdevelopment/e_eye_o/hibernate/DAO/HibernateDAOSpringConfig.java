package com.jtbdevelopment.e_eye_o.hibernate.DAO;

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
@Configuration
@ImportResource(value = "classpath*:/spring-context-hibernate.xml")
public class HibernateDAOSpringConfig {

    @Bean
    @Autowired
    public LocalSessionFactoryBean sessionFactory(final javax.sql.DataSource dataSource,
                                                  final @Qualifier("hibernateDefaultProperties") Properties hibernateDefaultProperties,
                                                  final @Qualifier("hibernateOverrideProperties") Properties hibernateOverrideProperties) {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setHibernateProperties(buildHibernateProperties(hibernateDefaultProperties, hibernateOverrideProperties));
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setPackagesToScan("com.jtbdevelopment");
        return sessionFactoryBean;
    }

    private Properties buildHibernateProperties(final Properties defaultProperties, final Properties overrideProperties) {
        Properties properties = new Properties();
        properties.putAll(defaultProperties);
        if (overrideProperties != null) {
            properties.putAll(overrideProperties);
        }
        return properties;
    }

    @Bean
    public AbstractPlatformTransactionManager transactionManager(final SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }
}
