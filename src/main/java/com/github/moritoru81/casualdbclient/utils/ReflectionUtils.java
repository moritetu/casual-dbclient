package com.github.moritoru81.casualdbclient.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReflectionUtils {

    private static final Map<String, Constructor<?>> CONSTRUCTOR_CACHE = 
            new ConcurrentHashMap<String, Constructor<?>>();

    public static <T> T newInstance(String className) {
        return newInstance(className, (Object[])null);
    }

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(String className, Object... arguments) {
        Class<T> klass;
        try {
            klass = (Class<T>) Class.forName(className);
            return newInstance(klass, arguments);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T newInstance(Class<T> klass) {
        return newInstance(klass, (Object[])null);
    }

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<T> klass, Object... arguments) {
        try {
            Class<?>[] klasses = extractClasses(arguments);
            String identification = makeIdentification(klass, klasses);
            Constructor<T> meth = (Constructor<T>) CONSTRUCTOR_CACHE.get(identification);
            if (meth == null) {
                meth = klass.getDeclaredConstructor(klasses);
                meth.setAccessible(true);
                CONSTRUCTOR_CACHE.put(identification, meth);
            }
            return (arguments != null) ? meth.newInstance(arguments): meth.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T invokeMethod(Object object, String methodName, Object ...arguments) {
        try {
            Class<?>[] klasses = extractClasses(arguments);
            Method method = object.getClass().getDeclaredMethod(methodName, klasses);
            method.setAccessible(true);
            return (T) method.invoke(object, arguments);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClass(T o) {
        return (Class<T>)o.getClass();
    }

    static void clearCache() {
        CONSTRUCTOR_CACHE.clear();
    }

    static int getCacheSize() {
        return CONSTRUCTOR_CACHE.size();
    }

    private static String makeIdentification(Class<?> klass, Class<?>... parameterTypes) {
        StringBuilder sb = new StringBuilder();
        sb.append(klass.getCanonicalName());
        if (parameterTypes == null) {
            return sb.toString();
        }

        sb.append("(");
        for (Class<?> cls : parameterTypes) {
            sb.append(cls.getCanonicalName()).append(",");
        }
        sb.setCharAt(sb.length() - 1, ')');

        return sb.toString();
    }

    private static Class<?>[] extractClasses(Object... parameters) {
        if (parameters == null) {
            return null;
        }
        Class<?>[] klasses = new Class<?>[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            klasses[i] = parameters[i].getClass();
        }
        return klasses;
    }
}
