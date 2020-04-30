package com.yufeng.interview.gcquestion;

public class S01_GCRoot {

    private static  Object o1;

    private static final Object o2 = new Object();

    public static void main(String[] args) throws InterruptedException {
        m1();
    }


    private static void m1() throws InterruptedException {
        S01_GCRoot gcRoot = new S01_GCRoot();
        System.gc();
        byte[] byteArray = new byte[50 * 1024 * 1024];
        System.out.println("GC 1st time");
        Thread.sleep(Integer.MAX_VALUE);
    }
}
