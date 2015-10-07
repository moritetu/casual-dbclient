package com.github.moritoru81.casualdbclient;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import com.github.moritoru81.casualdbclient.Database;

public class DatabaseTest {

    @Test
    public void testCreateDatabase_Host_User() {
        Database database = new Database("localhost", "user");

        assertThat(database.getHost(), is("localhost"));
        assertThat(database.getUser(), is("user"));
    }

    @Test
    public void testCreateDatabase_Host_Database_User() {
        Database database = new Database("localhost", "default", "user");

        assertThat(database.getHost(), is("localhost"));
        assertThat(database.getDatabase(), is("default"));
        assertThat(database.getUser(), is("user"));
    }

    @Test
    public void testCreateDatabase_Host_Database_User_Password() {
        Database database = new Database("localhost", "default", "user", "pass");

        assertThat(database.getHost(), is("localhost"));
        assertThat(database.getDatabase(), is("default"));
        assertThat(database.getUser(), is("user"));
        assertThat(database.getPassword(), is("pass"));
    }

    @Test
    public void testCreateDatabase_Host_Port_Database_User_Password() {
        Database database = new Database("localhost", 9999, "default", "user", "pass");

        assertThat(database.getHost(), is("localhost"));
        assertThat(database.getPort(), is(9999));
        assertThat(database.getDatabase(), is("default"));
        assertThat(database.getUser(), is("user"));
        assertThat(database.getPassword(), is("pass"));
    }

    @Test
    public void testDatabaseAccessor() {
        Database database = new Database();
        assertThat(database.getHost(), is(nullValue()));
        assertThat(database.getPort(), is(0));
        assertThat(database.getDatabase(), is(nullValue()));
        assertThat(database.getUser(), is(nullValue()));
        assertThat(database.getPassword(), is(nullValue()));

        database.setHost("localhost.com");
        assertThat(database.getHost(), is("localhost.com"));

        database.setPort(3306);
        assertThat(database.getPort(), is(3306));

        database.setDatabase("default");
        assertThat(database.getDatabase(), is("default"));

        database.setUser("user");
        assertThat(database.getUser(), is("user"));

        database.setPassword("password");
        assertThat(database.getPassword(), is("password"));
    }

}
