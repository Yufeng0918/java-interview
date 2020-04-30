package com.yufeng.interview.utilsquestion;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

class ShareResource {

    private int number = 1; // A:1, B:2, C:3

    private Lock lock = new ReentrantLock();
    private Condition c1 = lock.newCondition();
    private Condition c2 = lock.newCondition();
    private Condition c3 = lock.newCondition();

    public void print5() {
        lock.lock();
        try {
            while (number != 1) {
                c1.await();
            }

            IntStream.range(0, 5).forEach(i -> {
                System.out.println(Thread.currentThread().getName() + " " + i);
            });
            number = 2;
            c2.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void print10() {
        lock.lock();
        try {
            while (number != 2) {
                c2.await();
            }

            IntStream.range(0, 10).forEach(i -> {
                System.out.println(Thread.currentThread().getName() + " " + i);
            });
            number = 3;
            c3.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void print15() {
        lock.lock();
        try {
            while (number != 3) {
                c3.await();
            }

            IntStream.range(0, 15).forEach(i -> {
                System.out.println(Thread.currentThread().getName() + " " + i);
            });
            number = 1;
            c1.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}

public class S06_MultiCondition {

    public static void main(String[] args) {

        ShareResource shareResource = new ShareResource();
        new Thread(()-> {
            IntStream.range(0, 10).forEach(i -> shareResource.print5());
        }, "A").start();

        new Thread(()-> {
            IntStream.range(0, 10).forEach(i -> shareResource.print10());
        }, "B").start();

        new Thread(()-> {
            IntStream.range(0, 10).forEach(i -> shareResource.print15());
        }, "C").start();
    }
}
