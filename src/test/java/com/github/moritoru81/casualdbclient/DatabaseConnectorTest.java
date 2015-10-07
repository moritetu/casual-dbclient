package com.github.moritoru81.casualdbclient;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import com.github.moritoru81.casualdbclient.Database;
import com.github.moritoru81.casualdbclient.DatabaseConnector;

public class DatabaseConnectorTest {

    DatabaseConnector dbConnectorStub = new DatabaseConnector(new Database("localhost", "user")) {
        @Override
        protected Connection connectDatabase() throws SQLException {
            final Connection conn = mock(java.sql.Connection.class);
            return conn;
        }

        @Override
        String getUrl() {
            return "jdbc:test://localhost/";
        }
    };

    @Test
    public void testConnect() throws Exception {
        assertThat(dbConnectorStub.connect(), instanceOf(Connection.class));
    }

    @Test
    public void testGetUrl() {
        assertThat(dbConnectorStub.getUrl(), is("jdbc:test://localhost/"));
    }
}
