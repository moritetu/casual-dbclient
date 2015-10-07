package com.github.moritoru81.casualdbclient;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import com.github.moritoru81.casualdbclient.Database;
import com.github.moritoru81.casualdbclient.DatabaseConnector;
import com.github.moritoru81.casualdbclient.HiveConnector;

public class HiveConnectorTest {

    @Test
    public void testGetUrl() {
        DatabaseConnector dbConnecor = new HiveConnector(new Database("localhost", "db", "user", "pass"));
        assertThat(dbConnecor.getUrl(), is("jdbc:hive2://localhost:10000/db"));
    }

}
