package com.yufeng.interview.collectionquestion;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.IntStream;

public class S02_HashSet {

    public static void main(String[] args) {

        hashSetNotSafe();
    }


    public static void hashSetNotSafe() {
        Set<String> set = new CopyOnWriteArraySet<>();
        set.forEach(System.out::println);

        IntStream.range(0, 30).forEach(i -> {
            new Thread(() -> {
                set.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(set);
            }, String.valueOf(i)).start();
        });
    }

}
