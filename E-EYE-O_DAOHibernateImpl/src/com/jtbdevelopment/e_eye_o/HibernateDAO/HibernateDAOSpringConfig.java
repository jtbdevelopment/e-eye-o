package com.jtbdevelopment.e_eye_o.HibernateDAO;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Date: 11/19/12
 * Time: 5:26 PM
 */
@Configuration
@ImportResource(value = "classpath*:spring-config.xml")
public class HibernateDAOSpringConfig {

    @Bean
    public Properties hibernateOverrideProperties() {
        return new Properties();
    }

    @Bean
    @Autowired
    public Properties hibernateProperties(final String hibernateDialect, final @Qualifier("hibernateOverrideProperties") Properties hibernateOverrideProperties) {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", hibernateDialect);
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.format_sql", "false");
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.putAll(hibernateOverrideProperties);
        return properties;
    }

    @Bean
    @Autowired
    public LocalSessionFactoryBean sessionFactory(final javax.sql.DataSource dataSource, final @Qualifier("hibernateProperties") Properties hibernateProperties) {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setHibernateProperties(hibernateProperties);
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setPackagesToScan("com.jtbdevelopment");
        return sessionFactoryBean;
    }

    @Bean
    public AbstractPlatformTransactionManager transactionManager(final SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }
}
