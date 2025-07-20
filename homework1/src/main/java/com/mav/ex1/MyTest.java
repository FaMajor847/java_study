package com.mav.ex1;

import com.mav.ex1.annotations.AfterSuite;
import com.mav.ex1.annotations.BeforeSuite;
import com.mav.ex1.annotations.Test;

public class MyTest {
    @BeforeSuite
    public static void before() {
        System.out.println("BeforeSuite");
    }

    @Test(priority = 10)
    public void testMethod10() {
        System.out.println("priority = 10");
    }

    @Test(priority = 1)
    public void testMethod1() {
        System.out.println("priority = 1");
    }

    @Test()
    public void testMethodDefault() {
        System.out.println("default");
    }

    @AfterSuite
    public static void after() {
        System.out.println("AfterSuite");
    }
}
