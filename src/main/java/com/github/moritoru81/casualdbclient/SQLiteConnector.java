package com.github.moritoru81.casualdbclient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SQLiteConnector extends DatabaseConnector {

    private final static Logger LOG = LoggerFactory.getLogger(DatabaseConnector.class);

    static {
        SupportDatabase.SQLITE.loadDriver();
    }

    public SQLiteConnector(Database database) {
        super(database);
    }

    @Override
    protected Connection connectDatabase() throws SQLException {
        Connection connection = null;
        try {
            String url = getUrl();
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            LOG.error("Cannot connect to database. {}", e.getMessage());
            throw e;
        }
        return connection;
    }

    @Override
    String getUrl() {
        return String.format("jdbc:sqlite:%s", database.getDatabase());
    }
}
