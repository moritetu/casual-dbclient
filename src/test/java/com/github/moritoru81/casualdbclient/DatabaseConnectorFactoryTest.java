package com.github.moritoru81.casualdbclient;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.apache.commons.configuration.ConfigurationException;
import org.junit.Before;
import org.junit.Test;

import com.github.moritoru81.casualdbclient.Database;
import com.github.moritoru81.casualdbclient.DatabaseConnector;
import com.github.moritoru81.casualdbclient.DatabaseConnectorFactory;
import com.github.moritoru81.casualdbclient.HiveConnector;
import com.github.moritoru81.casualdbclient.ImpalaConnector;
import com.github.moritoru81.casualdbclient.MySQLConnector;
import com.github.moritoru81.casualdbclient.OracleConnector;
import com.github.moritoru81.casualdbclient.SQLiteConnector;
import com.github.moritoru81.casualdbclient.SupportDatabase;

public class DatabaseConnectorFactoryTest {

    final String PROPERTY_FILE_NAME = "casualdbclient-database-factory";

    final String PROPERTY_FILE_NAME_NOTEST = "casualdbclient-database-factory-notest"; 

    @Before
    public void before() {
    }

    @Test
    public void testCreateFromDatabase() {
        Database database = new Database("localhost", "test");

        DatabaseConnector dbConnector = DatabaseConnectorFactory.createFromDatabase(database, 
                com.github.moritoru81.casualdbclient.HiveConnector.class);
        assertThat(dbConnector, instanceOf(HiveConnector.class));
        assertThat(dbConnector.database.getHost(), is("localhost"));
    }

    @Test(expected = ConfigurationException.class)
    public void testCreateFromProperty_Not_Exists_PropertyFile() throws Exception {
        DatabaseConnectorFactory.createFromProperty("dummy-not-exists", SupportDatabase.HIVE);
    }

    @Test
    public void testCreateFromProperty() throws Exception {
        DatabaseConnector dbConnector;
        final String propertyValueSuffix = ".localhost.notest";
        dbConnector = DatabaseConnectorFactory.createFromProperty(PROPERTY_FILE_NAME_NOTEST, SupportDatabase.HIVE);
        assertThat(dbConnector, instanceOf(HiveConnector.class));
        assertThat(dbConnector.database.getHost(), is(SupportDatabase.HIVE.toString().toLowerCase() +  propertyValueSuffix));

        dbConnector = DatabaseConnectorFactory.createFromProperty(PROPERTY_FILE_NAME_NOTEST, SupportDatabase.IMPALA);
        assertThat(dbConnector, instanceOf(ImpalaConnector.class));
        assertThat(dbConnector.database.getHost(), is(SupportDatabase.IMPALA.toString().toLowerCase() + propertyValueSuffix));

        dbConnector = DatabaseConnectorFactory.createFromProperty(PROPERTY_FILE_NAME_NOTEST, SupportDatabase.ORACLE);
        assertThat(dbConnector, instanceOf(OracleConnector.class));
        assertThat(dbConnector.database.getHost(), is(SupportDatabase.ORACLE.toString().toLowerCase() + propertyValueSuffix));

        dbConnector = DatabaseConnectorFactory.createFromProperty(PROPERTY_FILE_NAME_NOTEST, SupportDatabase.MYSQL);
        assertThat(dbConnector, instanceOf(MySQLConnector.class));
        assertThat(dbConnector.database.getHost(), is(SupportDatabase.MYSQL.toString().toLowerCase() + propertyValueSuffix));

        dbConnector = DatabaseConnectorFactory.createFromProperty(PROPERTY_FILE_NAME_NOTEST, SupportDatabase.SQLITE);
        assertThat(dbConnector, instanceOf(SQLiteConnector.class));
        assertThat(dbConnector.database.getDatabase(), is(SupportDatabase.SQLITE.toString().toLowerCase() + propertyValueSuffix));
    }

    @Test
    public void testCreateFromProperty_With_DatabaseType() throws Exception {
        DatabaseConnector dbConnector;
        dbConnector = DatabaseConnectorFactory.createFromProperty(PROPERTY_FILE_NAME, SupportDatabase.HIVE);
        assertThat(dbConnector, instanceOf(HiveConnector.class));

        dbConnector = DatabaseConnectorFactory.createFromProperty(PROPERTY_FILE_NAME, SupportDatabase.IMPALA);
        assertThat(dbConnector, instanceOf(ImpalaConnector.class));

        dbConnector = DatabaseConnectorFactory.createFromProperty(PROPERTY_FILE_NAME, SupportDatabase.ORACLE);
        assertThat(dbConnector, instanceOf(OracleConnector.class));

        dbConnector = DatabaseConnectorFactory.createFromProperty(PROPERTY_FILE_NAME, SupportDatabase.MYSQL);
        assertThat(dbConnector, instanceOf(MySQLConnector.class));

        dbConnector = DatabaseConnectorFactory.createFromProperty(PROPERTY_FILE_NAME, SupportDatabase.SQLITE);
        assertThat(dbConnector, instanceOf(SQLiteConnector.class));
    }

    @Test
    public void testCreateFromProperty_With_DatabaseType_Namespace() throws Exception {
        DatabaseConnector dbConnector;
        final String namespace = "namespace";

        dbConnector = DatabaseConnectorFactory.createFromProperty(PROPERTY_FILE_NAME, SupportDatabase.HIVE, namespace);
        assertThat(dbConnector, instanceOf(HiveConnector.class));
        assertThat(dbConnector.database.getHost(), is(SupportDatabase.HIVE.toString().toLowerCase() + "." + namespace));

        dbConnector = DatabaseConnectorFactory.createFromProperty(PROPERTY_FILE_NAME, SupportDatabase.IMPALA, namespace);
        assertThat(dbConnector, instanceOf(ImpalaConnector.class));
        assertThat(dbConnector.database.getHost(), is(SupportDatabase.IMPALA.toString().toLowerCase() + "." + namespace));

        dbConnector = DatabaseConnectorFactory.createFromProperty(PROPERTY_FILE_NAME, SupportDatabase.ORACLE, namespace);
        assertThat(dbConnector, instanceOf(OracleConnector.class));
        assertThat(dbConnector.database.getHost(), is(SupportDatabase.ORACLE.toString().toLowerCase() + "." + namespace));

        dbConnector = DatabaseConnectorFactory.createFromProperty(PROPERTY_FILE_NAME, SupportDatabase.MYSQL, namespace);
        assertThat(dbConnector, instanceOf(MySQLConnector.class));
        assertThat(dbConnector.database.getHost(), is(SupportDatabase.MYSQL.toString().toLowerCase() + "." + namespace));

        dbConnector = DatabaseConnectorFactory.createFromProperty(PROPERTY_FILE_NAME, SupportDatabase.SQLITE, namespace);
        assertThat(dbConnector, instanceOf(SQLiteConnector.class));
        assertThat(dbConnector.database.getDatabase(), is(SupportDatabase.SQLITE.toString().toLowerCase() + "." + namespace));
    }

    @Test
    public void testConnectorCache() throws Exception {
        DatabaseConnectorFactory.clearCache();

        DatabaseConnectorFactory.createFromProperty(PROPERTY_FILE_NAME, SupportDatabase.HIVE);
        DatabaseConnectorFactory.createFromProperty(PROPERTY_FILE_NAME, SupportDatabase.IMPALA);
        DatabaseConnectorFactory.createFromProperty(PROPERTY_FILE_NAME, SupportDatabase.ORACLE);
        DatabaseConnectorFactory.createFromProperty(PROPERTY_FILE_NAME, SupportDatabase.MYSQL);
        DatabaseConnectorFactory.createFromProperty(PROPERTY_FILE_NAME, SupportDatabase.SQLITE);

        DatabaseConnectorFactory.createFromProperty(PROPERTY_FILE_NAME_NOTEST, SupportDatabase.HIVE);
        DatabaseConnectorFactory.createFromProperty(PROPERTY_FILE_NAME_NOTEST, SupportDatabase.IMPALA);
        DatabaseConnectorFactory.createFromProperty(PROPERTY_FILE_NAME_NOTEST, SupportDatabase.ORACLE);
        DatabaseConnectorFactory.createFromProperty(PROPERTY_FILE_NAME_NOTEST, SupportDatabase.MYSQL);
        DatabaseConnectorFactory.createFromProperty(PROPERTY_FILE_NAME_NOTEST, SupportDatabase.SQLITE);

        assertThat(DatabaseConnectorFactory.getCacheSize(), is(SupportDatabase.values().length * 2));

        // After clear, size is 0.
        DatabaseConnectorFactory.clearCache();
        assertThat(DatabaseConnectorFactory.getCacheSize(), is(0));
    }
}
