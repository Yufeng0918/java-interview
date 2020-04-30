package com.yufeng.interview.collectionquestion;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

public class S03_HashMap {

    public static void main(String[] args) {
        hashMapNotSafe();
    }

    public static void hashMapNotSafe() {
        Map<String, String> map = new ConcurrentHashMap<>();
        map.entrySet().forEach(System.out::println);

        IntStream.range(0, 30).forEach(i -> {
            new Thread(() -> {
                map.put(UUID.randomUUID().toString().substring(0, 8), String.valueOf(i));
                System.out.println(map);
            }, String.valueOf(i)).start();
        });
    }
}
