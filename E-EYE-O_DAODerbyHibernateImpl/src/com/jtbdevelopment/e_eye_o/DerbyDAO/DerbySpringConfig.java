package com.jtbdevelopment.e_eye_o.DerbyDAO;

import org.apache.derby.jdbc.EmbeddedConnectionPoolDataSource40;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import java.util.Properties;

/**
 * Date: 11/18/12
 * Time: 10:55 PM
 */
@Configuration
@ImportResource(value = "classpath*:spring-config.xml")
public class DerbySpringConfig {
    public final static String DEFAULT_DATABASE = "EEYEODB";
    public final static String DATABASE_NAME = "derbyDatabaseName";
    public final static String CREATE_FLAG = "derbyCreateDatabase";
    public final static String DEFAULT_CREATE = "create";

    @Bean
    public Properties derbyOverrideProperties() {
        return new Properties();
    }

    @Bean
    public Properties derbyProperties(final @Qualifier("derbyOverrideProperties") Properties derbyOverrideProperties) {
        Properties properties = new Properties();
        properties.setProperty(DATABASE_NAME, DEFAULT_DATABASE);
        properties.setProperty(CREATE_FLAG, DEFAULT_CREATE);
        properties.putAll(derbyOverrideProperties);
        return properties;
    }

    @Bean
    public String hibernateDialect() {
        return DerbyUniqueNonNull.class.getName();
    }

    @Bean
    @Autowired
    public javax.sql.DataSource dataSource(final @Qualifier("derbyProperties") Properties derbyProperties) {
        EmbeddedConnectionPoolDataSource40 embeddedDerby = new EmbeddedConnectionPoolDataSource40();
        embeddedDerby.setDatabaseName((String) derbyProperties.get(DATABASE_NAME));
        embeddedDerby.setCreateDatabase((String) derbyProperties.get(CREATE_FLAG));
        return embeddedDerby;
    }

}
