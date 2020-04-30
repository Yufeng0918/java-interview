package com.yufeng.interview.casquestion;

import java.util.concurrent.atomic.AtomicInteger;

public class S01_CASDemo {

    public static void main(String[] args) {

        AtomicInteger atomicInteger = new AtomicInteger(5);
        System.out.println(atomicInteger.compareAndSet(5, 2020) + " " + atomicInteger.get());
        System.out.println(atomicInteger.compareAndSet(5, 1024) + " " + atomicInteger.get());

    }
}
