package com.github.moritoru81.casualdbclient;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import com.github.moritoru81.casualdbclient.Database;
import com.github.moritoru81.casualdbclient.DatabaseConnector;
import com.github.moritoru81.casualdbclient.OracleConnector;

public class OracleConnectorTest {

    Database database = new Database("localhost", "db", "testuser", "testpass");

    @Test
    public void testGetUrl() {
        DatabaseConnector dbConnector = new OracleConnector(database);
        assertThat(dbConnector.getUrl(), is("jdbc:oracle:thin:@localhost:1521:db"));
    }

}
