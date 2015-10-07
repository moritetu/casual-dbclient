package com.github.moritoru81.casualdbclient.utils;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * <pre>
 * // in test.properties
 * 
 * __context__=debug
 * __additividy__=true
 * 
 * test=100
 * test.debug=200
 * 
 * // in java (test.debugあり)
 * cxtProp.getString("test"); // 200
 * 
 * // in java (test.debugなし)
 * cxtProp.getString("test"); // 100
 * </pre>
 */
public class ContextProperties {

    private Configuration configuration;

    private final static String KEY_CONTEXT    = "__context__";
    private final static String KEY_ADDITIVITY = "__additivity__";

    private final static String SYSTEM_PROPERTY_CONTEXT    = "casualdbclient.context";
    private final static String SYSTEM_PROPERTY_ADDITIVITY = "casualdbclient.additivity";

    public ContextProperties() {
        configuration = new PropertiesConfiguration();
        readPropertyFromSystemProperties();
    }

    
    public ContextProperties(Configuration configuration) {
        this.configuration = configuration;
        readPropertyFromSystemProperties();
    }

    public ContextProperties(String context) {
        configuration = new PropertiesConfiguration();
        readPropertyFromSystemProperties();
        configuration.setProperty(KEY_CONTEXT, context);
    }

    public ContextProperties(String context, boolean additivity) {
        configuration = new PropertiesConfiguration();
        readPropertyFromSystemProperties();
        configuration.setProperty(KEY_CONTEXT, context);
        configuration.setProperty(KEY_ADDITIVITY, additivity);
    }

    /**
     * <pre>
     * -Dcasualdbclient.context=hoge
     * -Dcasualdbclient.additivity=true
     * </pre>
     */
    void readPropertyFromSystemProperties() {
        String context, additivity;
        if ((context = System.getProperty(SYSTEM_PROPERTY_CONTEXT, null)) != null) {
            setContext(context);
        }
        if ((additivity = System.getProperty(SYSTEM_PROPERTY_ADDITIVITY, null)) != null) {
            setAdditivity(Boolean.parseBoolean(additivity));
        }
    }

    public String getString(String key) {
        String value = getPropertyWithSuffix(key);
        return value;
    }

    public String getString(String key, String defaultValue) {
        String value = getPropertyWithSuffix(key);
        return (value == null) ? defaultValue: value;
    }

    public int getInt(String key) {
        String value = getPropertyWithSuffix(key);
        int intValue = Integer.parseInt(value);
        return intValue;
    }

    public int getInt(String key, int defaultValue) {
        int intValue = defaultValue;
        try {
            intValue = getInt(key);
        } catch (NumberFormatException e) {
            ;
        }
        return intValue;
    }

    public long getLong(String key) {
        String value = getPropertyWithSuffix(key);
        long longValue = Long.parseLong(value);
        return longValue;
    }

    public long getLong(String key, long defaultValue) {
        long longValue = defaultValue;
        try {
            longValue = getLong(key);
        } catch (NumberFormatException e) {
            ;
        }
        return longValue;
    }

    public float getFloat(String key) {
        String value = getPropertyWithSuffix(key);
        float floatValue = Float.parseFloat(value);
        return floatValue;
    }

    public float getFloat(String key, float defaultValue) {
        float floatValue = defaultValue;
        try {
            floatValue = getFloat(key);
        } catch (NumberFormatException e) {
            ;
        } catch (NullPointerException e) {
            ;
        }
        return floatValue;
    }

    public double getDouble(String key) {
        String value = getPropertyWithSuffix(key);
        double doubleValue = Double.parseDouble(value);
        return doubleValue;
    }

    public double getDouble(String key, double defaultValue) {
        double doubleValue = defaultValue;
        try {
            doubleValue = getDouble(key);
        } catch (NumberFormatException e) {
            ;
        } catch (NullPointerException e) {
            ;
        }
        return doubleValue;
    }

    public boolean getBoolean(String key) {
        String value = getPropertyWithSuffix(key);
        if (value == null || "false".equals(value.toLowerCase()) || "0".equals(value)) {
            return false;
        }
        return true;
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String value = getPropertyWithSuffix(key);
        if (value == null) {
            return defaultValue;
        }
        return getBoolean(key);
    }

    public Object getObject(String key) {
        return getPropertyWithSuffix(key);
    }


    private String getPropertyWithSuffix(String key) {
        // reserved property
        if (KEY_CONTEXT.equals(key) || KEY_ADDITIVITY.equals(key)) {
            return configuration.getString(key);
        }

        if (getContext().length() > 0) {
            String value = configuration.getString(key + "." + getContext());
            if (value != null) {
                return value;
            }
            if (!getAdditivity()) {
                return null;
            }
        }

        return configuration.getString(key);
    }

    public String getContext() {
        return configuration.getString(KEY_CONTEXT, "");
    }

    public void setContext(String context) {
        configuration.setProperty(KEY_CONTEXT, context);
    }

    public boolean getAdditivity() {
        return configuration.getBoolean(KEY_ADDITIVITY, false);
    }

    public void setAdditivity(boolean additivity) {
        configuration.setProperty(KEY_ADDITIVITY, additivity);
    }

    public void setProperty(String key, Object value) {
        configuration.setProperty(key, value);
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
