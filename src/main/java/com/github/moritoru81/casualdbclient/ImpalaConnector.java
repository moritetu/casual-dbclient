package com.github.moritoru81.casualdbclient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImpalaConnector extends DatabaseConnector {

    private final static Logger LOG = LoggerFactory.getLogger(DatabaseConnector.class);

    private LinkedList<String> hosts = new LinkedList<String>();

    private Object lock = new Object();

    private final static int DEFAULT_PORT = 21050;

    static {
        SupportDatabase.IMPALA.loadDriver();
    }

    public ImpalaConnector(Database database) {
        super(database);
        if (database.getPort() <= 0) {
            database.setPort(DEFAULT_PORT);
        }
        if (database.getParameters() == null) {
            database.setParameters("auth=noSasl");
        }
        hosts.addAll(Arrays.asList(database.getHost().split(",")));
    }

    @Override
    protected Connection connectDatabase() throws SQLException {
        Connection connection = null;
        synchronized (lock) {
            connection = resolveConnectHost(database, true);
        }
        return connection;
    }

    Connection resolveConnectHost(Database database, boolean shouldSearchNextHost) throws SQLException {
        Connection connection = null;
        int failedCount = 0;
        for (String host: hosts) {
            try {
                String url = String.format(
                        "jdbc:hive2://%s:%d/%s;%s", host, database.getPort(), 
                        database.getDatabase(), database.getParameters());
                connection = DriverManager.getConnection(url, database.getUser(), database.getPassword());
                break;
            } catch (SQLException e) {
                LOG.warn("Cannot connect to database. {}", e.getMessage());
            }
            failedCount++;
        }

        while (failedCount-- > 0) {
            hosts.add(hosts.removeFirst());
        }

        if (connection == null) {
            throw new SQLException("Could not connect to any impalad hosts.");
        }

        return connection;
    }

    @Override
    String getUrl() {
        return String.format(
                "jdbc:hive2://%s:%d/%s;%s", hosts.getFirst(), 
                database.getPort(), database.getDatabase(), database.getParameters());
    }

    List<String> getHosts() {
        return Collections.unmodifiableList(hosts);
    }
}
