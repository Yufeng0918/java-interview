package com.yufeng.interview.lockquestion;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class S01_RecursiveLock {

    public static void main(String[] args) throws InterruptedException {

        Phone phone = new Phone();
        new Thread(() -> {
            phone.sendSMS();
        }).start();

        new Thread(() -> {
            phone.sendSMS();
        }).start();

        TimeUnit.SECONDS.sleep(1);

        Phone phone1 = new Phone();
        new Thread(phone1).start();
        new Thread(phone1).start();
    }

}

class Phone implements Runnable{

    public synchronized void sendSMS() {
        System.out.println(Thread.currentThread().getId() + " sendSMS");
        sendEmail();
    }

    public synchronized void sendEmail() {
        System.out.println(Thread.currentThread().getId() + " sendEmail");
    }

    Lock lock = new ReentrantLock();

    @Override
    public void run() {
        get();
    }

    public void get() {
        lock.lock();
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " get()");
            set();
        } finally {
            lock.unlock();
            lock.unlock();
        }
    }


    public void set() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " set()");
        } finally {
            lock.unlock();
        }
    }
}
