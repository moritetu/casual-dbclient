package com.github.moritoru81.casualdbclient.utils;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.moritoru81.casualdbclient.utils.ContextProperties;

public class ContextPropertiesTest {

    final static String PROPERTY_FILENAME_WITH_CONTEXT    = "casualdbclient-database-unit-test.properties";
    final static String PROPERTY_FILENAME_WITHOUT_CONTEXT = "casualdbclient-database-unit-test-nocxt.properties";

    static ContextProperties propUnitTestWithCxt;
    static ContextProperties propUnitTestWithoutCxt;

    @BeforeClass
    public static void beforeClass() throws ConfigurationException {
        propUnitTestWithCxt = new ContextProperties(new PropertiesConfiguration(PROPERTY_FILENAME_WITH_CONTEXT));
        propUnitTestWithoutCxt = new ContextProperties(new PropertiesConfiguration(PROPERTY_FILENAME_WITHOUT_CONTEXT));
    }

    @After
    public void after() {
        System.setProperty("casualdbclient.context", "");
        System.setProperty("casualdbclient.additivity", "");
    }

    @Test
    public void testReadPropertyFromSystemEnviroment() {
        ContextProperties contextProperties;
        System.setProperty("casualdbclient.context", "system");
        System.setProperty("casualdbclient.additivity", "false");
        contextProperties = new ContextProperties("foo", true);

        assertThat(contextProperties.getContext(), is("foo"));
        assertThat(contextProperties.getAdditivity(), is(true));

        contextProperties = new ContextProperties();
        assertThat(contextProperties.getContext(), is("system"));
        assertThat(contextProperties.getAdditivity(), is(false));
    }

    @Test
    public void testGetString() {
        assertThat(propUnitTestWithCxt.getString("string.test.dummy", "dummy"), is("dummy"));
        assertThat(propUnitTestWithCxt.getString("string.test"), is("test-dev"));
        assertThat(propUnitTestWithoutCxt.getString("string.test"), is("test"));
    }

    @Test
    public void testGetInt() {
        assertThat(propUnitTestWithCxt.getInt("int.test.dummy", 77), is(77));
        assertThat(propUnitTestWithCxt.getInt("int.test"), is(-123));
        assertThat(propUnitTestWithoutCxt.getInt("int.test"), is(123));
    }

    @Test
    public void testGetLong() {
        assertThat(propUnitTestWithCxt.getInt("long.test.dummy", 77), is(77));
        assertThat(propUnitTestWithCxt.getInt("long.test"), is(-456));
        assertThat(propUnitTestWithoutCxt.getInt("long.test"), is(456));
    }

    @Test
    public void testGetFloat() {
        assertThat(propUnitTestWithCxt.getFloat("float.test.dummy", 77f), is(77f));
        assertThat(propUnitTestWithCxt.getFloat("float.test"), is(50.0f));
        assertThat(propUnitTestWithoutCxt.getFloat("float.test"), is(10.0f));
    }

    @Test
    public void testGetDouble() {
        assertThat(propUnitTestWithCxt.getDouble("double.test.dummy", 77.7), is(77.7));
        assertThat(propUnitTestWithCxt.getDouble("double.test"), is(30.0));
        assertThat(propUnitTestWithoutCxt.getDouble("double.test"), is(20.0));
    }

    @Test
    public void testGetBoolean() {
        assertThat(propUnitTestWithCxt.getBoolean("bool.test.dummy", false), is(false));
        assertThat(propUnitTestWithCxt.getBoolean("bool.test"), is(true));
        assertThat(propUnitTestWithoutCxt.getBoolean("bool.test"), is(false));

        // 0, 0以外
        assertThat(propUnitTestWithoutCxt.getBoolean("bool.0.test"), is(false));
        assertThat(propUnitTestWithoutCxt.getBoolean("bool.other.test"), is(true));
    }

    @Test
    public void testGetReservedProperties() {
        assertThat(propUnitTestWithCxt.getString("__context__"), is("dev"));
        assertThat(propUnitTestWithCxt.getBoolean("__additivity__"), is(true));

        ContextProperties contextProp = new ContextProperties();

        contextProp.setContext("product");
        assertThat(contextProp.getContext(), is("product"));

        contextProp.setAdditivity(false);
        assertThat(contextProp.getAdditivity(), is(false));
    }

    @Test
    public void testSetProperty() {
        ContextProperties contextProp;

        contextProp = new ContextProperties("__test__");
        contextProp.setProperty("test", "hoge");
        contextProp.setProperty("test.__test__", "hoge-test");
        assertThat(contextProp.getString("test"), is("hoge-test"));

        contextProp = new ContextProperties("__test__");
        contextProp.setAdditivity(true);
        contextProp.setProperty("test", "hoge");
        assertThat(contextProp.getString("test"), is("hoge"));

        contextProp = new ContextProperties("__test__");
        contextProp.setAdditivity(false);
        contextProp.setProperty("test", "hoge");
        assertThat(contextProp.getString("test"), is(nullValue()));
    }
}
