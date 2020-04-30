package com.yufeng.interview.threadpoolquestion;

import java.util.concurrent.*;
import java.util.stream.IntStream;

public class S02_ThreadPool {

    public static void main(String[] args) {

//        System.out.println(Runtime.getRuntime().availableProcessors());
//        ExecutorService threadPool = Executors.newFixedThreadPool(5);
//        ExecutorService threadPool = Executors.newSingleThreadExecutor();
//        ExecutorService threadPool = Executors.newCachedThreadPool();


//        public ThreadPoolExecutor(int corePoolSize,
//        int maximumPoolSize,
//        long keepAliveTime,
//        TimeUnit unit,
//        BlockingQueue<Runnable> workQueue) {

        int maxSize = Runtime.getRuntime().availableProcessors();
        ExecutorService threadPool = new ThreadPoolExecutor(
            2, maxSize,
            1L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(3), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());


        try {
            IntStream.range(0, 30).forEach(i -> {
                threadPool.submit(() -> {
                    System.out.println(Thread.currentThread().getName() + " doing");
                });
            });
        } finally {
            threadPool.shutdown();
        }

    }
}
