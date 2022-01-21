package com.geekbrains.cloud.stream;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class StreamInterfaces {

    public static void main(String[] args) {

        // forEach - terminal (void)
        // peek - stream (Stream<R>)
        Consumer<Object> printer = (arg) -> {
            System.out.println("Log: " + arg);
        };

        // A -> B, A = B, A != B
        // map, flatMap  - A stream (Stream<B>)
        // map - arg -> Stream<List> -> List.stream -> Stream<Stream>
        // flatMap -> arg -> Stream<List> -> List.stream -> Stream
        Function<String, Integer> length = String::length;
        printer.accept(length.apply("Hello"));
        Function<String, Integer> l1 = (str) -> str.length();

        // filter - stream (Stream<R>)
        // anyMatch, allMatch, noneMatch - boolean
        Predicate<Integer> oddPredicate = x -> x % 2 != 0;
        printer.accept(oddPredicate.test(1));

        // collectors
        Supplier<Integer> getter = () -> 1;
        Supplier<HashMap<String, Set<Integer>>> supplier = () -> {
            HashMap<String, Set<Integer>> map = new HashMap<>();
            map.put("key1", new HashSet<>());
            map.put("key2", new HashSet<>());
            return map;
        };
        printer.accept(getter.get());
        printer.accept(supplier.get());
    }

}
