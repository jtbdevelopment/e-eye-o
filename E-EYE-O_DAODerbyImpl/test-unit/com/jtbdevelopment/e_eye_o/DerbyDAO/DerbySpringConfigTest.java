package com.jtbdevelopment.e_eye_o.DerbyDAO;

import org.apache.derby.jdbc.EmbeddedConnectionPoolDataSource40;
import org.testng.annotations.Test;

import javax.sql.DataSource;
import java.util.Properties;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Date: 1/6/13
 * Time: 9:50 PM
 */
public class DerbySpringConfigTest {
    private final DerbySpringConfig config = new DerbySpringConfig();

    @Test
    public void testDerbyOverrideProperties() throws Exception {
        assertTrue(config.derbyOverrideProperties().isEmpty());
    }

    @Test
    public void testDerbyPropertiesNoOverrides() throws Exception {
        Properties overrides = new Properties();
        Properties props = config.derbyProperties(overrides);
        assertEquals(2, props.size());
        assertEquals(DerbySpringConfig.DEFAULT_DATABASE, props.getProperty(DerbySpringConfig.DATABASE_NAME));
        assertEquals(DerbySpringConfig.DEFAULT_CREATE, props.getProperty(DerbySpringConfig.CREATE_FLAG));
    }

    @Test
    public void testDerbyPropertiesOverrides() throws Exception {
        Properties overrides = new Properties();
        final String newdb = "NEWDB";
        overrides.put(DerbySpringConfig.DATABASE_NAME, newdb);
        final String additional = "additional";
        final String value = "value";
        overrides.put(additional, value);

        Properties props = config.derbyProperties(overrides);
        assertEquals(3, props.size());
        assertEquals(newdb, props.getProperty(DerbySpringConfig.DATABASE_NAME));
        assertEquals(DerbySpringConfig.DEFAULT_CREATE, props.getProperty(DerbySpringConfig.CREATE_FLAG));
        assertEquals(value, props.getProperty(additional));
    }

    @Test
    public void testHibernateDialect() throws Exception {
        assertEquals(DerbyUniqueNonNull.class.getCanonicalName(), config.hibernateDialect());
    }

    @Test
    public void testDataSource() throws Exception {
        Properties properties = new Properties();
        final String test = "TEST";
        properties.put(DerbySpringConfig.DATABASE_NAME, test);
        final String aTrue = "badValue";
        properties.put(DerbySpringConfig.CREATE_FLAG, aTrue);
        DataSource ds = config.dataSource(config.derbyProperties(properties));
        assertTrue(ds instanceof EmbeddedConnectionPoolDataSource40);
        EmbeddedConnectionPoolDataSource40 eDs = (EmbeddedConnectionPoolDataSource40) ds;
        assertEquals(test, eDs.getDatabaseName());
        assertEquals(null, eDs.getCreateDatabase());
    }
}
