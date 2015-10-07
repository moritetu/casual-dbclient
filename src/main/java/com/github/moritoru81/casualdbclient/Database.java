package com.github.moritoru81.casualdbclient;

public class Database {

    private String host;

    private String database;

    private String user;

    private String password;

    private int port;

    private String parameters;

    public Database() { }

    public Database(String host, int port, String database, String user, String password) {
        this.port = port;
        this.host = host;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    public Database(String host, String database, String user, String password) {
        this(host, 0, database, user, password);
    }

    public Database(String host, String user) {
        this(host, 0, null, user, null);
    }

    public Database(String host, String database, String user) {
        this(host, 0, database, user, null);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getParameters() {
        return parameters;
    }

    /**
     * <pre>
     * ex) For impala without Kerberos authentication.
     * db.setParameters("auth=noSasl");
     * 
     * ex) For impala with Kerberos authentication.
     * db.setParameters("principal=impala/myhost.example.com@H2.EXAMPLE.COM");
     * </pre>
     * @param parameters
     */
    public void setParameters(String parameters) {
        this.parameters = parameters;
    }
}
