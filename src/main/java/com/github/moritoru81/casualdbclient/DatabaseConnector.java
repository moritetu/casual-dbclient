package com.github.moritoru81.casualdbclient;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class DatabaseConnector {

    private final static Logger LOG = LoggerFactory.getLogger(DatabaseConnector.class);

    Database database;

    public DatabaseConnector(Database database) {
        this.database = database;
    }

    public Connection connect() throws SQLException {
        Connection connection;
        try {
            connection = connectDatabase();
        } catch (SQLException e) {
            LOG.error("Cannot connect to database. ({}, {}, {})", new Object[] {
                    this.database.getHost(), this.database.getDatabase(), this.database.getUser()
            });
            throw e;
        }
        return connection;
    }

    protected abstract Connection connectDatabase() throws SQLException;

    abstract String getUrl();
}
