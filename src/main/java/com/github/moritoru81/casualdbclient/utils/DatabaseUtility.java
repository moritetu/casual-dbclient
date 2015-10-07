package com.github.moritoru81.casualdbclient.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.configuration.ConfigurationException;

import com.github.moritoru81.casualdbclient.DatabaseConnectorFactory;
import com.github.moritoru81.casualdbclient.SupportDatabase;

public final class DatabaseUtility {

    private DatabaseUtility() {
        throw new AssertionError();
    }

    public static Connection getConnection(SupportDatabase databaseType) {
        Connection conn = null;
        try {
            conn = DatabaseConnectorFactory.create(databaseType).connect();
            conn.setAutoCommit(false);
        } catch (ConfigurationException e) {
            throw new IllegalStateException(e);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return conn;
    }

    public static Connection getConnection(SupportDatabase databaseType, String namespace) {
        Connection conn = null;
        try {
            conn = DatabaseConnectorFactory.create(databaseType, namespace).connect();
            conn.setAutoCommit(false);
        } catch (ConfigurationException e) {
            throw new IllegalStateException(e);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return conn;
    }

    public static Connection getConnection(String fileName, SupportDatabase databaseType, String namespace) {
        Connection conn = null;
        try {
            conn = DatabaseConnectorFactory.createFromProperty(fileName, databaseType, namespace).connect();
            conn.setAutoCommit(false);
        } catch (ConfigurationException e) {
            throw new IllegalStateException(e);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return conn;
    }

    public static void commit(Connection conn) {
        if (conn != null) {
            try {
                conn.commit();
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public static void rollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public static void closeResultSet(ResultSet rs) throws SQLException {
        if (rs != null) {
            rs.close();
        }
    }

    public static void closeResultSetQuietly(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                ;
            }
        }
    }

    public static void closeStatement(Statement stmt) throws SQLException {
        if (stmt != null) {
            stmt.close();
        }
    }

    public static void closeStatementQuietly(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                ;
            }
        }
    }

    public static void closeConnection(Connection conn) throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }

    public static void closeConnectionQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                ;
            }
        }
    }

    public static void closeAll(Connection conn, Statement stmt, ResultSet rs) throws SQLException {
        closeResultSet(rs);
        closeStatement(stmt);
        closeConnection(conn);
    }

    public static void closeAllQuietly(Connection conn, Statement stmt, ResultSet rs) {
        closeResultSetQuietly(rs);
        closeStatementQuietly(stmt);
        closeConnectionQuietly(conn);
    }
}
