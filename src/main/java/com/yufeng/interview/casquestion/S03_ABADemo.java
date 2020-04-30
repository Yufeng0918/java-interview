package com.yufeng.interview.casquestion;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

public class S03_ABADemo {

    static AtomicReference<Integer> atomicReference = new AtomicReference<>(100);
    static AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(100, 1);

    public static void main(String[] args) throws InterruptedException {

        new Thread(() -> {
            atomicReference.compareAndSet(100, 101);
            atomicReference.compareAndSet(101, 100);
        }, "t1").start();

        TimeUnit.SECONDS.sleep(1);


        new Thread(() -> {
            atomicReference.compareAndSet(100, 2019);
            System.out.println(atomicReference.get());
        }, "t2").start();

        TimeUnit.SECONDS.sleep(2);



        new Thread(() -> {
            int stamp = atomicStampedReference.getStamp();

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName() + " set " +atomicStampedReference.compareAndSet(100, 101, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1));
            System.out.println(Thread.currentThread().getName() + " set " +atomicStampedReference.compareAndSet(101, 100, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1));
        }, "t3").start();


        new Thread(() -> {
            int stamp = atomicStampedReference.getStamp();

            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean result = atomicStampedReference.compareAndSet(100, 2019, stamp, stamp + 1);
            System.out.println(Thread.currentThread().getName() + " set version" + atomicStampedReference.getStamp() + ", operation: " + result);
        }, "t4").start();
    }
}
