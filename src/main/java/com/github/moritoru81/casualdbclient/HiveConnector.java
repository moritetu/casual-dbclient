package com.github.moritoru81.casualdbclient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>参照</p>
 * <ul>
 *  <li>https://cwiki.apache.org/confluence/display/Hive/HiveClient</li>
 * </ul>
 */
public class HiveConnector extends DatabaseConnector {

    private final static Logger LOG = LoggerFactory.getLogger(DatabaseConnector.class);

    private final static int DEFAULT_PORT = 10000;

    static {
        SupportDatabase.HIVE.loadDriver();
    }

    public HiveConnector(Database database) {
        super(database);
        if (database.getPort() <= 0) {
            database.setPort(DEFAULT_PORT);
        }
    }

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
        return String.format(
                // Use hive server2
                // http://www.cloudera.com/content/cloudera-content/cloudera-docs/CDH4/4.2.0/CDH4-Installation-Guide/cdh4ig_topic_18_5.html
                "jdbc:hive2://%s:%d/%s", database.getHost(), 
                database.getPort(), database.getDatabase());
    }
}
