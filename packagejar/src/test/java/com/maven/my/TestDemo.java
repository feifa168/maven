package com.maven.my;


import org.junit.Test;

import java.security.InvalidParameterException;

public class TestDemo {
    @Test
    public void test1() {
        System.out.println("test 1");
    }

    @Test
    public void testPath() {
        String userPath = System.getProperty("user.dir");
        String path = TestDemo.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String pathGetClass =this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        String classLoaderPath = getClass().getClassLoader().toString();

        System.out.println("userPath:        "+userPath);
        System.out.println("path:            "+path);
        System.out.println("pathGetClass:    "+pathGetClass);
        System.out.println("classLoaderPath: "+classLoaderPath);
    }
}
