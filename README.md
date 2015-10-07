# casual-dbclient

clumsy database client

## Usage

### Resources

**casualdbclient-database.properties**

This resource must be placed on classpath.

```
# Sample
__context__=
__additivity__=true

# Impala
impala.connector=com.github.moritoru81.casualdbclient.ImpalaConnector
impala.host=impala-host1,impala-host2,impala-host3
impala.port=21050
impala.database=
impala.user=impala
impala.password=
```

### Sample

Connects to impala and gets several data.

```java
package sample;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.github.moritoru81.casualdbclient.SupportDatabase;
import com.github.moritoru81.casualdbclient.utils.DatabaseUtility;


public class DbClientTest {
    public static void main(String args[]) throws Exception {
        Connection conn = DatabaseUtility.getConnection(SupportDatabase.IMPALA);
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from your_table limit 10");
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } finally {
            DatabaseUtility.closeAllQuietly(conn, stmt, rs);
        }
    }
}
```