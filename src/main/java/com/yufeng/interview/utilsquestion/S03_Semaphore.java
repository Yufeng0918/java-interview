package com.yufeng.interview.utilsquestion;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class S03_Semaphore {

    public static void main(String[] args) {

        Semaphore semaphore = new Semaphore(3);

        IntStream.range(0, 6).forEach(i -> {

            new Thread(() -> {
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + " parking");
                    TimeUnit.SECONDS.sleep(3);
                    System.out.println(Thread.currentThread().getName() + " leaving");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            }, String.valueOf(i)).start();
        });

    }
}
