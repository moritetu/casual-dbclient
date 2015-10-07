package com.github.moritoru81.casualdbclient;

import org.junit.Test;

import com.github.moritoru81.casualdbclient.SupportDatabase;

public class SupportDatabaseTest {

    @Test
    public void testLoadDriver() throws Exception {
        SupportDatabase[] databases = SupportDatabase.values();
        for (SupportDatabase db: databases) {
            System.out.println("load db driver: " + db.getDriverName());
            db.loadDriver();
        }
    }

}
