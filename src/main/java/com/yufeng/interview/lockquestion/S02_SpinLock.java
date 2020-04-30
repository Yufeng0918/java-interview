package com.yufeng.interview.lockquestion;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class S02_SpinLock {

    AtomicReference<Thread> atomicReference = new AtomicReference<>();

    public void myLock() {
        Thread thread = Thread.currentThread();
        while (!atomicReference.compareAndSet(null, thread)) {
            System.out.println(Thread.currentThread() + " spining");
        }
        System.out.println(Thread.currentThread().getName() + " lock");
    }

    public void myUnLock() {
        Thread thread = Thread.currentThread();
        atomicReference.compareAndSet(thread, null);
        System.out.println(Thread.currentThread().getName() + " release");
    }

    public static void main(String[] args) throws InterruptedException {

        S02_SpinLock spinLock = new S02_SpinLock();

        new Thread(() -> {
            spinLock.myLock();
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            spinLock.myUnLock();
        }, "AA").start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(() -> {
            spinLock.myLock();
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            spinLock.myUnLock();
        }, "BB").start();
    }
}
