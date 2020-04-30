package com.yufeng.interview.volatilequestion;

import java.util.stream.IntStream;

public class S03_SingletonDemo {

    private static volatile S03_SingletonDemo instance = null;

    private S03_SingletonDemo() {
        System.out.println("Thread " + Thread.currentThread().getId() + " create constructor");
    }


    public static S03_SingletonDemo getInstance() {
        if (instance == null) {
            synchronized (S03_SingletonDemo.class) {
                if (instance == null) {
                    instance = new S03_SingletonDemo();
                }
            }
        }
        return instance;
    }


    public static void main(String[] args) {

        IntStream.range(0, 10).forEach(i -> {
            new Thread(() -> {
                S03_SingletonDemo.getInstance();
            }, String.valueOf(i)).start();
        });

    }
}
