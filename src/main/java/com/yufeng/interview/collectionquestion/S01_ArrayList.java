package com.yufeng.interview.collectionquestion;


import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

public class S01_ArrayList {

    public static void main(String[] args) {
//        vectorSafe();
        synchronziedList();
    }

    public static void arrayListNotSafe() {
        List<String> list = new ArrayList<>();
        list.forEach(System.out::println);

        IntStream.range(0, 30).forEach(i -> {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(list);
            }, String.valueOf(i)).start();
        });
    }


    public static void vectorSafe() {
        List<String> list = new Vector<>();
        list.forEach(System.out::println);

        IntStream.range(0, 30).forEach(i -> {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(list);
            }, String.valueOf(i)).start();
        });
    }


    public static void synchronziedList() {
        List<String> list = Collections.synchronizedList(new ArrayList<>());
        list.forEach(System.out::println);

        IntStream.range(0, 30).forEach(i -> {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(list);
            }, String.valueOf(i)).start();
        });
    }

    public static void copyOnWriteArrayList() {
        List<String> list = new CopyOnWriteArrayList<>();
        list.forEach(System.out::println);

        IntStream.range(0, 30).forEach(i -> {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(list);
            }, String.valueOf(i)).start();
        });
    }
}
