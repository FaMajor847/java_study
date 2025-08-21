package com.mav.ex2;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        System.out.println("Задание 1: Найдите в списке целых чисел 3-е наибольшее число.");
        List<Integer> list = List.of(5, 2, 10, 9, 4, 3, 10, 1, 13);

        int thirdLargestNumber = list.stream()
                .sorted(Comparator.reverseOrder())
                .skip(2)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("В списке меньше 3 элементов"));

        System.out.println(thirdLargestNumber);
        System.out.println("________________________________________________");





        System.out.println("Задание 2: Найдите в списке целых чисел 3-е наибольшее уникальное число.");

        int thirdLargestUniqueNumber = list.stream()
                .distinct()
                .sorted(Comparator.reverseOrder())
                .skip(2)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("В списке меньше 3 элементов"));

        System.out.println(thirdLargestUniqueNumber);
        System.out.println("________________________________________________");





        System.out.println("Задание 3:  Есть список Employee(name, age, title). Получите список имён трёх самых старших сотрудников с должностью \"Инженер\", в порядке убывания возраста.");

        List<Employee> employees = List.of(
                new Employee("Петя", 19, "Инженер"),
                new Employee("Вася", 25, "Аналитик"),
                new Employee("Коля", 34, "Инженер"),
                new Employee("Света", 31, "Тестировщик"),
                new Employee("Маша", 56, "Тимлид"),
                new Employee("Саша", 34, "Инженер"));

        List<String> result = employees.stream()
                .filter(e -> "Инженер".equals(e.getTitle()))
                .sorted(Comparator.comparingInt(Employee::getAge).reversed())
                .limit(3)
                .map(Employee::getName)
                .toList();

        System.out.println(result);
        System.out.println("________________________________________________");






        System.out.println("Задание 4:  Есть список Employee(name, age, title). Посчитайте средний возраст сотрудников с должностью \"Инженер\".");

        OptionalDouble averageAgeOfEngineers = employees.stream().filter(e -> "Инженер".equals(e.getTitle())).mapToInt(Employee::getAge).average();

        if (averageAgeOfEngineers.isPresent()) {
            System.out.println("Средний возраст инженеров: " + averageAgeOfEngineers.getAsDouble());
        } else {
            System.out.println("В исходном списке нет инженеров");
        }
        System.out.println("________________________________________________");

        System.out.println("Задание 5:   Найдите в списке слов самое длинное слово.Если несколько максимальных — вернуть любое из них..");
        List<String> wordList = List.of("Машина", "Дом", "Бронетранспортер", "Кафка", "Интеграция");
        String resultWord = wordList.stream().max(Comparator.comparingInt(String::length)).orElse("В списке нет слов");
        System.out.println(resultWord);
        System.out.println("________________________________________________");





        System.out.println("Задание 6:  Есть строка в нижнем регистре, слова разделены пробелом. Постройте Map<слово, количество> — сколько раз каждое слово встречается.");

        String text = "hello world hello java world java hello study inno inno java hello study";
        Map<String, Long> wordsCount = Arrays.stream(text.split(" "))
                .collect(Collectors.groupingBy(word -> word, Collectors.counting()));
        System.out.println(wordsCount);
        System.out.println("________________________________________________");




        System.out.println("Задание 7:  ★ Отпечатайте в консоль строки из списка по возрастанию длины слова; при равной длине — в алфавитном порядке.");
        List<String> words = List.of("Java", "Python", "Kotlin", "Scala", "Go", "Rust", "Dart", "Swift");

        words.stream().sorted(Comparator.comparingInt(String::length).thenComparing(word -> word)).forEach(System.out::println);
        System.out.println("_________________________________________________");
    }

}
