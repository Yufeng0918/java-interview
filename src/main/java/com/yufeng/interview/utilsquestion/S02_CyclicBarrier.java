package com.yufeng.interview.utilsquestion;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.IntStream;

public class S02_CyclicBarrier {

    public static void main(String[] args) {

        int count = 7;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7, () -> {System.out.println("Final call");});

        IntStream.range(0, count).forEach(i -> {
            new Thread(()-> {
                System.out.println(Thread.currentThread().getName() + " get ticket");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }

            }).start();
        });
    }
}
