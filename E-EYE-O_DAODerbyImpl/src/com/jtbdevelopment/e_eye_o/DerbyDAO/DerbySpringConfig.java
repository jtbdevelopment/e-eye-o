package com.jtbdevelopment.e_eye_o.DerbyDAO;

import org.apache.derby.jdbc.EmbeddedConnectionPoolDataSource40;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Date: 11/18/12
 * Time: 10:55 PM
 */
@Configuration
@ImportResource(value = "classpath*:spring-config.xml")
public class DerbySpringConfig {
    @Bean
    public javax.sql.DataSource embeddedDerbyDataSource() {
        EmbeddedConnectionPoolDataSource40 embeddedDerby = new EmbeddedConnectionPoolDataSource40();
        embeddedDerby.setDatabaseName("EEYEODB");
        embeddedDerby.setCreateDatabase("create");
        return embeddedDerby;
    }

}
