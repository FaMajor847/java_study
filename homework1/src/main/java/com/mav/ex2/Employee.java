package com.mav.ex2;

public class Employee {
    private final String name;
    private final int age;
    private final String title;

    public Employee(String name, int age, String title) {
        this.name = name;
        this.age = age;
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getTitle() {
        return title;
    }
}
