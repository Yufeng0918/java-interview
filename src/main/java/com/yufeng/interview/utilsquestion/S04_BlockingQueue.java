package com.yufeng.interview.utilsquestion;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class S04_BlockingQueue {

    public static void main(String[] args) throws InterruptedException {

//        testOfferPoll();
//        testPutTake();
        testOfferPollTimeout();
    }

    private static void testOfferPollTimeout() throws InterruptedException {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(3);
        queue.offer("a", 1L, TimeUnit.SECONDS);
        queue.offer("b", 1L, TimeUnit.SECONDS);
        queue.offer("c", 1L, TimeUnit.SECONDS);
        queue.offer("x", 1L, TimeUnit.SECONDS);

        System.out.println("=========");

        queue.poll(1L, TimeUnit.SECONDS);
        queue.poll(1L, TimeUnit.SECONDS);
        queue.poll(1L, TimeUnit.SECONDS);
        queue.poll(1L, TimeUnit.SECONDS);
    }


    private static void testPutTake() throws InterruptedException {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(3);
        queue.put("a");
        queue.put("b");
        queue.put("c");
        queue.put("x");

        System.out.println(queue.peek());

        queue.take();
        queue.take();
        queue.take();
        queue.take();
    }

    private static void testOfferPoll() {
        Queue<String> queue = new ArrayBlockingQueue<>(3);
        System.out.println(queue.offer("a"));
        System.out.println(queue.offer("b"));
        System.out.println(queue.offer("c"));
        System.out.println(queue.offer("x"));

        System.out.println(queue.peek());

        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println(queue.poll());
    }


    private static void testAddRemove() {
        Queue<String> queue = new ArrayBlockingQueue<>(3);
        System.out.println(queue.add("a"));
        System.out.println(queue.add("b"));
        System.out.println(queue.add("c"));

        System.out.println(queue.element());

        System.out.println(queue.remove());
        System.out.println(queue.remove());
        System.out.println(queue.remove());
    }
}
