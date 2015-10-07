package com.github.moritoru81.casualdbclient;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.mockito.Mockito;

import com.github.moritoru81.casualdbclient.Database;
import com.github.moritoru81.casualdbclient.DatabaseConnector;
import com.github.moritoru81.casualdbclient.ImpalaConnector;

public class ImpalaConnectorTest {

    Database database = new Database("localhost,localhost2", "db", "user", "pass");

    static class ImpalaConnectorStub extends ImpalaConnector {
        public ImpalaConnectorStub(Database database) {
            super(database);
        }

        @Override
        protected Connection connectDatabase() throws SQLException {
            try {
                resolveConnectHost(database, false);
            } catch (Exception e) {
                //
            }
            return Mockito.mock(java.sql.Connection.class);
        }
    }

    @Test
    public void testGetUrl() {
        DatabaseConnector dbConnecor = new ImpalaConnector(database);
        assertThat(dbConnecor.getUrl(), is("jdbc:hive2://localhost:21050/db;auth=noSasl"));
    }

    @Test
    public void testGetHosts() {
        ImpalaConnector dbConnecor = new ImpalaConnector(database);
        assertThat(dbConnecor.getHosts().size(), is(2));
    }

    @Test
    public void testResolveConnectHost_HostRotation() {
        ImpalaConnectorStub impalaConnectorSub = new ImpalaConnectorStub(new Database("hoge,hoge2,hoge3", ""));

        assertThat(impalaConnectorSub.getHosts().size(), is(3));

        try {
            impalaConnectorSub.connect();
        } catch (SQLException e) {
            // 失敗したホストはリスト末尾にローテーションされる
            assertThat(impalaConnectorSub.getHosts().get(0), is("hoge2"));
        }

        try {
            impalaConnectorSub.connect();
        } catch (SQLException e) {
            // 失敗したホストはリスト末尾にローテーションされる
            assertThat(impalaConnectorSub.getHosts().get(0), is("hoge3"));
        }

        try {
            impalaConnectorSub.connect();
        } catch (SQLException e) {
            // 失敗したホストはリスト末尾にローテーションされる
            assertThat(impalaConnectorSub.getHosts().get(0), is("hoge"));
        }
    }

    @Test
    public void testResolveConnectHost() {
        ImpalaConnector impalaConnector = new ImpalaConnector(new Database("hoge,hoge2,hoge3", ""));

        assertThat(impalaConnector.getHosts().size(), is(3));

        try {
            impalaConnector.connect();
        } catch (SQLException e) {
            assertThat(impalaConnector.getHosts().get(0), is("hoge"));
        }
    }

}
