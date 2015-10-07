package com.github.moritoru81.casualdbclient.utils;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.github.moritoru81.casualdbclient.utils.ReflectionUtils;

public class ReflectionUtilsTest {

    public static class TestClass {
        String name;
        TestClass() {}
        TestClass(String message) {
        }
        @SuppressWarnings("unused")
        private String getName() {
            return name;
        }
        @SuppressWarnings("unused")
        private void setName(String name) {
            this.name = name;
        }
    }

    public static class TestClass2 {
        TestClass2() {}
        TestClass2(String message) {
        }
    }

    private final String TESTCLASS_CANONICAL_NAME  = "com.github.moritoru81.casualdbclient.utils.ReflectionUtilsTest$TestClass";
    private final String TESTCLASS2_CANONICAL_NAME = "com.github.moritoru81.casualdbclient.utils.ReflectionUtilsTest$TestClass2";

    @Before
    public void before() {
        ReflectionUtils.clearCache();
    }

    @Test
    public void testClassCache() {
        TestClass instance;

        instance = ReflectionUtils.newInstance(TESTCLASS_CANONICAL_NAME);
        assertThat(instance, instanceOf(TestClass.class));
        assertThat(ReflectionUtils.getCacheSize(), is(1));

        // from cache
        instance = ReflectionUtils.newInstance(TESTCLASS_CANONICAL_NAME);
        assertThat(instance, instanceOf(TestClass.class));
        assertThat(ReflectionUtils.getCacheSize(), is(1));

        // new class
        TestClass2 instance2 = ReflectionUtils.newInstance(TESTCLASS2_CANONICAL_NAME);
        assertThat(instance2, instanceOf(TestClass2.class));
        assertThat(ReflectionUtils.getCacheSize(), is(2));
    }

    @Test
    public void testNewInstance_ClassName() {
        TestClass instance = ReflectionUtils.newInstance(TESTCLASS_CANONICAL_NAME);
        assertThat(instance, instanceOf(TestClass.class));
        assertThat(ReflectionUtils.getCacheSize(), is(1));
    }

    @Test
    public void testNewInstance_ClassName_With_Parameters() {
        TestClass instance = ReflectionUtils.newInstance(TESTCLASS_CANONICAL_NAME, "hoge");
        assertThat(instance, instanceOf(TestClass.class));
        assertThat(ReflectionUtils.getCacheSize(), is(1));
    }

    @Test
    public void testNewInstance_Class() {
        TestClass instance = ReflectionUtils.newInstance(TestClass.class);
        assertThat(instance, instanceOf(TestClass.class));
        assertThat(ReflectionUtils.getCacheSize(), is(1));
    }

    @Test
    public void testNewInstance_Class_With_Parameters() {
        TestClass instance = ReflectionUtils.newInstance(TestClass.class, "hoge");
        assertThat(instance, instanceOf(TestClass.class));
        assertThat(ReflectionUtils.getCacheSize(), is(1));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testNewInstance_With_GenericParameter() {
        ArrayList list = ReflectionUtils.newInstance("java.util.ArrayList");
        list.add("hoge");
        assertThat(list.size(), is(1));
        assertThat(list, instanceOf(ArrayList.class));
    }

    @Test
    public void testInvokeMethod() {
        TestClass testClass = new TestClass();
        assertThat(ReflectionUtils.invokeMethod(testClass, "getName"), is(nullValue()));

        ReflectionUtils.invokeMethod(testClass, "setName", "hoge");
        assertThat((String)ReflectionUtils.invokeMethod(testClass, "getName"), is("hoge"));
    }

    @Test
    public void testGetFieldValue() {
        TestClass testClass = new TestClass();
        ReflectionUtils.invokeMethod(testClass, "setName", "hoge");
        assertThat((String)ReflectionUtils.getFieldValue(testClass, "name"), is("hoge"));
    }

}
