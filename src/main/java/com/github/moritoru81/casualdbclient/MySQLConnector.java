package com.github.moritoru81.casualdbclient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySQLConnector extends DatabaseConnector {

    private final static Logger LOG = LoggerFactory.getLogger(DatabaseConnector.class);

    private final static int DEFAULT_PORT = 3306;

    static {
        SupportDatabase.MYSQL.loadDriver();
    }

    public MySQLConnector(Database database) {
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
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            LOG.error("Cannot load mysql driver.: {}", e.getMessage());
            throw e;
        }
        return connection;
    }

    @Override
    String getUrl() {
        return String.format(
                "jdbc:mysql://%s:%d/%s?user=%s&password=%s",
                database.getHost(), database.getPort(), database.getDatabase(),
                database.getUser(), database.getPassword());
    }
}
