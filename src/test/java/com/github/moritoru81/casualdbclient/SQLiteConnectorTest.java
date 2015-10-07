package com.github.moritoru81.casualdbclient;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.moritoru81.casualdbclient.Database;
import com.github.moritoru81.casualdbclient.SQLiteConnector;

public class SQLiteConnectorTest {

    static SQLiteConnector connector;
    final static String DATABASE_FILENAME = "unittest_sampledb.sqlite";

    @BeforeClass
    public static void beforeClass()  {
        deleteDatabase();

        Database database = new Database();
        database.setDatabase(DATABASE_FILENAME);
        database.setHost("localhost");
        connector = new SQLiteConnector(database);
    }

    @AfterClass
    public static void afterClass() {
        deleteDatabase();
    }

    @Test
    public void testConnect() throws SQLException {
        Connection con = connector.connect();
        assertThat(new File(DATABASE_FILENAME).exists(), is(true));
        closeConnection(con);
    }

    void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                ;
            }
        }
    }

    static void deleteDatabase() {
        final File file = new File(DATABASE_FILENAME);
        if (file.exists()) {
            file.delete();
        }
    }
}
