package com.github.moritoru81.casualdbclient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OracleConnector extends DatabaseConnector {

    private final static Logger LOG = LoggerFactory.getLogger(DatabaseConnector.class);

    private final static int DEFAULT_PORT = 1521;

    static {
        SupportDatabase.ORACLE.loadDriver();
    }

    public OracleConnector(Database database) {
        super(database);
        if (database.getPort() <= 0) {
            database.setPort(DEFAULT_PORT);
        }
    }

    @Override
    protected Connection connectDatabase() throws SQLException {
        Connection connection = null;
        try {
            String url = getUrl();
            connection = DriverManager.getConnection(url, database.getUser(), database.getPassword());
        } catch (SQLException e) {
            LOG.error("Cannot connect to database. {}", e.getMessage());
            throw e;
        }
        return connection;
    }

    @Override
    String getUrl() {
        return String.format("jdbc:oracle:thin:@%s:%d:%s", database.getHost(), 
                database.getPort(), database.getDatabase());
    }
}
