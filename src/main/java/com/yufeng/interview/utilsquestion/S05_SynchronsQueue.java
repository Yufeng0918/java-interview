package com.yufeng.interview.utilsquestion;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class S05_SynchronsQueue {

    public static void main(String[] args) {

        BlockingQueue<String> blockingQueue = new SynchronousQueue<>();

        new Thread(()-> {
            try {
                blockingQueue.put("1");
                System.out.println("put 1");

                blockingQueue.put("2");
                System.out.println("put 2");

                blockingQueue.put("3");
                System.out.println("put 3");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(()-> {
            try {

                TimeUnit.SECONDS.sleep(3);
                blockingQueue.take();

                TimeUnit.SECONDS.sleep(3);
                blockingQueue.take();

                TimeUnit.SECONDS.sleep(3);
                blockingQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
