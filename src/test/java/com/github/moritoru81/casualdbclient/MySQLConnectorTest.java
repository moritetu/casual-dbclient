package com.github.moritoru81.casualdbclient;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import com.github.moritoru81.casualdbclient.Database;
import com.github.moritoru81.casualdbclient.DatabaseConnector;
import com.github.moritoru81.casualdbclient.MySQLConnector;

public class MySQLConnectorTest {

    Database database = new Database("localhost", "db", "user", "pass");

    @Test
    public void testGetUrl() {
        DatabaseConnector dbConnecor = new MySQLConnector(database);
        assertThat(dbConnecor.getUrl(), is("jdbc:mysql://localhost:3306/db?user=user&password=pass"));
    }

}
