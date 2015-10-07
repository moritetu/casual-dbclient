package com.github.moritoru81.casualdbclient.utils;

import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;

import com.github.moritoru81.casualdbclient.utils.DatabaseUtility;


public class DatabaseUtilityTest {

    @Test
    public void testCommit() throws Exception {
        Connection conn = mock(Connection.class);
        DatabaseUtility.commit(conn);
    }

    @Test(expected = IllegalStateException.class)
    public void testCommit_With_Exception() throws Exception {
        Connection conn = mock(Connection.class);
        doThrow(new SQLException()).when(conn).commit();
        DatabaseUtility.commit(conn);
    }

    @Test
    public void testRollback() {
        Connection conn = mock(Connection.class);
        DatabaseUtility.rollback(conn);
    }

    @Test(expected = IllegalStateException.class)
    public void testRollback_With_Exception() throws Exception {
        Connection conn = mock(Connection.class);
        doThrow(new SQLException()).when(conn).rollback();
        DatabaseUtility.rollback(conn);
    }

    @Test
    public void testCloseConnection() throws Exception {
        Connection conn = mock(Connection.class);
        DatabaseUtility.closeConnection(conn);
    }

    @Test
    public void testCloseConnectionQuietly() throws Exception {
        Connection conn = mock(Connection.class);
        doThrow(new SQLException()).when(conn).close();
        DatabaseUtility.closeConnectionQuietly(conn);
    }

    @Test
    public void testCloseStatement() throws Exception {
        Statement stmt = mock(Statement.class);
        DatabaseUtility.closeStatement(stmt);
    }

    @Test
    public void testCloseStatementQuietly() throws Exception {
        Statement stmt = mock(Statement.class);
        doThrow(new SQLException()).when(stmt).close();
        DatabaseUtility.closeStatementQuietly(stmt);
    }

    @Test
    public void testCloseResultSet() throws Exception {
        ResultSet rs = mock(ResultSet.class);
        DatabaseUtility.closeResultSet(rs);
    }

    @Test
    public void testCloseResultSetQuietly() throws Exception {
        ResultSet rs = mock(ResultSet.class);
        doThrow(new SQLException()).when(rs).close();
        DatabaseUtility.closeResultSetQuietly(rs);
    }

    @Test
    public void testCloseAll() throws Exception {
        Connection conn = mock(Connection.class);
        Statement stmt = mock(Statement.class);
        ResultSet rs = mock(ResultSet.class);
        DatabaseUtility.closeAll(conn, stmt, rs);
    }


    @Test
    public void testCloseAllQuietly() throws Exception {
        Connection conn = mock(Connection.class);
        doThrow(new SQLException()).when(conn).close();

        Statement stmt = mock(Statement.class);
        doThrow(new SQLException()).when(stmt).close();

        ResultSet rs = mock(ResultSet.class);
        doThrow(new SQLException()).when(rs).close();

        DatabaseUtility.closeAllQuietly(conn, stmt, rs);
    }
}
