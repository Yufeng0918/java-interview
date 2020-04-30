package com.yufeng.interview.threadpoolquestion;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

class MyThead implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        System.out.println("in callable");
        return 1024;
    }
}

public class S01_Callable {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        FutureTask<Integer> futureTask1 = new FutureTask<>(new MyThead());
        new Thread(futureTask1).start();

        Integer result = futureTask1.get();
        System.out.println(result);
    }
}
