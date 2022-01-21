package com.geekbrains.cloud.stream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamExamples {

    // sort
    public static void printTop10(Map<String, Integer> wordsMap) {
        wordsMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue((o1, o2) -> o2 - o1))
                .limit(10)
                .forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue()));
    }


    public static void main(String[] args) throws IOException {

        Consumer<Object> printer = System.out::println;

        // simple print
        Stream.iterate(1, x -> x + 1)
                .limit(10)
                .forEach(printer);
        System.out.println();

        // filter even
        Stream.iterate(1, x -> x + 1)
                .limit(10)
                .filter(x -> x % 2 == 0)
                .forEach(printer);

//        Stream.iterate(1, x -> x + 1)
//                .limit(10)
//                .anyMatch(x -> x > 5);

        // modify elements - map
        System.out.println();
        Stream.iterate(1, x -> x + 1)
                .limit(10)
                .map(x -> String.format("value: %d\nsquare: %d\ncube: %d\n",
                        x, x * x, x * x * x))
                .forEach(printer);

        // flatMap
        Files.lines(Paths.get("serverDir", "1.txt"))
                .flatMap(line -> Arrays.stream(line.split(" +")))
                .filter(word -> !word.isEmpty())
                .forEach(printer);

        // collect
        // toList
        List<String> listWords = Files.lines(Paths.get("serverDir", "1.txt"))
                .flatMap(line -> Arrays.stream(line.split(" +")))
                .filter(word -> !word.isEmpty() && !word.matches("\\d+"))
                .map(String::toLowerCase)
                .map(word -> word.replaceAll("\\W+", ""))
                .collect(Collectors.toList());
        System.out.println(listWords);

        // toSet
        Set<String> setWords = Files.lines(Paths.get("serverDir", "1.txt"))
                .flatMap(line -> Arrays.stream(line.split(" +")))
                .filter(word -> !word.isEmpty() && !word.matches("\\d+"))
                .map(String::toLowerCase)
                .map(word -> word.replaceAll("\\W+", ""))
                .collect(Collectors.toSet());
        System.out.println(setWords);

        // toMap
        // частотный анализ (хотим узнать сколько каждое слово встречается в тексте)
        Map<String, Integer> mapWords = Files.lines(Paths.get("serverDir", "1.txt"))
                .flatMap(line -> Arrays.stream(line.split(" +")))
                .filter(word -> !word.isEmpty() && !word.matches("\\d+"))
                .map(String::toLowerCase)
                .map(word -> word.replaceAll("\\W+", ""))
                .collect(Collectors.toMap(
                        Function.identity(),
                        v -> 1,
                        Integer::sum,
                        TreeMap::new
                ));
        System.out.println(mapWords);
        printTop10(mapWords);

        // Comparable - чтобы класс был сравним
        Comparable<Integer> cmpI = o -> -o;

        // reduce - агрегация (вычисление применяемое ко всему потоку данных)
        Integer sum = Stream.iterate(1, x -> x + 1)
                .limit(10)
                .reduce(0, Integer::sum);
        // 0 | 1 2 3 4 5 6 7 8 9 10
        // 0 + 1 + 2 + 3 ...
        System.out.println(sum);

        Stream.iterate(1, x -> x + 1)
                .limit(10)
                .reduce(Integer::sum)
                .ifPresent(System.out::println);

        TreeSet<Integer> set = Stream.of(5, 3, 4, 3, 3, 1, 7)
                .reduce(
                        new TreeSet<>(),
                        (s, val) -> {
                            s.add(val);
                            return s;
                        },
                        (l, r) -> l
                );
        System.out.println(set);


    }
}
