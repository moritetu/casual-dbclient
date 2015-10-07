package com.github.moritoru81.casualdbclient;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.moritoru81.casualdbclient.utils.ContextProperties;
import com.github.moritoru81.casualdbclient.utils.ReflectionUtils;

public class DatabaseConnectorFactory {

    private final static Logger LOG = LoggerFactory.getLogger(DatabaseConnectorFactory.class);

    private static final String CONFIG_FILENAME_PREFIX = "casualdbclient-database";

    private final static ConcurrentHashMap<String, DatabaseConnector> CONNECTOR_CACHE =
            new ConcurrentHashMap<String, DatabaseConnector>();

    public static DatabaseConnector create(SupportDatabase databaseType) throws ConfigurationException {
        DatabaseConnector connector = null;
        connector = createFromProperty(CONFIG_FILENAME_PREFIX, databaseType);
        return connector;
    }

    /**
     * プロパティファイルからDatabaseConnectorオブジェクトを生成する。
     * プロパティファイルは，{@code CONFIG_FILENAME_PREFIX}.properties または 
     * {@code CONFIG_FILENAME_PREFIX}-test.properties。
     * {@code CONFIG_FILENAME_PREFIX}-test.propertiesが優先される。
     * 
     * @param databaseType サポートデータベースタイプ
     * @param namespace プロパティ名のプリフィックス
     * @return DatabaseConnector
     * @throws ConfigurationException 
     */
    public static DatabaseConnector create(SupportDatabase databaseType, String namespace) throws ConfigurationException  {
        DatabaseConnector connector = null;
        connector = createFromProperty(CONFIG_FILENAME_PREFIX, databaseType, namespace);
        return connector;
    }

    /**
     * {@code Database}オブジェクトと指定のConnectorクラスを使って{@code DatabaseConnector}オブジェクトを生成する。
     * 
     * @param database データベース設定情報
     * @param connectorClass Connectorクラスオブジェクト
     * @return DatabaseConnector
     */
    public static DatabaseConnector createFromDatabase(Database database, 
            Class<? extends DatabaseConnector> connectorClass)  {
        DatabaseConnector connector = null;
        connector = (DatabaseConnector) ReflectionUtils.newInstance(connectorClass, database);
        return connector;
    }

    /**
     * 指定のプロパティファイルから{@code DatabaseConnector}オブジェクトを生成する。
     * 拡張子.propertiesはなくてもよい。
     * <pre>
     * ex) mydatabase.propertiesから生成する場合
     * 
     * DatabaseConnectorFactory.create("mydatabase");
     * 
     * </pre>
     * 
     * @param fileName プロパティファイル名(.propertiesはなくてもよい）
     * @param databaseType サポートデータベースタイプ
     * @return DatabaseConnector
     * @throws ConfigurationException
     */
    public static DatabaseConnector createFromProperty(String fileName, SupportDatabase databaseType)
            throws ConfigurationException {
        DatabaseConnector connector = null;
        connector = createFromProperty(fileName, databaseType, "");
        return connector;
    }

    /**
     * 指定のプロパティファイルから指定の{@code namespace}を使って{@code DatabaseConnector}オブジェクトを生成する。
     * 拡張子.propertiesはなくてもよい。
     * 
     * <pre>
     * ex) mydatabase.propertiesから生成する場合
     * 
     * ##### mydatabase.properties #####
     * 
     * default.mysql.host=localhost
     * default.mysql.connector=at.database.MySQLConnector
     * 
     * ##### code
     * 
     * DatabaseConnectorFactory.create("mydatabase", SupportDatabase.MYSQL, "default");
     * 
     * </pre>
     * @param fileName プロパティファイル名(.propertiesはなくてもよい）
     * @param databaseType サポートデータベースタイプ
     * @param namespace プロパティ名のプリフィックス
     * @return DatabaseConnectorオブジェクト
     * @throws ConfigurationException 
     */
    public static DatabaseConnector createFromProperty(String fileName, SupportDatabase databaseType,
            String namespace) throws ConfigurationException  {
        DatabaseConnector connector = null;
        String dbTypeLowerCase = databaseType.toString().toLowerCase();
        String prefix = (namespace != null && namespace.length() != 0) ? namespace + "." + dbTypeLowerCase: dbTypeLowerCase;
        String cacheKey = fileName + ":" + prefix;

        // 最初にキャッシュから探す。
        if ((connector = CONNECTOR_CACHE.get(cacheKey)) == null) {
            ContextProperties contextProperties = lookUpProperties(fileName);

            // Database設定情報の構築
            Database database = new Database();
            String className = contextProperties.getString(prefix + ".connector");
            database.setDatabase(contextProperties.getString(prefix + ".database"));
            database.setHost(contextProperties.getString(prefix + ".host", null));
            database.setUser(contextProperties.getString(prefix + ".user", null));
            database.setPort(contextProperties.getInt(prefix + ".port", -1));
            database.setPassword(contextProperties.getString(prefix + ".password", ""));
    
            // コネクタクラスからインスタンスを生成
            connector = ReflectionUtils.newInstance(className, database);
            CONNECTOR_CACHE.put(cacheKey, connector);
            LOG.trace("connector was cached: {}", cacheKey);
        }

        return connector;
    }

    /**
     * プロパティファイルを探索する。以下の順に探索される。設定ファイルの探索順は，
     * {@link org.apache.commons.configuration.Configuration}に従う。
     * <ul>
     *  <li>casual-dbclient-database-test.properties</li>
     *  <li>casual-dbclient-database.properties</li>
     * </ul>
     * 
     * @param fileName プロパティファイル名
     * @return 最初に見つかったプロパティファイルから生成されたContextPropertiesオブジェクト。
     * @throws ConfigurationException 
     */
    private static ContextProperties lookUpProperties(String fileName) throws ConfigurationException {
        String fileNameWithoutExt = fileName.endsWith(".properties") ?
                fileName.replaceFirst(".properties", ""): fileName;
        ContextProperties contextProperties = null;

        for (String suffix: new String[] {"-test", ""}) {
            String fileNameWithExt = String.format("%s%s.properties", fileNameWithoutExt, suffix);
            try {
                contextProperties = new ContextProperties(new PropertiesConfiguration(fileNameWithExt));
                LOG.debug("found a property file. ({})", fileNameWithExt);
                break;
            } catch (ConfigurationException e) {
                LOG.debug("failed to load the property file. ({})", fileNameWithExt);
            }
        }

        if (contextProperties == null) {
            throw new ConfigurationException(String.format("failed to look up property files."));
        }

        return contextProperties;
    }

    static void clearCache() {
        CONNECTOR_CACHE.clear();
    }

    static int getCacheSize() {
        return CONNECTOR_CACHE.size();
    }
}
