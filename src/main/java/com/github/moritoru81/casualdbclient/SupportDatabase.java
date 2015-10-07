package com.github.moritoru81.casualdbclient;

public enum SupportDatabase {
    /**
     * http://www.cloudera.com/content/cloudera-content/cloudera-docs/Impala/latest/Installing-and-Using-Impala/ciiu_impala_jdbc.html
     */
    IMPALA("org.apache.hive.jdbc.HiveDriver"),
    /**
     * https://cwiki.apache.org/confluence/display/Hive/HiveClient
     */
    HIVE("org.apache.hive.jdbc.HiveDriver"),
    /**
     * http://www.oracle.com/technetwork/jp/database/enterprise-edition/jdbc-111060-097832-ja.html
     */
    ORACLE("oracle.jdbc.driver.OracleDriver"),
    /**
     * http://dev.mysql.com/downloads/connector/j/
     */
    MYSQL("com.mysql.jdbc.Driver"),
    /**
     * https://bitbucket.org/xerial/sqlite-jdbc
     */
    SQLITE("org.sqlite.JDBC")
    ;

    private String driverName;

    SupportDatabase(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void loadDriver() {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
