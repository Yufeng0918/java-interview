package com.yufeng.interview.volatilequestion;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class S01_VolatileDemo {

    public static void main(String[] args) throws InterruptedException {

        atomicNoGurantee();
    }

    public static void atomicNoGurantee() {

        MyData myData = new MyData();
        IntStream.range(0, 20).forEach(i -> {
            new Thread(() -> {

                for (int j = 0; j < 1000; j++) {
                    myData.plusPlus();
                    myData.atomicPlus();
                }
            }, String.valueOf(i)).start();
        });

        while (Thread.activeCount() > 2) {
            Thread.yield();
        }

        System.out.println("final number " + myData.number);
        System.out.println("final atomic integer " + myData.atomicInteger);
    }

    public static void visiblity() {
        MyData myData = new MyData();
        Thread thread = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myData.addT060();});
        thread.start();

        while (myData.number == 0) { }
        System.out.println("number is updated");
    }
}



class MyData {

    volatile int number = 0;

    AtomicInteger atomicInteger = new AtomicInteger(0);

    public void addT060() {
        this.number = 60;
    }

    public void plusPlus() {
        this.number++;
    }

    public void atomicPlus() {
        this.atomicInteger.getAndIncrement();
    }
}
