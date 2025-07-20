package com.mav.ex1;

import com.mav.ex1.annotations.AfterSuite;
import com.mav.ex1.annotations.BeforeSuite;
import com.mav.ex1.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Runner {
    public static void run(Class<?> clazz) throws Exception {
        List<Method> tests = new ArrayList<>();
        Method beforeSuite = null;
        Method afterSuite = null;

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                if (beforeSuite != null) {
                    throw new RuntimeException("В классе больше одного метода с аннотацией BeforeSuite");
                } else {
                    beforeSuite = method;
                }
            } else if (method.isAnnotationPresent(AfterSuite.class)) {
                if (afterSuite != null) {
                    throw new RuntimeException("В классе больше одного метода с аннотацией AfterSuite!");
                } else {
                    afterSuite = method;
                }
            } else if (method.isAnnotationPresent(Test.class)) {
                if (method.getAnnotation(Test.class).priority() < 1 ||
                        method.getAnnotation(Test.class).priority() > 10) {
                    throw new RuntimeException("priority должно быть в пределах от 1 до 10");
                } else {
                    tests.add(method);
                }
            }
            tests.sort(Comparator.comparingInt((Method m) ->
                    m.getAnnotation(Test.class).priority()).reversed());
        }

        Object instance = clazz.getDeclaredConstructor().newInstance();
        if (beforeSuite != null) {
            beforeSuite.invoke(null);
        }
        for (Method m : tests) {
            m.invoke(instance);
        }
        if (afterSuite != null) {
            afterSuite.invoke(null);
        }
    }
}
