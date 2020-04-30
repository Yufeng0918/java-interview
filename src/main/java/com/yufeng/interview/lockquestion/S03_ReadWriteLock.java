package com.yufeng.interview.lockquestion;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;

class MyCache {

    private volatile Map<String, Object> map = new HashMap<>();

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public void put(String key, Object value) throws InterruptedException {

        lock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + " writing " + key);
            TimeUnit.MILLISECONDS.sleep(300);
            map.put(key, value);
            System.out.println(Thread.currentThread().getName() + " writing done");
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void get(String key) throws InterruptedException {

        lock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + " reading " + key);
            TimeUnit.MILLISECONDS.sleep(300);
            map.get(key);
            System.out.println(Thread.currentThread().getName() + " reading done");
        } finally {
            lock.readLock().unlock();
        }
    }
}

public class S03_ReadWriteLock {

    public static void main(String[] args) {

        MyCache myCache = new MyCache();

        IntStream.range(0, 5).forEach(i -> {

            new Thread(() -> {
                try {
                    myCache.put(String.valueOf(i), String.valueOf(i));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, String.valueOf(i)).start();

        });

        IntStream.range(0, 5).forEach(i -> {

            new Thread(() -> {
                try {
                    myCache.get(String.valueOf(i));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, String.valueOf(i)).start();

        });
    }
}
